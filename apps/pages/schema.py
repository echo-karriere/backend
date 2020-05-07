import graphene
from graphene_django.types import DjangoObjectType
from .models import Page


class PageType(DjangoObjectType):
    class Meta:
        model = Page


class Query(object):
    page = graphene.Field(PageType, id=graphene.Int(), title=graphene.String())
    all_pages = graphene.List(PageType)

    def resolve_all_pages(self, info, **kwargs):
        return Page.objects.all()

    def resolve_page(self, info, **kwargs):
        id = kwargs.get("id")
        title = kwargs.get("title")

        if id is not None:
            return Page.objects.get(pk=id)

        if title is not None:
            return Page.objects.get(title=title)

        return None
