module.exports = {
  parser: "@typescript-eslint/parser",
  parserOptions: {
    ecmaVersion: 2018,
    project: "tsconfig.json",
    sourceType: "module",
  },
  plugins: ["@typescript-eslint/eslint-plugin", "simple-import-sort", "node"],
  extends: [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:@typescript-eslint/recommended-requiring-type-checking",
    "plugin:import/errors",
    "plugin:import/warnings",
    "plugin:import/typescript",
    "plugin:prettier/recommended",
    "plugin:node/recommended-module",
    "plugin:unicorn/recommended",
  ],
  root: true,
  env: {
    node: true,
    jest: true,
    es6: true,
  },
  ignorePatterns: [".eslintrc.js", "*.config.js", "prisma/seed.js", "scripts/*.js"],
  rules: {
    // Not needed, see https://github.com/typescript-eslint/typescript-eslint/blob/master/packages/eslint-plugin/docs/rules/typedef.md#when-not-to-use-it
    "@typescript-eslint/typedef": "off",
    "import/no-default-export": "error",
    "import/no-unresolved": "off",
    // Fixes this warning for TS, https://github.com/typescript-eslint/typescript-eslint/blob/master/packages/eslint-plugin/docs/rules/no-unused-vars.md
    "no-unused-vars": "off",
    // https://github.com/typescript-eslint/typescript-eslint/blob/master/docs/getting-started/linting/FAQ.md#i-get-errors-from-the-no-undef-rule-about-global-variables-not-being-defined-even-though-there-are-no-typescript-errors
    "no-undef": "off",
    // Disabled due to overlap
    "simple-import-sort/imports": "error",
    "simple-import-sort/exports": "error",
    "sort-imports": "off",
    "import/order": "off",
    // not needed because of the `import` plugin
    "node/no-missing-require": "off",
    "node/no-missing-import": "off",
    "node/no-extraneous-import": "off",
    "node/no-extraneous-require": "off",
    // I really hate this rule, please stop
    "unicorn/prevent-abbreviations": "off",
  },
  overrides: [
    {
      files: ["**/*.ts", "**/*.tsx"],
      settings: {
        "import/parsers": {
          "@typescript-eslint/parser": [".ts", ".tsx"],
        },
      },
    },
    {
      files: ["**/*spec.ts"],
      rules: {
        "node/no-unpublished-import": "off",
      },
    },
  ],
};
