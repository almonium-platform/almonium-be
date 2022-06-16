package com.linguatool.model.dto.api.response.wordnik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class WordnikAudioDto {
    String createdBy;
    String id;
    String word;
    String duration;
    String audioType;
    String createdAt;
    String description;
    String attributionUrl;
    String fileUrl;
}
