# ChibiOtaku

## About

---

ChibiOtaku is an application through which you can organize your list of watched anime and manga, you can see what score other users have added to an anime and manga. In case you want to add a score to anime/manga, the application also comes with search pages. Through the application you can add a review to a manga or anime, if a review created by you has bad content, the application will delete the feedback in two days. If you don't find an anime or manga you want, you can add it manually but you will have to wait a while for it to be approved by an administrator, rejected anime/manga will be deleted from the database in two days.
</br>
</br>

The basic elements of the application

- add anime/manga to your list
- creating an anime/manga
- creating an account to keep all your actions in the database
- visualize anime/manga details and give a score

</br>

## The technologies behind the application

---

> The project was made in Gradle.

</br>

### Database

To store the information I used MySQL. The application is created in Spring Boot. I used a lot of frameworks to prevent SQL injection and to not write native SQL. As Driver, I used Java DataBase Connection to use the ORM I worked with Java Persistence API because it was much easier for me to add annotations than to make configuration XML files for each Entity. Another advantage was the automatic creation of a database and having a relationship with the objects in the application. Spring boot works with the repository so I used Hibernate Query Language to extract information from the database based on some parameters.
</br>
</br>

### Security

> To authenticate a user, Spring Security is the best framework and is easy to understand and use.

Type of roles

- Admin
- User

Spring Security has a Password Encoder but also CSRF Protection, we don't want our application to add the password as it was written by the user, therefore password encryption is essential, the application uses BCryptPasswordEncoder.

A visitor should not view certain pages, only the user must see them, spring security can analyze HTTP requests and can make certain URLs public or others intended only for authorized persons or only for users who have the role of admin. If a user searches for a URL that is not associated with the application, the Software returns the status 404 Not found, which leads to the return of a page for the user to realize that the added URL does not exist. Different pages are created for different statuses.

Admin account

- username: admin
- First name Admin
- Last name Admin
- email: admin@ex.com
- password: topsecret

Other ursers

- username: chris96
- First name Christian
- Last name Smith
- email: chris@none.com
- password: lovel123
  </br>
  </br>

- username: calex96
- First name Alexander
- Last name Jeff
- email: alex@none.com
- password: somepw534

</br>

> ### Other frameworks

1. ModelMaper - To convert an Object into another object for example AnimeEntity --> AnimeDto
2. H2Database - To test if the query repository works correctly, I had to use a separate database that would not affect my current database
3. Thymeleaf - To generate HTML templates
4. Validation - We need to check if the inserted data is correct, for example when we want to create an account on ChibiOtaku.
5. JUnit 5 - To make sure that the application runs optimally, I created unit tests and integration testing through this framework.
6. Mockito - When we create unit or integration tests, we need to isolate a part of the program from the other programs, which Mockito was very useful for me.

  </br>

## JavaScript

---

> I used JavaScript to extract the most recent reviews created by users, I extracted information from the database by fetch.

## How to install

---

> To run the application you need an IDE, the IDE I used to create the application was Intelji Idea Ultimate edition, I needed to view the database and run javascript files, which is the ultimate edition that was very helpful.

  </br>
After you download the file until run the program you must make sure that you have MySql 8 installed on your computer, after making sure that you have MySql make sure that you have Java Developer Kit 11, I used this version because it has better support compared to the other versions and the Java programming language community uses this version, which made it very easy for me to look for certain problems that I encounter when creating the application.

</br>
When you followed all steps you need to access the application.yml file from the resources folder and edit the username and password from `spring.datasource` like in the photos

</br>

<img src="https://i.ibb.co/sscRV1f/step-1.png" alt="step-1" border="0">

<img src="https://i.ibb.co/S0vwckC/step.png" alt="step" border="0">
</br>

After making sure that you have JDK 11 and MySql 8, you just have to open run it and the dependencies will be downloaded automatically before you run the app

</br>

## How to use it

---

The application has a simple mode of use, but also has a navigation bar that will explain the pages in a simple way. The application has a slide show and below you have certain details related to anime and manga, you just have to access the page like in the given example: http://localhost:8080/, bellow is an image how app looks after run the program

<img src="https://i.ibb.co/RSvvHyC/dashboard.jpg" style = "width: 700px" alt="dashboard" border="0">

## Credits

---

> The biggest source of inspiration goes to the platform [My Anime List](https://myanimelist.net/)

through these platform, I got an idea of how the web application should look, so if you like anime and read manga, I recommend this site.

## Will I make updates in the future?

---

The answer is Yes! I want to bring the app to another level of complexity.

For now, I'm not sure to public my app, at the moment you can access it just by downloading the app from my repository.
