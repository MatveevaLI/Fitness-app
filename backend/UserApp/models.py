from django.utils import timezone
from distutils.command.upload import upload
from email.policy import default
from unicodedata import category
from django.db import models
from django.forms import ValidationError
from numpy import product
from django.contrib.auth.models import AbstractUser
from django.utils.timezone import now, datetime
import datetime 


# Create your models here.
# vytvorit  classy

class User(AbstractUser):
    name = models.CharField(max_length=100)
    phone = models.CharField(max_length=100,blank=True)
    city = models.CharField(max_length=100,blank=True)
    role = models.CharField(max_length=100,blank=True)
    created_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    updated_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    weight = models.FloatField(default=0.0,blank=True)
    height = models.IntegerField(default=0,blank=True)
    gender = models.CharField(max_length=10,blank=True)
    birthday = models.DateField(null=True, blank=True)
    photo = models.CharField(max_length=100, blank=True , default="users/default.png")
    
    
    USERNAME_FIELD = 'username' 
    REQUIRED_FIELDS = ['email', 'photo']

    class Meta:
        db_table = "users"


class Product(models.Model):
    created_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    updated_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    name = models.CharField(max_length=100)
    calories = models.IntegerField()
    fats = models.FloatField()
    carbohydrates = models.FloatField()
    protein = models.FloatField()
    mass = models.IntegerField()
    # photo = models.ImageField(upload_to='products/', verbose_name='Product Image', default="products/default.jpg")
    # user_id of owner who added food to database
    user = models.ForeignKey('User', on_delete=models.CASCADE, null=True)
    type = models.ForeignKey('TypeFood', on_delete=models.CASCADE, null=True)
    
    REQUIRED_FIELDS = ['name']

    class Meta:
        db_table = "products"


class Category(models.Model):
    name = models.CharField(max_length=40)

    class Meta:
        db_table = "categories"


class TypeFood(models.Model):
    name = models.CharField(max_length=40)

    class Meta:
        db_table = "type"


class FoodDiary(models.Model):
    created_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    updated_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    user = models.ForeignKey('User', on_delete=models.CASCADE)
    product = models.ForeignKey('Product', on_delete=models.CASCADE)
    category = models.ForeignKey('Category', on_delete=models.CASCADE, blank=True,  null=True)

    mass = models.IntegerField(default=0,blank=True)
    calories = models.IntegerField(default=0,blank=True)
    fats = models.FloatField(default=0.0,blank=True)
    carbohydrates = models.FloatField(default=0.0,blank=True)
    protein = models.FloatField(default=0.0,blank=True)
    name = models.CharField(max_length=100,blank=True)
    
    class Meta:
        db_table = "food_diaries"


class Call(models.Model):
    created_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    finished_at = models.DateField(null=True, blank=True, default=datetime.date.today)
    user_from = models.ForeignKey('User', on_delete=models.CASCADE, related_name='user_from', null=True)
    user_to = models.ForeignKey('User', on_delete=models.CASCADE, related_name='user_to',  null=True)

    class Meta:
        db_table = "calls"
    

class Group(models.Model):
    user_owner = models.ForeignKey('User', on_delete=models.CASCADE, related_name='user_owner', null=True)
    user_invited = models.ForeignKey('User', on_delete=models.CASCADE, related_name='user_invited', null=True)
    name = models.CharField(max_length=10, blank=True)

    class Meta:
        db_table = "groups"
    