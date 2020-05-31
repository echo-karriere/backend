from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import viewsets

from .models import Page
from .serializer import PageSerializer


@method_decorator(name="list", decorator=swagger_auto_schema(operation_summary="Get all published pages"))
@method_decorator(name="create", decorator=swagger_auto_schema(operation_summary="Create a new page"))
@method_decorator(name="retrieve", decorator=swagger_auto_schema(operation_summary="Get published page by id"))
@method_decorator(name="update", decorator=swagger_auto_schema(operation_summary="Update a page"))
@method_decorator(name="partial_update", decorator=swagger_auto_schema(operation_summary="Apply a partial update"))
@method_decorator(name="destroy", decorator=swagger_auto_schema(operation_summary="Delete a page"))
class PageViewSet(viewsets.ModelViewSet):
    """
    Query the API for information about pages (though only published pages appear in the API).
    """

    queryset = Page.objects.all().filter(status=Page.PUBLISHED)
    serializer_class = PageSerializer
