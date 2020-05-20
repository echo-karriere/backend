from ckeditor.fields import RichTextField
from django.db import models


class Page(models.Model):
    title = models.CharField(max_length=100)
    content = RichTextField()
    slug = models.SlugField()

    def __str__(self):
        return self.title
