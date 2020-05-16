from django.contrib import admin

from .models import Event, EventDate, EventContent


class EventDateInline(admin.TabularInline):
    model = EventDate
    extra = 0


class EventContentInline(admin.StackedInline):
    model = EventContent
    extra = 0
    exclude = ("title_id",)


@admin.register(Event)
class EventAdmin(admin.ModelAdmin):
    list_display = ("name", "active", "year")
    inlines = [
        EventContentInline,
        EventDateInline,
    ]
