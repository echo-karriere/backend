from rest_framework import status
from rest_framework.test import APITestCase

from apps.namespace.models import Namespace
from apps.pages.models import Page


class PageTest(APITestCase):
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

    def test_get_all_status(self):
        response = self.client.get("/api/page/")
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data), 0)

        Page.objects.all().update(status=Page.PUBLISHED)

        response = self.client.get("/api/page/")
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data), 2)

    def test_get_data(self):
        Page.objects.all().update(status=Page.PUBLISHED)
        response = self.client.get("/api/page/")
        p1 = dict(response.data[0])
        self.assertEqual(p1["title"], "Page w/no slug")
        self.assertEqual(p1["content"], "No slug here")
        self.assertEqual(p1["status"], "P")
        self.assertEqual(p1["slug"], "/page-wno-slug/")

        p2 = dict(response.data[1])
        self.assertEqual(p2["title"], "Information")
        self.assertEqual(p2["content"], "Info")
        self.assertEqual(p2["status"], "P")
        self.assertEqual(p2["slug"], "/things/information/")

    def test_str(self):
        p1 = Page.objects.all()[0]
        p2 = Page.objects.all()[1]
        self.assertEqual(str(p1), "Page w/no slug")
        self.assertEqual(str(p2), "Information")
