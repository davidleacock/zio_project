package repo

import domain.AppError.RepositoryError
import domain.User
import repo.container.PostgresContainer
import repo.impl.PostgresUserUserRepository
import zio.sql.ConnectionPool
import zio.test.TestAspect._
import zio.test._
import zio.{ Scope, ZIO, ZLayer }

import scala.util.Random

object PostgresUserRepositorySpec extends ZIOSpecDefault {

  val testLayer: ZLayer[Any, Throwable, UserRepository] =
    ZLayer.make[UserRepository](
      PostgresContainer.connectionPoolConfigLayer,
      PostgresUserUserRepository.live,
      ConnectionPool.live,
      PostgresContainer.createContainer
    )

  val user: User = User(
    Random.between(1, 10).toString,
    Random.between(1, 10).toString,
    Random.between(1, 10).toString
  )

  override def spec: Spec[TestEnvironment with Scope, RepositoryError] =
    suite("user repository test with postgres test container")(
      test("insert new user") {
        for {
          _ <- UserRepository.put(user.id, user.name, user.email)
          user <- UserRepository.get(user.id)
          _ <- ZIO.logInfo(s"Got $user from db")
        } yield assertTrue(user == user)
      }
    ).provideLayerShared(testLayer.orDie) @@ sequential
}
