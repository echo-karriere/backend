from rest_framework import serializers

from .models import ContactSchema


class ContactSchemaSerializer(serializers.ModelSerializer):
    class Meta:
        model = ContactSchema
        fields = ["contact_person", "contact_email", "message"]
