from django.urls import re_path as url, include
from UserApp import views
from django.urls import path

from rest_framework import permissions
from drf_yasg.views import get_schema_view
from drf_yasg import openapi
from django.urls import re_path

schema_view = get_schema_view(
   openapi.Info(
      title="API",
      default_version='v1',
      description="Test description",
      terms_of_service="https://www.google.com/policies/terms/",
      contact=openapi.Contact(email="contact@snippets.local"),
      license=openapi.License(name="BSD License"),
   ),
   public=True,
   permission_classes=[permissions.AllowAny],
   authentication_classes=[]
)

urlpatterns = [
   path('products/', views.ProductList.as_view()),
   path('products/search/', views.ProductListSearch.as_view()),
   path('products/<int:product_id>/', views.ProductDetail.as_view()),
   path('product/', views.Products.as_view()),
   path('groups/', views.ProductList.as_view()),
   path('groups/<slug:group_name>/', views.GroupDetail.as_view()),
   path('users/me/', views.UserDetail.as_view()),
   path('users/<int:user_id>/', views.UserDetail.as_view()),
   path('users/me/nutrition/', views.FoodDiaryList.as_view()),
   path('users/me/nutrition/<int:record_id>/', views.FoodDiaryDetail.as_view()),
   path('users/me/progress/', views.FoodProgress.as_view()),
   # url(r'^product$', views.GroupList.as_view()),
   # url(r'^product/(?P<product_id>\d+)', views.ProductDetail.as_view()),
   # url(r'^group$', views.GroupList.as_view()),
   # url(r'^group/(?P<group_name>[\w\-]+)/$', views.GroupDetail.as_view()),
   # url(r'^users/me$', views.UserDetail.as_view()),
   # url(r'^users/(?P<user_id>\d+)', views.UserDetailSpecificID.as_view()),
   re_path(r'^swagger(?P<format>\.json|\.yaml)$', schema_view.without_ui(cache_timeout=0), name='schema-json'),
   re_path(r'^swagger/$', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
   re_path(r'^redoc/$', schema_view.with_ui('redoc', cache_timeout=0), name='schema-redoc'),
]