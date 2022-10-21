export interface MachineTranslationDto {
  provider: string;
  text: string
}

export interface TranslationCard {
  provider: string;
  definitions: Definition[];
}

export interface Definition {
  text: string;
  pos: string;
  transcription: string;
  translations: Translation[];
}

export interface Translation {
  text: string;
  pos: string;
  frequency: number;
}
