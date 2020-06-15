from django.test import TestCase

from apps.namespace.models import Namespace
from apps.pages.models import Page


class PageTest(TestCase):
    def setUp(self) -> None:
        Page.objects.create(title="Page w/no slug", content="No slug here", namespace=None)
        info = Namespace.objects.create(title="Things", description="Things")
        Page.objects.create(title="Information", content="Info", namespace=info)

    def test_slug(self):
        p1: Page = Page.objects.all()[0]
        p2: Page = Page.objects.all()[1]
        self.assertEqual(p1.slug, "/page-wno-slug/")
        self.assertEqual(p2.slug, "/things/information/")

    def test_save_and_publish(self):
        p = Page.objects.all()[0]
        p.status = Page.PUBLISHED
        p.save()
        self.assertNotEqual(p.status, None)

    def test_str(self):
        p1 = Page.objects.all()[0]
        p2 = Page.objects.all()[1]
        self.assertEqual(str(p1), "Page w/no slug")
        self.assertEqual(str(p2), "Information")
