from chi.data.observer import Observer


class TestObserver(object):
    Respond = False

    def teardown_method(self, method):
        TestObserver.Respond = False

    def invert_respond(self):
        TestObserver.Respond = not TestObserver.Respond

    def test_observe_unobserve(self):
        observer = Observer()
        observer.test_attr = 'Initial'
        observer.observe('test_attr', self.invert_respond)
        observer.test_attr = 'Updated'

        assert TestObserver.Respond, 'Observe is not working!'

        observer.unobserve('test_attr', self.invert_respond)
        observer.test_attr = 'Another update'
        assert not TestObserver.Respond, 'Unobserve is not working!'