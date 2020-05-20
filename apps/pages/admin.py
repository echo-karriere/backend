from django.contrib import admin
from .models import Page, Namespace


@admin.register(Page)
class PageAdmin(admin.ModelAdmin):
    list_display = ("title", "status", "published_on", "created_on", "slug")
    list_filter = ["published_on", "status", "modified_on"]


@admin.register(Namespace)
class NamespaceAdmin(admin.ModelAdmin):
    list_display = ("title", "description", "namespace")
