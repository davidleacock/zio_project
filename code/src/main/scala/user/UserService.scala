package user

import domain.UserError.{InvalidData, UserNotFound}
import domain.{AppError, User, UserError}
import repo.UserRepository
import zio._

trait UserService {
  def createUser(
    id: String,
    name: String,
    email: String
  ): ZIO[Any, UserError, User]
  def getUser(id: String): ZIO[Any, UserError, Option[User]]
}

// TODO add some Validation
object UserService {
  lazy val live: ZLayer[UserRepository, Nothing, UserService] = ZLayer {
    for {
      userRepo <- ZIO.service[UserRepository]
    } yield UserServiceLiveImpl(userRepo)
  }

  private final case class UserServiceLiveImpl(
    userRepository: UserRepository)
      extends UserService {
    override def createUser(
      id: String,
      name: String,
      email: String
    ): ZIO[Any, UserNotFound, User] =
      userRepository.put(id, name, email).mapBoth({
        case AppError.RepositoryError(cause) => UserNotFound(s"${cause.getMessage}")
      }, {
        res =>
          //  todo fix the response on the repo so that it doesnt just return an int
          User("a", "B", "c")
      })

    override def getUser(id: String): ZIO[Any, UserError, Option[User]] =
      userRepository.get(id).mapBoth({
        case AppError.RepositoryError(cause) => UserNotFound(s"${cause.getMessage}")
      }, {
        res => Some(res) // TODO clean up this return type as well
      })
  }

}
