import { AuthenticationResult, ClientCredentialRequest, ConfidentialClientApplication } from "@azure/msal-node";
import axios from "axios";

const msalConfig = {
  auth: {
    clientId: process.env.AZURE_CLIENT_ID,
    authority: process.env.AAD_ENDPOINT + process.env.AZURE_TENANT_ID,
    clientSecret: process.env.AZURE_CLIENT_SECRET,
  },
};

export const tokenRequest = {
  scopes: [process.env.GRAPH_ENDPOINT + ".default"],
};

export const apiConfig = {
  users: process.env.GRAPH_ENDPOINT + "v1.0/users",
};

export const cca = new ConfidentialClientApplication(msalConfig);

export async function getToken(tokenRequest: ClientCredentialRequest): Promise<AuthenticationResult> {
  return await cca.acquireTokenByClientCredential(tokenRequest);
}

export async function msalApiQuery<T>(endpoint: string, accessToken: string): Promise<T | Error> {
  const options = {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  };

  try {
    const response = await axios.get<T>(endpoint, options);
    return response.data;
  } catch (error) {
    const err = error as Error;
    console.error(err);
    return err;
  }
}
