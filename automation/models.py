from django.db import models
from definitions.models import Actuator, Sensor


class Schedule(models.Model):
    actuator = models.ForeignKey(Actuator, on_delete=models.CASCADE)


class ScheduleLine(models.Model):
    pass


class WeeklySchedule(Schedule):
    pass


class WeeklyScheduleDay(models.Model):
    schedule = models.ForeignKey(WeeklySchedule, on_delete=models.CASCADE)
    day_of_week_start = models.IntegerField()
    day_of_week_end = models.IntegerField()


class WeeklyScheduleDayTimeInterval(ScheduleLine):
    schedule_day = models.ForeignKey(WeeklyScheduleDay, on_delete=models.CASCADE)
    start_time = models.TimeField()
    end_time = models.TimeField()
    remark = models.TextField()
    on_schedule_actuator_status = models.TextField()
    post_schedule_actuator_status = models.TextField()


class DaySpecificSchedule(Schedule):
    pass


class DaySpecificScheduleLine(ScheduleLine):
    schedule = models.ForeignKey(DaySpecificSchedule, on_delete=models.CASCADE)
    day = models.DateField()
    remark = models.TextField()
    on_schedule_actuator_status = models.TextField()
    post_schedule_actuator_status = models.TextField()


class SimpleTrigger(models.Model):
    schedule_line = models.ForeignKey(ScheduleLine, on_delete=models.CASCADE)


class SensorTrigger(SimpleTrigger):
    expression = models.TextField()
    threshold_value = models.FloatField()
    threshold_operator = models.CharField(max_length=2)


class SensorTriggerSensor(models.Model):
    sensor_trigger = models.ForeignKey(SensorTrigger, on_delete=models.CASCADE)
    sensor = models.ForeignKey(Sensor, on_delete=models.CASCADE)
