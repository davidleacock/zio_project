import http.Routes
import repo.impl.InMemoryUserRepository
import zhttp.service.Server
import zio._

object Main extends ZIOAppDefault {

  private val main: ZIO[Any, Throwable, Unit] = for {
    _ <- Console.printLine("Running program....")
    _ <- Server.start(
      port = 9000,
      http = Routes()
    ).provide(
      InMemoryUserRepository.live
    )
    _ <- Console.printLine("Stopping program....")
  } yield ()

  def run = main
}
