import {TranslationCard} from './translation.model';

export interface CardDto {
  id: number;
  hash: string;
  entry: string;
  activeLearning: boolean;
  irregularSpelling: boolean;
  irregularPlural: boolean;
  falseFriend: boolean;
  frequency: number;
  priority: number;
  iteration: number;
  notes: string;
  source: string;
  ipa: string;
  language: string;
  wordFamily: string[];
  userId: number;
  tags: TagDto[];
  translations: TranslationDto[];
  examples: ExampleDto[];
  created: string;
  updated: string;
  lastRepeat: string;
  learnt: boolean;
}

export interface Suggestion {
  card: CardDto;
  sender: number;
}

export interface ReportDto {
  lemmas: string[];
  posTags: string[];
  frequency: number;
  cefr: string;
  translationCards: TranslationCard;
}

export interface ExampleDto {
  id: number;
  example: string;
  translation: string;
}

export interface TranslationDto {
  id: number;
  translation: string;
}

export interface TagDto {
  id: number;
  text: string;
}
