FROM python:3.8-buster

WORKDIR /usr/src/app

# Set some environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Install dependencies
RUN pip install --upgrade pip
RUN pip install pipenv
COPY Pipfile ./Pipfile.lock /usr/src/app/
RUN pipenv install --deploy --system

COPY . /usr/src/app/