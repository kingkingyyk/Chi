from ..config.config import Config

class DatabaseConfig(Config):
    _CONFIG_FILE = 'cassandra.json'
    _CLUSTER_NAME_KEY = 'cluster-name'
    _KEYSPACE_KEY = 'keyspace'
    _REPLICATION_KEY = 'replication'
    _EXECUTOR_THREADS_KEY = 'executor-threads'
    _ENDPOINTS_KEY = 'endpoints'
    _ENDPOINTS_HOST_KEY = 'hosts'
    _ENDPOINTS_PORT_KEY = 'port'

    _DEFAULT_CLUSTER_NAME = 'Test Cluster'
    _DEFAULT_KEYSPACE = 'Chi'
    _DEFAULT_REPLICATIION = '\'class\': \'SimpleStrategy\', \'replication_factor\': 1'
    _DEFAULT_EXECUTOR_THREADS = 10
    _DEFAULT_ENDPOINT_HOSTS = ['192.168.0.152']
    _DEFAULT_ENDPOINT_PORT = 9042

    def __init__(self):
        super().__init__(DatabaseConfig._CONFIG_FILE)

    def _init_default(self):
        self.settings = {DatabaseConfig._CLUSTER_NAME_KEY: DatabaseConfig._DEFAULT_CLUSTER_NAME,
                         DatabaseConfig._KEYSPACE_KEY: DatabaseConfig._DEFAULT_KEYSPACE,
                         DatabaseConfig._REPLICATION_KEY: DatabaseConfig._DEFAULT_REPLICATIION,
                         DatabaseConfig._EXECUTOR_THREADS_KEY: DatabaseConfig._DEFAULT_EXECUTOR_THREADS,
                         DatabaseConfig._ENDPOINTS_KEY:  {
                                                            DatabaseConfig._ENDPOINTS_HOST_KEY: DatabaseConfig._DEFAULT_ENDPOINT_HOSTS,
                                                            DatabaseConfig._ENDPOINTS_PORT_KEY: DatabaseConfig._DEFAULT_ENDPOINT_PORT
                                                        }
                         }

    @property
    def cluster_name(self):
        return self.settings[DatabaseConfig._CLUSTER_NAME_KEY]

    @cluster_name.setter
    def cluster_name(self, n):
        self.settings[DatabaseConfig._CLUSTER_NAME_KEY]

    @property
    def keyspace(self):
        return self.settings[DatabaseConfig._KEYSPACE_KEY]

    @keyspace.setter
    def keyspace(self, n):
        self.settings[DatabaseConfig._KEYSPACE_KEY]

    @property
    def replication(self):
        return self.settings[DatabaseConfig._REPLICATION_KEY]

    @replication.setter
    def replication(self, n):
        self.settings[DatabaseConfig._REPLICATION_KEY]

    @property
    def executor_threads(self):
        return self.settings[DatabaseConfig._EXECUTOR_THREADS_KEY]

    @executor_threads.setter
    def executor_threads(self, n):
        self.settings[DatabaseConfig._EXECUTOR_THREADS_KEY] = n

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