package com.almonium.analyzer.analyzer.dto;

import java.util.List;
import java.util.Map;

/**
 * One word behind the Discover sheet's senses: a lemma in one part of speech, as the lexicon holds it. The senses
 * that belong to it name it by {@code index}. {@code grammar} is what holds for the whole word (gender, how it
 * counts, its auxiliary), {@code spellings} every way it is written with the varieties that write it so (each
 * {@code {text, varieties, pre_reform}}), empty for a word with one spelling; {@code forms} the forms a learner
 * could not guess, and {@code relations} the words it comes from or is mixed up with. {@code components} are the
 * words of a multiword, each {@code {text, lemma, inflects}}, and {@code separable} whether others may stand
 * between them.
 */
public record DiscoverLexemeResponse(
        int index,
        String lemma,
        String partOfSpeech,
        String kind,
        Map<String, Object> grammar,
        List<Map<String, Object>> spellings,
        List<Form> forms,
        List<Relation> relations,
        List<Map<String, Object>> components,
        Boolean separable) {

    /**
     * {@code features} are Universal Dependencies features (Number=Plur); {@code varieties} and {@code senses}
     * ({@code DiscoverSenseResponse.index}) are empty when the form belongs to all of them.
     */
    public record Form(
            String form,
            Map<String, Object> features,
            List<String> varieties,
            List<Integer> senses,
            String reading,
            String note) {}

    /** {@code senses} ({@code DiscoverSenseResponse.index}) is empty when the relation holds for the whole word. */
    public record Relation(String type, String lemma, String partOfSpeech, List<Integer> senses) {}
}
