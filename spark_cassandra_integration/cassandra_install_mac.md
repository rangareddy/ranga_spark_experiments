Installing Cassandra on Mac OS X
================================

Install Homebrew
----------------
Homebrew is a great little package manager for OS X. If you haven't already, installing it is pretty easy:

```sh
ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/homebrew/go/install)"
```

Install Python
--------------
Mac OS X has a copy of Python preinstalled, but this makes sure you get the newest version.

```sh
brew install python3
```

Install cql
-----------
To use cqlsh, the Cassandra query language shell, you need to install cql:

```sh
pip3 install cql
```

Install Cassandra
-----------------
This installs Apache Cassandra:

```sh
brew install cassandra
```

Check the Cassandra version
-----------------
```sh
cassandra --version
```
Starting/Stopping Cassandra
---------------------------
Use this command to start Cassandra:

```Shell
launchctl load ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist
```

Use this command to stop Cassandra:

```sh
launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist
```

On Mavericks, Homebrew failed to move the plist file into LaunchAgents, which gives this error message:

```sh
launchctl: Couldn't stat("/Users/<user>/Library/LaunchAgents/homebrew.mxcl.cassandra.plist"): No such file or directory
```

To fix this just issue the following command. Then, try using the `launchctl load` command again:

```sh
cp /usr/local/Cellar/cassandra/<version number>/homebrew.mxcl.cassandra.plist ~/Library/LaunchAgents/
```

Cassandra file locations
------------------------
- Properties: `/usr/local/etc/cassandra`
- Logs: `/usr/local/var/log/cassandra`
- Data: `/usr/local/var/lib/cassandra/data`

Start the Cassandra and cqlsh
-------------------
```sh
cassandra
cqlsh
```

Cassandra Commands
------------------
### Show Host
```sql
cqlsh> show host;
Connected to Test Cluster at 127.0.0.1:9042.
```

### Describe Cluster
```sql
cqlsh> describe cluster;

Cluster: Test Cluster
Partitioner: Murmur3Partitioner
```
### Describing Keyspaces

```sql
cqlsh> describe keyspaces;

system_traces  system_schema  system_auth  system  system_distributed
```

### Creating a Keyspace
```sql
cqlsh> CREATE KEYSPACE ranga_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };
cqlsh> describe keyspaces;

system_schema  ranga_keyspace  system_distributed
system_auth    system          system_traces
```
### Using a Keyspace
```sql
cqlsh> use ranga_keyspace;
```

### Describe Tables
```sql
cqlsh:ranga_keyspace> describe tables;

<empty>
```

### Create a Table
```sql
cqlsh:ranga_keyspace> CREATE TABLE ranga_keyspace.employees(id bigint PRIMARY KEY, name TEXT, age int, salary float);
```

### Insert the data
```sql
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (1, 'Ranga Reddy', 33, 50000.00);
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (2, 'Nishanth Reddy', 4, 80000.00);
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (3, 'Raja Sekhar Reddy', 60, 150000.00);
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (4, 'Mani Reddy', 16, 90000.00);
```

### Select the data
```sql
cqlsh:ranga_keyspace> SELECT * FROM ranga_keyspace.employees;

 id | age | name              | salary
----+-----+-------------------+---------
  2 |   4 |    Nishanth Reddy |   80000
  3 |  60 | Raja Sekhar Reddy | 1.5e+05
  4 |  16 |        Mani Reddy |   90000
  1 |  33 |       Ranga Reddy |   50000

(4 rows)
```

Happy Learning!!