from django.contrib import admin

from .models import Namespace


@admin.register(Namespace)
class NamespaceAdmin(admin.ModelAdmin):
    list_display = ("title", "description", "namespace")
