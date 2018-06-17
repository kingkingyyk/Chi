from .test_model import TestModelBase
from chi.database.model.model import Site


class TestSite(TestModelBase):

    def create_model_object(self):
        self.name = 'mysite'
        self.url = 'https://www.mysite.com/img.jpg'
        site = Site.create(name=self.name, map_url=self.url)
        site.save()
        self.id = site.id

    def test_query(self):
        site = Site.objects(id=self.id).first()
        assert site.id == self.id
        assert site.name == self.name
        assert site.map_url == self.url

    def test_update(self):
        new_name = 'mynewsite'
        new_url = 'abcde'
        Site.objects(id=self.id).update(name=new_name, map_url=new_url)

        site = Site.objects(id=self.id).first()
        assert site.id == self.id
        assert site.name == new_name
        assert site.map_url == new_url

    def test_delete(self):
        Site.objects(id=self.id).first().delete()

        assert Site.objects(id=self.id).count() == 0
