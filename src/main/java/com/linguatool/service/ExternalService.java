package com.linguatool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguatool.client.DatamuseClient;
import com.linguatool.client.FDClient;
import com.linguatool.client.OxfordClient;
import com.linguatool.client.UrbanClient;
import com.linguatool.client.WordnikClient;
import com.linguatool.model.dto.api.response.datamuse.DatamuseEntryDto;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExternalService {
    UrbanClient urbanClient;
    DatamuseClient datamuseClient;
    WordnikClient wordnikClient;
    OxfordClient oxfordClient;

    ObjectMapper objectMapper;
    FDClient fdClient;


    public void create(String word) {
//        ResponseEntity<UrbanResponse> response = urbanClient.submit(word);
//        ResponseEntity<WordnikFrequencyDto> response = wordnikClient.submit(word);
//        ResponseEntity response = oxfordClient.submit(word);
//        ResponseEntity<List<FDEntry>> response = fdClient.request(word);
//        System.out.println("fd" + response);
        ResponseEntity<List<DatamuseEntryDto>> response = datamuseClient.request(word);
//        UrbanResponse dto = response.getBody();
//        System.out.println((dto.getList().size()));

    }

    public double getFrequency(String entry) {
        ResponseEntity<List<DatamuseEntryDto>> response = datamuseClient.request(entry);
        String fTag = response.getBody().get(0).getTags()[2];
        return Double.parseDouble(fTag.split(":")[1]);
    }
}
