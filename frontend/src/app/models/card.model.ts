export interface CardDto {
  entry: string;
  activeLearning: boolean;
  irregularSpelling: boolean;
  irregularPlural: boolean;
  frequency: number;
  priority: number;
  notes: string;
  ipa: string;
  language: string;
  wordFamily: string[];
  tags: TagDto[];
  translations: TranslationDto[];
  examples: ExampleDto[];
  created: string;
  modified: string;
  lastRepeat: string;
  learnt: boolean;
}

export interface Analysis {
  lemmas: string[];
  frequency: number;
}

export interface ExampleDto {
  example: string;
  translation: string;
}

export interface TranslationDto {
  translation: string;
}

export interface TagDto {
  tag: string;
}
