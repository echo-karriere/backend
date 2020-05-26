from django.forms import ModelForm

from .models import InterestSchema


class InterestForm(ModelForm):
    class Meta:
        model = InterestSchema
        fields = []
