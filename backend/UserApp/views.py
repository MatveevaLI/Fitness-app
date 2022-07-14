from genericpath import exists
from math import prod
from turtle import update

from django.http import HttpResponse
from django.shortcuts import render
from numpy import number, require
from rest_framework.parsers import JSONParser
from django.http.response import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework import permissions
from UserApp import serializers
from django.conf import settings
from itertools import chain, product
from UserApp.permissions import IsOwner, IsOwnerOrIsInvited
from UserApp.models import Category, FoodDiary, User, Group, Product, TypeFood
from UserApp.serializers import User1Serializer, ProductSerializer, GroupSerializer, FoodDiarySerializer
from rest_framework.views import APIView
from rest_framework import generics
from datetime import datetime
from django.utils.html import escape
import json
import base64, os

from drf_yasg import openapi
from drf_yasg.utils import swagger_auto_schema
from rest_framework.decorators import api_view
# Create your views here.


class ProductList(generics.GenericAPIView):
    allowed_methods = ('POST', 'GET')
    queryset = Product.objects.none()
    permission_classes = [permissions.IsAuthenticated]
    # queryset = Product.objects.none()
    serializer_class = ProductSerializer

    def get(self, request, format=None):
        """
        Vypis zoznamu vsetkych jedal 
        """
        foods = Product.objects.all()
        foods_serializer = ProductSerializer(foods, many=True)
        return HttpResponse(json.dumps(foods_serializer.data), content_type="application/json", status=200)

    def post(self, request, format=None):
        """
        Pridavanie noveho jedla  
        """
        food_data = JSONParser().parse(request)
        food_data['user'] = request.user.id
        
        type = TypeFood.objects.filter(name=food_data['type'])
        print(food_data)
        print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        print(food_data['type'])

        for t in range(len(type)): 
           food_data['type'] = type[t].id 
           print(type[t].id )
        
        print(food_data['type'])

        
        food_serializer = ProductSerializer(data=food_data)
    
        print(request)
        if food_serializer.is_valid():
            food_serializer.save()
            return HttpResponse(json.dumps("FOOD ADDED Successfully"), content_type="application/json", status=201)
        return HttpResponse(json.dumps("FOOD was NOT added"), content_type="application/json", status=400)


class Products(generics.GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = ProductSerializer

    def get(self, request, format=None):
            """
            Vypis jedneho jedla podla name 
            """
            
            food_data = JSONParser().parse(request)
            
            food = Product.objects.filter(name=food_data['name'])
            prod_id = 0
            for f in range(len(food)): 
                prod_id = food[f].id 

            food = Product.objects.get(id=prod_id)
            foods_serializer = ProductSerializer(food, many=False)

            return HttpResponse(json.dumps(foods_serializer.data), content_type="application/json", status=200)



class ProductDetail(generics.GenericAPIView):

    permission_classes = [IsOwner]
    serializer_class = ProductSerializer

    def get(self, request, product_id, format=None):
        """
        Vypis jedneho jedla podla id 
        """
        food = Product.objects.get(id=product_id)
        foods_serializer = ProductSerializer(food, many=False)
        return HttpResponse(json.dumps(foods_serializer.data), content_type="application/json", status=200)

    
    def delete(self, request, product_id, format=None):
        """
        Vymazanie jedneho jedla podla id 
        """
        food = Product.objects.get(id=product_id)
        self.check_object_permissions(self.request, food)
        food.delete()
        return HttpResponse(json.dumps("Product has been deleted"), content_type="application/json", status=200)

    def put(self, request, product_id, format=None):
        """
        Zmena dat v jednom jedle podla id 
        """
        food_data = JSONParser().parse(request)

        product = Product.objects.get(id=product_id)
        self.check_object_permissions(self.request, product)

        food_serializer = ProductSerializer(product, data=food_data)

        if food_serializer.is_valid():
            food_serializer.save()
            return HttpResponse(json.dumps("product updated"), content_type="application/json", status=200)
        else:
            error_list = [food_serializer.errors[error][0]
                          for error in food_serializer.errors]
            return HttpResponse(json.dumps(error_list), content_type="application/json", status=400)


class ProductListSearch(generics.GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = ProductSerializer

    def get(self, request, format=None):
        """
        Vyhladavanie jedla 
        """

        data = JSONParser().parse(request)
        serached_str = data['searched']

        if len(serached_str) <= 0: 
            return HttpResponse(json.dumps("String is empty"), content_type="application/json", status=200)

        foods = Product.objects.filter(name__icontains=serached_str)
        foods_serializer = ProductSerializer(foods, many=True)
        return HttpResponse(json.dumps(foods_serializer.data), content_type="application/json", status=200)


class GroupList (generics.GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = GroupSerializer

    def get(self, request, format=None):
        """
        Vypis vsetkych skupin usera
        """
        groups_owner = Group.objects.filter(user_owner=request.user.id)
        groups_invited = Group.objects.filter(user_invited=request.user.id)

        groups = list(chain(groups_owner, groups_invited))
        groups_serializer = GroupSerializer(groups, many=True)
        return HttpResponse(json.dumps(groups_serializer.data), content_type="application/json", status=200)

    def post(self, request, format=None):
        """
        Pridavanie novej skupiny  
        """
        group_data = JSONParser().parse(request)
        print(group_data)

        group_data['user_owner'] = request.user.id
        print("----------------")
        print(group_data)
        users = User.objects.all()

        exists = False
        for user in range(len(users)):
            if str(users[user]) == group_data['user_invited']:  # ak sa rovnaju mena
                group_data['user_invited'] = users[user].id
                print("----------------")
                print(group_data)
                exists = True
                break   # zatial kedze mame iba 2 ludi v skupine na zaciatok

        if exists is True:
            group_serializer = GroupSerializer(data=group_data)
            print(f"AHA: {group_serializer}")
            if group_serializer.is_valid():
                group_serializer.save()
                return HttpResponse(json.dumps("GROUP ADDED Successfully"), content_type="application/json", status=201)
        return HttpResponse(json.dumps("GROUP NOT added"), content_type="application/json", status=400)


class GroupDetail (generics.GenericAPIView):
    permission_classes = [IsOwnerOrIsInvited]
    serializer_class = GroupSerializer

    def get(self, request, group_name, format=None):
        """
        Vypis jednej skupiny usera podla id 
        """
        groups = Group.objects.all()
        group_id = 0

        for group in range(len(groups)):
            if str(groups[group].name) == group_name:
                group_id = groups[group].id
        if group_id == 0:
            return HttpResponse(json.dumps("Group doesn't exist"), content_type="application/json", status=200)

        group = Group.objects.get(id=group_id)
        self.check_object_permissions(self.request, group)

        group_serializer = GroupSerializer(group, many=False)
        return HttpResponse(json.dumps(group_serializer.data), content_type="application/json", status=200)

    def delete(self, request, group_name, format=None):
        """
        Vypis jednej skupiny usera podla id 
        """
        groups = Group.objects.all()
        group_id = 0

        for group in range(len(groups)):
            if str(groups[group].name) == group_name:
                group_id = groups[group].id
        if group_id == 0:
            return HttpResponse(json.dumps("Group doesn't exist"), content_type="application/json", status=404)

        group = Group.objects.get(id=group_id)
        self.check_object_permissions(self.request, group)
        group.delete()
        return HttpResponse(json.dumps("group deleted"), content_type="application/json", status=200)

    def put(self, request, group_name, format=None):
        """
        Zmena jednej skupiny usera podla id 
        """
        group_data = JSONParser().parse(request)

        groups = Group.objects.all()
        group_id = 0

        for group in range(len(groups)):
            if str(groups[group].name) == group_name:
                group_id = groups[group].id
        if group_id == 0:
            return HttpResponse(json.dumps("Group doesn't exist"), content_type="application/json", status=200)

        group = Group.objects.get(id=group_id)

        self.check_object_permissions(self.request, group)
        group_serializer = GroupSerializer(group, data=group_data)

        if group_serializer.is_valid():
            group_serializer.save()
            return HttpResponse(json.dumps("group updated"), content_type="application/json", status=200)
        else:
            error_list = [group_serializer.errors[error][0]
                          for error in group_serializer.errors]
            return HttpResponse(json.dumps(error_list), content_type="application/json", status=400)


# USERA riesi djoser + toto ako doplnok
class UserDetail(generics.GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = User1Serializer

    def get(self, request, format=None):
        """
        Vypis dat usera podla id 
        """
        user = User.objects.get(id=request.user.id)
        users_serializer = User1Serializer(user, many=False)
        return HttpResponse(json.dumps(users_serializer.data), content_type="application/json", status=200)

    def put(self, request, format=None):
        """
        Zmena dat usera podla id 
        """
        user_data = JSONParser().parse(request)

        if 'image' in user_data:
            file1 = open('images/img_number.txt', 'r')
            img_number = file1.readline()
            
            file1.close()
            file2 = open('images/img_number.txt', 'w')
            file2.write(str(int(img_number)+1))
            file2.close()
         
            img_encoded = user_data["image"]
            name = f"users/image_{img_number}.png"
            with open(f"images/{name}", "wb+") as new_file:    
               new_file.write(base64.decodebytes(str.encode(img_encoded)))
            user_data["photo"] = f"{name}"
            user_data["image"] = None
        
        user = User.objects.get(id=request.user.id)
        self.check_object_permissions(self.request, user)

        user_serializer = User1Serializer(user, data=user_data)

        if user_serializer.is_valid():
            user_serializer.save()
            return HttpResponse(json.dumps("user updated"), content_type="application/json", status=200)
        else:
            error_list = [user_serializer.errors[error][0]
                          for error in user_serializer.errors]
            return HttpResponse(json.dumps(error_list), content_type="application/json", status=400)

    def delete(self, request, format=None):
        """
        Vymazanie usera podla id 
        """
        user = User.objects.get(id=request.user.id)
        self.check_object_permissions(self.request, user)
        user.delete()
        return HttpResponse(json.dumps("user deleted"), content_type="application/json", status=200)


class UserDetailSpecificID(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, user_id, format=None):
        """
        Vypis dat usera podla id 
        """
        print(user_id)
        user = User.objects.get(id=user_id)
        user_serializer = User1Serializer(user, many=False)
        return HttpResponse(json.dumps(user_serializer.data), content_type="application/json", status=200)


class FoodDiaryList(generics.GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]

    # def get(self, request, format=None):
    #     """
    #     Vypis dennika jedla podla kategorie (breakfast, lunch, dinner, snack) a datumu a user.id 
    #     """
    #     data = JSONParser().parse(request)

    #     category = Category.objects.filter(name=data['category'])
    #     for c in range(len(category)):
    #         category_id = category[c].id

    #     food_diaries = FoodDiary.objects.filter(updated_at=data['date']).filter(user_id=request.user.id).filter(category=category_id)
    #     food_id = []

    #     for item in range(len(food_diaries)):
    #         if food_diaries[item].product_id in food_id: 
    #             continue
    #         else: 
    #             food_id.append(food_diaries[item].product_id)

    #     products = Product.objects.filter(id__in=food_id)        
        
    #     food_serializer = ProductSerializer(products, many=True)
    #     return HttpResponse(json.dumps(food_serializer.data), content_type="application/json", status=200)

    def get(self, request, format=None):
        """
        Vypis dennika jedla podla kategorie (breakfast, lunch, dinner, snack) a datumu a user.id 
        """
        data = JSONParser().parse(request)
        
        category = Category.objects.filter(name=data['category'])
        
        category_id = 0

        for c in range(len(category)):
            category_id = category[c].id

        food_diaries = FoodDiary.objects.filter(updated_at=data['date']).filter(user_id=request.user.id).filter(category=category_id)
            
        food_diares_serializer = FoodDiarySerializer(food_diaries, many=True)
        return HttpResponse(json.dumps(food_diares_serializer.data), content_type="application/json", status=200)


    def post(self, request, format=None):
        """
        Pridavanie noveho zaznamu do food diary  
        """
        diary_record_data = JSONParser().parse(request)

        category = Category.objects.filter(name=diary_record_data['category'])
        
        category_id = 0

        for c in range(len(category)):
            # print(category[c].name)
            category_id = category[c].id

        diary_record_data['user'] = request.user.id
        diary_record_data['category'] = category_id

        product = Product.objects.get(id=diary_record_data['product'])
        
        mass = diary_record_data['mass']
        
        calories = int(round(float(mass) * product.calories / float(product.mass), 2))
        fats =  round(float(mass) * product.fats / float(product.mass), 2) 
        carbohydrates = round(float(mass) * product.carbohydrates / float(product.mass), 2)  
        protein = round(float(mass) * product.protein / float(product.mass), 2)  
        
        diary_record_data['calories'] = calories
        diary_record_data['fats'] = fats
        diary_record_data['carbohydrates'] = carbohydrates
        diary_record_data['protein'] = protein
        diary_record_data['name'] = product.name

        record_serializer = FoodDiarySerializer(data=diary_record_data)

        if record_serializer.is_valid():
            record_serializer.save()
            return HttpResponse(json.dumps("Record added Successfully"), content_type="application/json", status=201)
        return HttpResponse(json.dumps("Record was NOT added"), content_type="application/json", status=400)




class FoodDiaryDetail(generics.GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, record_id, format=None):
        """
        Vypis jedneho zaznamu v food diary podla id 
        """
        record = FoodDiary.objects.get(id=record_id)
        record_serializer = FoodDiarySerializer(record, many=False)
        return HttpResponse(json.dumps(record_serializer.data), content_type="application/json", status=200)


    def delete(self, request, record_id, format=None):
        """
        Vymazanie jedneho zaznamu v food diary  
        """
        if FoodDiary.objects.filter(user_id=request.user.id).filter(id=record_id).exists():
            FoodDiary.objects.filter(user_id=request.user.id).filter(
                id=record_id).delete()
        else:
            return HttpResponse(json.dumps("record doesn't exists"), content_type="application/json", status=404)

        return HttpResponse(json.dumps("record deleted"), content_type="application/json", status=200)

    def put(self, request, record_id, format=None):
        """
        Zmena dat v jednom zazname jedla podla id 
        """
        record_data = JSONParser().parse(request)

        record = FoodDiary.objects.get(id=record_id)

        # category = Category.objects.filter(name=record_data['category'])
        # for c in range(len(category)):
        #     category_id = category[c].id
        
        # print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        # print(record_data.category)
        
        # record_data['category'] = record_data.category
        
        record_data['user'] = request.user.id        
        record_data['updated_at'] = datetime.today().strftime('%d-%m-%Y') 

        record_serializer = FoodDiarySerializer(record, data=record_data)

        if record_serializer.is_valid():
            record_serializer.save()
            return HttpResponse(json.dumps("record updated"), content_type="application/json", status=200)
        else:
            error_list = [record_serializer.errors[error][0]
                          for error in record_serializer.errors]
            return HttpResponse(json.dumps(error_list), content_type="application/json", status=400)


class FoodProgress(generics.GenericAPIView):
    """
    Vypis progresu od .. do .. 
    """

    def get(self, request, format=None):

        data = JSONParser().parse(request)

        date_from = data['date_from']
        date_to = data['date_to']

        records = FoodDiary.objects.filter(
            updated_at__gte=date_from, updated_at__lte=date_to).filter(user_id=request.user.id)

        records_id = []
        for rec in range(len(records)):
            if records[rec].product_id in records_id:
                continue
            else:
                records_id.append(records[rec].product_id)

        # for x in range(len(records_id)):
        #     print(records_id[x])

        products = Product.objects.filter(id__in=records_id)

        calories = 0
        fats =  0
        carbohydrates = 0
        protein = 0

        for rec in range(len(products)):
            calories += products[rec].calories
            fats += products[rec].fats
            carbohydrates += products[rec].carbohydrates
            protein += products[rec].protein
        
        progress = {}

        progress['calories'] = round(calories,2)
        progress['fats'] = round(fats,2)
        progress['carbohydrates'] = round(carbohydrates,2)
        progress['protein'] = round(protein,2)

        food_serializer = ProductSerializer(products, many=True)
        return HttpResponse(json.dumps(progress), content_type="application/json", status=200)
