from django.contrib import admin

from .models import Event, EventDate


class EventDateInline(admin.TabularInline):
    model = EventDate


@admin.register(Event)
class EventAdmin(admin.ModelAdmin):
    list_display = ("name", "active", "year")
    inlines = [
        EventDateInline,
    ]
