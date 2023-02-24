package repo.impl

import domain.User
import repo.UserRepository
import zio._

import scala.collection.mutable

class InMemoryUserRepository(dataMap: mutable.Map[String, User])
    extends UserRepository {
  override def get(id: String): ZIO[Any, Throwable, User] = ZIO.attempt {
    dataMap(id)
  }
  override def put(
    id: String,
    name: String,
    email: String
  ): ZIO[Any, Throwable, String] = ZIO.succeed {
    dataMap += (id -> User(id, name, email))
    id
  }
}

object InMemoryUserRepository {
  def layer: ZLayer[Any, Nothing, InMemoryUserRepository] =
    ZLayer.fromZIO(
      ZIO.succeed {
        val data = mutable.Map.empty[String, User]
        new InMemoryUserRepository(data)
      }
    )
}
