from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import viewsets

from .models import Namespace
from .serializer import NamespaceSerializer


@method_decorator(name="list", decorator=swagger_auto_schema(operation_summary="Get all namespaces"))
@method_decorator(name="create", decorator=swagger_auto_schema(operation_summary="Create a new namespace"))
@method_decorator(name="retrieve", decorator=swagger_auto_schema(operation_summary="Get namespace by id"))
@method_decorator(name="update", decorator=swagger_auto_schema(operation_summary="Update a namespace"))
@method_decorator(name="partial_update", decorator=swagger_auto_schema(operation_summary="Apply a partial update"))
@method_decorator(name="destroy", decorator=swagger_auto_schema(operation_summary="Delete a namespace"))
class NamespaceViewSet(viewsets.ModelViewSet):
    """
    Query the API for a `namespace` (some way to categorize content by namespacing them),
    used to for example create slugs for pages.
    """

    queryset = Namespace.objects.all()
    serializer_class = NamespaceSerializer
