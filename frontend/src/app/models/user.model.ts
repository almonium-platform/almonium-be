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
  uiLang: string;
  roles: string[];
  tags: string[];
  targetLangs: string[];
  curLang: string;
  profilePicLink: string;
  fluentLangs: string[];
}
export interface FriendshipActionDto {
  idInitiator: number,
  idAcceptor: number,
  action: string;
}
