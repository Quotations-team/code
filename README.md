# Daily Quotation Project
## User Interface Design / Functional Specifications for Quotations project
### Introduction

The Daily Qiotation Project is an Android app that displays famous (or infamous) quotes. 
The users can select one or more categories from the quote database, and can also perform searches in the database based on a key word.

### Views and Navigation

The app consists of the following views
* Start / Splashscreen
* Welcome / Homepage
* Categories
* Search

The diagram below shows the user navigation between views:

![View navigation](quotes_funcspec_app_screens.png)

#### Start / Splashscreen

* Proposal 1-A:

The splashscreen shows a funny logo with a quote "bubble". An actual quote can be printed within the bubble. User taps the screen to dismiss the splashscreen and enter the Welcome view.

![start 1A](http://www.speaklikeaceo.com/Portals/25382/images/iStock_000018464529Small.jpg)

* Proposal 1-B:

Upon starting the application for the first time a static logo splash screen appears for about one second. 

![start 1B](http://www.parature.com/wp-content/uploads/2012/01/customerservicequotations.jpg)


#### Search

Search view allows the user to enter one or more search keywords. The app searches the entire quote database for matching quotes (all keywords = logical AND) and displays them as a list. 

##### Questions
* do we allow more than a single search keyword?
* do we AND or OR multiple keywords
* is the list scrollable if more quotes than fits on the screen?

![search](Project_Screenshot_SearchActivity_11-7-14.png)

#### Category

The Category option allows the user to select from different categories. When the user chooses a certain category, the scrollable list of quotes appears on the screen.
