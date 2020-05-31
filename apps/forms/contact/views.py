from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import mixins, status, viewsets
from rest_framework.response import Response

from .models import ContactSchema
from .serializer import ContactSchemaSerializer


@method_decorator(
    name="create",
    decorator=swagger_auto_schema(
        operation_summary="Send a contact us message", responses={201: "OK", 400: "Bad request"}
    ),
)
class ContactViewSet(viewsets.GenericViewSet, mixins.CreateModelMixin):
    """
    API endpoint for the contact us form on our website.
    """

    queryset = ContactSchema.objects.all()
    serializer_class = ContactSchemaSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(status=status.HTTP_201_CREATED)
        return Response(status=status.HTTP_400_BAD_REQUEST)
