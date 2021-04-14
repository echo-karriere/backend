export interface AzureToken {
  iss: string;
  exp: number;
  nbf: number;
  aud: string;
  oid: string;
  sub: string;
  given_name: string;
  family_name: string;
  tfp: string;
  nonce: string;
  scp: string;
  azp: string;
  ver: string;
  iat: number;
}
