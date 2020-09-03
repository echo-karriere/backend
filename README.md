<h1 align="center">echo karriere backend</h1>

<p align="center">
   <a href="https://github.com/echo-karriere/backend/actions"><img alt="GitHub Actions Status" src="https://github.com/echo-karriere/backend/workflows/Pipeline/badge.svg" /></a>
   <br />
</p>

<p align="center">
   <strong>The backbone of echo karriere</strong>
</p>

<details>
<summary>Table of Contents</summary>
<br />

<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->

**Table of Contents**

- [Installation](#installation)
  - [Configuration](#configuration)
  - [Production](#production)
  - [Developing](#developing)
- [Inspiration, help](#inspiration-help)
- [License](#license)

<!-- markdown-toc end -->

</details>

## What

This is the backend that powers [echo karriere](https://www.echokarriere.no/)
for managing our events, content and data. It's a student developed project
written in Kotlin using Ktor, PostgreSQL and GraphQL.

# Developing

## Configuration

You should look over the
[documentation](https://docs.echokarriere.no/backend/docker/) if you haven't
already for installing Docker and configuring IntelliJ. First, copy the
`.env.example` file to `.env` and update its content to the following:

``` ini
POSTGRES_DB=echokarriere
POSTGRES_USER=karriere
POSTGRES_PASSWORD=password
POSTGRES_HOST=localhost
```

**NB:** The `.env` file is ignored by `git` by default so that we never
accidentally expose secrets, make sure you never commit this file to the
repositiory!

## Starting the database

Once you've configured the `.env` file you can simply run `docker-compose up -d`
to start the database in the background. To see what is happening run `docker
logs echo_backend_db`, it might offer up a hint if it is not working.

## Running

To run the application you can either start it with `./gradlew run` or run it
from IntelliJ. Simply press the green play icon in the sidebar in
`Application.kt`.

# LICENSE

MIT.
