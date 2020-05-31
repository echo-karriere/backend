from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import viewsets

from .models import Namespace
from .serializer import NamespaceSerializer


# @method_decorator(name="get", decorator=swagger_auto_schema(operation_summary="Hello!"))
class NamespaceViewSet(viewsets.ModelViewSet):
    queryset = Namespace.objects.all()
    serializer_class = NamespaceSerializer
