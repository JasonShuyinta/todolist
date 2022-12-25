# TodoList

# Table of Contents
1. [Introduction](#introduction)
2. [Architecture](#architecture)
3. [Tech stack](#techstack)
4. [DevOps](#devops)


## Introduction
TodoList is a simple web-app used for keeping progress of your daily activities, and track them through time. 

It has basic functionalities and minimal configuration, and I created it purely to test my skills on various technologies that I find interesting and particularly useful as they are the *de-facto* standard for building enterprise-level applications.

The site can be accessed at [TodoList](https://main.d2c926ip23yazk.amplifyapp.com/)

## Architecture

For building such a simple app, it is not necessary to implement such an intricate architecture, has it is going to be highly costly and difficult to maintain as a single developer. On the other hand, such architecture would be suitable for more complicated applications, where there is a lot of incoming and outgoing data, different teams working on different parts of the project and frequent delivery of software artifacts. Therefore it is not recommended to use such an architecture for basic projects. 

As you can see from the figure, the software is composed of a Backend (BE),a Frontend (FE) and a proxy server as the means of communication between the two ends. We will discuss each part further in the document. 

The Backend retrieves and saves its data to a MongoDB database, where all the data in is stored in document-like objects.
Each component is then deployed to the Cloud using two different providers: the BE and FE use AWS, while the proxy server uses Heroku. 

All of the communication between components happen through REST API endpoint using the json format. 


![Cloud Architecture](https://user-images.githubusercontent.com/50492920/196665362-03de1eaf-10af-47f3-a059-8688efb24d3e.png)



## Techstack

Here follows the tech stack used for each component of the application. 

#### Backend
* Java 11
* Maven 3.8.6
* Spring Boot Framework 2.7.4
* MongoDB 

The BE was developed using Java 11 boostrapped with Spring Boot. For the dependecies I chose to use Maven has it is the tool in which I have more confidence. The BE follows a simple MVC (Model-View-Controller) structure, where each part is well defined and enclosed, and it only executes the specific task it is written for.

##### Model Layer
There are two Models: **User** and **Item**. The User model stores all the information about a single User, such as *id*, *username*, *password* and *subscriptionDate*. The Item model stores fields such as *id*, *text*, *state*, *User*, *creationTime*, *updateTime*. 

Both of these models are MongoDB documents, so the *id* is automatically created by the DB when a new User or Item is saved. 

As you may notice, there are 4 different Java classes inside of each of the Model folders. This is because we want to separate the data that goes to the BE, from the data that is saved to the DB and also from the data that is sent back to the FE. This is why, for each and every Model we will have a Model.java class (the class that represents the object in the DB), the ModelInput.java (the object that directly comes from the FE to the BE), the ModelOutput.java (the object that is given back to the FE from the BE) and a ModelConverter.java (a class that is used to convert safely the ModelInput object to the Model object, and the Model object to the ModelOutput object).

But why we do this? 

For example, let's take our user John. When a John wants to register to our app, he first needs to save some information about himself to the DB in order for the application to be able to remember him the next time he logs into it. So he will procede to fill out a Sign-up form. Here the user will have to insert a username and a password which will be sent to the BE in json format:
```
{
  "username" : "John",
  "password" : "johnspassword"
}
```
The BE will save this data into the DB. 

Imagine now, the application needs John's info: the BE will have to send John's data back to the FE, and in an unsafe application we would send back to the browser this same object. As you can imagine, we must absolutely avoid sending from the BE to the FE sensitive data about the users, that is why we will convert the User object stored in the DB to a UserOutput object, so we can safely send back this object without the risk of leaking sensitive information. So the UserOutput object will look something like this: 
```
{
 "username": "John",
 "subscriptionDate": "2022-10-19 11:11:11"
}
```
You can see we completely removed the password field, and added a *subscriptionDate* which was created at the moment a user was saved in the DB, which for this app's purpose it is not considered a sensitive information.


##### Service Layer
The Service Layer, is in charge of executing all of the business logic of our application. Each Model will have its own Service class, as we must keep each *domain* separate. 

In this classes we will find a bunch of methods that are going to be called either by other service classes, or more frequently by the Controller classes. The service class is annotated with the Spring annotation *@Service*, and it simply retrieves, modifies, deletes, filters or sorts data from the DB. In our app, Spring communicates with the DB using MongoRepository. 

**MongoRepository**, which is the equivalent of *JpaRepository* for SQL databases, is able to transform your *findBy* methods into database-language-specific queries.
For example, the method *findByUsername("John")* in the UserService class, will be automatically transformed into a NoSQL MongoDB query by the MongoRepository library into 
```
db.user.find({username:"John"}) 
```
or into a MySQL query by the JpaRepository library:
```
SELECT * FROM USER WHERE USERNAME = 'John'
```
Pretty cool, isn't?

##### Controller Layer
The Controller layer is the part of the BE that the FE communicates with. It is the junction point between the 2 ends. Here we will expose what we call REST API, which are nothing but URLs that the FE calls. It is in fact the same thing it happens when you write an URL into your browser, once you hit Enter it gives you back data in the form of HTML web pages, except here it all happens behind the curtains, everytime you press a button, scroll a page, click an element or everytime you interact with the FE.

Let's take the UserController class. We can see that we have annotated it with *@Controller*, and also added *@RequestMapping("/api/v1/user")*. This corresponds to the prefix of the URL for our REST enpoints. We then have a method annotated with *@GetMapping("/{userId}"). This means that every time we call the endpoint *"/api/v1/user/{userId}"* it will execute the code inside of the method (if you are wondering where is the rest of the URL, don't worry, it will be explained soon).
The curly braces, indicate that the *userId* part is a parameter, so it is not fixed but it varies upon what the FE sends. So for example, if we make a GET request, using John's *id* (let it be "01"), that the endpoint will look like this */api/v1/user/01*. 

Inside of this method, we will receive some input from the caller method in the FE. This input can be an object, or a single parameter like a String. In case it is an object it will send us a ModelInput type of object, and inside of the method we will then convert this ModelInput object into a Model object, which can be than used by the Service class.


##### Authentication
The authentication and authorization of users was done using the [JsonWebToken](https://jwt.io/) library . This library uses some of your data and a timeframe and encrypts them using an ecryption algorithm (and a secret key) into an alphanumeric String (a token), which is then used by the FE and BE to keep the user logged and authorize requests.
This token is stored in the localStorage of your browser, and when needed can be passed as a Bearer token into your requests. Once this token gets to your BE, it is then decrypted using your secret key, from which you can then obtain your data.


##### Testing
Good software must to be throughly tested in all its parts. In this project, unit-testing was done using Junit and [Mockito](https://site.mockito.org/). Unit tests must be ideally carried out for each Controller and Service class, in order to reach a code coverage of at least 80%.
For testing purposes, a different DB was used, exploiting the *spring.profiles* in the *application.properties* file. This means that when Spring recognizes that we are running a test, it uses a different profile, in our case a *test* profile, getting its properties from the *application-test.properties* file which is not present on this Git Repository for security purposes. 

Unit tests are basically mocking the behaviour of an API call, and verifying that the return value of the call is the expected one.
For example, if we call the GET Request *"/api/v1/user/{userId}"*, where the userId actually exists, we then expect to receive an HttpStatus code of 200. While on the other hand, if we insert a userId which isn't present on the DB than we expect to return an HttpStatus code of 400. 


#### Frontend
* React Js 18.2.0
* Material UI 5.10

React Js was the library that was used to created the FE. It is a very easy FE project, with minimal styling and basic state management.

In particular, for the styling we chose to use [Material-UI](https://mui.com/), as it is a very easy-to-learn tool and has quite pretty components. React JS uses a Virtual DOM on top of the actual DOM to render its pages, and we believe it makes FE development much easier, even though you need to be attentive on the state of your variables. 

For this reason, we chose to use the in-built state management tool for React, which is Context. Instead of passing down your data through props all the way through your component tree, you simply store all your global variables and functions in a single file which can than be access by other component inside of your project. That is the function of the  *Context.js*. Here we store variables such as the JWT token when available, the user data and the color theme of the app. 

Making the API requests was made simple thanks to [axios](https://axios-http.com/), with which just a few lines we are able to make all type of requests to our BE.

#### Proxy
* Node Js 14.15.1

A proxy server deployed on Heroku was implemented in the architecture. We decided to implement a proxy server for a few reasons. First of all, it is a matter of security, as we don't want to directly expose the URL of our server where the backend is deployed, in order make it harder for malicious attackers to find out where our data is stored and processed. So how do we achieve this? Basically, our FE makes the requests to our proxy's endpoint, which is https://todo-list-app-proxy.herokuapp.com/, which then makes requests to our BE endpoint, which is safely stored and not disclosed into our proxy servers environment variables (in never appears in the code). 

So for example, let's take our previous endpoint: *"/api/v1/user/01"* used to retrieve the User with *id* 01 in our DB.

The FE makes the request to *https://todo-list-app-proxy.herokuapp.com/api/v1/user/01* . This endpoint is located on our proxy server writtend in Node Js, and what is does is simply redirect the final part of the API to our actual BE -> *http://xxxxx/api/v1/user/01*. 
This way, from your browser you will only see the requests being sent to the Heroku server, where we can optionally add some other security logic, like for example, only allowing traffic from a specific IP to be able to communicate to our server, or don't allow too many requests to hit the server at the same time (DDOS attack). 

For this project, just a simple CORS policy was implemented, to allow any request of any type to pass through and reach our server, but in a real world application, some additional functionalities needs to be implemented.

#### Cloud
* AWS EC2 
* AWS Amplify
* Heroku

The application was deployed on AWS, and the proxy server on Heroku. Hereafter are the reason for this choices:
 AWS is a pretty common Cloud provider as it allows to easily deploy any application on various type of servers. 

EC2 is the service used where you can deploy your Java application and run it and let anybody in the world be able to use it. There is a free-tier , where under specific thresholds you won't be charged of any fees, and even if you reach the threshold, costs are very low for the type of services you can get. 
What you need to do to deploy your application to the Cloud is basically create an executable file of your Java application (a .jar file), and run it using the command
```
java -jar yourProject.jar
```
This is basically everything you need to boot up your application, and then requests can be made to your BE. There is some configuration to be done though: for example, once you obtain your EC2 instance from AWS, you need to download and install Java, and open the port on your server that the application runs on (Security rules). 

Amplify, on the other hand is a similar service to EC2, but we find it much easier to use, especially for FE apps, thats why we uploaded our React app on it. Just simply create a new application following the instructions and connect it to your Git repository where you keep your FE code. Enable *automatic deploys*, and everytime you push code to your repository, a new build will start, deploying your code into production.

Heroku was used as our proxy server Cloud provider mainly because it was free and easy to use. Just like Amplify, automatic deploys are allowed, so all we had to do was create an App following the UI instructions and connect it to our Git repository, and the rest is dealt by Heroku. 


## DevOps

We decided to follow an automated approach for the deployment of the application using popular tools such as [Docker](https://docs.docker.com/) and [Jenkins](https://www.jenkins.io/). 

First of all we created a Dockerfile in the BE source file were we simply use the Java 11 image, get the executable file after it has been compiled by Maven and run it with the command.
```
FROM openjdk:11 as build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} todo-list-backend.jar
ENTRYPOINT ["java","-jar","/todo-list-backend.jar"]
```
We then created a Jenkinsfile in the same directory. 

Jenkins was installed on my local machine running on Windows 11 and started with the command *"java -jar .jenkins.war"*. 

There are various stages that the Jenkins pipeline executes, here we will explain in detail each of them.

```
tools {
            maven 'maven_3.8.6'
            jdk 'jdk11'
      }
```
You of course need to declare the Maven version you are planning to run your application on, followed along by the jdk if the one use by default on your machine is different from the one your app is running on (in my case, my JAVA_HOME pointed to version 8 of the jdk, so in *Jenkins -> Manage Jenkins -> Global Tool Configuration* you need to set the path of the correct jdk).

```

stage('Git Checkout') {
                steps {
                    checkout([$class: 'GitSCM', branches: [[name: '**']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHubPasswordLocalSystem', url: 'https://github.com/JasonShuyinta/ReactSpringTodoList.git']]])
                }
            }
```
In this step we are retrieving any changes to the repository detected by Jenkins. This means that the build gets triggered each time a commit is pushed to the repository. As you can see we safely store Git passwords using Jenkins credentials.
```
stage('Junit Tests') {
                steps {
                    withCredentials([string(credentialsId: 'spring-mongo-uri-test', variable: 'springMongoUriTest'), string(credentialsId: 'authentication_key', variable: 'AuthKey')]) {
                    bat 'mvn test -f backend/pom.xml -Dspring.app.secretKey=%AuthKey% -Dspring.data.mongodb.uri=%springMongoUriTest% -Dactive.profile=test'
                    }
                }
            }
```
The next step, is testing. Each time a build is triggered, our code gets test using the *test* profile with our properties defined in the *application-test.properties* file. The command used to trigger test is the *mvn test* command. The *-f* is used to let Maven know where our pom file is located, so in the repository, under the "backend" folder. We then pass som environment variables to the command. These variables are stored as secrets because they are the *secret key* used for JWT encryption and decryption, as well as the MongoDB connection String. It is best practice to never upload such secrets into repository, so that's why we pass them as credentials using Jenkins credentials.
```
 stage('Build Artifacts') {
                steps {
                    bat 'mvn -f backend/pom.xml clean install -Dmaven.test.skip=true -Dactive.profile=prod'
                }
                post {
                    success {
                        bat '''
                        cd backend/target
                        rename \"todo-list-backend.jar\" \"todo-list-backend-%BUILD_NUMBER%.jar\"
                        '''
                        archiveArtifacts 'backend/target/*.jar'
                    }
                }
            }
```

In this stage, we will build and store the artifacts. First of all, we give 2 Maven goals to be executed *mvn clean install*. This deletes the target folder from our BE folder and recompiles the code to create a new .jar file with our latest updates. We skip the tests because in the install phase, tests are included but we already got positive feedback from the early stage. Remember to active the *prod* profile, as it will take the Mongo connection string of our Production DB. 
If the step is successful, we then proced to rename our jar file adding to it as a suffix the *%BUILD_NUMBER%* taken from Jenkins as a default environment variable. This way we will have a jar called for example *"todo-list.backend-85.jar"*. We can this way keep track of the versions of our app. 
```
            stage('Deploy artifacts to S3 Bucket') {
                steps {
                    s3Upload(workingDir:'backend/target', includePathPattern:'*.jar', bucket:'todo-app-artifacts-repository', path:'builds')
                }
            }
```
In this stage, we get the jar file we have just renamed from the target folder and we upload it to an S3 bucket in AWS. In this way we can store permanently our different versions of jar files somewhere in case we need to rollback to a past version. In this case we are using a Jenkins Plugin called [AWS Pipeline Steps](https://plugins.jenkins.io/pipeline-aws/) which provides us with the function *s3Upload* where all we have to do is specify which file we want to upload and the bucket in which we want to upload it (in our case is *todo-app-artifacts-repository*), and we will store our jar file the *builds* folder of our bucket. In order to use this service we obviously need to add AWS credentials to Jenkins (*Jenkins -> Manage Credentials*).
```
            stage('Build Docker Image') {
                steps {
                    bat '''
                        cd backend
                        docker build -t jason9722/todo-list-app .
                        '''
                }
            }
  ```
  After we have stored permanently our artifact, we can now start to build our Docker image. First we need to navigate into the directory where the Dockerfile is located, in our case inside of the *backend* folder. Then give the command to build the image. The *docker build* command executes the instructions we gave inside of the Dockerfile, so it is going to install the jdk11 if it isn't already installed, and get the jar file and name it back to "todo-list-backend.jar".
  Docker Desktop needs to be running in background to do this, as the docker command cannot be executed without it.
  ```
              stage('Push to Docker Hub') {
                steps {
                    withCredentials([string(credentialsId: 'DockerId', variable: 'DockerPassword')]) {
                        bat 'docker login -u jason9722 -p %DockerPassword%'
                        bat 'docker push jason9722/todo-list-app'
                    }
                }
            }
  ```
  Our next step is to push our docker image into [Docker Hub](https://hub.docker.com/). Docker Hub is nothing but a repository for docker image, where just like git, we can push and pull images from it. 
Our account credentials need to be passed for us to be able to push the image (jason9722/todo-list-app) to the Hub.
The image is public, so you can at any time pull the image and use it in you local machine. 

In case you decide not to use Docker but ECR (Elastic Container Registry) of AWS, which is the equivalent of Docker HUB for storing your docker images you could use the following commands:

```
stage('Create and push docker image to ECR') {
                steps {
                    bat 'aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/e3x2p2e8'
                    bat 'docker build -t todolist backend/'
                    bat 'docker tag todolist:latest public.ecr.aws/e3x2p2e8/todolist:latest'
                    bat 'docker push public.ecr.aws/e3x2p2e8/todolist:latest'
                }
            }

```
It being a public repository, there is no need to execute the Docker-login step each time you run a build. 

```
        stage('Run shell script on EC2') {
            steps {
                withCredentials([string(credentialsId: 'ec2-server', variable: 'ec2ServerUrl')]) {
                    bat 'ssh -T %ec2ServerUrl% "docker stop todolistapp"' 
                    bat 'ssh -T %ec2ServerUrl% "docker rm todolistapp"'
                    bat 'ssh -T %ec2ServerUrl% "docker rmi jason9722/todo-list-app"'
                    bat 'ssh -T %ec2ServerUrl% "docker pull jason9722/todo-list-app:latest" '
                    bat 'ssh -T %ec2ServerUrl% "docker run -d -p 8003:8003 --name todolistapp jason9722/todo-list-app:latest" '
                }
            }
        }
```
Our last step, is executed on our EC2 server. We connect remotely to our server, giving its hostname and key as a credential to Jenkins. 
Java and Docker need to be installed on the machine as well, as we will be running a Java image in Docker.
We then ask the server to execute some commands:
1. First we ask docker to stop the currently running container named *todolistapp*
2. We then tell docker to remove and delete the container named *todolistapp*
3. After the container is removed we can delete the image *jason9722/todo-list-app*
4. After the image is deleted, we pull the latest image from Docker Hub. Our image has the same name as the early one plus the *:latest* tag.
5. Finally we tell Docker to run our freshly downloaded image (*jason9722/todo-list-app:latest*) in detached mode ( *-d* ), map the server port to the image port (8003:8003) and name our container *todolistapp*.

In this way our Java backend should start at port 8003, and you can verify if by running the command
```
docker ps
```
and check for the logs at
```
docker logs [CONTAINER_ID]
```

As said before, the FE and the proxy server are automatically deployed using respectively Amplify and Heroku, so each time a commit is pushed to this repository everything will be updated.
It is not recommended though, to keep all of your project parts in the same repository, it is better to separate them and deal with each one of them separetly.


#### Useful tips

The backend could also be tested using the Swagger Open API documentation tool, where just by visiting http://localhost:8000/swagger-ui.html you could test out your API endpoints using a User Interface. 
This was achieved by adding the swagger dependency to the pom.xml
```
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```
and creating a SwaggerConfig file annotated with @Configuration.

Obviously this endpoint needs to be disabled when launched in production, and you can do so by adding the following annotation to the SwaggerConfig class
```
@Profile({"!prod && dev"})
```
In this way the endpoint will be disabled in prod environment.

Another useful tip would be regarding the PWA (Progressive Web App). You could build a web app and make it installable on any device, from Mobile to Desktop. 
To do so you just need to add a service worker file, which is automatically created by React if you use the correct template when creating the app:
```
npx create-react-app my-app --template cra-template-pwa
```
In this way, React automatically builds the service worker files, and all you have to do is change the serviceWorkerRegistration.unregister(); function to serviceWorkerRegistration.register(); inside of you index.js file

Another useful thing to do in other to make a good pwa would be to produce different types of images and icons for different types of devices, and to do so you could use: 
```
npx pwa-asset-generator <icon-file> <destination-folder>
```
By doing so and following the instructions you can now substitute the image array in your manifest.json and your the generated images in your index.html

If you have any comments, questions of feedback please feel free to write to me.

A video will soon be uploaded on my e-learning platform, [Youtorial](https://www.youtorial.org/), so stay tuned!

### Author
### Jason Shuyinta
