# Todolist
###### Todolist WebAPP  <img src="https://img.shields.io/badge/version-1.0.0-green" alt="version"/>

<p>
<img src="https://img.shields.io/badge/react%20-%2320232a.svg?&style=for-the-badge&logo=react" alt="react"/>
<img src="https://img.shields.io/badge/spring%20boot%20-%2320232a.svg?&style=for-the-badge&logo=springboot" alt="springboot"/>
<img src="https://img.shields.io/badge/rabbitMQ%20-%2320232a.svg?&style=for-the-badge&logo=rabbitmq" alt="rabbitmq"/>
<img src="https://img.shields.io/badge/mongoDB%20-%2320232a.svg?&style=for-the-badge&logo=mongodb" alt="mongodb"/>
<img src="https://img.shields.io/badge/docker%20-%2320232a.svg?&style=for-the-badge&logo=docker" alt="docker"/>
</p>

## Overview
Free and open source todo task manager app.


#### Register new user

https://user-images.githubusercontent.com/23532861/146496298-30c57ed9-e778-4c4b-953a-a30cff72eab9.mp4

#### Manage groups


https://user-images.githubusercontent.com/23532861/146496816-7cc69b36-841c-4758-bcf5-51a8f0fbc8e4.mp4


#### Manage tasks


https://user-images.githubusercontent.com/23532861/146496829-d69f5714-ca66-46e3-8de6-fd2e5b38066b.mp4


#### Login as Admin
in the first initialize of the project it adds two user `admin@domain.test`,`user@domain.test` with password `1234`.


https://user-images.githubusercontent.com/23532861/146496845-b69afc47-5d8c-47fe-a371-5cb4b3b117e9.mov



> here we just simulate the admin authorization the admin page is not completed

<br/>

## installation
For Local Installation
<details><summary>Prerequisites</summary>
<p>

#### Environment requests
  - nodejs v14.15.0
  - JDK 16 + maven
  - git
  - mongodb 4.0 +
  - rabbitmq
  
</p>
</details>

Clone the repository
```
git clone https://github.com/MustafaSmesem/todolist.git
```

### Backend build
From `./todolist-spring` file execute:
```
mvn dependency:go-offline 
```
This will install all the dependencies then:
```
mvn package
```
This will create the `*.jar` file inside **target folder** which you can start as follows:
```
java -jar target/*.jar
```
This will start the backend with development profile on *http://localhost:9090*, and it expects that mongodb is run on `port 27017` and has no authentication methods.
also, it expects that rabbitmq is running on port 5672 and with username & password as `"guest"`,  you can change these properties from `application.yml` file **_dev profile section_**:
```
  data:
    mongodb:
      database: todolist

  rabbitmq:
    port: 5672
    username: guest
    password: guest
```
Also you can see full api documentation in **http://localhost:9090/swagger-ui.html**


### Frontend build
First from `./tosolist-react` file we need to install node dependencies as:
```
npm install
```
After install the dependencies for test and development purpose you can just run:
```
npm start
```
The development server will start on `port 3000` and you can test the app from **http://localhost:3000**

for deploying the app after installing the dependencies you need to build it:
```
npm run build
```
then in case of NGINX cop the compiled files from `build file` to `/usr/share/nginx/html`.


### Docker Build
You need both docker engine and docker compose.
> Note: if you on Mac/Windows you got the both with installing Docker Desktop

Now from the base directory just build the images with composer
```
    docker-compose build
```
and run it
```
    docker-compose up
```
And that's it :smiley:. you can now go to **http://localhost** and start manage your tasks.


### *Authors*
* Mustafa Bahaa SAMISM (email: mustafa.smesem@gmail.com)
  - [linkedin](https://www.linkedin.com/in/mustafa-samism/)
