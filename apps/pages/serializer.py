from rest_framework import serializers

from apps.namespace.serializer import NamespaceSerializer

from .models import Page


class PageSerializer(serializers.ModelSerializer):
    namespace = NamespaceSerializer(many=False, read_only=False)

    class Meta:
        model = Page
        fields = "__all__"
