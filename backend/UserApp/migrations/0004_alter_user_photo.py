# Generated by Django 4.0.3 on 2022-04-18 22:03

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('UserApp', '0003_fooddiary_name'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='photo',
            field=models.CharField(blank=True, default='users/default.png', max_length=100),
        ),
    ]