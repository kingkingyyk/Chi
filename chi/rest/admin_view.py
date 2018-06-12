import json
from flask_classful import FlaskView


class AdminView(FlaskView):

    def shutdown(self):
        return json.dumps({'status' : True})