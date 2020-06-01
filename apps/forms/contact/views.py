from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import viewsets

from .models import ContactSchema
from .serializer import ContactSchemaSerializer


@method_decorator(name="list", decorator=swagger_auto_schema(operation_summary="Get all contact requests"))
@method_decorator(name="retrieve", decorator=swagger_auto_schema(operation_summary="Get a single contact request"))
@method_decorator(name="update", decorator=swagger_auto_schema(operation_summary="Update a request form"))
@method_decorator(name="partial_update", decorator=swagger_auto_schema(operation_summary="Partially update a request"))
@method_decorator(name="destroy", decorator=swagger_auto_schema(operation_summary="Delete a request"))
@method_decorator(name="create", decorator=swagger_auto_schema(operation_summary="Send a contact us message"))
class ContactViewSet(viewsets.ModelViewSet):
    """
    API endpoint for the contact us form on our website.
    """

    queryset = ContactSchema.objects.all()
    serializer_class = ContactSchemaSerializer
