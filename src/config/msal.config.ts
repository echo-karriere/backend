import { AuthenticationResult, ClientCredentialRequest, ConfidentialClientApplication } from "@azure/msal-node";
import axios, { AxiosRequestConfig } from "axios";

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

interface GraphApiResponse<T> {
  value: T;
}

/**
 * A utility function for fetching data from the Azure Graph API.
 *
 * @param endpoint - Endpoint to fetch data from, e.q. `/user`
 * @param body - If you want to use `$select` on the query
 * @returns - A
 */
export async function msalApiQuery<T>(endpoint: string, body: Record<string, unknown> = {}): Promise<T | Error> {
  const token = await getToken(tokenRequest);
  const options: AxiosRequestConfig = {
    headers: {
      Authorization: `Bearer ${token.accessToken}`,
    },
    params: body,
  };

  try {
    const response = await axios.get<GraphApiResponse<T>>(endpoint, options);
    return response.data.value;
  } catch (error) {
    const err = error as Error;
    console.error(err);
    return err;
  }
}
