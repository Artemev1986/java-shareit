# java-shareit
## Description
The service gives users the opportunity to tell what things they are ready to share,
and also allows you to find the right thing and rent it for a while. The service allows 
you to book a thing for certain dates, and also closes access to it for the time of booking 
from other people. If the necessary thing is not on the service, users have the opportunity 
to leave requests. Upon request, you can add new items to exchange.

The following commands are available in this service:
1. To manage users:
- **POST** /users endpoint. Create a user;
- **PATCH** /users/{userID} endpoint. Update user;
- **GET** /users endpoint. Get a list of all users;
- **GET** /users/{userId} endpoint. Get the user by Id;
- **DELETE** /users/{userId} endpoint. Delete the user by Id.

2. To manage items:
- **POST** /items endpoint. Adding a new item. The userId in the *X-Sharer-User-Id* header is the ID of the user who is adding the thing. This user is the owner of the thing. The owner ID will be used as input in each of the requests discussed below;
- **PATCH** /items/{itemId} endpoint. Editing a thing. You can edit the title, description, and rental access status. Only the owner of the item can edit it;
- **GET** /items/{itemId} endpoint. View information about a specific item by its ID. Information about a thing can be viewed by any user;
- **GET** /items endpoint. View by the owner of a list of all his things with a name and description for each;
- **GET** /items/search?text={text} endpoint. The text to be searched is passed to text. Make sure the search only returns things that are available for rent. Search for a thing by a potential tenant. The user passes text in the query string, and the system looks for things that contain this text in the title or description;
- **POST** /items/{itemId}/comment endpoint. Adding a comment for an item.

3. To manage bookings:
- **POST** /bookings endpoint. Adding a new booking request. A request can be created by any user and then approved by the owner of the item. After creation, the request is in the **WAITING** status - “waiting for confirmation”.
- **PATCH** /bookings/{bookingId}?approved={approved} endpoint. The approved parameter can be either true or false. Confirmation or rejection of a booking request. Can only be performed by the owner of the item. The booking status then becomes either **APPROVED** or **REJECTED**;
- **GET** /bookings/{bookingId} endpoint. Getting data about a specific booking (including its status). Can be done either by the author of the booking or by the owner of the item to which the booking relates;
- **GET** /bookings?state={state} endpoint. Get a list of all bookings for the current user. The state parameter is optional and defaults to ALL. It can also take on the values **​​CURRENT** (eng. "current"), **PAST** (eng. "completed"), **FUTURE** (eng. "future"), **WAITING** (eng. "awaiting confirmation"), **REJECTED** (eng. "rejected"). Reservations must be returned sorted by date from newest to oldest;
- **GET** /bookings/owner?state={state} endpoint. Getting a list of bookings for all items of the current user. This request makes sense for the owner of at least one thing. The operation of the state parameter is similar to its operation in the previous scenario.

4. To manage requests:
- **POST** /requests - add a new item request. The main part of the request is the text of the request, where the user describes what kind of thing he needs;
- **GET** /requests - get a list of your requests along with data about the responses to them. For each request, a description, date and time of creation, and a list of responses must be indicated in the format: item id, name, owner id. So in the future, using the specified item id, you can get detailed information about each item. Requests must be returned in sorted order from newest to oldest;
- **GET** /requests/all?from={from}&size={size} - get a list of requests created by other users. With this endpoint, users will be able to view existing requests that they could respond to. Requests are sorted by creation date, from newest to oldest. Results should be returned page by page. To do this, you need to pass two parameters: from - the index of the first element, starting from 0, and size - the number of elements to display;
- **GET** /requests/{requestId} - get data about one specific request, along with data about responses to it, in the same format as in the **GET** /requests endpoint. Any user can view data about an individual request.

### How to run this project :

```sh
##First you need to build the project
mvn clean install
```

```sh
##start up all containers. Use the -d option to start up in deamon mode
docker-compose up
```

### How to stop this project :
```sh

##stop the running containers
docker-compose stop
```

Once all the services are up, the following URLs will be available

Address | Description
--- | ---
http://<\<docker-host>\>:8080 | gateway service. It does all the validation of requests.
http://<\<docker-host>\>:9090 | server service. It contains all the main logic and interaction with the database
http://<\<docker-host>\>:6541 | postgres service. It contains a database.