from django.db import models


class InterestSchema(models.Model):
    company = models.CharField(max_length=250)
    person = models.CharField(max_length=250)
    email = models.EmailField()
    phone = models.IntegerField()
    day = models.CharField(max_length=10)
    extras = models.CharField(max_length=250)
    banquet = models.BooleanField()
    other = models.TextField()
    confirmation = models.BooleanField()

    class Meta:
        verbose_name = "Interest form"
        app_label = "forms"

    def __str__(self):
        return self.company
