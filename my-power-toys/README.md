
# My Power Tools

This is a collection scripts and tools bundeled together under a common launcher. 

## Project modules

The first set of modules estanblish base to build tools from:

| Module          |                                                            |
| --------------- | ---------------------------------------------------------- |
| `mpt-app`       | Main launcher invoked from the command line                |
| `mpt-commns`    | A common set of utilties shared across the project modules |
| `mpt-framework` | Framework to build tools on top on.                        |

The `mpt-framework` includes the `mpt-commons` as transitive API dependency, which means that any module dependent on
it will also get access to the commons module on a implementation level.

## Tool Modules

The following tool modules are included: 

1. `mpt-tool-javadev` - A collection of java tools for kotlin/java developers.
2. `mpt-tool-installer` - A installer tool which installs the toolset on a target machine. Intially ony unix like
    hosts are catered for. Windows could follow.

## Building and installing

To build the project run:

```shell
./gradlew build
```

There are two of installing:

### Developer Installation

Append the following to your path: `mpt-app/build/install/mpt/bin`. This will make the following assumptions:

1. There is a local folder under the root project called `local`. If not exists it will create one on first run.
2. A file called `local/mpt.conf` configuration file.

### User Installation

Download and run the following command:

```shell
java -jar mtp.jar install
```

To remove the installation:

```shell
java -jar mtp.jar uninstall
```

### Configuration

The tool uses HOCON configuration files.

