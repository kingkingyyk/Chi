from ..database.model.model import *
from flask_classful import FlaskView, route, make_response
from flask import request
from chi.globals import main
from .representations import return_response
from http import HTTPStatus

class UserView(FlaskView):
    excluded_methods = ['validate_session', 'validate_json']
    USERNAME_KEY = 'username'
    PASSWORD_KEY = 'password'
    TOKEN_KEY = 'session'

    @staticmethod
    def validate_session():
        token = request.cookies.get(UserView.TOKEN_KEY)
        if token is not None and main.chi_server.rest_server.is_valid_token(token):
            return True, return_response()

        return False, return_response(code=HTTPStatus.UNAUTHORIZED)

    @staticmethod
    def validate_credential_json():
        return request.json is not None and type(request.json.get(UserView.USERNAME_KEY)) is str and type(request.json.get(UserView.PASSWORD_KEY)) is str

    @staticmethod
    def get_current_logged_in_user():
        token = request.cookies.get(UserView.TOKEN_KEY)
        token_obj = TokenBind.objects(token=token).first()
        return User.objects(username=token_obj).first()

    @route('/login', methods=['POST'])
    def login(self):
        has_session, response = UserView.validate_session()

        if has_session:
            return response

        if not UserView.validate_credential_json():
            return return_response(code=HTTPStatus.BAD_REQUEST)

        username = request.json[UserView.USERNAME_KEY]
        password = request.json[UserView.PASSWORD_KEY]

        user = User.objects(username=username).first()
        if user is None or not user.is_password_match(password):
            return return_response(code=HTTPStatus.UNAUTHORIZED)
        else:
            token = main.chi_server.rest_server.generate_token(username)
            return return_response(cookies={UserView.TOKEN_KEY: str(token)})

    @route('/logout', methods=['POST'])
    def logout(self):
        has_session, response = self.validate_session()

        if has_session:
            token = request.cookies.get(UserView.TOKEN_KEY)
            main.chi_server.rest_server.release_token(token)
            response = make_response('', HTTPStatus.OK)
            response.delete_cookie(UserView.TOKEN_KEY)

        return response

    NEW_USERNAME_KEY = 'username_new'
    NEW_PASSWORD_KEY = 'password_new'
    NEW_ROLE_KEY = 'role_new'

    @route('/create', methods=['POST'])
    def create(self):
        has_session, response = self.validate_session()

        if has_session:
            user = UserView.get_current_logged_in_user()
            if user is not None and \
                    user.role == User.ADMINISTRATOR and \
                    type(request.json.get(UserView.NEW_USERNAME_KEY)) is str and \
                    type(request.json.get(UserView.NEW_PASSWORD_KEY)) is str and \
                    type(request.json.get(UserView.NEW_ROLE_KEY)) in User.POSSIBLE_ROLES:

                new_username = request.json[UserView.NEW_USERNAME_KEY]
                new_password = request.json[UserView.NEW_PASSWORD_KEY]
                new_role = request.json[UserView.NEW_ROLE_KEY]

                existing_user = User.objects(username=new_username).first()
                if existing_user is None:
                    if len(new_password) > 0:
                        user = User.create(username=new_password, password=User.encrypt_password(new_password), role=new_role)
                        user.save()
                        response = return_response(code=HTTPStatus.ACCEPTED)
                    else:
                        response = return_response('Password can\'t be empty!', code=HTTPStatus.NOT_MODIFIED)
                else:
                    response = return_response('Username already exists!', code=HTTPStatus.NOT_MODIFIED)

        return response

    @route('/update', methods=['PUT'])
    def update(self):
        has_session, response = self.validate_session()

        if has_session:
            user = UserView.get_current_logged_in_user()
            if user is not None and type(request.json.get(UserView.USERNAME_KEY)) is str:
                username = request.json[UserView.USERNAME_KEY]
                target_user = User.objects(username=username).first()
                if target_user is not None:
                    update_password = UserView.NEW_PASSWORD_KEY in request.json.keys()
                    update_role = UserView.NEW_ROLE_KEY in request.json.keys()

                    if (update_role and not request.json[UserView.NEW_ROLE_KEY] in User.POSSIBLE_ROLES) or update_password and len(request.json[UserView.NEW_PASSWORD_KEY])==0:
                        response = return_response(code=HTTPStatus.NOT_MODIFIED)
                    else:
                        if update_password and (user.username == username or user.role == User.ADMINISTRATOR):
                            target_user.password = User.encrypt_password(request.json[UserView.NEW_PASSWORD_KEY])

                        if update_role and user.role == User.ADMINISTRATOR:
                            target_user.role = request.json[UserView.NEW_ROLE_KEY]

                        target_user.save()
                        response = return_response(code=HTTPStatus.ACCEPTED)
                else:
                    response = return_response('Username doesn\'t exist!', code=HTTPStatus.NOT_MODIFIED)

        return response

    @route('/delete', methods=['POST'])
    def delete(self):
        has_session, response = self.validate_session()

        if has_session:
            user = UserView.get_current_logged_in_user()
            if user is not None and type(request.json.get(UserView.USERNAME_KEY)) is str:
                username = request.json[UserView.USERNAME_KEY]
                if user.username == username:
                    response = return_response('You can\'t delete yourself!', code=HTTPStatus.NOT_MODIFIED)
                else:
                    target_user = User.objects(username=username).first()
                    if target_user is None:
                        response = return_response('Username doesn\'t exist!', code=HTTPStatus.NOT_MODIFIED)
                    else:
                        target_user.delete()
                        response = return_response(code=HTTPStatus.ACCEPTED)
            else:
                response = return_response('Username doesn\'t exist!', code=HTTPStatus.NOT_MODIFIED)

        return response

    @route('/all', methods=['POST'])
    def get_all(self):
        has_session, response = self.validate_session()

        if has_session:
            user = UserView.get_current_logged_in_user()
            if user is not None and user.role == User.ADMINISTRATOR:
                list = [{'id': x.id, 'username': x.username, 'role': x.role} for x in User.objects().all()]
                response = return_response(data=list, code=HTTPStatus.OK)
            else:
                response = return_response(code=HTTPStatus.UNAUTHORIZED)

        return response