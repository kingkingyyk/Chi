from flask import Flask, request, make_response
from gevent import monkey
from gevent.pywsgi import WSGIServer
import threading
from .admin_view import AdminView
from .user_view import UserView

class RESTServer (threading.Thread):

    def __init__(self, config):
        threading.Thread.__init__(self)
        self.config = config

        monkey.patch_all()
        self.app = Flask('Chi')
        self.app.config.update(DEBUG=True)
        AdminView.register(self.app)
        UserView.register(self.app)

    def run(self):
        rest_server = WSGIServer(('0.0.0.0', self.config.port), self.app)
        rest_server.serve_forever()