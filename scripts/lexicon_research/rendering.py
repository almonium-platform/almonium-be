"""One compact plain-text view of an entry, the same for every model, so formatting cannot sway a judge."""
import json


def render(e):
    if not e.get("lexemes"):
        return f"REJECTED: {e.get('reject', '')}"
    out = []
    for lx in e["lexemes"]:
        out.append(f"{lx['lemma']} [{lx['pos']}] grammar={json.dumps(lx.get('grammar', {}), ensure_ascii=False)}")
        out.append("  ipa: " + "; ".join(f"{p.get('variety')} {p.get('ipa')}" for p in lx.get("pronunciations", [])))
        out.append("  forms: " + ", ".join(f"{f.get('form')}({','.join(f'{k}={v}' for k, v in (f.get('features') or {}).items())})" for f in lx.get("forms", [])))
        for i, s in enumerate(lx.get("senses", []), 1):
            gl = ", ".join(f"{k}={v if isinstance(v, str) else (v or {}).get('text', '') + ('' if (v or {}).get('exact', True) else '~')}" for k, v in (s.get("glosses") or {}).items())
            out.append(f"  {i}. {s.get('definition')} [{s.get('cefr')}, {s.get('register')}] glosses: {gl}")
            out.append(f"     example: {s.get('example')}")
            out.append(f"     cues: {' | '.join(c.get('text', '') for c in s.get('cues', []))}")
        if lx.get("relations"):
            out.append("  relations: " + ", ".join(f"{r.get('type')}:{r.get('lemma')}" for r in lx["relations"]))
    return "\n".join(out)
