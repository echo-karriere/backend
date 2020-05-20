from ckeditor.fields import RichTextField
from django.db import models
from django.utils.text import slugify


class Namespace(models.Model):
    title = models.CharField(max_length=100)
    description = models.CharField(max_length=250)
    namespace = models.SlugField(editable=False)

    def __str__(self):
        return self.title

    def save(self, *args, **kwargs):
        namespace = slugify(self.title)

        self.namespace = namespace
        super(Namespace, self).save(*args, **kwargs)


class Page(models.Model):
    title = models.CharField(max_length=100)
    content = RichTextField()
    slug = models.SlugField(editable=False)
    namespace = models.ForeignKey(
        Namespace, on_delete=models.CASCADE, blank=True, null=True
    )

    def __str__(self):
        return self.title

    def save(self, *args, **kwargs):
        super(Page, self).save(*args, **kwargs)

        if Namespace.objects.filter(page=self.id).exists():
            namespace = Namespace.objects.get(page=self.id)
            slug = f"/{namespace.namespace}/{slugify(self.title)}/"
            self.slug = slug
        else:
            self.slug = f"/{slugify(self.title)}/"

        super(Page, self).save(*args, **kwargs)
