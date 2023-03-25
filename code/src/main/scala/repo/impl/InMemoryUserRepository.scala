package repo.impl

import domain.AppError.RepositoryError
import domain.User
import repo.UserRepository
import zio._

import scala.collection.mutable

class InMemoryUserRepository(dataMap: mutable.Map[String, User])
    extends UserRepository {

  override def get(id: String): ZIO[Any, RepositoryError, User] = ZIO.attempt {
    dataMap(id)
  }.mapError(e => RepositoryError(e.getCause))

  override def put(
    id: String,
    name: String,
    email: String
  ): ZIO[Any, RepositoryError, Int] =
    ZIO
    .attempt {
      dataMap += (id -> User(id, name, email))
      0 // TODO fix this
    }
    .tapError(e => ZIO.logError(s"InMemory put error: ${e.getMessage}"))
    .mapError(e => RepositoryError(e.getCause))
    .debug(s"id=$id name=$name email=$email added to repository")
}

object InMemoryUserRepository {
  def live: ZLayer[Any, Nothing, InMemoryUserRepository] =
    ZLayer.fromZIO(
      ZIO
        .succeed {
          val data = mutable.Map.empty[String, User]
          new InMemoryUserRepository(data)
        }
        .debug("Constructing InMemoryUserRepository")
    )
}
