from rest_framework import parsers, renderers

from utils.casing import deep_camel_case_transform, deep_snake_case_transform


class CamelCaseJSONRenderer(renderers.JSONRenderer):
    """
    Converts the default Django REST Framework JSONRenderer from using snake_case
    naming to camelCasing it.
    """

    def render(self, data, *args, **kwargs):
        converted_data = deep_camel_case_transform(data)
        return super().render(converted_data, *args, **kwargs)


class CamelCaseJSONParser(parsers.JSONParser):
    """
    Converts the default Django REST Framework JSONParse from parsing snake_case
    to parsing camelCase and converting it to snake_case.
    """

    def parse(self, stream, *args, **kwargs):
        data = super().parse(stream, *args, **kwargs)
        return deep_snake_case_transform(data)
