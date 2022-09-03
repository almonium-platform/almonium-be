export interface CardDto {
  id: number;
  entry: string;
  activeLearning: boolean;
  irregularSpelling: boolean;
  irregularPlural: boolean;
  frequency: number;
  priority: number;
  iteration: number;
  notes: string;
  ipa: string;
  language: string;
  wordFamily: string[];
  userId: number;
  tags: TagDto[];
  translations: TranslationDto[];
  examples: ExampleDto[];
  created: string;
  updated: string
  lastRepeat: string;
  learnt: boolean;
}

export interface Suggestion {
  card: CardDto;
  sender: number;
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
