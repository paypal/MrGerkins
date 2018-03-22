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
* Install JDK 1.8 on the instance [Link](#installing-java-8-on-the-instance)
* Copy the mr-gerkins executable jar to the instance
* Deploy and start Mr. Gerkins as a process
```
sudo chmod 755 ./mr-gerkins.jar
sudo mkdir /var/mr-gerkins/
sudo cp mr-gerkins.jar  /var/mr-gerkins/
sudo ln -s /var/mr-gerkins/mr-gerkins.jar  /etc/init.d/mr-gerkins
sudo /etc/init.d/mr-gerkins start
```

Some Linux systems don't have init.d, you can use systemd instead
```
sudo ln -s /var/mr-gerkins/mr-gerkins.jar /etc/systemd/mr-gerkins
sudo /etc/systemd/mr-gerkins start
```

You can follow a full tutorial for [init.d](http://www.jcgonzalez.com/linux-java-service-wrapper-example) here and for systemd (ubuntu 16+) [here](http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example)

If the server shuts down, even after using init.d or systemd, you can use nohup command.
```
nohup java -jar /var/mr-gerkins/mr-gerkins.jar &
```

## External Dependencies

1. Lombok
Include the plugin for Lombok in the IDE
For Intellij: https://github.com/mplushnikov/lombok-intellij-plugin

## Installing Java 8 on the instance
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```
### For CentOS/Fedora/RedHat
Install Oracle Java 8
This section of the guide will show you how to install Oracle Java 8 JRE and JDK (64-bit).

Note: You must accept the Oracle Binary Code License Agreement for Java SE, which is one of the included steps, before installing Oracle Java.

#### Install Oracle Java 8 JDK
Note: In order to install Oracle Java 8 JDK, you will need to go to the [Oracle Java 8 JDK Downloads Page](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), accept the license agreement, and copy the download link of the appropriate **Linux .rpm** package. Substitute the copied download link in place of the highlighted part of the wget command.

Change to your home directory and download the Oracle Java 8 JDK RPM with these commands:

```
$ cd ~
$ wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://link_copied_from_site"
```
Eg.
```
$ cd ~
$ wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jdk-8u161-linux-x64.rpm"
```
This will probably create a file with a huge name like `jdk-8u161-linux-x64.rpm?AuthParam=1521577495_6cf3fdd93385c24f22d9d6df977a39c6`. Rename the file if that happens.
```
$ mv jdk-8u161-linux-x64.rpm?AuthParam=1521577495_6cf3fdd93385c24f22d9d6df977a39c6 jdk-8u161-linux-x64.rpm
```
Then install the RPM with this yum command (if you downloaded a different release, substitute the filename here):

```
$ sudo yum localinstall jdk-8u161-linux-x64.rpm
```
At the confirmation prompt, enter y then RETURN to continue with the installation.

Now Java should be installed at `/usr/java/jdk1.8.0_161/bin/java`, and linked from `/usr/bin/java`.

You may delete the archive file that you downloaded earlier:

```
$ rm ~/jdk-8u161-linux-x64.rpm
```
Congratulations! You have installed Oracle Java 8 JDK.

#### Set Default Java
If you installed multiple versions of Java, you may want to set one as your default (i.e. the one that will run when a user runs the java command). Additionally, some applications require certain environment variables to be set to locate which installation of Java to use. This section will show you how to do this.

By the way, to check the version of your default Java, run this command:

```
$ java -version
```
##### Using Alternatives
The alternatives command, which manages default commands through symbolic links, can be used to select the default Java command.

To print the programs that provide the java command that are managed by alternatives, use this command:

```
$ sudo alternatives --config java
```
Here is an example of the output:
output
```
There are 5 programs which provide 'java'.

  Selection    Command
-----------------------------------------------
   1           java-1.7.0-openjdk.x86_64 (/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.161-2.6.12.0.el7_4.x86_64/jre/bin/java)
   2           java-1.8.0-openjdk.x86_64 (/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.151-5.b12.el7_4.x86_64/jre/bin/java)
   3           /usr/lib/jvm/jre-1.6.0-openjdk.x86_64/bin/java
*+ 4           /usr/java/jre-9.0.4/bin/java
   5           /usr/java/jdk-9.0.4/bin/java




Enter to keep the current selection[+], or type selection number: 
```
Simply enter the a selection number to choose which java executable should be used by default.

##### Using Environment Variables
Many Java applications use the JAVA_HOME or JRE_HOME environment variables to determine which java executable to use.

For example, if you installed Java to /usr/java/jdk1.8.0_161/jre/bin (i.e. java executable is located at /usr/java/jdk1.8.0_161/jre/bin/java), you could set your JAVA_HOME environment variable in a bash shell or script like so:

Open a terminal.
Append the following line to the ~/.bashrc file:
```
export JAVA_HOME="/usr/java/jdk1.8.0_161/jre"
```
Execute the command 
```
source ~/.bashrc
```
Check to see if JAVA_HOME is set to the correct location:
```
$ echo $JAVA_HOME
```
The output should look like this:
```
$ echo $JAVA_HOME
/usr/lib/jvm/jre-1.6.0-openjdk.x86_64
```

If you want JAVA_HOME to be set for every user on the system by default, add the previous line to the /etc/environment file. An easy way to append it to the file is to run this command:

```
$ sudo sh -c "echo export JAVA_HOME=/usr/java/jdk1.8.0_161/jre >> /etc/environment"
```

## Installing Maven 

### Install Maven on Fedora, CentOS, Red Hat and Scientific Linux

If you are using Fedora, CentOS, Red Hat or Scientific Linux sometimes yum does not have the package for the product you would like to install.

Download Maven and untar it from [Maven Download pages](http://maven.apache.org/download.cgi).

```
$ wget http://apache.mirrors.ionfish.org/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.tar.gz
$ sudo tar -zxvf apache-maven-3.5.3-bin.tar.gz -C /opt/
```

Setup the Maven Environment Variables in shared profile.

The next step is to setup the Maven environment variables in a shared profile so all users on the system will get them import at login time.

```
$ sudo vi ~/.bashrc
```
Add the following lines to maven.sh
```
# Add the following lines to maven.sh
$ export PATH=/opt/apache-maven-3.5.3/bin:$PATH
$ export M2_HOME=/opt/apache-maven-3.0.5
$ export M2=$M2_HOME/bin
$ PATH=$M2:$PATH 
```
Save the file (:wq).

Run the command
```
$ source ~/.bashrc
```
Now test your install of Maven.

Logout of the system and then log back into it. Enter the following command:
```
$ mvn -version 
```

If you did everything right your output should look something like the one below:
```
Apache Maven 3.5.3 (3383c37e1f9e9b3bc3df5050c29c8aff9f295297; 2018-02-24T11:49:05-08:00)
Maven home: /opt/apache-maven-3.5.3
Java version: 1.8.0_161, vendor: Oracle Corporation
Java home: /usr/java/jdk1.8.0_161/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "2.6.18-419.0.0.0.2.el5", arch: "amd64", family: "unix"
```
