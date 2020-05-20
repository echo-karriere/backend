from graphene import Node
from graphene_django.filter import DjangoFilterConnectionField
from graphene_django.types import DjangoObjectType

from .models import Page


class PageNode(DjangoObjectType):
    class Meta:
        model = Page
        interfaces = (Node,)
        fields = ("title", "content", "slug")
        filter_fields = {"title": ["exact"]}


class Query(object):
    page = Node.Field(PageNode)
    all_pages = DjangoFilterConnectionField(PageNode)

    def resolve_page(self: Page, info):
        return Page.objects.get(pk=self.id).filter(status=Page.PUBLISHED)

    def resolve_all_pages(self: Page, info, **kwargs):
        title = kwargs.get("title")

        if title:
            return Page.objects.filter(title=title, status=Page.PUBLISHED)
        else:
            return Page.objects.filter(status=Page.PUBLISHED)
