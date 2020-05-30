# Generated by Django 3.0.6 on 2020-05-30 14:21

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ("event", "0001_initial"),
    ]

    operations = [
        migrations.AlterField(
            model_name="eventcontent",
            name="event",
            field=models.ForeignKey(
                on_delete=django.db.models.deletion.CASCADE, related_name="content", to="event.Event"
            ),
        ),
        migrations.AlterField(
            model_name="eventdate",
            name="event",
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name="date", to="event.Event"),
        ),
    ]