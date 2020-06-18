interface Configuration {
  port: number;
  production: boolean;
}

export default (): Configuration => ({
  port: parseInt(process.env.PORT ?? "3000", 10),
  production: (process.env.PRODUCTION ?? "true") === "true",
});
