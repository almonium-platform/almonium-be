export interface TranslationCard {
  provider: string;
  definitions: Definition[];
}

export interface Definition {
  pos: string;
  transcription: string;
  translations: Translation[];
}

export interface Translation {
  text: string;
  pos: string;
  frequency: number;
}
