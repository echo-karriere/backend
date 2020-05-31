from django.db import models

from config import settings


class ContactSchema(models.Model):
    contact_person = models.CharField(max_length=255, help_text="Name of person who contacted us")
    contact_email = models.EmailField(max_length=255, help_text="Email of person who contacted us")
    message = models.TextField(help_text="Message from user")
    date_submitted = models.DateTimeField(auto_now_add=True, editable=False, help_text="Date message was sent")
    answered = models.BooleanField(
        blank=True,
        default=False,
        help_text="Whether we have answered the person back (will be automatically updated when saving)",
    )
    answered_by = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        editable=False,
        on_delete=models.CASCADE,
        blank=True,
        null=True,
        help_text="Who replied",
    )

    class Meta:
        verbose_name = "Contact Form"
        verbose_name_plural = "Contact Form"

    def __str__(self):
        return f"{self.contact_person}"
