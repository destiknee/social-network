# social-network
simple social networking application

<h3>HOW TO RUN</h3>
The Social network application is a Spring Boot application written in Java 8.<br/>
Before running please ensure that your JAVA_HOME environment variable is set to the location of your Java 8 JRE
<br/><br/>

<b>To run the social-network application:</b>
1. checkout the codebase to a suitable local directory
2. navigate into the social-network folder
3. open command prompt/shell in the directory
4. build and deploy the application using the gradle wrapper command:<br/>
	<t> <b>a. "./gradlew clean bootRun" </b>(Linux/Mac)<br/>
	<t> <b>b. ".\gradlew clean bootRun" </b>(Windows) <br/>
5. after the build and deploy is done, you should now be able to hit the end points <br/>


<h3>END POINTS  (port = 8080) </h3>
<table>
<tr><th>Scenario</th> <th>URI</th>  <th>HTTP Method</th></tr>
<tr><th>post a message*</th> <th>/users/{username}/message</th>  <th>POST</th></tr>
<tr><th>view user wall</th> <th>/users/{username}/message</th>  <th>GET</th></tr>
<tr><th>follow another user</th> <th>/users/(follower-name)/(followee)</th>  <th>POST</th></tr>
<tr><th>unfollow another user</th> <th>/users/(follower-name)/(followee)</th>  <th>DELETE</th></tr>
<tr><th>view timeline</th> <th>/users/{username}/timeline</th>  <th>GET</th></tr>
</table>
*takes a "content" Form Paramter. See example usage below.

<h3>EXAMPLE END POINT USE (using curl command-line tool) </h3>
machine = localhost <br />
username = bob <br />
followee = kate <br />
<br/>
1. <code>curl -i -X POST -H "Content-Type: application/x-www-form-urlencoded" http://localhost:8080/users/bob/messages -d "content=bobs first post"</code> <br/>
2. <code>curl -i -X GET http://localhost:8080/users/bob/messages</code><br/>
3. <code>curl -i -X POST http://localhost:8080/users/bob/kate</code><br/>
4. <code>curl -i -X DELETE http://localhost:8080/users/bob/kate</code><br/>
5. <code>curl -i -X GET http://localhost:8080/users/bob/timeline</code><br/>

<h3>Further Assumptions & Design Decisions</h3>
1. a user name is case insensitive so that posting a message with BOB, bob or bOB, all have the same effect <br/>
2. user names are used as unique ID for simplicity in this application <br/>
3. a user cannot follow or unfollow themselves <br/>
4. based on the initial requirement, every user has at least one post (first post when creating user) <br/>
5. user timeline is backed by an ArrayList hence has a maximum size defined by Integer.MAX_VALUE. Future enhancements will see this subsituted, possibly for a linkedlist, as the number of users increases.<br/>
