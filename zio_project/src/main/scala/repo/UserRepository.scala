package repo

import domain.User
import zio.ZIO

trait UserRepository {
  def get(id: String): ZIO[Any, Throwable, User]
  def put(id: String, name: String, email: String): ZIO[Any, Throwable, String]
}

object UserRepository {
  def get(id: String): ZIO[UserRepository, Throwable, User] =
    ZIO.serviceWithZIO[UserRepository](repo => repo.get(id))

  def put(id: String, name: String, email: String): ZIO[UserRepository, Throwable, String] =
    ZIO.serviceWithZIO[UserRepository](repo => repo.put(id, name, email))
}