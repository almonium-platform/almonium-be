package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.dto.request.EntryPublication;
import com.almonium.learning.lexicon.dto.response.EntryPublicationResult;
import com.almonium.learning.lexicon.model.EntryDocument;
import com.almonium.learning.lexicon.model.EntryDocument.CueDocument;
import com.almonium.learning.lexicon.model.EntryDocument.FormDocument;
import com.almonium.learning.lexicon.model.EntryDocument.LexemeDocument;
import com.almonium.learning.lexicon.model.EntryDocument.PronunciationDocument;
import com.almonium.learning.lexicon.model.EntryDocument.RelationDocument;
import com.almonium.learning.lexicon.model.EntryDocument.SenseDocument;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.LexemeForm;
import com.almonium.learning.lexicon.model.entity.LexemeRelation;
import com.almonium.learning.lexicon.model.entity.LexiconBuild;
import com.almonium.learning.lexicon.model.entity.Pronunciation;
import com.almonium.learning.lexicon.model.entity.RejectedHeadword;
import com.almonium.learning.lexicon.model.entity.Sense;
import com.almonium.learning.lexicon.model.entity.SenseCue;
import com.almonium.learning.lexicon.model.entity.SenseExample;
import com.almonium.learning.lexicon.model.entity.SenseGloss;
import com.almonium.learning.lexicon.model.enums.LexiconStatus;
import com.almonium.learning.lexicon.repository.LexemeRelationRepository;
import com.almonium.learning.lexicon.repository.LexemeRepository;
import com.almonium.learning.lexicon.repository.RejectedHeadwordRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;

/**
 * Takes one published entry into the lexicon. The entry replaces whatever stood for its headword: each lexeme is
 * matched by (language, lemma, pos, homograph number) and rewritten with its forms, senses and relations; lexemes
 * of the headword the entry no longer lists are deleted. Pronunciations are the exception: audio clips hang off
 * them, so an existing one is updated in place when the new entry has the same variety for the same form. Every
 * publish adds a build row per lexeme, which is where provenance is read from.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LexiconPublicationService {
    private static final String CEFR_BY_WRITER = "writer";
    private static final int COST_SCALE = 6;

    private final LexemeRepository lexemeRepository;
    private final LexemeRelationRepository relationRepository;
    private final RejectedHeadwordRepository rejectedHeadwordRepository;

    public EntryPublicationResult publish(EntryPublication publication) {
        EntryDocument entry = publication.entry();
        String language = entry.language();
        String headword = entry.headword();
        Instant now = Instant.now();

        List<Lexeme> standing = lexemeRepository.findByLanguageAndHeadword(language, headword);
        List<Placement> placements = new ArrayList<>();
        Map<String, Integer> homographs = new HashMap<>();
        for (LexemeDocument document : entry.lexemes()) {
            int homographNo = homographs.merge(document.lemma() + "\u0000" + document.pos(), 1, Integer::sum);
            Lexeme lexeme = lexemeRepository
                    .findByLanguageAndLemmaAndPosAndHomographNo(language, document.lemma(), document.pos(), homographNo)
                    .orElseGet(Lexeme::new);
            placements.add(new Placement(lexeme, document, homographNo));
        }
        for (Lexeme old : standing) {
            if (placements.stream().noneMatch(placement -> placement.lexeme() == old)) {
                lexemeRepository.deleteTree(old.getId());
            }
        }
        // Old senses go first and alone: a new sense at the same position would otherwise be inserted before them.
        for (Placement placement : placements) {
            if (placement.lexeme().getId() != null) {
                placement.lexeme().getSenses().clear();
                placement.lexeme().getRelations().clear();
                placement.lexeme().getParts().clear();
            }
        }
        lexemeRepository.flush();

        for (Placement placement : placements) {
            write(placement.lexeme(), placement.document(), entry, placement.homographNo(), publication, now);
            lexemeRepository.save(placement.lexeme());
        }
        lexemeRepository.flush();
        for (Placement placement : placements) {
            Lexeme lexeme = placement.lexeme();
            relationRepository.pointDanglingAt(lexeme.getId(), language, lexeme.getLemma(), lexeme.getPos());
        }
        recordVerdict(entry, publication, now);
        return EntryPublicationResult.accepted(language, headword, placements.size());
    }

    /** A lexeme of the entry, the row it lands on (new or standing) and its number among same-lemma homographs. */
    private record Placement(Lexeme lexeme, LexemeDocument document, int homographNo) {}

    private void write(
            Lexeme lexeme,
            LexemeDocument document,
            EntryDocument entry,
            int homographNo,
            EntryPublication publication,
            Instant now) {
        boolean fresh = lexeme.getId() == null;
        lexeme.setLanguage(entry.language());
        lexeme.setHeadword(entry.headword());
        lexeme.setLemma(document.lemma());
        lexeme.setPos(document.pos());
        lexeme.setHomographNo(homographNo);
        lexeme.setKind(document.kind());
        lexeme.setGrammar(new LinkedHashMap<>(document.grammar()));
        lexeme.setPattern(document.pattern());
        lexeme.setZipf(
                publication.zipf() == null
                        ? null
                        : BigDecimal.valueOf(publication.zipf()).setScale(2, RoundingMode.HALF_UP));
        lexeme.setEntryVersion(fresh ? 1 : lexeme.getEntryVersion() + 1);
        lexeme.setStatus(LexiconStatus.PROVISIONAL);
        lexeme.setUpdatedAt(now);
        if (fresh) {
            lexeme.setCreatedAt(now);
        }
        writeFormsAndPronunciations(lexeme, document, publication.writer());
        for (SenseDocument sense : document.senses()) {
            lexeme.addSense(sense(sense));
        }
        for (RelationDocument relation : document.relations()) {
            LexemeRelation link = new LexemeRelation();
            link.setType(relation.type());
            link.setTargetLemma(relation.lemma());
            link.setTargetPos(relation.pos());
            link.setEvidence(relation.why());
            link.setTarget(lexemeRepository
                    .findByLanguageAndLemmaAndPosAndHomographNo(entry.language(), relation.lemma(), relation.pos(), 1)
                    .orElse(null));
            lexeme.addRelation(link);
        }
        lexeme.addBuild(build(publication, now));
    }

    /**
     * Forms are rewritten; pronunciations are matched to the new forms by variety and spelling (the n-th form spelled
     * alike takes the n-th matching pronunciation) and updated where they stand, so a clip recorded for one stays
     * attached. A changed transcription drops the row back to provisional. Pronunciations the entry no longer has go
     * first, in a flush of their own while their forms still exist: were they removed together with the forms,
     * Hibernate would null their form before deleting them, and a pronunciation with no form is the lexeme's own.
     */
    private void writeFormsAndPronunciations(Lexeme lexeme, LexemeDocument document, String writer) {
        Map<String, Pronunciation> existing = new HashMap<>();
        Map<String, Integer> seen = new HashMap<>();
        Map<LexemeForm, String> formKeys = new HashMap<>();
        for (LexemeForm form : lexeme.getForms()) {
            formKeys.put(form, form.getForm() + "#" + seen.merge(form.getForm(), 1, Integer::sum));
        }
        for (Pronunciation pronunciation : lexeme.getPronunciations()) {
            String formKey = pronunciation.getForm() == null ? "" : formKeys.get(pronunciation.getForm());
            existing.put(key(pronunciation.getVariety(), pronunciation.getRank(), formKey), pronunciation);
        }
        Map<String, Pronunciation> stale = new HashMap<>(existing);
        stale.keySet().removeAll(wantedPronunciations(document));
        if (!stale.isEmpty()) {
            lexeme.getPronunciations().removeAll(stale.values());
            existing.keySet().removeAll(stale.keySet());
            lexemeRepository.flush();
        }

        lexeme.getForms().clear();
        Map<String, Integer> ranks = new HashMap<>();
        for (PronunciationDocument spoken : document.pronunciations()) {
            int rank = ranks.merge(spoken.variety(), 1, Integer::sum);
            place(
                    lexeme,
                    existing.remove(key(spoken.variety(), rank, "")),
                    null,
                    spoken.variety(),
                    rank,
                    spoken.ipa(),
                    writer);
        }
        seen.clear();
        for (FormDocument formDocument : document.forms()) {
            LexemeForm form = new LexemeForm();
            form.setForm(formDocument.form());
            form.setNormalizedForm(WordNormalizer.normalize(formDocument.form()));
            form.setFeatures(new LinkedHashMap<>(formDocument.features()));
            lexeme.addForm(form);
            String formKey = formDocument.form() + "#" + seen.merge(formDocument.form(), 1, Integer::sum);
            for (Map.Entry<String, String> ipa : formDocument.ipa().entrySet()) {
                place(
                        lexeme,
                        existing.remove(key(ipa.getKey(), 1, formKey)),
                        form,
                        ipa.getKey(),
                        1,
                        ipa.getValue(),
                        writer);
            }
        }
    }

    /**
     * The keys the entry's pronunciations will stand under: "variety#rank|" for the lexeme's own, numbered within
     * the variety in the entry's order, and "variety#1|form#n" for the n-th form spelled alike.
     */
    private static Set<String> wantedPronunciations(LexemeDocument document) {
        Set<String> wanted = new HashSet<>();
        Map<String, Integer> ranks = new HashMap<>();
        for (PronunciationDocument spoken : document.pronunciations()) {
            wanted.add(key(spoken.variety(), ranks.merge(spoken.variety(), 1, Integer::sum), ""));
        }
        Map<String, Integer> seen = new HashMap<>();
        for (FormDocument form : document.forms()) {
            String formKey = form.form() + "#" + seen.merge(form.form(), 1, Integer::sum);
            for (String variety : form.ipa().keySet()) {
                wanted.add(key(variety, 1, formKey));
            }
        }
        return wanted;
    }

    private static String key(String variety, int rank, String formKey) {
        return variety + "#" + rank + "|" + formKey;
    }

    private Pronunciation place(
            Lexeme lexeme,
            Pronunciation existing,
            LexemeForm form,
            String variety,
            int rank,
            String phonemic,
            String writer) {
        Pronunciation pronunciation = existing != null ? existing : lexeme.addPronunciation(new Pronunciation());
        if (existing == null || !phonemic.equals(existing.getPhonemic())) {
            pronunciation.setStatus(LexiconStatus.PROVISIONAL);
        }
        pronunciation.setForm(form);
        pronunciation.setVariety(variety);
        pronunciation.setRank(rank);
        pronunciation.setPhonemic(phonemic);
        pronunciation.setSource(writer);
        return pronunciation;
    }

    private Sense sense(SenseDocument document) {
        Sense sense = new Sense();
        sense.setDefinition(document.definition());
        sense.setCefr(document.cefr());
        sense.setCefrSource(document.cefr() == null ? null : CEFR_BY_WRITER);
        sense.setRegister(document.register());
        sense.setGrammar(document.grammar() == null ? null : new LinkedHashMap<>(document.grammar()));
        sense.setUsage(document.usage());
        for (Map.Entry<String, JsonNode> gloss : document.glosses().entrySet()) {
            SenseGloss rendered = new SenseGloss();
            rendered.setFluentLanguage(gloss.getKey());
            rendered.setRank(1);
            JsonNode value = gloss.getValue();
            if (value.isString()) {
                rendered.setGloss(value.stringValue());
            } else {
                rendered.setGloss(value.path("text").asString(""));
                rendered.setExact(value.path("exact").asBoolean(true));
            }
            sense.addGloss(rendered);
        }
        if (document.example() != null) {
            SenseExample example = new SenseExample();
            example.setText(document.example());
            example.setOrigin(SenseExample.GENERATED);
            sense.addExample(example);
        }
        for (CueDocument cue : document.cues()) {
            SenseCue prompt = new SenseCue();
            prompt.setKind(cue.kind());
            prompt.setText(cue.text());
            sense.addCue(prompt);
        }
        return sense;
    }

    private LexiconBuild build(EntryPublication publication, Instant now) {
        Map<String, Object> meta = publication.meta();
        LexiconBuild build = new LexiconBuild();
        build.setWriter(publication.writer());
        build.setModel(text(meta, "model"));
        build.setPromptVersion(text(meta, "prompt_version"));
        build.setRegisterVersion(text(meta, "register_version"));
        build.setSchemaVersion(publication.schemaVersion());
        build.setEffort(text(meta, "effort"));
        build.setListCostUsd(decimal(meta, "total_cost_usd"));
        build.setRelabelCostUsd(decimal(meta, "relabel_cost_usd"));
        build.setInputTokens(integer(meta, "input_tokens"));
        build.setOutputTokens(integer(meta, "output_tokens"));
        build.setCacheReadTokens(integer(meta, "cache_read"));
        build.setCacheWriteTokens(integer(meta, "cache_write"));
        build.setMeta(meta.isEmpty() ? null : new LinkedHashMap<>(meta));
        build.setBuiltAt(publication.builtAt() != null ? publication.builtAt() : now);
        return build;
    }

    /** A headword the writer refused stays refused until an entry with lexemes arrives for it. */
    private void recordVerdict(EntryDocument entry, EntryPublication publication, Instant now) {
        RejectedHeadword rejected = rejectedHeadwordRepository
                .findByLanguageAndHeadword(entry.language(), entry.headword())
                .orElse(null);
        if (!entry.lexemes().isEmpty() || entry.reject() == null) {
            if (rejected != null) {
                rejectedHeadwordRepository.delete(rejected);
            }
            return;
        }
        if (rejected == null) {
            rejected = new RejectedHeadword();
            rejected.setLanguage(entry.language());
            rejected.setHeadword(entry.headword());
        }
        rejected.setReason(entry.reject());
        rejected.setWriter(publication.writer());
        rejected.setModel(text(publication.meta(), "model"));
        rejected.setPromptVersion(text(publication.meta(), "prompt_version"));
        rejected.setRejectedAt(publication.builtAt() != null ? publication.builtAt() : now);
        rejectedHeadwordRepository.save(rejected);
    }

    private static String text(Map<String, Object> meta, String key) {
        Object value = meta.get(key);
        return value == null ? null : value.toString();
    }

    private static Integer integer(Map<String, Object> meta, String key) {
        return meta.get(key) instanceof Number number ? number.intValue() : null;
    }

    private static BigDecimal decimal(Map<String, Object> meta, String key) {
        return meta.get(key) instanceof Number number
                ? new BigDecimal(number.toString()).setScale(COST_SCALE, RoundingMode.HALF_UP)
                : null;
    }
}
