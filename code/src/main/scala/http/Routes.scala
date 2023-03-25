package http

import domain.User
import repo.UserRepository
import zhttp.http._
import zio._
import zio.json._

object Routes {

  implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]

  def apply(): Http[UserRepository, Throwable, Request, Response] = {
    val getUserRoute: Http[UserRepository, Throwable, Request, Response] =
      Http.collectZIO[Request] { case Method.GET -> !! / "user" / int(id) =>
        UserRepository
          .get(id.toString)
          .map(user => Response.text(s"returning user: $user"))
      }

    val postRoute: Http[UserRepository, Throwable, Request, Response] =
      Http.collectZIO[Request] { case request @ (Method.POST -> !! / "user") =>
        for {
          user <- request.bodyAsString.map(_.fromJson[User])
          result <- user match {
            case Left(error) =>
              ZIO
                .debug(s"Failed to create user due to error: $error")
                .as(Response.text(error).setStatus(Status.BadRequest))
            case Right(user) =>
              UserRepository
                .put(user.id, user.name, user.email)
                .map(id => Response.text(id.toString))
          }
        } yield result
      }

    getUserRoute ++ postRoute
  }
}
