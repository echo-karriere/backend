from django.contrib import admin
from .models import Page, Namespace


@admin.register(Page)
class PageAdmin(admin.ModelAdmin):
    list_display = ("title", "status", "slug")


@admin.register(Namespace)
class NamespaceAdmin(admin.ModelAdmin):
    list_display = ("title", "description", "namespace")
