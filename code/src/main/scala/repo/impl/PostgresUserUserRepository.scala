package repo.impl

import domain.AppError.RepositoryError
import domain.User
import repo.UserRepository
import zio._
import zio.sql.ConnectionPool

final class PostgresUserUserRepository(connectionPool: ConnectionPool)
    extends UserRepository
    with PostgresUserTableDescription {

  private lazy val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
    ZLayer.make[SqlDriver](SqlDriver.live, ZLayer.succeed(connectionPool))

  override def get(id: String): ZIO[Any, RepositoryError, User] = {
    val query =
      select(userId, userName, userEmail)
        .from(users)
        .where(userId === id)

    ZIO
      .logInfo(s"Query to execute `get`: ${renderRead(query)}") *>
      execute(query.to((User.apply _).tupled))
        .runHead
        .some
        .tapError {
          case None        => ZIO.unit
          case Some(error) => ZIO.logError(error.getMessage)
        }
        .mapError {
          case None =>
            RepositoryError(new RuntimeException(s"User $id does not exist"))
          case Some(error) => RepositoryError(error.getCause)
        }
        .provide(driverLayer)
  }

  override def put(
    id: String,
    name: String,
    email: String
  ): ZIO[Any, RepositoryError, Int] = {
    val query =
      insertInto(users)(userId, userName, userEmail).values((id, name, email))

    ZIO
      .logInfo(s"Query to execute `put`: ${renderInsert(query)}") *>
      execute(query)
        .tapError(error => ZIO.logError(error.getMessage))
        .mapError(error => RepositoryError(error.getCause))
        .provide(driverLayer)
  }
}

object PostgresUserUserRepository {
  val live: ZLayer[ConnectionPool, Nothing, UserRepository] =
    ZLayer.fromFunction { connectionPool: ConnectionPool => new PostgresUserUserRepository(connectionPool) }
}
