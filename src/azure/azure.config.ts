import { AuthenticationResult, ClientCredentialRequest, ConfidentialClientApplication } from "@azure/msal-node";

const config = {
  graphEndpoint: process.env.GRAPH_ENDPOINT ?? "example.org/graph",
  clientSecret: process.env.AZURE_CLIENT_SECRET ?? "verysecret",
  aadEndpoint: process.env.AAD_ENDPOINT ?? "example.org",
  tenantId: process.env.AZURE_TENANT_ID ?? "id",
};

const msalConfig = {
  auth: {
    clientId: process.env.AZURE_CLIENT_ID,
    authority: config.aadEndpoint + config.tenantId,
    clientSecret: config.clientSecret,
  },
};

export const accessTokenRequestData = {
  scopes: [config.graphEndpoint + ".default"],
};

export const msalApiEndpoints = {
  users: config.graphEndpoint + "v1.0/users",
  userGroups: (id: string): string => config.graphEndpoint + `v1.0/users/${id}/getMemberGroups`,
  groups: config.graphEndpoint + "v1.0/groups",
};

export const cca = new ConfidentialClientApplication(msalConfig);

export async function getToken(credentials: ClientCredentialRequest): Promise<AuthenticationResult> {
  return cca.acquireTokenByClientCredential(credentials);
}
