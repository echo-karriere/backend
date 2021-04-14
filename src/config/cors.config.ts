import { CorsOptions } from "apollo-server-express";

export const corsConfiguration: CorsOptions = {
  methods: ["POST", "GET"],
  origin: ["http://localhost:3000", "https://dev.api.echokarriere.no", "https://echokarriere-dev.azurewebsites.net"],
};
