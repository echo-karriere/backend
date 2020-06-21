console.log(process.env.JWT_TOKEN);

export const jwtConstants = {
  jwtSecret: process.env.JWT_TOKEN ?? "changeme",
  jwtExpiry: process.env.JWT_EXPIRY ?? "1d",
};
