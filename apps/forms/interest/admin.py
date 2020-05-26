from django.contrib import admin

from .models import InterestSchema


@admin.register(InterestSchema)
class InterestFormAdmin(admin.ModelAdmin):
    list_display = ("company", "person")
