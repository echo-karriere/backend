# echo karriere backend ![Pipeline](https://github.com/echo-karriere/backend/workflows/Pipeline/badge.svg) [![codecov](https://codecov.io/gh/echo-karriere/backend/branch/master/graph/badge.svg?token=a1G1xHaXPi)](https://codecov.io/gh/echo-karriere/backend)

## Installation

This repo requires that you have the `poetry` Python tool installed, once you have that you can simply run `poetry install` and you're good to go.

## Developing

To start the development environment run `docker-compose build` and `docker-compose up -d` to start the environment.

## Adding dependencies

Once you have added dependencies, it's recommended that you update the `requirements.txt` file as it is used by Docker to install dependencies for production. Run the command `poetry export -f requirements.txt -o requirements.txt -E production` to export packages.
