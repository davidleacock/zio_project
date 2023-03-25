package repo

import domain.AppError.RepositoryError
import domain.User
import zio.ZIO

trait UserRepository {
  def get(id: String): ZIO[Any, RepositoryError, User]
  def put(id: String, name: String, email: String): ZIO[Any, RepositoryError, Int]
}

object UserRepository {
  def get(id: String): ZIO[UserRepository, RepositoryError, User] =
    ZIO.serviceWithZIO[UserRepository](repo => repo.get(id))

  def put(id: String, name: String, email: String): ZIO[UserRepository, RepositoryError, Int] =
    ZIO.serviceWithZIO[UserRepository](repo => repo.put(id, name, email))
}