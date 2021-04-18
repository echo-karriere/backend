import { Client, GraphRequest } from "@microsoft/microsoft-graph-client";
import { Injectable } from "@nestjs/common";

import { AuthProvider } from "./azure.config";

@Injectable()
export class GraphService {
  private readonly client: Client;

  constructor() {
    this.client = Client.initWithMiddleware({
      authProvider: new AuthProvider(),
    });
  }

  api(path: string): GraphRequest {
    return this.client.api(path);
  }
}
