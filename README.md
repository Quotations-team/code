# Daily Quotation Project
## User Interface Design / Functional Specifications for Quotations project
### Introduction

The Daily Qiotation Project is an Android app that displays famous (or infamous) quotes. 
The users can select one or more categories from the quote database, and can also perform searches in the database based on a key word.
#Quotation App


###Technical Spec


1. **Hardware Spec**  
    1. `device`  
    This app will work on android devices.  
    2. `minSdkVersion`  
    Oldest android phone can run on: API level 8, platform 2.2, FROYO.  
    3. `targetSdkVersion`  
    Newest android phone can run on: API level 19, platform 4.4, KitKat.  

2. **Database**  
    1. `parse.com`  
    It is free/pay online back-end tools services. Developer can relay on parse.com to take care of the back-end remotely while focus on front-end work. database is one of the services we are using to host our quotes and quote categories. Database, table, and data can be created on parse.com user interface, or by use API parse.com provided kind like using Object-relational mapping. Click here to [parse.com](https://www.parse.com/).  
    2. `database table`  
    It is remote database, we add quotes using parse interface, user can add data like registering new user. "Quotation" table store all quotes, and "Category" store all categories. The app will query quotes from Quotation table. User **HAVE TO** be able to connect to internet to get the quotes. ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p12_parse_com.png)  
    3. `register parse.com account`  
    Developer need to register an account first. After that, you will get application key and 
client key. Then, download the ParseStarterProject and put in your application key and client key there, then you can run the starter project, build your project on top of it. Click here to [start project](https://parse.com/apps/quickstart#parse_data/mobile/android/native/new).  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p13_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p14_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p15_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p16_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p17_parse_com_start_project.png)  
    4. `parse.com API`  
    After you got a runnable ParseStarterProject. When you create table on parse.com you need to add it to ParseApplication. Click here to see [parse.com API](https://parse.com/docs/android_guide#top).  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p18_parse_com_start_project.png)  
    5. `parse.com free usage limitation and tracker`  
    Limitation can be change all the time please check it once a while. Free account gets 30 request per second per one jab.  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p19_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p20_parse_com_start_project.png)  

3. **General Layout**  
    1. `screenOrientation`  
    We set to portrait mode only, so no rotation. We just want to save our self some trouble.

4. **Guide Pages**  
    1. `ViewPager`  
    ViewPager is a android class for screen paging nevigation (3 dots). We are using a already make ViewPager starter project.  
    2. `Button`  
    Click the Button to go to splash screen.  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p2_guide_pages.png)  
	
5. **Splash Screen**  
    1. `Handler`  
    To set wait time by use Handler class to create a thread to wait for amount of time. Usually splash screen is use to load up stuff for the app. The wait time can be longer, usually have a nice background logo image or text displaying.
    Splash screen will replace signin page if user already signin, display only for 1 second.  

6. **Sign up / Sign in / skip page**  
    1. Use TabHost class to create 2 tabs to select between Sign in or Sign up TabActivity.  
    2. Create 2 layouts Sign in / Sign up.  
        1. Use 4 EditText and a Button to insert new data to parse.com database, "Users" table, "username, password, email" fields.  
        2. Use 2 EditText and a Button to match query data from parse.com database, "Users" table, "username, password". See parse.com API to find out how to do it.  
    3. A Button without border looks like just text to go to Home page, with out Sign up or Sign in.  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p3_signup_signin_skip_page.png)  
    4. Difference between signin and not signin.  
    Only signin user can give "like", leave "comment", and add to personal "favorite" quotes.  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p10_login_skip_differ.png)  

7. **Home Page**  
    1. `ListView with custom view for each item`  
    The home page will show quotes by android class ListVeiw, order by popularity of paste 24 hr. By customize the view instead of using the build-in view for each list item, we can add image to each list item and format the displaying text. The build in view only allow text.  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p4_home_page.png)  
    2. `EndlessScrollListener`  
    When user scroll down, more quote will show up until the last quote, this is done by EndlessScrollListener. It request more item if over the threshold of remaining item before the end of current page. It is more useful for loading huge amount of data or data size will keep on growing by the second; which only load a small amount at a time not all at once to your device, so the wait time will be cut into section, or not overflow your View class capacity. First download a already  make EndlessScrollListener.java, find one that come with documentation that tell you how to use it. Some useful parameter or variable for your app to use or pass in; EX: "page" load more base on fix number of item consider one page, "totalItemsCount" load more base on number of item reached, "visibleThreshold" start load more base on how many items left before reach the end of current page. ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p11_endlessscrolllistener.png)  
    3. `navigation drawer`  
    User can bring out the overflow menu by press the ActionBar icon on the upper left, or the icon on the upper right, or slide a finger from left edge of screen by using ActionBarDrawerToggle, DrawerLayout, ListView, and android.support.v4.widget.DrawerLayout. Developer can just use a Menu instead of navigation drawer to do the same thing.   
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p5_home_page_navigation_drawer.png)  
    4. `overflow menu`  
    This menu contain category of quotes. By select one of them, home page will only show quotes from selected category.   
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p6_navigation_drawer_overflow_menu.png)  
    5. `SearchView`   
    In ActionBar embeded SearchView use as a search box that can expand out and contract to an icon. It need SearchManager to access the SEARCH_SERVICE. The search is an "logical AND" search, all key words have to be in the same quote, in any location. It search all the quotes query from the online database parse.com. The "magnifying glass" use to expand to search field. The "X" icon is both use to clear the search field and to contract back to icon.   
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p7_home_page_embeded_searchview.png)  
    6. `Menu`  
        1. `menu item "Logout"`  
        It will go to Sign up / Sign in / skip page.  
        ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p8_logout.png)  
        2. `menu item "Favorites"`  
        If user had signup and login, there will be a Favorites menu item. It will go to Favorites page.  
        ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p9_login_favorite.png)  

8. **Notification**
    1. `Parse push notification`  
    Use the push notification from parse.com instead of notification from Android class API. Notification happen only when we puch. Follow this link to tutorial [Parse push notification](https://parse.com/docs/push_guide#top/Android).  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p21_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p22_parse_com_start_project.png)  
    ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p23_notification.png)  
	
9. **Utility created**  
    1. `pseudo random`  
    to get random quote  
    2. `regular expression`  
    for String matching  
    3. `android standard version of notification`  

10. **Assets**  
    1. `graphical tool`  
        1. `Resizer_1.3.3`  
        Used to auto resize a image to multiple size to fit android usage.  
        2. `MS-Paint`  
        Used to edit phone screen shot for tech spec.  
        3. `Markdown`  
        Used to write and format this tech spec.
	4. `Adobe Photoshop & Illustrator`  
	Used to create graphics for app.
    2. `general styles`  
        1. `App icon`:  
        ![alt text](http://hills.ccsf.edu/~yliu192/cs177/p1_app_icon.png)  
        2. `Backgrounds`:  
        #FF8000 (255, 128, 0), orange  
        3. `Font`:  
	Rockwell - for logo  
	Sanchez - for quotes page  
	Helvetica Neue Medium - for category menu  

11. **Quotes**  
    1. `quote source credit`  
     	1. http://thewebminer.com/blog/tag/free-famous-quotes-database/
    	2. Bartleby's Familiar Quotations 
    2. `legality`  
        1. Copyright 2014 Quotations-team CCSF CS177 Software Engineering  
        2. Fair Use of CopyRighted Material  
        3. Fair use guidelines for educational multimedia  
