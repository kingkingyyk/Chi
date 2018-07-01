import json
from .test_view import TestView


class TestAdminView(TestView):

    def test_shutdown(self, client):
        rv = client.post('/admin/shutdown/')
        assert json.loads(rv.data) == {'status': True}