class Observer(object):

    def __init__(self):
        self._observer_callback = {}

    def __setattr__(self, key, value):
        super().__setattr__(key, value)
        if key is not '_observer_callback' and key in self._observer_callback.keys():
            for x in self._observer_callback[key]:
                if len(x) is 1:
                    x[0]()
                else:
                    x[0](x[1])

    def observe(self, key, method, *args):
        if key not in self._observer_callback.keys():
            self._observer_callback[key] = []
        self._observer_callback[key].append((method, *args))

    def unobserve(self, key, method):
        for tuple in [x for x in self._observer_callback if x[0] is method]:
            self._observer_callback[key].remove(tuple)