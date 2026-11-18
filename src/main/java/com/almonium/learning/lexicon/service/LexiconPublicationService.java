package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.dto.request.EntryPublication;
import com.almonium.learning.lexicon.dto.response.EntryPublicationResult;
import com.almonium.learning.lexicon.model.EntryDocument;
import com.almonium.learning.lexicon.model.EntryDocument.FormDocument;
import com.almonium.learning.lexicon.model.EntryDocument.LexemeDocument;
import com.almonium.learning.lexicon.model.EntryDocument.PronunciationDocument;
import com.almonium.learning.lexicon.model.EntryDocument.RelationDocument;
import com.almonium.learning.lexicon.model.EntryDocument.SenseDocument;
import com.almonium.learning.lexicon.model.entity.EntryAlias;
import com.almonium.learning.lexicon.model.entity.EntryVersion;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.LexemeForm;
import com.almonium.learning.lexicon.model.entity.LexemeRelation;
import com.almonium.learning.lexicon.model.entity.LexiconBuild;
import com.almonium.learning.lexicon.model.entity.Pronunciation;
import com.almonium.learning.lexicon.model.entity.RejectedHeadword;
import com.almonium.learning.lexicon.model.entity.Sense;
import com.almonium.learning.lexicon.model.entity.SenseExample;
import com.almonium.learning.lexicon.model.enums.LexiconStatus;
import com.almonium.learning.lexicon.repository.EntryAliasRepository;
import com.almonium.learning.lexicon.repository.EntryVersionRepository;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Takes one published entry into the lexicon. The entry replaces whatever stood for its headword: each lexeme is
 * matched by (language, lemma, pos, homograph number) and rewritten with its forms, senses and relations; lexemes
 * of the headword the entry no longer lists are deleted, and so are the spellings it answered for, which are
 * written anew. Pronunciations are the exception: audio clips hang off them, so an existing one is updated in place
 * when the new entry has the same variety for the same form. Every publish adds a build row per lexeme, which is
 * where provenance is read from, and one entry version, the whole entry as published. Senses are recreated, and each
 * is keyed by the id the entry gives it: the ingest step owns that id and keeps it across rewrites, so a card saved
 * from a sense keeps naming it, and the entry lists the senses it retired.
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
    private final EntryVersionRepository entryVersionRepository;
    private final EntryAliasRepository aliasRepository;

    public EntryPublicationResult publish(EntryPublication publication) {
        EntryDocument entry = publication.entry();
        String language = entry.language();
        String headword = entry.headword();
        Instant now = Instant.now();

        List<Lexeme> standing = lexemeRepository.findByLanguageAndHeadword(language, headword);
        List<Placement> placements = new ArrayList<>();
        for (LexemeDocument document : entry.lexemes()) {
            Lexeme lexeme = lexemeRepository
                    .findByLanguageAndLemmaAndPosAndHomographNo(
                            language, document.lemma(), document.pos(), document.homograph())
                    .orElseGet(Lexeme::new);
            placements.add(new Placement(lexeme, document));
        }

        for (Lexeme old : standing) {
            if (placements.stream().noneMatch(placement -> placement.lexeme() == old)) {
                lexemeRepository.deleteTree(old.getId());
            }
        }
        // Old senses go first and alone: a new sense at the same position, or with the same key, would otherwise be
        // inserted before them.
        for (Placement placement : placements) {
            if (placement.lexeme().getId() != null) {
                placement.lexeme().getSenses().clear();
                placement.lexeme().getRelations().clear();
                placement.lexeme().getParts().clear();
            }
        }
        aliasRepository.deleteOf(language, headword);
        lexemeRepository.flush();

        for (Placement placement : placements) {
            write(placement.lexeme(), placement.document(), entry, publication, now);
            lexemeRepository.save(placement.lexeme());
        }
        lexemeRepository.flush();
        for (Placement placement : placements) {
            Lexeme lexeme = placement.lexeme();
            relationRepository.pointDanglingAt(lexeme.getId(), language, lexeme.getLemma(), lexeme.getPos());
        }
        writeAliases(entry);
        recordVerdict(entry, publication, now);
        recordVersion(entry, publication, now);
        return EntryPublicationResult.accepted(language, headword, placements.size());
    }

    /** A lexeme of the entry and the row it lands on, new or standing. */
    private record Placement(Lexeme lexeme, LexemeDocument document) {}

    /**
     * Every spelling the entry answers for besides its headword: those ingest lists (a form or a spelling that was
     * asked for as a headword of its own) and the other spellings of its lexemes.
     */
    private void writeAliases(EntryDocument entry) {
        Set<String> spellings = new LinkedHashSet<>(entry.aliases());
        for (LexemeDocument lexeme : entry.lexemes()) {
            lexeme.spellings().forEach(spelling -> spellings.add(spelling.text()));
        }
        spellings.remove(entry.headword());
        for (String spelling : spellings) {
            EntryAlias alias = new EntryAlias();
            alias.setLanguage(entry.language());
            alias.setHeadword(entry.headword());
            alias.setAlias(spelling);
            alias.setNormalizedAlias(WordNormalizer.normalize(spelling));
            aliasRepository.save(alias);
        }
    }

    private void write(
            Lexeme lexeme, LexemeDocument document, EntryDocument entry, EntryPublication publication, Instant now) {
        boolean fresh = lexeme.getId() == null;
        lexeme.setLanguage(entry.language());
        lexeme.setHeadword(entry.headword());
        lexeme.setLemma(document.lemma());
        lexeme.setPos(document.pos());
        lexeme.setHomographNo(document.homograph());
        lexeme.setKind(document.kind());
        lexeme.setGrammar(new LinkedHashMap<>(document.grammar()));
        lexeme.setSpellings(
                document.spellings().isEmpty()
                        ? null
                        : document.spellings().stream()
                                .map(LexiconPublicationService::spelling)
                                .toList());
        lexeme.setComponents(
                document.components() == null
                        ? null
                        : document.components().stream()
                                .map(part -> Map.<String, Object>of(
                                        "text", part.text(), "lemma", part.lemma(), "inflects", part.inflects()))
                                .toList());
        lexeme.setSeparable(document.separable());
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
            lexeme.addSense(sense(sense)).setSenseKey(sense.id());
        }
        for (RelationDocument relation : document.relations()) {
            LexemeRelation link = new LexemeRelation();
            link.setType(relation.type());
            link.setTargetLemma(relation.lemma());
            link.setTargetPos(relation.pos());
            link.setEvidence(relation.why());
            link.setSenses(relation.senses());
            link.setTarget(lexemeRepository
                    .findByLanguageAndLemmaAndPosAndHomographNo(entry.language(), relation.lemma(), relation.pos(), 1)
                    .orElse(null));
            lexeme.addRelation(link);
        }
        lexeme.addBuild(build(publication, now));
    }

    private static Map<String, Object> spelling(EntryDocument.SpellingDocument document) {
        Map<String, Object> spelling = new LinkedHashMap<>();
        spelling.put("text", document.text());
        if (document.varieties() != null) {
            spelling.put("varieties", document.varieties());
        }
        if (document.preReform() != null) {
            spelling.put("pre_reform", document.preReform());
        }
        if (document.script() != null) {
            spelling.put("script", document.script());
        }
        return spelling;
    }

    /**
     * Forms are rewritten; pronunciations are matched to the new forms by variety and spelling (the n-th form spelled
     * alike takes the n-th matching pronunciation) and updated where they stand, so a clip recorded for one stays
     * attached. A changed transcription or reading drops the row back to provisional. Pronunciations the entry no longer has go
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
            Pronunciation pronunciation = place(
                    lexeme,
                    existing.remove(key(spoken.variety(), rank, "")),
                    null,
                    spoken.variety(),
                    rank,
                    spoken.ipa(),
                    spoken.reading(),
                    writer);
            pronunciation.setNote(spoken.note());
            pronunciation.setNotation(spoken.notation());
            pronunciation.setAccent(spoken.accent());
            pronunciation.setSpelling(spoken.spelling());
        }
        seen.clear();
        for (FormDocument formDocument : document.forms()) {
            LexemeForm form = new LexemeForm();
            form.setForm(formDocument.form());
            form.setNormalizedForm(WordNormalizer.normalize(formDocument.form()));
            form.setFeatures(new LinkedHashMap<>(formDocument.features()));
            form.setVarieties(formDocument.varieties());
            form.setSenseKeys(formDocument.senseIds());
            form.setReading(formDocument.reading());
            form.setNotation(formDocument.notation());
            form.setNote(formDocument.note());
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
                        null,
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
            String reading,
            String writer) {
        Pronunciation pronunciation = existing != null ? existing : lexeme.addPronunciation(new Pronunciation());
        if (existing == null
                || !Objects.equals(phonemic, existing.getPhonemic())
                || !Objects.equals(reading, existing.getReading())) {
            pronunciation.setStatus(LexiconStatus.PROVISIONAL);
        }
        pronunciation.setForm(form);
        pronunciation.setVariety(variety);
        pronunciation.setRank(rank);
        pronunciation.setPhonemic(phonemic);
        pronunciation.setReading(reading);
        pronunciation.setSource(writer);
        return pronunciation;
    }

    private Sense sense(SenseDocument document) {
        Sense sense = new Sense();
        sense.setDefinition(document.definition());
        sense.setCefr(document.cefr());
        sense.setCefrSource(document.cefr() == null ? null : CEFR_BY_WRITER);
        sense.setLabels(document.labels() == null ? null : new LinkedHashMap<>(document.labels()));
        sense.setGrammar(document.grammar() == null ? null : new LinkedHashMap<>(document.grammar()));
        sense.setFrames(document.frames());
        sense.setPatterns(document.patterns());
        sense.setCollocations(document.collocations());
        sense.setAntonyms(document.antonyms());
        sense.setSpellings(document.spellings());
        sense.setStandardAlternative(document.standardAlternative());
        sense.setRegions(document.scopes());
        sense.setCited(document.from() == null || document.from().isEmpty() ? null : List.copyOf(document.from()));
        SenseExample example = new SenseExample();
        example.setText(document.example());
        example.setOrigin(SenseExample.GENERATED);
        sense.addExample(example);
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

    /**
     * The entry as published, as the headword's next version. The document is never changed afterwards; it is what a
     * card's sense key is traced through: every sense carries its key as {@code id}, and a lexeme lists under
     * {@code retired} the senses it once had.
     */
    private void recordVersion(EntryDocument entry, EntryPublication publication, Instant now) {
        EntryVersion version = new EntryVersion();
        version.setLanguage(entry.language());
        version.setHeadword(entry.headword());
        version.setVersion(entryVersionRepository
                        .findFirstByLanguageAndHeadwordOrderByVersionDesc(entry.language(), entry.headword())
                        .map(EntryVersion::getVersion)
                        .orElse(0)
                + 1);
        version.setWriter(publication.writer());
        version.setSchemaVersion(publication.schemaVersion());
        version.setDocument(new LinkedHashMap<>(publication.document()));
        version.setBuiltAt(publication.builtAt() != null ? publication.builtAt() : now);
        entryVersionRepository.save(version);
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
