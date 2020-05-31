from datetime import datetime

from django.utils.timezone import make_aware
from rest_framework import status
from rest_framework.test import APITestCase

from apps.event.models import Event, EventContent, EventDate


class EventTests(APITestCase):
    def setUp(self) -> None:
        event = Event.objects.create(name="Test Event", active=True, year=datetime(2020, 2, 14))
        EventContent.objects.create(event=event, title="About", content="Hello, world!")
        EventDate.objects.create(
            event=event,
            title="Day 1",
            start=make_aware(datetime(2020, 2, 14, 12)),
            end=make_aware(datetime(2020, 2, 14, 16)),
        )

    def test_get_event(self):
        response = self.client.get("/api/event/")
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(
            response.data,
            {
                "name": "Test Event",
                "year": "2020-02-14",
                "content": [{"title": "About", "title_id": "about", "content": "Hello, world!"}],
                "dates": [{"title": "Day 1", "start": "2020-02-14T12:00:00Z", "end": "2020-02-14T16:00:00Z"}],
            },
        )

    def test_save_active(self):
        new_event = Event.objects.create(name="Next Event", active=True, year=datetime(2020, 3, 10))
        event = Event.objects.all()[0]
        self.assertEqual(event.active, False)
        self.assertEqual(new_event.active, True)

    def test_save_not_active(self):
        event = Event.objects.create(name="Another Event", active=False, year=datetime(2019, 4, 1))
        self.assertEqual(event.active, False)

    def test_str(self):
        event = Event.objects.all()[0]
        event_date = EventDate.objects.all()[0]
        event_content = EventContent.objects.all()[0]

        self.assertEqual(str(event), "Test Event")
        self.assertEqual(str(event_date), "Feb 14 2020: 12:00:00 - 16:00:00")
        self.assertEqual(str(event_content), "About")
