import http.Routes
import zhttp.service.Server
import zio._

object Main extends ZIOAppDefault {

  private val main: ZIO[Any, Throwable, Unit] = for {
    _ <- Console.printLine("Running program....")
    _ <- Server.start(9000, Routes.combinedRoutes)
    _ <- Console.printLine("Stopping program....")
  } yield ()

  def run = main

}
