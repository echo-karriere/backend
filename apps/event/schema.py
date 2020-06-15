import graphene
from django.contrib.auth.models import User
from graphene_django import DjangoObjectType

from .models import Event, EventContent, EventDate


class EventDateType(DjangoObjectType):
    class Meta:
        model = EventDate
        fields = ("start", "end")


class EventContentType(DjangoObjectType):
    class Meta:
        model = EventContent
        fields = ("title_id", "title", "content")


class UserType(DjangoObjectType):
    class Meta:
        model = User
        fields = ("first_name", "last_name")


class EventType(DjangoObjectType):
    class Meta:
        model = Event
        fields = ("id", "name", "active", "year")

    dates = graphene.List(EventDateType)
    content = graphene.List(EventContentType)
    staff = graphene.List(UserType)

    def resolve_dates(self: Event, info):
        return EventDate.objects.filter(event=self.id)

    def resolve_staff(self: Event, info):
        return User.objects.filter(is_staff=True)

    def resolve_content(self: Event, info):
        return EventContent.objects.filter(event=self.id)


class EventQuery(object):
    active_event = graphene.Field(EventType)

    def resolve_active_event(self, info):
        return Event.objects.get(active=True)
