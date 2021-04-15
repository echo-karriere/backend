import { AuthenticationResult, ClientCredentialRequest, ConfidentialClientApplication } from "@azure/msal-node";
import axios, { AxiosError, AxiosRequestConfig, Method } from "axios";

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
  userGroups: (id: string): string => config.graphEndpoint + `v1.0/users/${id}/getMemberGroups`,
  groups: config.graphEndpoint + "v1.0/groups",
};

export const cca = new ConfidentialClientApplication(msalConfig);

export async function getToken(credentials: ClientCredentialRequest): Promise<AuthenticationResult> {
  return cca.acquireTokenByClientCredential(credentials);
}

interface GraphApiResponse<T> {
  value: T;
}

interface MsalApiConfig {
  body?: Record<string, unknown>;
  params?: Record<string, unknown>;
  method?: Method;
}

const defaultApiConfig: MsalApiConfig = {
  body: undefined,
  params: undefined,
  method: "GET",
};

/**
 * A utility function for fetching data from the Azure Graph API.
 *
 * @param endpoint - Endpoint to fetch data from, e.q. `/user`
 * @param body - If you want to use `$select` on the query
 * @returns - A
 */
export async function msalApiQuery<T>(endpoint: string, _config: MsalApiConfig = defaultApiConfig): Promise<T | Error> {
  const config = { ...defaultApiConfig, ..._config };
  const token = await getToken(tokenRequest);
  const options: AxiosRequestConfig = {
    url: endpoint,
    headers: {
      Authorization: `Bearer ${token.accessToken}`,
      "Content-Type": "application/json",
    },
    data: config.body,
    params: config.params,
    method: config.method,
  };

  try {
    const response = await axios.request<GraphApiResponse<T>>(options);
    return response.data.value;
  } catch (error) {
    const err = error as AxiosError;
    console.log(err.response.data);
    console.error(err);
    return new Error("Could not query Graph API");
  }
}
