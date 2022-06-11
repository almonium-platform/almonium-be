interface FDPhonetic {
  audio: string;
  license: string;
  sourceUrl: string;
}

export interface FDEntry {
  word: string;
  meanings: FDMeaning[];
  phonetic: string;
  phonetics: FDPhonetic[];
  sourceUrls: string[];
}

export interface FDDefinition {
  synonyms: string[];
  antonyms: string[];
  definition: string;
}

export interface FDMeaning {
  partOfSpeech: string;
  synonyms: string[];
  antonyms: string[];
  definitions: FDDefinition[];
}
