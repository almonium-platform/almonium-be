package com.linguatool.model.dto.lang;

import com.linguatool.model.entity.lang.Card;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CardWrapper extends Card {

    Card entity;

    public CardWrapper(Card entity) {
        parseWordFamily();
        parseHardIndices();
    }

    private void parseWordFamily() {
        this.wordFamily = List.of(entity.getWordFamily().split("\n"));
    }

    private void parseHardIndices() {
        this.hardIndices = entity.getHardIndices().chars().map(c -> c - '0').boxed().collect(Collectors.toSet());
    }

    List<String> wordFamily;

    Set<Integer> hardIndices;

}
