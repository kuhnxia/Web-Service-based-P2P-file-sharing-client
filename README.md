# Web-Service-based-P2P-file-sharing-client

- This is a file sharing client based on Jboss registration service.
- Sharing files between clients relies on Java Socket.
- The GUI is implemented by JavaFX.

## Quick Start

- Waiting Maven auto-building completed.
- Choose Java 17 as the Project JDK.
- Change the url to your real Jboss service IP in [FileShareService](src/main/java/kun/service/FileShareService.java).
```shell
private static final String BASE_URI = "http://localhost:8080/share";
```
- Run JavaFX application
```shell
mvn clean javafx:run
```

## JavaFX

- Current version is 17.0.8.
- You can try different version by change maven dependencies.
- Try another client on the same machine, just run it on a new terminal.
```shell
mvn clean javafx:run
```

## Socket

### 1. Server Socket
- Server socket thread will be running at the backend until you stop the client program.
- It can be connected by different IPs if you have more than one interfaces, such as Ethernet, Wi-Fi, VPN, Cellular.
- The client will ask you to choose the correct IP you will use in your local network to test.
- There will be a new child thread to interact with a new client socket.

### 2. Client Socket
- When request file sharing, a new client socket thread will be created and connect to the server socket.

## Local Helpers relying on specific OS.

### 1. Search ports in use.
- `listPortsInUse()` method in [LocalNetworkHelper.java](src/main/java/kun/helpers/LocalNetworkHelper.java) is only available for macOS.
- `startSocketServer()` method in [MainMenuStageStart.java](src/main/java/kun/stages/MainMenuStageStart.java) has limit the use of `listPortsInUse()` to macOS.
- On windows, you need manually handle port in use error.

### 2. Local File Operation
- These operation methods have only been tested on macOS.
- Errors may occur on windows although every effort has been made to make these operations as general as possible.