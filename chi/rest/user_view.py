from aiohttp import web
from ..database.model.model import *
from flask_classful import FlaskView
from flask import request
import json


class UserView(FlaskView):

    def get_all(self):
        list = [{'id':x.id, 'username': x.username} for x in User.objects().all()]
        return json.dumps({'values':list})

    def get_by_id(self, uuid):
        user = User.objects(uuid,uuid).if_exists()

    def create(self):
        user = User(username = request.json.params['username'], password = request.json.params['password'])
        user.save()
        return web.Response(body=json.dumps({'id':user.id, 'username': user.username}), status=200)

    def update(self, uuid):
        user = User(username = request.json.params['username'], password = request.json.params['password'])
