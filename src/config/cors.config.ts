import { CorsOptions } from "@nestjs/common/interfaces/external/cors-options.interface";

export const corsConfiguration: CorsOptions = {
  methods: ["POST", "GET"],
  origin: ["http://localhost:3000", "https://dev.portal.echokarriere.no", /frontend-[\D\w]*-echokarriere\.vercel\.app/],
};
