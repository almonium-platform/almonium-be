package com.almonium.learning.lexicon.mapper;

import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.Capabilities;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.CueView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ExampleView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.FormView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.GlossView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.LexemeView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.PartView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.PronunciationView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.Provenance;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.RelationView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.SenseView;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.LexemeForm;
import com.almonium.learning.lexicon.model.entity.LexemePart;
import com.almonium.learning.lexicon.model.entity.LexemeRelation;
import com.almonium.learning.lexicon.model.entity.LexiconBuild;
import com.almonium.learning.lexicon.model.entity.Pronunciation;
import com.almonium.learning.lexicon.model.entity.Sense;
import com.almonium.learning.lexicon.model.entity.SenseCue;
import com.almonium.learning.lexicon.model.entity.SenseExample;
import com.almonium.learning.lexicon.model.entity.SenseGloss;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

/** Shapes lexicon rows into the one entry every client reads. Written by hand: the graph is deep and computed. */
@Component
public class LexiconEntryMapper {

    /** {@code matches} pairs each lexeme with the form the reader's word matched, or null when it named the lexeme. */
    public LexiconEntryResponse toResponse(String language, String text, Map<Lexeme, LexemeForm> matches) {
        List<LexemeView> lexemes = new ArrayList<>();
        matches.forEach((lexeme, matched) -> lexemes.add(toView(lexeme, matched)));
        return new LexiconEntryResponse(language, text, lexemes);
    }

    public LexemeView toView(Lexeme lexeme, LexemeForm matched) {
        List<FormView> forms = lexeme.getForms().stream().map(this::toView).toList();
        List<PronunciationView> pronunciations =
                lexeme.getPronunciations().stream().map(this::toView).toList();
        List<SenseView> senses = lexeme.getSenses().stream().map(this::toView).toList();
        List<RelationView> relations =
                lexeme.getRelations().stream().map(this::toView).toList();
        List<PartView> parts = lexeme.getParts().stream().map(this::toView).toList();
        return new LexemeView(
                lexeme.getId(),
                lexeme.getHeadword(),
                lexeme.getLemma(),
                lexeme.getPos(),
                lexeme.getHomographNo(),
                lexeme.getKind(),
                lexeme.getGrammar(),
                lexeme.getPattern(),
                lexeme.getZipf() == null ? null : lexeme.getZipf().doubleValue(),
                lexeme.getStatus().label(),
                matched == null ? null : toView(matched),
                forms,
                pronunciations,
                senses,
                relations,
                parts,
                new Capabilities(
                        !forms.isEmpty(),
                        senses.stream().anyMatch(sense -> sense.cefr() != null),
                        !pronunciations.isEmpty(),
                        false,
                        !parts.isEmpty(),
                        !relations.isEmpty()),
                provenance(lexeme));
    }

    private FormView toView(LexemeForm form) {
        return new FormView(form.getId(), form.getForm(), form.getFeatures());
    }

    private PronunciationView toView(Pronunciation pronunciation) {
        return new PronunciationView(
                pronunciation.getId(),
                pronunciation.getForm() == null ? null : pronunciation.getForm().getId(),
                pronunciation.getVariety(),
                pronunciation.getPhonemic(),
                pronunciation.getStatus().label());
    }

    private SenseView toView(Sense sense) {
        Map<String, List<GlossView>> glosses = new LinkedHashMap<>();
        for (SenseGloss gloss : sense.getGlosses()) {
            glosses.computeIfAbsent(gloss.getFluentLanguage(), language -> new ArrayList<>())
                    .add(new GlossView(gloss.getGloss(), gloss.getExact(), gloss.getCollidesWith()));
        }
        return new SenseView(
                sense.getId(),
                sense.getPosition(),
                sense.getDefinition(),
                sense.getCefr(),
                sense.getCefrSource(),
                sense.getRegister(),
                sense.getGrammar(),
                sense.getUsage(),
                sense.getRegions(),
                glosses,
                sense.getExamples().stream().map(this::toView).toList(),
                sense.getCues().stream().map(this::toView).toList());
    }

    private ExampleView toView(SenseExample example) {
        return new ExampleView(example.getText(), example.getOrigin(), example.getTranslation());
    }

    private CueView toView(SenseCue cue) {
        return new CueView(cue.getKind(), cue.getText(), cue.getDifficulty());
    }

    private RelationView toView(LexemeRelation relation) {
        return new RelationView(
                relation.getType(),
                relation.getTargetLemma(),
                relation.getTargetPos(),
                relation.getTarget() == null ? null : relation.getTarget().getId(),
                relation.getEvidence());
    }

    private PartView toView(LexemePart part) {
        return new PartView(part.getText(), part.getGloss(), part.getRole());
    }

    private Provenance provenance(Lexeme lexeme) {
        LexiconBuild build = lexeme.latestBuild();
        if (build == null) {
            return null;
        }
        return new Provenance(
                build.getWriter(),
                build.getModel(),
                build.getPromptVersion(),
                build.getRegisterVersion(),
                build.getSchemaVersion(),
                lexeme.getEntryVersion(),
                Objects.requireNonNull(build.getBuiltAt()));
    }
}
