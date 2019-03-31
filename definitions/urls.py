from django.urls import path

from . import views

urlpatterns = [
    path('devices/', views.devices, name='devices'),
    path('devices/add/', views.add_device, name='add_device'),
    path('devices/add/building/<int:building_id>/sites', views.add_device_site, name='add_device_site'),
    path('devices/add/create', views.add_device_create, name='add_device_create'),
]