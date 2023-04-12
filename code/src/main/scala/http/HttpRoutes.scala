package http

import domain.{ AppError, User }
import repo.UserRepository
import zio._
import zio.http.model._
import zio.http._
import zio.json._

object HttpRoutes {
  implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
  implicit val encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]

  val app: HttpApp[UserRepository, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "user" / zio.http.int(id) =>
        UserRepository
          .get(id.toString)
          .either
          .map {
            case Right(user) => Response.json(user.toJson)
            case Left(error) => Response.text(error.getMessage)
          }

      case req @ Method.POST -> !! / "user" =>
        (
          for {
            user <- req
              .body
              .asString
              .flatMap(request =>
                ZIO
                  .fromEither(request.fromJson[User])
                  .mapError(e => new Throwable(e))
              )
              .mapError(e => AppError.DecodingError(e.getMessage))
              .tapError(e => ZIO.logInfo(s"Unable to parse body=$e"))
            _ <- UserRepository.put(user.id, user.name, user.email)
          } yield ()
        ).either.map {
          case Right(_) => Response.status(Status.Created)
          case Left(_)  => Response.status(Status.BadRequest)
        }
    }
}
