package com.almonium.engine.client.ngrams.client;

import com.almonium.engine.client.ngrams.dto.NgramsResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface NgramsClient {

    @GetExchange(value = "/{corpus}/search?flags=cr")
    NgramsResponseDto searchWord(@PathVariable String corpus, @RequestParam("query") String query);
}
