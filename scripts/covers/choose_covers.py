"""Choose cover art for the works on the shelf, on the owner's plan.

For each work the backend has never looked at, this asks the backend for the files it may be given (Wikimedia
Commons, already narrowed by the licence rule and spread across the work's categories there), lays them out on
numbered contact sheets of twelve, shows the sheets to a vision model, and has the pick looked at again on its own,
at full size, before it is accepted: a thumbnail hides a caption strip or a page of text, and the crop is set from
the real image. A pick that fails the second look is struck out and the sheets are asked again, twice at most. The
backend fetches the chosen file itself and keeps it with its source, artist and licence category. A work with no
good candidate is recorded as such and keeps its typographic cover.

The model is asked through the book processor's AI gateway (its `plan-manage ask`), like the dictionary's review
loop: on a plan's seat, under the owner's monthly cap and paused accounts, recorded in the ledger, and found again
by content, so a run that is stopped and started again buys nothing twice. When the gateway holds a call (a cap, a
pause, a plan's limit) the run stops there and the remaining works stay pending.

Requests to the backend are signed with the publisher secret (ALMONIUM_BOOKS_PUBLISHER_SECRET, exported or from the
backend's .env). Needs Pillow and requests. Run from the repository's root.

  choose_covers.py                         every pending work, against the local backend
  choose_covers.py --limit 5               the first five
  choose_covers.py --only emma,dracula     these works, looked at again even if already decided
  choose_covers.py --all                   every work on the shelf again (after the search or the rules changed)
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
CELL, COLUMNS, PER_SHEET = 300, 4, 12
MAX_SHEETS = 4  # the backend offers up to forty-eight candidates
PICKS = 3  # a pick struck out by the second look is replaced at most twice
RESTART_WAITS, RESTART_WAIT_SECONDS = 12, 15  # how long a backend that is restarting is waited for: three minutes

INSTRUCTIONS = """You choose cover art for a classic book in a reading app. You are shown contact sheets of numbered \
candidate images, all already cleared for copyright. Pick the one that works best as the book's cover, or none.

A good cover is an illustration, engraving, drawing or painting that shows a scene, a figure or a place from this \
book or made for it, and reads clearly when printed small in one ink. It is a flat scan of the artwork. Prefer a \
picture with one clear subject, strong shapes and no lettering over a crowded or faint one, and the work's best-known \
illustrator where several are offered.

Never pick: a title page or any page that is mostly text; a page where a small picture shares the page with text; a \
book's binding, spine or cover design; a portrait or photograph of the author; a photograph of a physical book, a \
building, a statue or a museum room; a playbill, theatre or film poster, or anything that is mostly lettering; a map, \
diagram or family tree; a film still or anything modern; a picture made for a different book, including a sequel or \
another work by the same author; a scan too dark, faint or damaged to read.

Each candidate is listed with the Commons category it was found in and its file name, which often say what it is and \
which book it belongs to. If no candidate is good, pick none: a plain typographic cover is better than a wrong picture.

pick is the number on the image, or 0 for none. reason is one short sentence."""

SCHEMA = {
    "type": "object",
    "required": ["pick", "reason"],
    "additionalProperties": False,
    "properties": {"pick": {"type": "integer", "minimum": 0}, "reason": {"type": "string"}},
}

SECOND_LOOK = """You check one image that was picked from thumbnails as cover art for a classic book in a reading \
app. Look at it at full size and decide whether it may be used.

It is shown as a square crop, printed small in one ink: a tall image keeps its full width and a square slice of its \
height, a wide image keeps its full height and a square slice of its width. You choose where that slice sits.

Acceptable: an illustration, engraving, drawing or painting of a scene, a figure or a place from this book, where a \
square crop exists that shows the subject and no lettering.

Not acceptable: a page that is mostly text; a title page; a book's binding or cover design; a playbill or poster; a \
portrait or photograph of the author; a photograph of an object or a place; an image whose subject cannot be shown in \
a square crop without a caption, a title or other lettering inside it; a scan too dark, faint or damaged to read small; \
a picture that plainly belongs to a different book.

focus_x and focus_y give the centre of the square crop as a percentage from the image's left edge and from its top \
edge; set them so the main subject is inside the crop and any caption or margin lettering is outside it. reason is \
one short sentence."""

QUERIES = """You help find public-domain cover art on Wikimedia Commons for a classic book that has no usable \
illustrations of its own there. Give six short search queries (two to five words each) for period paintings, \
engravings, drawings or old photographs that would suit its cover: the story's setting (the city, the landscape, the \
kind of building or room), its era, a central object, animal or kind of figure. Use concrete nouns and place names, \
in English or the book's own language; add a word such as painting, engraving or a decade where it helps. Vary the \
queries. Do not use the author's name or the book's title."""

QUERIES_SCHEMA = {
    "type": "object",
    "required": ["queries"],
    "additionalProperties": False,
    "properties": {"queries": {"type": "array", "minItems": 1, "maxItems": 6, "items": {"type": "string"}}},
}

FITTING = """You choose cover art for a classic book in a reading app. The book has no usable illustrations of its \
own, so you are shown contact sheets of numbered period pictures found by searching for its setting and motifs, all \
already cleared for copyright. Pick the one that best suits the book's cover, or none.

A good cover here is a painting, engraving, drawing or old photograph, made in or before the book's own time, that a \
reader of the book would recognise as its world: the place it is set in, the kind of scene or figure at its heart, \
its mood. It has one clear subject and strong shapes, and reads clearly when printed small in one ink.

Never pick: anything with lettering in it; a page of text; a portrait of the author or of any named real person; a \
map or diagram; a modern photograph or anything made well after the book; a picture whose link to the book is only \
a word in its file name; a picture that would mislead about what the book is; a scan too dark, faint or damaged.

Each candidate is listed with the search it came from and its file name. If none truly suits the book, pick none: a \
plain typographic cover is better than a picture that does not belong.

pick is the number on the image, or 0 for none. reason is one short sentence saying what the picture shows and why \
it suits the book."""

FITTING_LOOK = """You check one period picture that was picked from thumbnails as cover art for a classic book in a \
reading app. It was not made for the book; it was chosen because it suits the book's setting or subject. Look at it \
at full size and decide whether it may be used.

It is shown as a square crop, printed small in one ink: a tall image keeps its full width and a square slice of its \
height, a wide image keeps its full height and a square slice of its width. You choose where that slice sits.

Acceptable: a painting, engraving, drawing or old photograph from the book's time or earlier that a reader would \
recognise as the book's world, where a square crop exists that shows the subject and no lettering.

Not acceptable: anything modern; a page of text; a portrait of a named real person; a map or diagram; an image whose \
subject cannot be shown in a square crop without a caption or other lettering inside it; a scan too dark, faint or \
damaged to read small; a picture that does not in fact suit this book.

focus_x and focus_y give the centre of the square crop as a percentage from the image's left edge and from its top \
edge; set them so the main subject is inside the crop and any caption or margin lettering is outside it. reason is \
one short sentence."""

SECOND_SCHEMA = {
    "type": "object",
    "required": ["acceptable", "focus_x", "focus_y", "reason"],
    "additionalProperties": False,
    "properties": {
        "acceptable": {"type": "boolean"},
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
    headers = {"Content-Type": "application/json", "X-Almonium-Books-Timestamp": timestamp, "X-Almonium-Books-Signature": signature}
    for attempt in range(RESTART_WAITS + 1):
        try:
            response = requests.request(method, backend.rstrip("/") + path, data=body, headers=headers, timeout=300)
            if response.status_code not in (502, 503, 504):
                break
        except requests.ConnectionError:
            # the backend is restarting under the run (a deploy, a rebuild): a paid answer must not be lost to that
            if attempt == RESTART_WAITS:
                raise
        time.sleep(RESTART_WAIT_SECONDS)
        timestamp = str(int(time.time()))
        signature = hmac.new(token.encode(), f"{timestamp}.".encode() + body, hashlib.sha256).hexdigest()
        headers = {**headers, "X-Almonium-Books-Timestamp": timestamp, "X-Almonium-Books-Signature": signature}
    if response.status_code != 200:
        raise RuntimeError(f"{path}: HTTP {response.status_code}: {response.text[:300]}")
    return response.json()


def contact_sheets(candidates, folder):
    """The candidates that could be fetched, numbered across sheets of twelve: (the sheets, the candidates shown).

    Each candidate shown carries the path of its own file, for the second look.
    """
    try:
        font = ImageFont.truetype("DejaVuSans-Bold.ttf", 34)
    except OSError:
        font = ImageFont.load_default()
    thumbs, shown = [], []
    for candidate in candidates[: MAX_SHEETS * PER_SHEET]:
        try:
            fetched = requests.get(candidate["imageUrl"], headers={"User-Agent": USER_AGENT}, timeout=60)
            fetched.raise_for_status()
            path = folder / f"candidate-{len(shown) + 1}.{'png' if candidate['mime'] == 'image/png' else 'jpg'}"
            path.write_bytes(fetched.content)
            thumb = Image.open(path).convert("RGB")
            thumb.thumbnail((CELL - 8, CELL - 8))
        except Exception as broken:  # one file Commons will not serve must not lose the work its other candidates
            print(f"    skipped {candidate['title']}: {str(broken)[:120]}")
            continue
        thumbs.append(thumb)
        shown.append({**candidate, "file": str(path)})
        time.sleep(0.2)
    sheets = []
    for start in range(0, len(shown), PER_SHEET):
        page = thumbs[start : start + PER_SHEET]
        rows = (len(page) + COLUMNS - 1) // COLUMNS
        sheet = Image.new("RGB", (COLUMNS * CELL, rows * CELL), "white")
        draw = ImageDraw.Draw(sheet)
        for index, thumb in enumerate(page):
            x, y = (index % COLUMNS) * CELL, (index // COLUMNS) * CELL
            sheet.paste(thumb, (x + (CELL - thumb.width) // 2, y + (CELL - thumb.height) // 2))
            label = str(start + index + 1)
            draw.rectangle([x, y, x + 30 + 22 * len(label), y + 44], fill="black")
            draw.text((x + 8, y + 3), label, fill="white", font=font)
        target = folder / f"sheet-{len(sheets) + 1}.jpg"
        sheet.save(target, quality=80)
        sheets.append(target)
    return sheets, shown


def gateway(request):
    """One call through the book processor's gateway: (the answer, what the gateway said about it)."""
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


def pick(seat, title, author, shown, sheets, struck, instructions):
    """The number the model picks from the sheets, 0 for none; candidates struck out before are named as such."""
    listed = "\n".join(f"{number}. [{candidate.get('foundIn') or 'no category'}] {candidate['title']}" for number, candidate in enumerate(shown, 1))
    barred = f"\nThese were looked at closely and rejected; do not pick them: {', '.join(str(number) for number in sorted(struck))}." if struck else ""
    return gateway({
        "seat": seat,
        "purpose": "cover_choice",
        "caller": "choose_covers",
        "instructions": instructions,
        "input": f'The book: "{title}" by {author}. The sheets show {len(shown)} candidates, numbered across the sheets:\n{listed}{barred}',
        "schema": SCHEMA,
        "images": [str(sheet) for sheet in sheets],
    })


def second_look(seat, title, author, candidate, instructions):
    return gateway({
        "seat": seat,
        "purpose": "cover_check",
        "caller": "choose_covers",
        "instructions": instructions,
        "input": f'The book: "{title}" by {author}. The image\'s file name on Commons: {candidate["title"]}. Found in the category: {candidate.get("foundIn") or "none"}.',
        "schema": SECOND_SCHEMA,
        "images": [candidate["file"]],
    })


def judged(seat, offered, folder, instructions, look):
    """One round of picking from what was offered: (the candidate chosen or None, the crop, the reasons, the cost, the model)."""
    sheets, shown = contact_sheets(offered["candidates"], folder)
    if not sheets:
        return None, (50, 50), ["No candidate could be downloaded."], 0.0, None
    spent, struck, notes, model = 0.0, set(), [], None
    for _ in range(PICKS):
        verdict, said = pick(seat, offered["title"], offered["author"], shown, sheets, struck, instructions)
        spent += said.get("cost_usd") or 0
        model = f"{said.get('provider')}:{said.get('model')}"[:120]
        number = verdict["pick"]
        if not 1 <= number <= len(shown) or number in struck:
            notes.append(verdict["reason"])
            break
        checked, said = second_look(seat, offered["title"], offered["author"], shown[number - 1], look)
        spent += said.get("cost_usd") or 0
        if checked["acceptable"]:
            chosen = {key: value for key, value in shown[number - 1].items() if key != "file"}
            return chosen, (checked["focus_x"], checked["focus_y"]), [verdict["reason"]], spent, model
        struck.add(number)
        notes.append(f"{number} rejected at full size: {checked['reason']}")
    return None, (50, 50), notes, spent, model


def choose(backend, token, seat, offered, folder):
    """The decision for one work, with what the gateway recorded as its cost.

    First among the pictures made for the book. When there are none, or none is good, among period pictures that
    suit it: the model proposes what to search Commons for, and the backend answers with what is free to take.
    """
    decision = {"workSlug": offered["workSlug"], "chosen": None, "focusX": 50, "focusY": 50, "reason": offered["note"], "model": None}
    spent, notes = 0.0, [offered["note"]] if offered["note"] else []
    if offered["candidates"]:
        own = folder / "own"
        own.mkdir()
        chosen, focus, said, cost, model = judged(seat, offered, own, INSTRUCTIONS, SECOND_LOOK)
        spent += cost
        notes = said
        decision["model"] = model
        if chosen:
            return {**decision, "chosen": chosen, "focusX": focus[0], "focusY": focus[1], "reason": said[0][:1000]}, spent
    proposed, said = gateway({
        "seat": seat,
        "purpose": "cover_queries",
        "caller": "choose_covers",
        "instructions": QUERIES,
        "input": f'The book: "{offered["title"]}" by {offered["author"]}.',
        "schema": QUERIES_SCHEMA,
    })
    spent += said.get("cost_usd") or 0
    fitting = call(backend, token, "POST", "/internal/covers/fitting", {"workSlug": offered["workSlug"], "queries": proposed["queries"][:6]})
    if fitting["candidates"]:
        suited = folder / "fitting"
        suited.mkdir()
        chosen, focus, said, cost, model = judged(seat, fitting, suited, FITTING, FITTING_LOOK)
        spent += cost
        decision["model"] = model
        if chosen:
            return {**decision, "chosen": chosen, "focusX": focus[0], "focusY": focus[1], "reason": ("Not made for the book. " + said[0])[:1000]}, spent
        notes += said
    else:
        notes.append(fitting["note"])
    return {**decision, "reason": " ".join(note for note in notes if note)[:1000]}, spent


def main():
    parser = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    parser.add_argument("--backend", default="http://localhost:9998/v1", help="the backend's API root")
    parser.add_argument("--seat", default=SEAT, help="the gateway seat that looks at the sheet; only a claude-code seat is shown images")
    parser.add_argument("--limit", type=int, default=0, help="stop after this many works")
    parser.add_argument("--only", default="", help="comma-separated work slugs, looked at again even if already decided")
    parser.add_argument("--all", action="store_true", help="every work on the shelf, decided or not")
    args = parser.parse_args()
    token = backend_env.publisher_secret()

    works = [slug.strip() for slug in args.only.split(",") if slug.strip()]
    if not works:
        works = call(args.backend, token, "POST", "/internal/covers/" + ("works" if args.all else "pending"), {})
    if args.limit:
        works = works[: args.limit]
    print(f"{len(works)} works to look at")
    counts = {"APPROVED": 0, "NONE": 0, "failed": 0}
    spent = 0.0
    for number, slug in enumerate(works, 1):
        try:
            offered = call(args.backend, token, "POST", "/internal/covers/candidates", {"workSlug": slug})
            with tempfile.TemporaryDirectory(prefix="almonium-covers-") as folder:
                decision, cost = choose(args.backend, token, args.seat, offered, Path(folder))
            spent += cost
            status = call(args.backend, token, "PUT", "/internal/covers/decisions", decision)["status"]
            counts[status] += 1
            print(f"[{number}/{len(works)}] {slug}: {status} ({len(offered['candidates'])} candidates) {decision['reason'] or ''}"[:260], flush=True)
        except Held as held:
            print(f"The gateway is holding calls: {held}\nStopped; {len(works) - number + 1} works stay as they were.")
            sys.exit(HELD)
        except Exception as broken:  # one work's failure leaves it as it was for the next run and does not stop the rest
            counts["failed"] += 1
            print(f"[{number}/{len(works)}] {slug}: failed, left as it was: {str(broken)[:200]}", flush=True)
    print(f"Done: {counts['APPROVED']} covers, {counts['NONE']} without art, {counts['failed']} failed. Plan cost recorded: ${spent:.2f}.")


if __name__ == "__main__":
    main()
