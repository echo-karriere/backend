from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import viewsets
from rest_framework.response import Response

from .models import Event
from .serializer import EventSerializer


@method_decorator(name="list", decorator=swagger_auto_schema(operation_summary="Get current event"))
@method_decorator(
    name="retrieve",
    decorator=swagger_auto_schema(
        operation_summary="Get event by id", responses={200: EventSerializer, 404: "Event not found"},
    ),
)
class EventViewSet(viewsets.ReadOnlyModelViewSet):
    """
    Query the API for information about the current or any previous
    event that has been managed by this API.
    """

    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def list(self, request, *args, **kwargs):
        queryset = Event.objects.get(active=True)
        serializer = EventSerializer(queryset)
        return Response(serializer.data)
