package repo

import domain.AppError.RepositoryError
import domain.User
import repo.impl.InMemoryUserRepository
import zio.test.TestAspect._
import zio.test._
import zio.{Scope, ZLayer}

import scala.util.Random

object InMemoryUserRepositorySpec extends ZIOSpecDefault {

  val testLayer: ZLayer[Any, Throwable, UserRepository] =
    ZLayer.make[UserRepository](
      InMemoryUserRepository.live
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
        } yield assertTrue(user == user)
      }
    ).provideLayerShared(testLayer.orDie) @@ sequential
}
