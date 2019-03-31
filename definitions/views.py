from django.shortcuts import render
from .models import *
from django.db import transaction
from Chi.settings import STATICFILES_DIRS
from django.http import JsonResponse
import uuid, os, json

# Create your views here.
def devices(request):
    context = {
        'devices': Device.objects.all()
    }
    return render(request, 'definitions/devices.html', context)


def add_device(request):
    context = {
        'buildings': Building.objects.all()
    }
    return render(request, 'definitions/add_device.html', context)


def add_device_site(request, building_id):
    building = Building.objects.filter(id=building_id).get()
    context = {
        'sites': Site.objects.filter(building=building).all()
    }
    return render(request, 'definitions/add_device_site.html', context)

@transaction.atomic
def add_device_create(request):
    data = json.loads(request.body)
    if data['building']['type'] == 'existing':
        building = Building.objects.filter(id=data['building']['id']).get()
    else:
        building = Building(name=data['building']['name'])
        building.save()
    if data['site']['type'] == 'existing':
        site = Building.objects.filter(id=data['site']['id']).get()
    else:
        picture_uuid = str(uuid.uuid4())+'.jpg'
        with open(os.path.join(STATICFILES_DIRS[0], 'definitions', 'site', 'floor-plan', picture_uuid), 'wb') as f:
            f.write(data['site']['floor-plan'].encode('UTF-16'))
        site = Site(building=building, floor=data['site']['floor'],
                    name=data['site']['name'], floor_plan='/static/definitions/site/floor-plan/'+picture_uuid)
        site.save()
    device = Device(site=site, name=data['name'], unique_id=uuid.uuid4(),
                    pos_x=0.5, pos_y=0.5)
    device.save()
    return JsonResponse({'device-id': device.id})
