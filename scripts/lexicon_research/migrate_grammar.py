"""Rewrite the grammar vocabulary of every written entry onto the schema-2 closed set. Reports what it mapped and what it could not."""
import json, glob, re, sys
from collections import Counter
import jsonschema
ROOT = "scripts/lexicon_research"
SCHEMA = json.load(open(f"{ROOT}/entry.schema.json"))
FEAT = SCHEMA["$defs"]["form"]["properties"]["features"]["properties"]
GENDER = {"masc": "Masc", "masculine": "Masc", "m": "Masc", "fem": "Fem", "feminine": "Fem", "f": "Fem", "neut": "Neut", "neuter": "Neut", "n": "Neut", "common": "Com", "com": "Com", "c": "Com"}
AUX = {"haben": "have", "avoir": "have", "avere": "have", "ter": "have", "have": "have", "sein": "be", "être": "be", "essere": "be", "ser": "be", "be": "be", "both": "both"}
CONJ = [(r"^(1|1st|1er|first)\b|-ar\b|-are\b|-er\b.*(group|groupe)|^-er$", "1"), (r"^(2|2nd|2e|second)\b|-ere\b|^-er\b|-ir\b.*(iss|2)", "2"), (r"^(3|3rd|3e|third)\b|-ire\b|^-ir\b|-re\b", "3"),
        (r"^strong|^stark", "strong"), (r"^weak|^schwach", "weak"), (r"^mixed|^gemischt", "mixed")]
counts = Counter(); notes = Counter(); dropped = Counter(); failures = []
def norm_gender(v):
    v = str(v).strip(); low = v.lower()
    if v in ("Masc", "Fem", "Neut", "Com"): return v, None
    if low in GENDER: return GENDER[low], None
    if re.search(r"(masc|fem).*(,|/| or | and ).*(masc|fem)", low): return "Com", None
    return None, v
def conj(v):
    low = str(v).strip().lower()
    if low in ("1", "2", "3", "strong", "weak", "mixed"): return low, None
    for pat, out in CONJ:
        if re.search(pat, low): return out, None
    return None, v
FORMS = []
def grammar(lang, g, lemma, forms=()):
    global FORMS; FORMS = list(forms)
    out, note = {}, []
    GP = SCHEMA["$defs"]["lexeme"]["properties"]["grammar"]["properties"]
    for k, v in g.items():
        kl = k.lower(); counts[f"g:{kl}"] += 1
        if k in GP and k != "note":
            try: jsonschema.validate(v, GP[k]); out[k] = v; continue  # already in the closed vocabulary
            except jsonschema.ValidationError: pass
        if kl == "gender":
            m, rest = norm_gender(v); (out.__setitem__("gender", m) if m else note.append(f"gender: {rest}"))
        elif kl in ("countability", "countable", "uncountable"):
            s = str(v).lower()
            if kl == "uncountable" and v is True: out["countability"] = "Mass"
            elif kl == "countable" and v is True or s == "countable": out["countability"] = "Count"
            elif kl == "countable" and v is False or s == "uncountable": out["countability"] = "Mass"
            elif "both" in s or ("count" in s and "uncount" in s): out["countability"] = "Both"
            else: note.append(f"{k}: {v}")
        elif kl in ("irregular", "separable", "reflexive", "invariable", "comparable", "indeclinable") and isinstance(v, bool):
            out["invariable" if kl == "indeclinable" else kl] = v
        elif kl == "separable" and isinstance(v, str):
            out["separable"] = True; out["separable_prefix"] = v
        elif kl in ("separable_prefix", "prefix"): out["separable_prefix"] = str(v)
        elif kl == "auxiliary":
            s = str(v).lower(); hit = {AUX[w] for w in re.findall(r"[a-zêé]+", s) if w in AUX}
            if len(hit) == 2 or "both" in hit: out["auxiliary"] = "both"
            elif hit: out["auxiliary"] = hit.pop()
            else: note.append(f"auxiliary: {v}")
            if re.search(r"[(:;]", s): note.append(f"auxiliary: {v}")
        elif kl in ("conjugation", "conjugation_class"):
            m, rest = conj(v); (out.__setitem__("conjugation", m) if m else note.append(f"conjugation: {rest}"))
        elif kl == "plural_class":
            s = str(v).strip().replace("¨", "¨")
            if s == "-e with umlaut": s = "-¨e"
            if re.match(r"^none|^no plural|singular", s.lower()): out["number"] = "Sing"
            elif re.match(r"^-¨?(e|en|n|er|s|nen)?$", s): out["plural_class"] = s
            else: note.append(f"plural: {v}")
        elif kl == "declension":
            s = str(v).lower()
            if s in ("1", "2", "3", "4", "weak", "strong", "mixed", "adjectival"): out["declension"] = s
            else: note.append(f"declension: {v}")
        elif kl == "aspect":
            s = str(v).lower(); out["aspect"] = "Perf" if s.startswith("perf") else "Imp" if s.startswith("imp") else note.append(f"aspect: {v}") or out.get("aspect")
            if out.get("aspect") is None: out.pop("aspect", None)
        elif kl == "aspect_pair": out["aspect_pair"] = str(v)
        elif kl == "animacy":
            s = str(v).lower(); out["animacy"] = "Anim" if s.startswith("anim") else "Inan" if s.startswith("inan") else None
            if out["animacy"] is None: out.pop("animacy"); note.append(f"animacy: {v}")
        elif kl == "plurale_tantum" and v: out["number"] = "Plur"
        elif kl == "singulare_tantum" and v: out["number"] = "Sing"
        elif kl == "number" and str(v) in ("Sing", "Plur"): out["number"] = str(v)
        elif kl == "attributive_only" and v: out["position"] = "Attr"
        elif kl == "predicative_only" and v: out["position"] = "Pred"
        elif kl == "position":
            s = str(v).lower(); out["position"] = "Attr" if s.startswith("attr") else "Pred" if s.startswith("pred") else None
            if out["position"] is None: out.pop("position"); note.append(f"position: {v}")
        elif kl == "valency" and str(v) in ("Tr", "Intr", "Both", "Refl", "Impers"): out["valency"] = str(v)
        elif kl == "note": note.append(str(v))
        elif kl == "plural" and any((f.get("features") or {}).get("Number") == "Plur" for f in FORMS): pass  # the plural is already a form
        else: note.append(f"{k}: {v}")
    if note:
        out["note"] = "; ".join(note)[:120]; notes[lang] += 1
    return out
PD = [(r"^(transitiv|transitive)$", ("valency", "Tr")), (r"^(intransitiv|intransitive)$", ("valency", "Intr")),
      (r"^(transitive|intransitive)( or | and |, |/)(transitive|intransitive)$", ("valency", "Both")), (r"^reflexive$|^reflexiv$", ("valency", "Refl")), (r"^impersonal$", ("valency", "Impers")),
      (r"^countable$|^zählbar$", ("countability", "Count")), (r"^uncountable$|^unzählbar$", ("countability", "Mass")), (r"^countable( or | and |, |/)uncountable$|^uncountable( or | and |, |/)countable$", ("countability", "Both")),
      (r"^(attributive( only)?|only before (a )?noun|before (a )?noun|nur attributiv)$", ("position", "Attr")), (r"^(predicative( only)?|nur prädikativ)$", ("position", "Pred")),
      (r"^(plural|plural only|plurale tantum|nur Plural)$", ("number", "Plur")), (r"^(singular|singular only|nur Singular)$", ("number", "Sing")),
      (r"(perfekt )?(mit|with) sein$", ("auxiliary", "be")), (r"(perfekt )?(mit|with) haben$", ("auxiliary", "have")),
      (r"^(transitif|transitivo|transitiva)$", ("valency", "Tr")), (r"^(intransitif|intransitivo|intransitiva)$", ("valency", "Intr")),
      (r"^(pronominal|verbo pronominale|verbe pronominal|riflessivo|reflexivo|réfléchi)$", ("valency", "Refl"))]
def sense_detail(s):
    pd = s.pop("pos_detail", None)
    if not pd: return
    g, usage = s.get("grammar", {}), []
    pieces = [p.strip() for p in re.split(r",\s*|;\s*", str(pd)) if p.strip()]
    whole = str(pd).strip().lower()
    for pat, (k, v) in PD:
        if re.search(pat, whole): g[k] = v; pieces = []; break
    for piece in pieces:
        low = piece.lower(); hit = False
        for pat, (k, v) in PD:
            if re.search(pat, low): g[k] = v; hit = True; break
        if not hit: usage.append(piece)
    if g: s["grammar"] = g
    if usage: s["usage"] = ", ".join(usage)[:60]; counts["usage"] += 1
    counts["pos_detail"] += 1
def features(f):
    out = {}
    for k, v in (f.get("features") or {}).items():
        if k not in FEAT: dropped[f"key {k}"] += 1; continue
        if k == "Person":
            out[k] = int(v) if str(v) in ("1", "2", "3") else dropped.update([f"Person={v}"]) or None
            if out[k] is None: out.pop(k)
            continue
        if re.match(FEAT[k]["pattern"], str(v)): out[k] = str(v)
        else: dropped[f"{k}={v}"] += 1
    f["features"] = out
changed = 0; total = 0
files = glob.glob(f"{ROOT}/out/cc-*/*/*.json") + glob.glob(f"{ROOT}/out/codex-*/*/*.json")
for path in files:
    if path.endswith(".meta.json"): continue
    try: e = json.load(open(path))
    except Exception: continue
    if "lexemes" not in e: continue
    total += 1; before = json.dumps(e, sort_keys=True, ensure_ascii=False); lang = e.get("language")
    for lx in e["lexemes"]:
        if isinstance(lx.get("grammar"), dict): lx["grammar"] = grammar(lang, lx["grammar"], lx.get("lemma"), lx.get("forms", []))
        for f in lx.get("forms", []): features(f)
        for s in lx.get("senses", []): sense_detail(s)
        for r in lx.get("relations", []):
            if r.get("pos") not in SCHEMA["$defs"]["lexeme"]["properties"]["pos"]["enum"]: dropped[f"relation pos {r.get('pos')}"] += 1; r["pos"] = "NOUN" if not r.get("pos") else r["pos"]
    try: jsonschema.validate(e, SCHEMA)
    except jsonschema.ValidationError as ex: failures.append(f"{path.split('/out/')[1]}: {ex.message[:90]}")
    after = json.dumps(e, sort_keys=True, ensure_ascii=False)
    if after != before and "--write" in sys.argv:
        json.dump(e, open(path, "w"), ensure_ascii=False, indent=1); changed += 1
    elif after != before: changed += 1
print(f"entries {total}, changed {changed}, still failing schema {len(failures)}")
print("notes per language:", dict(notes)); print("senses with pos_detail mapped:", counts["pos_detail"], "of which kept a usage label:", counts["usage"])
print("dropped features:", dropped.most_common(15))
print("failures sample:"); print("\n".join(failures[:25]))
