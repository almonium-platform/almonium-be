"""Choose cover art for the works on the shelf, on the owner's plan.

For each work the backend has never looked at, this asks the backend for the files it may be given (Wikimedia
Commons, already narrowed by the licence rule there), lays them out on one numbered contact sheet, shows the sheet
to a vision model, and hands the pick back. The backend fetches the chosen file itself and keeps it with its
source, artist and licence category. A work with no good candidate is recorded as such and keeps its typographic
cover.

The model is asked through the book processor's AI gateway (its `plan-manage ask`), like the dictionary's review
loop: on a plan's seat, under the owner's monthly cap and paused accounts, recorded in the ledger, and found again
by content, so a run that is stopped and started again buys nothing twice. When the gateway holds a call (a cap, a
pause, a plan's limit) the run stops there and the remaining works stay pending.

Requests to the backend are signed with the publisher secret (ALMONIUM_BOOKS_PUBLISHER_SECRET, exported or from the
backend's .env). Needs Pillow and requests. Run from the repository's root.

  choose_covers.py                         every pending work, against the local backend
  choose_covers.py --limit 5               the first five
  choose_covers.py --only emma,dracula     these works, looked at again even if already decided
  choose_covers.py --backend https://api.staging.almonium.com/v1
"""
import argparse, hashlib, hmac, json, os, subprocess, sys, tempfile, time
from pathlib import Path

import requests
from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "lexicon_research"))
import backend_env  # noqa: E402

GATEWAY = os.environ.get("ALMONIUM_GATEWAY") or str(Path(__file__).resolve().parents[3] / "almonium-book-processor" / "plan-manage")
SEAT = "claude-code:claude-sonnet-5-5:low"
HELD = 75  # the gateway's exit when a cap, a pause or a plan's limit refused and nothing was paid
USER_AGENT = "Almonium/1.0 (https://almonium.com)"  # Commons asks automated clients to say who they are
CELL, COLUMNS = 300, 4

INSTRUCTIONS = """You choose cover art for a classic book in a reading app. You are shown one contact sheet of \
numbered candidate images, all already cleared for copyright. Pick the one that works best as the book's cover, or none.

A good cover is an illustration, engraving, drawing or painting that shows a scene, a figure or a place from this \
book or made for it, and reads clearly when printed small in one ink. It is a flat scan of the artwork.

Never pick: a title page or any page that is mostly text; a portrait or photograph of the author; a photograph of a \
physical book, a building, a statue or a museum room; a playbill or poster that is mostly lettering; a map, diagram \
or family tree; a film still or anything modern; an image that has nothing to do with this book; a scan too dark, \
faint or damaged to read.

If no candidate is good, pick none: a plain typographic cover is better than a wrong picture.

pick is the number on the image, or 0 for none. focus_x and focus_y give the centre of the main subject inside the \
picked image, as a percentage from its left edge and from its top edge; the cover shows a square crop around it. \
reason is one short sentence."""

SCHEMA = {
    "type": "object",
    "required": ["pick", "focus_x", "focus_y", "reason"],
    "additionalProperties": False,
    "properties": {
        "pick": {"type": "integer", "minimum": 0},
        "focus_x": {"type": "integer", "minimum": 0, "maximum": 100},
        "focus_y": {"type": "integer", "minimum": 0, "maximum": 100},
        "reason": {"type": "string"},
    },
}


class Held(Exception):
    """The gateway refused before anything was paid: stop, the rest stays pending."""


def call(backend, token, method, path, payload):
    body = json.dumps(payload, ensure_ascii=False, separators=(",", ":")).encode()
    timestamp = str(int(time.time()))
    signature = hmac.new(token.encode(), f"{timestamp}.".encode() + body, hashlib.sha256).hexdigest()
    response = requests.request(
        method,
        backend.rstrip("/") + path,
        data=body,
        headers={"Content-Type": "application/json", "X-Almonium-Books-Timestamp": timestamp, "X-Almonium-Books-Signature": signature},
        timeout=300,
    )
    if response.status_code != 200:
        raise RuntimeError(f"{path}: HTTP {response.status_code}: {response.text[:300]}")
    return response.json()


def contact_sheet(candidates, folder):
    """The candidates that could be fetched, numbered on one sheet: (the sheet's path, the candidates shown)."""
    try:
        font = ImageFont.truetype("DejaVuSans-Bold.ttf", 34)
    except OSError:
        font = ImageFont.load_default()
    thumbs, shown = [], []
    for candidate in candidates:
        try:
            fetched = requests.get(candidate["imageUrl"], headers={"User-Agent": USER_AGENT}, timeout=60)
            fetched.raise_for_status()
            path = folder / f"{len(shown) + 1}"
            path.write_bytes(fetched.content)
            thumb = Image.open(path).convert("RGB")
            thumb.thumbnail((CELL - 8, CELL - 8))
        except Exception as broken:  # one file Commons will not serve must not lose the work its other candidates
            print(f"    skipped {candidate['title']}: {str(broken)[:120]}")
            continue
        thumbs.append(thumb)
        shown.append(candidate)
        time.sleep(0.3)
    if not shown:
        return None, []
    rows = (len(shown) + COLUMNS - 1) // COLUMNS
    sheet = Image.new("RGB", (COLUMNS * CELL, rows * CELL), "white")
    draw = ImageDraw.Draw(sheet)
    for index, thumb in enumerate(thumbs):
        x, y = (index % COLUMNS) * CELL, (index // COLUMNS) * CELL
        sheet.paste(thumb, (x + (CELL - thumb.width) // 2, y + (CELL - thumb.height) // 2))
        draw.rectangle([x, y, x + 52, y + 44], fill="black")
        draw.text((x + 8, y + 3), str(index + 1), fill="white", font=font)
    target = folder / "sheet.jpg"
    sheet.save(target, quality=80)
    return target, shown


def ask(seat, title, author, shown, sheet):
    names = "\n".join(f"{number}. {candidate['title']}" for number, candidate in enumerate(shown, 1))
    request = {
        "seat": seat,
        "purpose": "cover_choice",
        "caller": "choose_covers",
        "instructions": INSTRUCTIONS,
        "input": f'The book: "{title}" by {author}. The sheet shows {len(shown)} candidates. Their file names on Commons, which often say what a picture is:\n{names}',
        "schema": SCHEMA,
        "images": [str(sheet)],
    }
    try:
        ran = subprocess.run([GATEWAY, "ask"], input=json.dumps(request, ensure_ascii=False), capture_output=True, text=True, timeout=1200)
        said = json.loads(ran.stdout.strip().splitlines()[-1])
    except (subprocess.TimeoutExpired, OSError, json.JSONDecodeError, IndexError) as broken:
        raise RuntimeError(f"no answer from the gateway: {str(broken)[:200]}") from broken
    if said.get("status") == "held":
        raise Held(str(said.get("reason"))[:300])
    if said.get("status") != "completed":
        raise RuntimeError(str(said.get("error"))[:300])
    return json.loads(said["text"]), said


def main():
    parser = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    parser.add_argument("--backend", default="http://localhost:9998/v1", help="the backend's API root")
    parser.add_argument("--seat", default=SEAT, help="the gateway seat that looks at the sheet; only a claude-code seat is shown images")
    parser.add_argument("--limit", type=int, default=0, help="stop after this many works")
    parser.add_argument("--only", default="", help="comma-separated work slugs, looked at again even if already decided")
    args = parser.parse_args()
    token = backend_env.publisher_secret()

    works = [slug.strip() for slug in args.only.split(",") if slug.strip()] or call(args.backend, token, "POST", "/internal/covers/pending", {})
    if args.limit:
        works = works[: args.limit]
    print(f"{len(works)} works to look at")
    counts = {"APPROVED": 0, "NONE": 0, "failed": 0}
    spent = 0.0
    for number, slug in enumerate(works, 1):
        try:
            offered = call(args.backend, token, "POST", "/internal/covers/candidates", {"workSlug": slug})
            decision = {"workSlug": slug, "chosen": None, "focusX": 50, "focusY": 50, "reason": offered["note"], "model": None}
            if offered["candidates"]:
                with tempfile.TemporaryDirectory(prefix="almonium-covers-") as folder:
                    sheet, shown = contact_sheet(offered["candidates"], Path(folder))
                    if sheet is None:
                        decision["reason"] = "No candidate could be downloaded."
                    else:
                        verdict, said = ask(args.seat, offered["title"], offered["author"], shown, sheet)
                        spent += said.get("cost_usd") or 0
                        picked = verdict["pick"]
                        decision.update(
                            chosen=shown[picked - 1] if 1 <= picked <= len(shown) else None,
                            focusX=verdict["focus_x"],
                            focusY=verdict["focus_y"],
                            reason=verdict["reason"][:1000],
                            model=f"{said.get('provider')}:{said.get('model')}"[:120],
                        )
            status = call(args.backend, token, "PUT", "/internal/covers/decisions", decision)["status"]
            counts[status] += 1
            print(f"[{number}/{len(works)}] {slug}: {status} ({len(offered['candidates'])} candidates) {decision['reason'] or ''}"[:220], flush=True)
        except Held as held:
            print(f"The gateway is holding calls: {held}\nStopped; {len(works) - number + 1} works stay pending.")
            sys.exit(HELD)
        except Exception as broken:  # one work's failure leaves it pending for the next run and does not stop the rest
            counts["failed"] += 1
            print(f"[{number}/{len(works)}] {slug}: failed, stays pending: {str(broken)[:200]}", flush=True)
    print(f"Done: {counts['APPROVED']} covers, {counts['NONE']} without art, {counts['failed']} failed. Plan cost recorded: ${spent:.2f}.")


if __name__ == "__main__":
    main()
