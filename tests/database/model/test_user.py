from .test_model import TestModelBase
from chi.database.model.model import User


class TestUser(TestModelBase):

    def create_model_object(self):
        self.username = 'lol'
        self.password = '12345'
        user = User.create(username=self.username, password=self.password)
        user.save()
        self.id = user.id

    def test_create(self):
        user = User.objects().first()
        assert user.username == self.username
        assert user.password == self.password

    def test_update(self):
        new_password = 'abcde'
        User.objects(id=self.id, username=self.username).update(password=new_password)

        user = User.objects(id=self.id, username=self.username).first()
        assert user.password == new_password

    def test_delete(self):
        user = User.objects(id=self.id, username=self.username).first()
        user.delete()

        assert User.objects(id=self.id, username=self.username).count() == 0

