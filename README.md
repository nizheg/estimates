Usage:
=====
1. Update the /estimate/src/main/filters/config.properties file with your database settings.
2. Run 'mvn tomcat:run-war' to start embedded tomcat instance with this application deployed
3. OR run 'mvn tomcat:deploy' to deploy application to the started instance of tomcat. 
By default it is localhost:8080, but you can change it by updating 'deploy.domain' property in pom.xml directly or by setting -Ddeploy.domain=*your domain* argument.

Other useful properties:
------------------------
* deploy.domain - name of the host where tomcat is started, default: *localhost:8080* 
* deploy.username - user name of manager runner (having 'manager-script' role), default: *script*
* deploy.password - password of manager runner, default: *script*
* deploy.path - path where war should be deployed, default: */*
* java.version - using java version, default: *1.6*

Stand
=====
Stand of the given application you can find here: <http://estimates-nizheg.rhcloud.com/>
