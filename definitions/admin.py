from django.contrib import admin
from .models import *

# Register your models here.
admin.site.register(Building)
admin.site.register(Site)
admin.site.register(Device)
admin.site.register(Sensor)
admin.site.register(Actuator)
admin.site.register(SensorReading)