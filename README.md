# Product Management System

## Description
The Product Management System project is the Back End of a secure web application for managing products with REST API inluding user register and login features. With Role-Based authorization integration the product management is performed according to the authorization level of the user. The users interact with the system only through REST endpoints.



## Features
#### 1. Secure user registration and login.
A new user can register providing username, email and password. The password will be stored encypted in the database. The login to the system can be performed providing either email or username along with the password. The authentication mechanism will query the database for the specified user details and if the user exists and after successful credential identification the user is allowed to perform operations to the system.

#### 2. Role-Based Authorization.
A Role-Based Authorization policy is implemented with two possible roles USER and ADMIN. A user can take both roles and adding more roles in the future can be easily done. The perimissions of each role is defined as follows:
- ##### USER #####
    1. View products.
    2. Add new product.
- ##### ADMIN #####
    1. Update product details.
    2. Delete product.

 A user holding both roles can perform any operation to the products. By default when a new user is registered has the USER role. Currently, cahnges to the assignment of roles to users can only be done through the database tables, externally of the application (there is no API endpoint).

#### 3. JSON Web Token Security for authentication and authorization
The REST endpoints for product operations are secured. User autentication and authorization is ensured by stateless sessions using a token which provides the user's identity and permissions. Until the expiration of the token, the user can send HTTP requests on the REST endpoints and perform authorized operations on the products.


#### 4. CRUD operations with REST endpoints.
The only way for the user to interact with the system is through REST endpoints. The operations are listed below:
- View all products (for many products there are pages of results).
- View a product by Id.
- Add a product.
- Update details of a specific product by Id.
- Delete a product by Id.


## Tech Stack
The project is implemented on Spring Boot 3 Java framework and the PostgreSQL database. The project depedencies are listed below:

- Spring Web
- Spirng Security 6
- Spring data JPA
- PostgreSQL Driver
- Lombok
- Spring Boot DevTools
- io.jsonwebtoken jjwt-api, jjwt-impl, jjwt-jackson 0.11.15

#### The package diagram:

```    
com.apostolis.spms  # Root package
    |-exception     # Custom exception definition
    |-controller    # REST operations implementation
    |-model         # Entities 
    |-repository    # Interfaces for data extraction methods from the database
    |-security      # Security configuration and JWT token implementation
    |-service       # User details provider
```


#### Database schema:

![Database Schema](<README images/Database Schema-1.png>)
The database design implements a many-to-many relationship between the entities *User* and *Role* allowing a user to have many roles and a role to be assigned to many users. The table user_roles keeps the infomation about the role assignment to users. Also the columns *email* and *username* are constrained to be unique.

#### Security mechanism
The security mechanism to secure the REST endpoints of the appication is the JSON Web Token Security and is depicted in the diagram below. When a user is successfully logged in, the system generates a signed token with the user identity and roles with expiration time after 10 minutes and returns it in the HTTP response to the user. A filter (JwtAuthFilter) for every incoming HTTP request is implemented which allows access to the secured REST endpoints only if the HTTP Request contains a valid token of previously authenticated (signed in) user. The validation procedure takes the user information form User Details Service and the token information and checks if the token is valid (not expired and not changed). The authorization checking is performed on method level in the Controller for every CRUD (Create Read Update Delete) operation. If the token is expired the user must re-login and use the new token.

![JWT Security Schema](<README images/JWT Security Schema.png>)


## How to install
Clone this repository to your computer and connect a new Postgres database configuring the url, username and password in the *src/main/resources/application.properties* file. All the depedencies will be downloaded automatically.


## How to use the application

#### 1. Register as new user ####

To start using the application you must register as a user sending a POST request to the http://localhost:8080/api/auth/signup endpoint providing the username, email and password in the request body as shown below.
![Register](<README images/register.png>)

#### 2. Sign in ####
Sign in providing the credentials in a new POST request to the http://localhost:8080/api/auth/signin endpoint. You can use either email or username.
![Login](<README images/login.png>) 
After the succesfull login you will be given a token which you will need to provide in the next http requests to access the CRUD endpoints.

#### 3. Perform an operation ####
For example as a USER you can add a new product to the system. Send a POST request with the product details an the token in the Authorization tab as a Bearer token.
![Add Product](<README images/Add a new product.png>)

![Token](<README images/token.png>)

#### Token is Expired ####
After 10 minutes of its creation time the JWT token expires and it can no longer be used. To continue to interact with the system you must login again and use the new generated token.

## Credits
In the development process of this project I had valuable help from these tutorials:

- https://www.youtube.com/watch?v=BMbfL75mXyc&list=LL
- https://www.youtube.com/watch?v=VVn9OG9nfH0&list=LL&index=3&t=6846s
- https://www.youtube.com/watch?v=KxqlJblhzfI&list=LL&index=5&t=270s
- https://www.youtube.com/watch?v=R76S0tfv36w&list=LL&index=4
