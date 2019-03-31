from django.contrib import admin
from .models import *

# Register your models here.
admin.site.register(Schedule)
admin.site.register(ScheduleLine)
admin.site.register(WeeklySchedule)
admin.site.register(WeeklyScheduleDay)
admin.site.register(DaySpecificSchedule)
admin.site.register(DaySpecificScheduleLine)
admin.site.register(SimpleTrigger)
admin.site.register(SensorTrigger)
admin.site.register(SensorTriggerSensor)