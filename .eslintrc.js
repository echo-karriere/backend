module.exports = {
  parserOptions: {
    project: "tsconfig.json",
  },
  extends: ["@sondr3/typescript"],
  root: true,
  env: {
    jest: true,
  },
};
