# Modular Course Builder (MCB) - CRUD app
This program is a basic CRUD application for University Course Administrators.
Made with JavaFX, JavaFX media, Scenebuilder, JDBC and SQLite.
The purpose of the app is to allow the course admin to navigate between 
Courses, Modules, Sections, Resources and files available across the entire University.

## Using MCB
**JDK 11 is required for running this program.** <br>
After cloning the repository, navigate to the "target" directory.
You will find an executable .jar file called "ModularCourseBuilder-2.0-shaded".

Running this executable for the first time will create a database file "CourseDatabase.db".
This SQLite database manages user data, initialising by adding some sample data for the user. 

The app has one account made by default. You can access this account by providing 'admin' for username
and 'passWord!' for the password fields. 

After successful sign in, you will arrive at Home navigation page, where you can access various 
screens of the app.

# MVC
The MCB uses the Model-View-Controller pattern. 

## Model
The models used in the app are the following Java classes:
- Course
    Courses have a code, name, faculty name, school name, level (undergraduate/postgraduate), campus, and archival status.
- Module
    Modules have a name, description, and archival status.
- Section
    Sections have a name, associated module name, description, sequence number and archival status. 
- Resource
    Resources have a name, associated section, a resource extension and archival status.
- Outcome
    Outcomes have a name, associated module name, description, sequence number and archival status.
- Attachments
    Attachments have a name, associated course code, associated module name, associated seciton name, associated resource name, and its data in BLOB format.
## Views
Each model has its own view to display object properties.
### Login Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/login.JPG)
### Home Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/home.JPG)
### Course Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/course.JPG)
### Edit Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/course-edit.JPG)
### New Object Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/course-new.JPG)
### Module Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/module.JPG)
### Outcome Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/outcome.JPG)
### Section Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/section.JPG)
### Resource Window
!(/src/main/resources/ModularCourseBuilder/readme-ss/resource.JPG)

## Controller
Each view has its own controller that contains the logic and behaviour for UI elements. 



<!-- ### Creating a New User
To create a new user, click on the 'New User' button at the Home navigation. 
A new window will appear and you will have to provide a username and password for the
new account. Click on save and the new account will be inserted into the database
(provided that a user with the same name does not already exist).

## Manage Courses
To perform create, update or delete functions with courses, click on the Courses button from the 
Home navigation. The main table will be populated with the courses available within the database file. 

Selecting a course from the Course Table will allow you to perform more operations.
- You can edit the currently selected course,
- Upload a file to the currently selected course,
- Download a file from the currentlys selected course (if one exists)
- View associated modules with the selected course
- Associate a module with the selected course

Selecting an associated module will allow you to perform even more operations.
- You can remove the associated module from the selected course
- You can shift the sequence of the associated module with the selected course.  -->


### References
<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>