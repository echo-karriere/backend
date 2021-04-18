import "cross-fetch/polyfill";

import { AuthenticationResult, ClientCredentialRequest, ConfidentialClientApplication } from "@azure/msal-node";
import { AuthenticationProvider, Client } from "@microsoft/microsoft-graph-client";

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

export const cca = new ConfidentialClientApplication(msalConfig);

export async function getToken(credentials: ClientCredentialRequest): Promise<AuthenticationResult> {
  return cca.acquireTokenByClientCredential(credentials);
}

export class AuthProvider implements AuthenticationProvider {
  async getAccessToken(): Promise<string> {
    return new Promise((resolve, reject) => {
      getToken(accessTokenRequestData)
        .then((res) => resolve(res.accessToken))
        .catch((error) => reject(error));
    });
  }
}

export const azureClient = Client.initWithMiddleware({
  authProvider: new AuthProvider(),
});

export interface GraphApiResponse<T> {
  value: T;
}
