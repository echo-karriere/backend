from rest_framework import status
from rest_framework.test import APITestCase

from apps.namespace.models import Namespace


class EventTests(APITestCase):
    def setUp(self) -> None:
        Namespace.objects.create(title="About", description="About us")
        Namespace.objects.create(title="Information", description="Info")

    def test_get_all(self):
        response = self.client.get("/api/namespace/")
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(
            response.data,
            [
                {"id": 1, "title": "About", "description": "About us", "namespace": "about"},
                {"id": 2, "title": "Information", "description": "Info", "namespace": "information"},
            ],
        )

    def test_str(self):
        n1 = Namespace.objects.all()[0]
        n2 = Namespace.objects.all()[1]
        self.assertEqual(str(n1), "About")
        self.assertEqual(str(n2), "Information")
