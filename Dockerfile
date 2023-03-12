#Docker file Explanation

#FROM command will take 'maven:3.8.1-jdk-11-openj9' as a base image.
#WORKDIR /CucumberApiFramework will create a directory
 # /CucumberApiFramework inside the docker container which will act as a project directory.
#COPY  /abn-qa-backend-assingment will copy the src folder from your local machine to /abn-qa-backend-assingment directory inside the docker container.

FROM maven:3.8.1-jdk-11-openj9

WORKDIR    /CucumberApiFramework

COPY / abn-qa-backend-assingment


ADD Docker_trigger.sh /usr/local/bin/Docker_trigger.sh


RUN chmod 777 /usr/local/bin/Docker_trigger.sh


CMD ["sh", "/usr/local/bin/Docker_trigger.sh"]


