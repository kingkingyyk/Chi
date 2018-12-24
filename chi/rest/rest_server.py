from flask import Flask
from gevent.pywsgi import WSGIServer
import threading
from .admin_view import AdminView
from .user_view import UserView
from ..database.model.model import TokenBind
from datetime import datetime

class RESTServer (threading.Thread):

    def __init__(self, config):
        threading.Thread.__init__(self)
        self.config = config
        self.app = Flask('Chi')
        self.app.config.update(DEBUG=True)
        AdminView.register(self.app)
        UserView.register(self.app)

    def generate_token(self, username):
        t = TokenBind.create(username=username, grant_time=datetime.now())
        t.save()
        return t.token

    def release_token(self, token):
        t = TokenBind.objects(token=token).first()
        if t is not None:
            t.delete()
            return True
        return False

    def is_valid_token(self, token):
        return TokenBind.objects(token=token).first() is not None

    def run(self):
        rest_server = WSGIServer(('0.0.0.0', self.config.port), self.app)
        rest_server.serve_forever()


class AuthenticatedView:

    def requires_admin(func):
        def func_wrapper(self):
            return func(self)
        return func_wrapper
