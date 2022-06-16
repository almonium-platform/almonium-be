export interface Friend {
  status: string;
  id: number;
  username: string;
  email: string;
}
export interface User {
  id: number,
  username: string;
  email: string;
  ui_lang: string;
  roles: string[];
  tags: string[];
  langs: string[];
  curLang: string;
}
export interface FriendshipActionDto {
  idInitiator: number,
  idAcceptor: number,
  action: string;
}
