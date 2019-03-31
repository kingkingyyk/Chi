from django.db import models


# Create your models here.
class Building(models.Model):
    name = models.TextField()


class Site(models.Model):
    building = models.ForeignKey(Building, on_delete=models.CASCADE)
    floor = models.CharField(max_length=10)
    name = models.TextField()
    floor_plan = models.TextField()


class Device(models.Model):
    site = models.ForeignKey(Site, on_delete=models.SET_NULL, blank=True, null=True)
    name = models.TextField()
    unique_id = models.UUIDField(unique=True)
    pos_x = models.FloatField()
    pos_y = models.FloatField()


class Sensor(models.Model):
    device = models.ForeignKey(Device, on_delete=models.CASCADE)
    name = models.TextField()
    pos_x = models.FloatField()
    pos_y = models.FloatField()


class SensorReading(models.Model):
    sensor = models.ForeignKey(Sensor, on_delete=models.CASCADE)
    timestamp = models.DateTimeField()
    value = models.FloatField()


class Actuator(models.Model):
    device = models.ForeignKey(Device, on_delete=models.CASCADE)
    real_status = models.TextField()
    expected_status = models.TextField()