"""config URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.conf.urls.static import static
from django.contrib import admin
from django.urls import include, path
from drf_yasg import openapi
from drf_yasg.views import get_schema_view
from rest_framework import permissions, routers

from apps.event import views as EventViews
from apps.namespace import views as NamespaceViews
from apps.pages import views as PageViews
from config import settings

schema_view = get_schema_view(
    openapi.Info(
        title="echo karriere API",
        default_version="v1",
        description="API for the echokarriere.no application",
        terms_of_service="",
        contact=openapi.Contact(email="webmaster@echokarriere.no"),
        license=(openapi.License(name="MIT License")),
    ),
    public=True,
    permission_classes=(permissions.AllowAny,),
)

router = routers.DefaultRouter()
router.register("event", EventViews.EventViewSet, basename="event")
router.register("page", PageViews.PageViewSet, basename="page")
router.register("namespace", NamespaceViews.NamespaceViewSet, basename="namespace")

urlpatterns = [
    path("api/", include((router.urls, "api"), namespace="v1"), name="api"),
    path("api/swagger/", schema_view.with_ui("swagger", cache_timeout=0), name="schema-swagger-ui"),
    path("api/redoc/", schema_view.with_ui("redoc", cache_timeout=0), name="schema-redoc"),
    path("admin/", admin.site.urls, name="admin"),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
