from django.db import models


class Meta(models.Model):
    active = models.BooleanField()
    year = models.IntegerField()
    board = models.ForeignKey()
