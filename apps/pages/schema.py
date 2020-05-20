from graphene import Node
from graphene_django.filter import DjangoFilterConnectionField
from graphene_django.types import DjangoObjectType

from .models import Page


class PageNode(DjangoObjectType):
    class Meta:
        model = Page
        interfaces = (Node,)
        filter_fields = {"title": ["exact", "icontains", "istartswith"]}


class Query(object):
    page = Node.Field(PageNode)
    all_pages = DjangoFilterConnectionField(PageNode)
