from collections import OrderedDict
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
                "content": [OrderedDict([("title", "About"), ("title_id", "about"), ("content", "Hello, world!")])],
                "dates": [
                    OrderedDict(
                        [("title", "Day 1"), ("start", "2020-02-14T12:00:00Z"), ("end", "2020-02-14T16:00:00Z")]
                    )
                ],
            },
        )
