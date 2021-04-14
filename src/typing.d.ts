declare namespace NodeJS {
  interface ProcessEnv {
    DEV_MODE?: string;
    AZURE_CLIENT_ID: string;
    AZURE_TENANT_NAME: string;
    AZURE_TENANT_ID?: string;
    AZURE_POLICY_NAME: string;
    AZURE_CLIENT_SECRET?: string;
    AAD_ENDPOINT?: string;
    GRAPH_ENDPOINT?: string;
  }
}
