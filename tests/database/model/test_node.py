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

    def test_query(self):
        node = Node.objects(id=self.id).first()
        assert node.id == self.id
        assert node.name == self.name
        assert node.map_x == self.x
        assert node.map_y == self.y

    def test_update(self):
        new_name = 'abcde'
        new_x = -8.0
        new_y = 8.0
        Node.objects(id=self.id).update(name=new_name, map_x=new_x, map_y=new_y)

        node = Node.objects(id=self.id).first()
        assert node.id == self.id
        assert node.name == new_name
        assert node.map_x == new_x
        assert node.map_y == new_y

    def test_delete(self):
        node = Node.objects(id=self.id).first()
        node.delete()

        assert Node.objects(id=self.id).count() == 0