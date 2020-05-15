from django.db import models


class EventDate(models.Model):
    date = models.DateTimeField()
    event = models.ForeignKey("Event", on_delete=models.CASCADE)


class Event(models.Model):
    name = models.CharField(max_length=120)
    active = models.BooleanField()
    year = models.DateField()

    def __str__(self):
        return self.name
