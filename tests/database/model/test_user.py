from .test_model import TestModelBase
from chi.database.model.model import User


class TestUser(TestModelBase):

    def create_model_object(self):
        self.username = 'lol'
        self.password = '12345'
        self.role = User.READWRITE_USER
        user = User.create(username=self.username, password=User.encrypt_password(self.password), role=self.role)
        user.save()
        self.id = user.id
        self.password = user.password

    def test_query(self):
        user = User.objects(id=self.id).first()
        assert user.id == self.id
        assert user.username == self.username
        assert user.password == self.password
        assert user.role == self.role

    def test_update(self):
        new_password = User.encrypt_password('abcde')
        User.objects(id=self.id).update(password=new_password)

        user = User.objects(id=self.id).first()
        assert user.id == self.id
        assert user.username == self.username
        assert user.password == new_password
        assert user.role == self.role

    def test_delete(self):
        user = User.objects(id=self.id).first()
        user.delete()

        assert User.objects(id=self.id).count() == 0

