# zio_project

To run application

1) docker-compose up
2) run Main

two available endpoints for user

curl -i http://localhost:8080/user -d '{"id": "1234", "name":"hank", "email":"hank@stricklandpropane.com"}'

curl -GET -w '\n' http://localhost:8080/user/1234
