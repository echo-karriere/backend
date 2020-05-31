from django.db import models
from django.utils.text import slugify


class Namespace(models.Model):
    title = models.CharField(max_length=100)
    description = models.CharField(max_length=250)
    namespace = models.SlugField(editable=False)

    class Meta:
        pass

    def __str__(self):
        return self.title

    def save(self, *args, **kwargs):
        namespace = slugify(self.title)

        self.namespace = namespace
        super(Namespace, self).save(*args, **kwargs)
