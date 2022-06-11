package com.linguatool.service;

import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.dto.lang.POS;
import com.linguatool.model.entity.user.User;
import com.linguatool.repository.CardRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DiscoverService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    CoreNLPService coreNLPService;

    @Autowired
    ExternalService externalService;

    Optional<Card> search(String text, User user) {
        List<Card> cards = cardRepository.findAllByOwner(user);

        List<POS> posList = coreNLPService.posTagging(text);
        if (posList.get(0).equals(POS.TO) && posList.get(1).equals(POS.VERB)) {
            text = text.substring(text.indexOf(" ") + 1);
        }

        String finalText = text;
        Optional<Card> foundCard = cards.stream().filter(card -> card.getEntry().equals(finalText)).findFirst();
        if (foundCard.isPresent()) {
            return foundCard;
        }
        return foundCard;
    }

    public void getFrequency(String entry) {

    }

}
