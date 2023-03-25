# zio_project

Current status:  Simple User model, postgres database and two REST endpoints


To run application

1) `docker-compose up` in terminal
2) run `Main` from either command line or IDE

two available endpoints for user

Add User: `curl -i http://localhost:8080/user -d '{"id": "1234", "name":"hank", "email":"hank@stricklandpropane.com"}'`

Get User: `curl -GET -w '\n' http://localhost:8080/user/1234`
