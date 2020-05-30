from rest_framework import serializers

from .models import Event, EventContent, EventDate


class EventDateSerializer(serializers.ModelSerializer):
    class Meta:
        model = EventDate
        fields = ["title", "start", "end"]


class EventContentSerializer(serializers.ModelSerializer):
    class Meta:
        model = EventContent
        fields = ["title", "title_id", "content"]


class EventSerializer(serializers.ModelSerializer):
    content = EventContentSerializer(many=True, read_only=True)
    dates = EventDateSerializer(many=True, read_only=True)

    class Meta:
        model = Event
        fields = ["name", "year", "content", "dates"]
