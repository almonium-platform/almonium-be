import {TranslationCard} from './translation.model';

export interface CardDto {
  id: number;
  public_id: string;
  entry: string;
  activeLearning: boolean;
  irregularSpelling: boolean;
  irregularPlural: boolean;
  falseFriend: boolean;
  frequency: number;
  priority: number;
  iteration: number;
  notes: string;
  language: string;
  userId: number;
  tags: TagDto[];
  translations: TranslationDto[];
  examples: ExampleDto[];
  created: string;
  updated: string;
  learnt: boolean;
}

export interface CardSuggestionDto {
  recipientId: number;
  cardId: number;
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
export interface CardCreationDto {
  entry: string;
  language: string;
  priority: number;
  examples: ExampleDto[];
  translations: TranslationDto[];
  irregularPlural: boolean;
  falseFriend: boolean;
  irregularSpelling: boolean;
  notes: string;
  tags: string[];
  activeLearning: boolean;
}

export interface CardUpdateDto {
  id: number;
  entry?: string;
  language?: string;
  priority?: number;
  examples?: ExampleDto[];
  translations?: TranslationDto[];
  irregularPlural?: boolean;
  falseFriend?: boolean;
  irregularSpelling?: boolean;
  notes?: string;
  tags?: string[];
  activeLearning?: boolean;
}
