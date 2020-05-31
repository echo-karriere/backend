from django.contrib import admin, messages
from django.utils.translation import ngettext

from .models import ContactSchema


@admin.register(ContactSchema)
class ContactFormAdmin(admin.ModelAdmin):
    list_display = ("contact_person", "contact_email", "date_submitted", "answered", "answered_by")
    actions = ["make_answered", "make_unanswered"]
    list_filter = ["answered"]
    readonly_fields = ["date_submitted", "answered_by"]
    fieldsets = (
        ("Message", {"fields": ("contact_person", "contact_email", "date_submitted", "message")}),
        ("Response", {"fields": ("answered", "answered_by")}),
    )

    def save_model(self, request, obj, form, change):
        if obj.answered:
            obj.answered_by = request.user
        else:
            obj.answered_by = None
        super().save_model(request, obj, form, change)

    def make_answered(self, request, queryset):
        updated = queryset.update(answered=True, answered_by=request.user)
        self.message_user(
            request,
            ngettext("%d was successfully marked as answered.", "%d were successfully marked as answered.", updated,)
            % updated,
            messages.SUCCESS,
        )

    make_answered.short_description = "Mark as answered"

    def make_unanswered(self, request, queryset):
        updated = queryset.update(answered=False, answered_by=None)
        self.message_user(
            request,
            ngettext(
                "%d was successfully marked as unanswered.", "%d were successfully marked as unanswered.", updated,
            )
            % updated,
            messages.SUCCESS,
        )

    make_unanswered.short_description = "Mark as unanswered"
