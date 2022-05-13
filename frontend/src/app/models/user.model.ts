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
  roles: string[];
}
export interface FriendshipActionDto {
  idInitiator: number,
  idAcceptor: number,
  action: string;
}
