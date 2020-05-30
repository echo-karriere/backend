from rest_framework import generics

from .models import Event
from .serializer import EventSerializer


class EventViewSet(generics.RetrieveAPIView):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def get_object(self):
        return self.queryset.get(active=True)
