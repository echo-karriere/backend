from rest_framework import status
from rest_framework.test import APITestCase

from apps.forms.contact.models import ContactSchema


class ContactUsTest(APITestCase):
    def test_post(self):
        response = self.client.post(
            "/api/contact-us/",
            {"contact_person": "Test Testerson", "contact_email": "test@example.org", "message": "Hello, tests!"},
        )
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data, None)

    def test_str(self):
        c = ContactSchema.objects.create(
            contact_person="Test Testerson", contact_email="test@example.org", message="Hello, this is a lovely app"
        )
        self.assertEqual(str(c), "Test Testerson")
