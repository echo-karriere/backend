from django.test import TestCase

from apps.forms.contact.models import ContactSchema


class ContactUsTest(TestCase):
    def test_str(self):
        c = ContactSchema.objects.create(
            contact_person="Test Testerson", contact_email="test@example.org", message="Hello, this is a lovely app"
        )
        self.assertEqual(str(c), "Test Testerson")
