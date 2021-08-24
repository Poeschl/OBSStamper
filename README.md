# OBSStamper

![Latest release](https://img.shields.io/github/v/release/Poeschl/OBSStamper?label=latest%20release)

A little tool which writes timestamps of a obs recording into a text file.

## Installation

The OBSStamper talks via Websockets to OBS. Since OBS dont have a native interface the [OBS Websocket Plugin](https://github.com/Palakis/obs-websocket) needs to be installed. To do
so goto the [Releases page of the plugin](https://github.com/Palakis/obs-websocket/releases) and follow the instructions for your system.

Additionally **Java >= 8** needs to be installed on your system.

Afterwards download the [latest release form this repository](https://github.com/Poeschl/ObsStamper/releases) to a folder of your liking and remember the path to it.

## Execute

Everytime you run the OBSStamper with the command below, it will write the current timestamp of the recording into a text file.

```shell
java -jar path/from/install/OBSStamper-1.0.0.jar -f path/to/textfileparent -p MySuperSecretPassword
```

With `-f` the parent folder for the generated textfiles can be set. Inside it the files will be named after the namename of the recording file.

The parameter `-p` sets a password for a secured connection to the websocket plugin. Make sure it is matching the one you specified in the plugin settings.

The full list of parameter is shown with `--help`!

### Use with StreamDeck

Set one of your button to execute a command
