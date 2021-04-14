const { spawnSync } = require("child_process");
const { writeFileSync } = require("fs");
const path = require("path");

const sha = spawnSync("git", ["rev-parse", "--short", "HEAD"]);
const buildDate = new Date();

if (sha.error) {
  throw new Error(sha.error);
}

const data = {
  commit: sha.stdout.toString().trim(),
  buildDate,
};

writeFileSync(path.join(process.cwd(), "src/config/build.json"), JSON.stringify(data, null, 2));
