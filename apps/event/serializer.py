from rest_framework import serializers

from .models import Event


class EventSerializer(serializers.ModelSerializer):
    name = serializers.CharField()
    active = serializers.BooleanField()
    year = serializers.DateField()

    class Meta:
        model = Event
        fields = "__all__"
        read_only_fields = ["id"]
