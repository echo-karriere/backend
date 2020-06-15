from django.test import TestCase

from apps.namespace.models import Namespace


class NamespaceTests(TestCase):
    def setUp(self) -> None:
        Namespace.objects.create(title="About", description="About us")
        Namespace.objects.create(title="Information", description="Info")

    def test_str(self):
        n1 = Namespace.objects.all()[0]
        n2 = Namespace.objects.all()[1]
        self.assertEqual(str(n1), "About")
        self.assertEqual(str(n2), "Information")
