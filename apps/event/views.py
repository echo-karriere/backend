from django.utils.decorators import method_decorator
from drf_yasg.utils import swagger_auto_schema
from rest_framework import generics

from .models import Event
from .serializer import EventSerializer


@method_decorator(
    name="get",
    decorator=swagger_auto_schema(
        operation_summary="Information about current event", operation_description="GET /api/event",
    ),
)
class EventViewSet(generics.RetrieveAPIView):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def get_object(self):
        return self.queryset.get(active=True)
