from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from django.contrib.auth.models import Group

from .forms import UserChangeForm, UserCreationForm
from .models import User


@admin.register(User)
class UserAdmin(BaseUserAdmin):
    form = UserChangeForm
    add_form = UserCreationForm

    list_display = ["name", "email", "is_staff", "is_admin"]
    list_filter = ["is_admin", "is_staff", "is_active"]
    fieldsets = [
        ["Auth", {"fields": ["email", "password"]}],
        ["Personal info", {"fields": ["name", "avatar"]}],
        ["Settings", {"fields": ["is_active", "is_staff", "is_admin"]}],
        ["Important dates", {"fields": ["last_login", "registered_at"]}],
    ]

    add_fieldsets = [[None, {"classes": ["wide"], "fields": ["email", "name", "password1", "password2"]}]]
    search_fields = ["email", "name"]
    ordering = ["email"]
    filter_horizontal = []
    readonly_fields = ["last_login", "registered_at"]


admin.site.unregister(Group)
