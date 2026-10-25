"""Recompute pilot numbers from saved receipts. Never invokes a provider."""

import json
from collections import Counter
from statistics import mean

import review_pilot as pilot


def build(root=pilot.OUT):
    result = {
        "cost_basis": "allocated plan USD; Claude 0.03, Codex 0.07 of list",
        "quality_warning": "Review verdicts and editor checkpoints are model assessments, "
        "not measured human accuracy.",
        "writers": {},
        "reviewers": {},
        "correction_arms": {},
        "cases": [],
    }
    for writer in pilot.MODELS:
        generations, totals, verdicts, rows = [], [], Counter(), []
        for case in pilot.cases():
            path = pilot.ref_path(root, "generate", case, writer)
            if not path.exists():
                continue
            generation = pilot.load_ref(path, root)
            cost = generation["cost"]["estimated_plan_usd"]
            generations.append(cost)
            entry = generation["answer"] or {}
            row = {
                "case": case["id"],
                "writer": writer,
                "generation_plan_usd": cost,
                "senses": sum(len(lexeme["senses"]) for lexeme in entry.get("lexemes", [])),
                "syntax_repaired": generation.get("syntax_repair", False),
                "schema_errors": generation["validation_errors"],
                "reviews": {},
            }
            for judge in pilot.MODELS:
                if judge == writer:
                    continue
                p = pilot.ref_path(root, "review", case, writer, judge)
                if not p.exists():
                    continue
                review = pilot.load_ref(p, root)
                answer = review["answer"] or {}
                row["reviews"][judge] = {
                    "verdict": answer.get("verdict"),
                    "material_findings": sum(
                        f["severity"] == "material" for f in answer.get("findings", [])
                    ),
                    "all_findings": len(answer.get("findings", [])),
                    "plan_usd": review["cost"]["estimated_plan_usd"],
                    "schema_errors": review["validation_errors"],
                }
            if len(row["reviews"]) == 2:
                total = cost + sum(r["plan_usd"] for r in row["reviews"].values())
                totals.append(total)
                row["generation_plus_two_reviews_plan_usd"] = total
                pair = sorted(r["verdict"] or "invalid" for r in row["reviews"].values())
                verdicts[" / ".join(pair)] += 1
            rows.append(row)
        if generations:
            result["writers"][writer] = {
                "entries": len(generations),
                "mean_generation_plan_usd": mean(generations),
                "complete_panels": len(totals),
                "mean_generation_plus_two_reviews_plan_usd": mean(totals) if totals else None,
                "matched_cost_multiplier": (
                    sum(totals)
                    / sum(r["generation_plan_usd"] for r in rows if len(r["reviews"]) == 2)
                )
                if totals
                else None,
                "review_verdict_pairs": dict(verdicts),
            }
        result["cases"].extend(rows)
    for judge in pilot.MODELS:
        reviews = [r["reviews"][judge] for r in result["cases"] if judge in r["reviews"]]
        if reviews:
            result["reviewers"][judge] = {
                "n": len(reviews),
                "mean_plan_usd": mean(r["plan_usd"] for r in reviews),
                "verdicts": dict(Counter(r["verdict"] for r in reviews)),
                "material_findings": sum(r["material_findings"] for r in reviews),
                "all_findings": sum(r["all_findings"] for r in reviews),
            }
    for writer in pilot.MODELS:
        arms = []
        for row in result["cases"]:
            if row["writer"] != writer:
                continue
            case = next(c for c in pilot.cases() if c["id"] == row["case"])
            p = pilot.ref_path(root, "amend", case, writer)
            if not p.exists():
                continue
            ref = json.loads(p.read_text())
            edited = pilot.load_ref(p, root)
            judge = next(j for j in pilot.MODELS if j not in [writer, ref["editor"]])
            verified_path = pilot.ref_path(root, "verify", case, writer, judge)
            verified = pilot.load_ref(verified_path, root) if verified_path.exists() else None
            panel_cost = row.get("generation_plus_two_reviews_plan_usd", 0)
            arm = {
                "case": row["case"],
                "editor": ref["editor"],
                "edit_plan_usd": edited["cost"]["estimated_plan_usd"],
                "decision_counts": dict(
                    Counter(d["decision"] for d in edited["answer"]["decisions"])
                ),
                "schema_errors": ref["entry_validation_errors"],
                "unresolved": edited["answer"]["unresolved"],
                "checkpoint_results": edited["answer"]["checkpoint_results"],
                "before_senses": row["senses"],
                "after_senses": sum(
                    len(lexeme["senses"]) for lexeme in ref["revised_entry"]["lexemes"]
                ),
            }
            if verified:
                arm.update(
                    final_verdict=(verified["answer"] or {}).get("verdict"),
                    verify_plan_usd=verified["cost"]["estimated_plan_usd"],
                    total_plan_usd=panel_cost
                    + edited["cost"]["estimated_plan_usd"]
                    + verified["cost"]["estimated_plan_usd"],
                )
            arms.append(arm)
        if arms:
            result["correction_arms"][writer] = arms
    calls = list((root / "calls").glob("*/result.json"))
    result["completed_paid_calls"] = len(calls)
    result["total_estimated_plan_usd"] = sum(
        json.loads(p.read_text())["cost"]["estimated_plan_usd"] for p in calls
    )
    pilot.save(root / "comparison.json", result)
    return result


if __name__ == "__main__":
    report = build()
    print(
        json.dumps(
            {k: v for k, v in report.items() if k not in ["cases", "correction_arms"]}, indent=2
        )
    )
