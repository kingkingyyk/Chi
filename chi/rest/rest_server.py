from flask import Flask
from gevent import monkey
from gevent.pywsgi import WSGIServer
import threading
from .admin_view import AdminView
from .user_view import UserView

class RESTServer (threading.Thread):

    def __init__(self, config):
        threading.Thread.__init__(self)
        self.config = config

    def run(self):
        monkey.patch_all()
        app = Flask('Chi')
        app.config.update(DEBUG=True)

        AdminView.register(app)
        UserView.register(app)

        rest_server = WSGIServer(('', self.config.port), app)
        rest_server.serve_forever()

