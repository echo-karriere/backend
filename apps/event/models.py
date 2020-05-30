from ckeditor.fields import RichTextField
from django.db import models, transaction
from django.utils.text import slugify


class EventDate(models.Model):
    event = models.ForeignKey(
        "Event",
        related_name="dates",
        related_query_name="dates",
        on_delete=models.CASCADE,
        help_text="Event to link dates to",
    )
    title = models.CharField(max_length=120, help_text="Title for date, e.g. 'Dag 1'")
    start = models.DateTimeField(help_text="Date and time the event starts for a given day")
    end = models.DateTimeField(help_text="Date and time the event ends for a given day")

    class Meta:
        pass

    def __str__(self):
        return f"{self.start.strftime('%b %d %Y')}: {self.start.strftime('%H:%M:%S')} - {self.end.strftime('%H:%M:%S')}"


class EventContent(models.Model):
    event = models.ForeignKey(
        "Event",
        related_name="content",
        related_query_name="content",
        on_delete=models.CASCADE,
        help_text="Event to link content to",
    )
    title = models.CharField(max_length=120, help_text="Title for content block")
    title_id = models.CharField(
        max_length=120, editable=False, help_text="Automatically generated field, used by frontend"
    )
    content = RichTextField(help_text="Content for block")

    class Meta:
        pass

    def save(self, *args, **kwargs):
        self.title_id = slugify(self.title)
        super().save(*args, **kwargs)

    def __str__(self):
        return self.title


class Event(models.Model):
    name = models.CharField(max_length=120, help_text="Name of event")
    active = models.BooleanField(
        help_text="Whether the event is 'active', e.g. the current event", verbose_name="Active"
    )
    year = models.DateField(help_text="Year during which the event happens")

    class Meta:
        pass

    def save(self, *args, **kwargs):
        if not self.active:
            return super(Event, self).save(*args, **kwargs)
        with transaction.atomic():
            Event.objects.filter(active=True).update(active=False)
            return super(Event, self).save(*args, **kwargs)

    def __str__(self):
        return self.name
