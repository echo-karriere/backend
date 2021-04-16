import { HttpService, Injectable } from "@nestjs/common";
import { AxiosError, AxiosRequestConfig, Method } from "axios";

import { accessTokenRequestData, getToken } from "./azure.config";
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

@Injectable()
export class GraphService {
  constructor(private http: HttpService) {}

  /**
   * A utility function for fetching data from the Azure Graph API.
   *
   * @param endpoint - Endpoint to fetch data from, e.q. `/user`
   * @param body - If you want to use `$select` on the query
   * @returns - A
   */
  async query<T>(endpoint: string, _config: MsalApiConfig = defaultApiConfig): Promise<T | Error> {
    const config = { ...defaultApiConfig, ..._config };
    const token = await getToken(accessTokenRequestData);
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
      const response = await this.http.request<GraphApiResponse<T>>(options).toPromise();
      return response.data.value;
    } catch (error) {
      const err = error as AxiosError;
      console.log(err.response.data);
      console.error(err);
      return new Error("Could not query Graph API");
    }
  }
}
