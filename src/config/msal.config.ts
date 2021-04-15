import { AuthenticationResult, ClientCredentialRequest, ConfidentialClientApplication } from "@azure/msal-node";
import axios, { AxiosRequestConfig } from "axios";

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

export const tokenRequest = {
  scopes: [config.graphEndpoint + ".default"],
};

export const msalApiEndpoints = {
  users: config.graphEndpoint + "v1.0/users",
  groups: config.graphEndpoint + "v1.0/groups",
};

export const cca = new ConfidentialClientApplication(msalConfig);

export async function getToken(credentials: ClientCredentialRequest): Promise<AuthenticationResult> {
  return cca.acquireTokenByClientCredential(credentials);
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
