from ..database.model.model import *
from flask_classful import FlaskView, route, make_response
from flask import request
from chi.globals import main
from .representations import return_response
from http import HTTPStatus

class UserView(FlaskView):
    USERNAME_KEY = 'username'
    PASSWORD_KEY = 'password'
    TOKEN_KEY = 'session'

    @route('/login', methods=['POST'])
    def login(self):
        token = request.cookies.get(UserView.TOKEN_KEY)
        if token is not None:
            if main.chi_server.rest_server.is_valid_token(token):
                return return_response()
            else:
                return return_response(code=HTTPStatus.UNAUTHORIZED)

        if request.json is None or \
                UserView.USERNAME_KEY not in request.json.keys() or \
                UserView.PASSWORD_KEY not in request.json.keys():
            return return_response(code=HTTPStatus.BAD_REQUEST)

        username = request.json[UserView.USERNAME_KEY]
        password = request.json[UserView.PASSWORD_KEY]

        if type(username) is not str and type(password) is not str:
            return return_response(code=HTTPStatus.BAD_REQUEST)

        user = User.objects(username=username).first()
        if user is None or not user.is_password_match(password):
            return return_response(code=HTTPStatus.UNAUTHORIZED)
        else:
            token = main.chi_server.rest_server.generate_token(username)
            return return_response(cookies={UserView.TOKEN_KEY: str(token)})

    @route('/logout', methods=['POST'])
    def logout(self):
        token = request.cookies.get(UserView.TOKEN_KEY)
        if token is not None and main.chi_server.rest_server.is_valid_token(token):
            main.chi_server.rest_server.release_token(token)
            response = make_response('', HTTPStatus.OK)
            response.delete_cookie(UserView.TOKEN_KEY)
            return response
        else:
            return return_response(code=HTTPStatus.UNAUTHORIZED)

    """
    def get_all(self):
        list = [{'id': x.id, 'username': x.username} for x in User.objects().all()]
        return json.dumps({'values': list})

    def get_by_id(self, uuid):
        user = User.objects(uuid,uuid).if_exists()

    def create(self):
        user = User(username = request.json.params['username'], password = request.json.params['password'])
        user.save()
        return json.dumps({'id':user.id, 'username': user.username})

    def update(self, uuid):
        user = User(username = request.json.params['username'], password = request.json.params['password'])
    """