<h1 align="center">echo karriere backend</h1>

<p align="center">
   <a href="https://github.com/echo-karriere/backend/actions"><img alt="GitHub Actions Status" src="https://github.com/echo-karriere/backend/workflows/Pipeline/badge.svg" /></a>
   <a href="https://sonarcloud.io/dashboard?id=echo-karriere_backend"><img alt="Quality Gate Status" src="https://sonarcloud.io/api/project_badges/measure?project=echo-karriere_backend&metric=alert_status" /></a>
   <a href="https://sonarcloud.io/dashboard?id=echo-karriere_backend"><img alt="Code Coverage" src="https://sonarcloud.io/api/project_badges/measure?project=echo-karriere_backend&metric=coverage" /></a>
   <br />
</p>

<p align="center">
   <strong>The backbone of echo karriere</strong>
</p>

<details>
<summary>Table of Contents</summary>
<br />

**Table of Contents**

- [Developing](#developing)
  - [Installation](#installation)
  - [Running](#running)
  - [Other commands](#other-commands)
- [LICENSE](#license)

</details>

## What

This is the backend that powers [echo karriere](https://www.echokarriere.no/)
for managing our events, content and data. It's a student developed project written in TypeScript using NestJS, Prisma and Azure.

# Developing

## Installation

Run `yarn` to install everything you need. Once that's done run `node scripts/build-info` to generate
some metadata that is used for quering the status of the application. **NB:** You'll only ever need to
do this once.

### Docker

You should look over the
[documentation](https://docs.echokarriere.no/backend/docker/) if you haven't already for installing Docker and
configuring IntelliJ. Create a copy of the `env.example` file called `.env`, to get the Auth0 variables contact the web
master for echo karriere. Optionally, if you want to clear out the database each time the application starts you can add

```ini
DEV_MODE=on
```

to the file as well, though this should only be required when making major changes to the database schema.

**NB:** The `.env` file is ignored by `git` by default so that we never accidentally expose secrets, make sure you never
commit this file to the repositiory!

### Starting the database

Once you've created the `.env` file you and are ready to develop, run:

```shell
docker-compose -f docker-compose.yml -f docker-compose.test.yml up -d
```

to start the development and test server for. We use two different instances of PostgreSQL to avoid conflicts and errors
when running tests and a live development server.

## Running

To run the application you can either start it with `yarn start:dev` or run it from your IDE.

## Other commands

### Running the app

```bash
# development
$ yarn start

# watch mode
$ yarn start:dev

# production mode
$ yarn start:prod
```

### Test

```bash
# unit tests
$ yarn test

# e2e tests
$ yarn test:e2e

# test coverage
$ yarn test:cov
```

# LICENSE

MIT.
