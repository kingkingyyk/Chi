from .test_view import TestView
from chi.database.model.model import User
from chi.rest.user_view import UserView
from http import HTTPStatus
from flask import Flask
from chi.globals import main

class TestUsersView(TestView):

    def create_model_object(self):
        self.username = 'lol'
        self.password = '12345'

    def test_login_valid(self, client):
        user = User.create(username=self.username, password=User.encrypt_password(self.password))
        user.save()

        response = client.post('/user/login', json={UserView.USERNAME_KEY:self.username, UserView.PASSWORD_KEY: self.password})
        assert response.status_code == HTTPStatus.OK, 'Initial valid login'

        response = client.post('/user/login')
        assert response.status_code == HTTPStatus.OK, 'Saved login'

    def test_login_invalid_1(self, client):
        user = User.create(username=self.username, password=User.encrypt_password(self.password))
        user.save()

        response = client.post('/user/login', json={UserView.USERNAME_KEY:self.username, UserView.PASSWORD_KEY: self.password+'abcde'})
        assert response.status_code == HTTPStatus.UNAUTHORIZED, 'Invalid username or password login'

    def test_login_invalid_2(self, client):
        response = client.post('/user/login', json={UserView.USERNAME_KEY:self.username, UserView.PASSWORD_KEY: self.password})
        assert response.status_code == HTTPStatus.UNAUTHORIZED, 'Invalid username or password login'

    def test_login_invalid_3(self, client):
        response = client.post('/user/login', json={UserView.USERNAME_KEY:self.username})
        assert response.status_code == HTTPStatus.BAD_REQUEST, 'Invalid username or password login'

    def test_login_invalid_4(self, client):
        response = client.post('/user/login')
        assert response.status_code == HTTPStatus.BAD_REQUEST, 'Invalid username or password login'

    def test_login_invalid_5(self, client):
        response = client.post('/user/login', json={'abcde':'some random useless data!!!'})
        assert response.status_code == HTTPStatus.BAD_REQUEST, 'Invalid username or password login'

    def test_login_invalid_6(self, client):
        response = client.post('/user/login', json={'username':123, 'password': True})
        assert response.status_code == HTTPStatus.BAD_REQUEST, 'Invalid username or password login'

    def test_logoff_valid(self, client):
        user = User.create(username=self.username, password=User.encrypt_password(self.password))
        user.save()

        response = client.post('/user/login', json={UserView.USERNAME_KEY:self.username, UserView.PASSWORD_KEY: self.password})
        assert response.status_code == HTTPStatus.OK, 'Initial valid login'

        response = client.post('/user/logout')
        assert response.status_code == HTTPStatus.OK, 'Initial valid logout'

        response = client.post('/user/login')
        assert response.status_code == HTTPStatus.BAD_REQUEST, 'Cleared token login'

    def test_logoff_invalid(self, client):
        response = client.post('/user/logout')
        assert response.status_code == HTTPStatus.UNAUTHORIZED, 'Invalid logout'