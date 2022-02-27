<img src="https://i.ibb.co/rd4j2zk/brillan.png" width="400">

# Introduction
This project is about the creation of an online dynamic service platform where expert users can connect to the server in order to install Java classes _which must follow a precise standard_ from their FTP server that users can start by connecting using another port.

# Installation
## Client
The client used to access the server follows a naive protocol. One thread is about printing any received message from the server and the other thread is about sending any inputted message to the server.

An implementation of that client is given in the client folder.

## Server
In order to start the server, compile it and start the main function in the `serveur/appli/BRiLaunch.java` file.

## FTP
You need an FTP server in order to use this platform. An Apache FTP Server installation is given in the repository, set your JRE installation folder as `JAVA_HOME` environement variable and start your server using the following command `bin\ftpd.bat res\conf\ftpd-typical.xml`. (`ftpd.sh` on Linux)

Put your files in the `apache-ftpserver\res\home` folder.

# Usage
### Programmers
Programmers use the port 4000 to connect to the server.

When connected, programmers can log in their existing account or create a new account, in which case they need to provide the url of their FTP server. After authentification, different actions are possible as installing a new service, updating an existing one, activating or disactivating a service, changing the URL of your FTP server or deleting a service.

### Users
Users use the port 3000 to connect to the server.

When connected, users are shown the list of active services. They can chose one and directly start using them. 

## BRi Standard
Service classes must follow the BRi standard in order to be added to the platform, here are all the conditions.

* extend bri.ServiceClient
* not abstract
* public class
* have a public constructor (Socket as parameter) without exception
* its superclass must have a private final Socket field
* have a public static toStringue() method without exception
* class' package must be named same as programmer's login
