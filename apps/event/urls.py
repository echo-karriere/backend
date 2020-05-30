from django.urls import path

from .views import EventViewSet

app_name = "apps.event"
urlpatterns = [path("", EventViewSet.as_view())]
