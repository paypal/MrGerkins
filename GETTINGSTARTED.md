## Setting up Environment for Development
* Install JDK 1.8

* Install your favorite IDE for java

* Install maven

* Clone the forked repository

* Import the project into the IDE

## Generating executable jar
```
mvn clean package
```

## Deloying Mr. Gerkins
Here are the steps to install Mr. Gerkins on an Ubuntu instance:
* Install JDK 1.8 on the instance
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```
* Copy the mr-gerkins executable jar to the instance
* Deploy and start Mr. Gerkins as a process
```
sudo chmod 755 ./mr-gerkins.jar
sudo mkdir /var/mr-gerkins/
sudo cp mr-gerkins.jar  /var/mr-gerkins/
sudo ln -s /var/mr-gerkins/mr-gerkins.jar  /etc/init.d/mr-gerkins
sudo /etc/init.d/mr-gerkins start
```

## External Dependencies

1. Lombok
Include the plugin for Lombok in the IDE
For Intellij: https://github.com/mplushnikov/lombok-intellij-plugin
