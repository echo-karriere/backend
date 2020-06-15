from django.contrib.auth.models import AbstractBaseUser, BaseUserManager, PermissionsMixin
from django.db import models
from django.utils import timezone


class UserManager(BaseUserManager):
    def _create_user(self, email: str, password: str, is_staff: bool, is_superuser: bool, **extra_fields):
        user = self.model(
            email=self.normalize_email(email),
            is_active=True,
            is_staff=is_staff,
            is_superuser=is_superuser,
            registered_at=timezone.now(),
            **extra_fields,
        )
        user.set_password(password)
        user.save(using=self.db)
        return user

    def create_user(self, email=None, password=None, **extra_fields):
        is_staff = extra_fields.pop("is_staff", False)
        is_superuser = extra_fields.pop("is_superuser", False)
        return self._create_user(email, password, is_staff, is_superuser, **extra_fields)

    def create_superuser(self, email: str, password: str, **extra_fields):
        return self._create_user(email, password, is_staff=True, is_superuser=True, **extra_fields)


class User(AbstractBaseUser, PermissionsMixin):
    username = None
    email = models.EmailField(unique=True, max_length=255)
    name = models.CharField(max_length=255)
    avatar = models.ImageField(blank=True)

    is_staff = models.BooleanField(default=False)
    is_active = models.BooleanField(default=True)

    registered_at = models.DateTimeField(auto_now_add=True)

    EMAIL_FIELD = "email"
    USERNAME_FIELD = "email"
    REQUIRED_FIELDS = []

    objects = UserManager()

    class Meta:
        verbose_name = "User"
        verbose_name_plural = "Users"

    def get_full_name(self):
        return self.name

    def get_short_name(self):
        return self.name

    def __str__(self):
        return self.name
