from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import viewsets

from .models import Page
from .serializer import PageSerializer


@method_decorator(name="list", decorator=swagger_auto_schema(operation_summary="Hello!"))
class PageViewSet(viewsets.ModelViewSet):
    queryset = Page.objects.all().filter(status=Page.PUBLISHED)
    serializer_class = PageSerializer
