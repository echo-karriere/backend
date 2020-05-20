from ckeditor.fields import RichTextField
from django.db import models
from django.utils import timezone
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


class Page(models.Model):
    PUBLISHED = "P"
    DRAFT = "D"
    DELETED = "X"
    STATUS_CHOICES = [(PUBLISHED, "Published"), (DRAFT, "Draft"), (DELETED, "Deleted")]

    title = models.CharField(max_length=100, unique=True)
    content = RichTextField()
    slug = models.SlugField(editable=False)
    namespace = models.ForeignKey(Namespace, on_delete=models.CASCADE, blank=True, null=True)
    status = models.CharField(max_length=1, choices=STATUS_CHOICES, default=DRAFT)
    created_on = models.DateTimeField(editable=False, auto_now_add=True)
    modified_on = models.DateTimeField(editable=False, auto_now=True)
    published_on = models.DateTimeField(editable=False)

    class Meta:
        ordering = ["modified_on"]

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

        if self.status == Page.PUBLISHED:
            self.published_on = timezone.now()

        super(Page, self).save(*args, **kwargs)
