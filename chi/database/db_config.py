from ..config.config import Config

class DatabaseConfig(Config):
    _CONFIG_FILE = 'cassandra.json'
    _CLUSTER_NAME_KEY = 'cluster-name'
    _KEYSPACE_KEY = 'keyspace'
    _REPLICATION_KEY = 'replication-factor'
    _EXECUTOR_THREADS_KEY = 'executor-threads'
    _CONNECTION_TIMEOUT_KEY = 'connection-timeout'
    _QUERY_TIMEOUT_KEY = 'query-timeout'
    _ENDPOINTS_KEY = 'endpoints'
    _ENDPOINTS_HOST_KEY = 'hosts'
    _ENDPOINTS_PORT_KEY = 'port'

    _DEFAULT_CLUSTER_NAME = 'Test Cluster'
    _DEFAULT_KEYSPACE = 'chi'
    _DEFAULT_REPLICATIION = 1
    _DEFAULT_EXECUTOR_THREADS = 10
    _DEFAULT_CONNECTION_TIMEOUT = 20.0
    _DEFAULT_QUERY_TIMEOUT = 10.0
    _DEFAULT_ENDPOINT_HOSTS = ['192.168.0.95']
    _DEFAULT_ENDPOINT_PORT = 9042

    def __init__(self):
        super().__init__(DatabaseConfig._CONFIG_FILE)

    def _init_default(self):
        self.cluster_name = DatabaseConfig._DEFAULT_CLUSTER_NAME
        self.keyspace = DatabaseConfig._DEFAULT_KEYSPACE
        self.replication = DatabaseConfig._DEFAULT_REPLICATIION
        self.executor_threads = DatabaseConfig._DEFAULT_EXECUTOR_THREADS
        self.connection_timeout = DatabaseConfig._DEFAULT_CONNECTION_TIMEOUT
        self.query_timeout = DatabaseConfig._DEFAULT_QUERY_TIMEOUT

        self.settings[DatabaseConfig._ENDPOINTS_KEY] = {}
        self.endpoints = DatabaseConfig._DEFAULT_ENDPOINT_HOSTS
        self.endpoints_port = DatabaseConfig._DEFAULT_ENDPOINT_PORT

    @property
    def cluster_name(self):
        return self.settings[DatabaseConfig._CLUSTER_NAME_KEY]

    @cluster_name.setter
    def cluster_name(self, n):
        self.settings[DatabaseConfig._CLUSTER_NAME_KEY] = n

    @property
    def keyspace(self):
        return self.settings[DatabaseConfig._KEYSPACE_KEY]

    @keyspace.setter
    def keyspace(self, n):
        self.settings[DatabaseConfig._KEYSPACE_KEY] = n

    @property
    def replication(self):
        return self.settings[DatabaseConfig._REPLICATION_KEY]

    @replication.setter
    def replication(self, n):
        self.settings[DatabaseConfig._REPLICATION_KEY] = n

    @property
    def executor_threads(self):
        return self.settings[DatabaseConfig._EXECUTOR_THREADS_KEY]

    @executor_threads.setter
    def executor_threads(self, n):
        self.settings[DatabaseConfig._EXECUTOR_THREADS_KEY] = n

    @property
    def connection_timeout(self):
        return self.settings[DatabaseConfig._CONNECTION_TIMEOUT_KEY]

    @connection_timeout.setter
    def connection_timeout(self, n):
        self.settings[DatabaseConfig._CONNECTION_TIMEOUT_KEY] = n

    @property
    def query_timeout(self):
        return self.settings[DatabaseConfig._QUERY_TIMEOUT_KEY]

    @query_timeout.setter
    def query_timeout(self, n):
        self.settings[DatabaseConfig._QUERY_TIMEOUT_KEY] = n

    @property
    def endpoints_port(self):
        return self.settings[DatabaseConfig._ENDPOINTS_KEY][DatabaseConfig._ENDPOINTS_PORT_KEY]

    @endpoints_port.setter
    def endpoints_port(self, n):
        self.settings[DatabaseConfig._ENDPOINTS_KEY][DatabaseConfig._ENDPOINTS_PORT_KEY] = n

    @property
    def endpoints(self):
        return self.settings[DatabaseConfig._ENDPOINTS_KEY][DatabaseConfig._ENDPOINTS_HOST_KEY]

    @endpoints.setter
    def endpoints(self, n):
        self.settings[DatabaseConfig._ENDPOINTS_KEY][DatabaseConfig._ENDPOINTS_HOST_KEY] = n