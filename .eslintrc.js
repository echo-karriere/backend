module.exports = {
  parserOptions: {
    project: "tsconfig.json",
  },
  extends: ["@sondr3/typescript"],
  root: true,
  env: {
    jest: true,
  },
  overrides: [
    {
      files: ["./src/**/*.model.ts", "./src/**/*.resolver.ts", "./src/**/*.dto.ts"],
      rules: {
        "@typescript-eslint/no-unused-vars": "off",
      },
    },
  ],
};
