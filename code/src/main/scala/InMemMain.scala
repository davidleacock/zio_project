import _root_.http.HttpRoutes
import config.ServerConfig
import repo.impl.InMemoryUserRepository
import zio.http.Server
import zio.{Console, ZIO, ZIOAppDefault, ZLayer, http}

object InMemMain extends ZIOAppDefault {

  private val main: ZIO[Any, Throwable, Unit] = for {
    _ <- Console.printLine("Running IN-MEMORY version of application....")
    _ <- Server
      .serve(HttpRoutes.app)
      .provide(
        ServerConfig.layer,
        ZLayer.fromZIO(zio.config.getConfig[ServerConfig])
          .flatMap(config => zio.http.ServerConfig.live(http.ServerConfig.default.port(config.get.port))),
        Server.live,
        InMemoryUserRepository.live
      )
  } yield ()

  def run = main
}
