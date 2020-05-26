from graphene import Field, ObjectType
from graphene_django import DjangoObjectType
from graphene_django.forms.mutation import DjangoModelFormMutation

from .forms import InterestForm
from .models import InterestSchema


class InterestType(DjangoObjectType):
    class Meta:
        model = InterestSchema


class InterestFormMutation(DjangoModelFormMutation):
    interest = Field(InterestType)

    class Meta:
        form_class = InterestForm


class Mutation(ObjectType):
    create_interest = InterestFormMutation.Field()
