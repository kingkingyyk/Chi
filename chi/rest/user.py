from aiohttp import web
from ..database.model.model import *
import json


async def get_all(request):
    list = [{'id':x.id, 'username': x.username} for x in User.objects().all()]
    return web.Response(body=json.dumps({'values':list}), status=200)


async def get_by_id(request, uuid):
    user = User.objects(uuid,uuid).if_exists()


async def create(request):
    user = User(username = request.json.params['username'], password = request.json.params['password'])
    user.save()
    return web.Response(body=json.dumps({'id':user.id, 'username': user.username}), status=200)


async def update(request, uuid):
    user = User(username = request.json.params['username'], password = request.json.params['password'])
