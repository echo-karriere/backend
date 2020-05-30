from rest_framework import serializers

from .models import Event


class EventSerializer(serializers.ModelSerializer):
    name = serializers.CharField()
    year = serializers.DateField()

    class Meta:
        model = Event
        fields = ("name", "year")
        read_only_fields = ["id"]
