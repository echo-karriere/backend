from django.db import models, transaction


class EventDate(models.Model):
    start = models.DateTimeField()
    end = models.DateTimeField()
    event = models.ForeignKey("Event", on_delete=models.CASCADE)

    def __str__(self):
        return f"{self.start.strftime('%b %d %Y')}: {self.start.strftime('%H:%M:%S')} - {self.end.strftime('%H:%M:%S')}"


class Event(models.Model):
    name = models.CharField(max_length=120)
    active = models.BooleanField()
    year = models.DateField()

    def save(
        self, *args, **kwargs,
    ):
        if not self.active:
            return super(Event, self).save(*args, **kwargs)
        with transaction.atomic():
            Event.objects.filter(active=True).update(active=False)
            return super(Event, self).save(*args, **kwargs)

    def __str__(self):
        return self.name
