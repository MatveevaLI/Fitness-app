from dataclasses import fields
from email.policy import default
from unicodedata import name
from unittest.util import _MAX_LENGTH
from rest_framework import serializers
from UserApp.models import User, Product, Group, FoodDiary


class ProductSerializer(serializers.ModelSerializer):

    class Meta:
        model = Product
        fields = (
            'id', 'name', 'user', 'calories', 'fats', 'carbohydrates', 'protein', 'mass', 'type', 
        )

    def validate(self, data):
        if len(data['name']) < 3:
            raise serializers.ValidationError("product name is too short")
        return data


class User1Serializer(serializers.ModelSerializer):
    class Meta:

        def validate(self, data):
            #if not data['phone']:
                #raise serializers.ValidationError("phone is required")
            return data

        model = User
        fields = (
            'name', 'email', 'weight', 'height', 'gender', 'birthday', 'photo', 'id',
        )


class GroupSerializer(serializers.ModelSerializer):

    class Meta:
        model = Group
        fields = (
            'id', 'name', 'user_owner', 'user_invited'
        )


class FoodDiarySerializer(serializers.ModelSerializer):

    class Meta:
        model = FoodDiary
        fields = (
            'id', 'user', 'product', 'category', 'updated_at', 'calories', 'fats', 'carbohydrates', 'protein', 'mass', 'name'
        )
