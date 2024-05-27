package com.linguarium.engine.client.oxford.dto;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OxfordEntry {
    String[] etymologies;
    OxfordNote[] notes;
    OxfordPronunciation[] pronunciations;
    OxfordSense[] senses;
}
