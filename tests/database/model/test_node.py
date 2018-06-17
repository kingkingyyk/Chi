from .test_model import TestModelBase
from chi.database.model.model import Site, Node

class TestNode(TestModelBase):

    def create_model_object(self):
        self.site = Site.create(name='mysite', map_url='someurl')
        self.site.save()

        self.name = 'mynode'
        self.x = -4.0
        self.y = 4.0
        node = Node.create(name=self.name, site_id=self.site.id, map_x=self.x, map_y=self.y)
        node.save()
        self.id = node.id

    def test_create(self):
        node = Node.objects().first()
        assert node.name == self.name
        assert node.map_x == self.x
        assert node.map_y == self.y

    """
    def test_update(self):
        new_name = 'abcde'
        User.objects(id=self.id, username=self.username).update(password=new_password)

        user = User.objects(id=self.id, username=self.username).first()
        assert user.password == new_password

    def test_delete(self):
        user = User.objects(id=self.id, username=self.username).first()
        user.delete()

        assert User.objects(id=self.id, username=self.username).count() == 0
    """