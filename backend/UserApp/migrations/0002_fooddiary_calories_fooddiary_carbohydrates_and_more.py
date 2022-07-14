# Generated by Django 4.0.2 on 2022-04-17 10:37

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('UserApp', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='fooddiary',
            name='calories',
            field=models.IntegerField(blank=True, default=0),
        ),
        migrations.AddField(
            model_name='fooddiary',
            name='carbohydrates',
            field=models.FloatField(blank=True, default=0.0),
        ),
        migrations.AddField(
            model_name='fooddiary',
            name='fats',
            field=models.FloatField(blank=True, default=0.0),
        ),
        migrations.AddField(
            model_name='fooddiary',
            name='mass',
            field=models.IntegerField(blank=True, default=0),
        ),
        migrations.AddField(
            model_name='fooddiary',
            name='protein',
            field=models.FloatField(blank=True, default=0.0),
        ),
    ]