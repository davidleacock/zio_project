import http.HttpRoutes
import config.ServerConfig
import config.DbConfig
import zio._
import http._
import repo.impl.PostgresUserUserRepository
import zio.sql.ConnectionPool

object Main extends ZIOAppDefault {

  private val main: ZIO[Any, Throwable, Unit] = for {
    _ <- Console.printLine("Running program....")
    _ <- Server
      .serve(HttpRoutes.app)
      .provide(
        ServerConfig.layer,
        ZLayer.fromZIO(zio.config.getConfig[ServerConfig])
          .flatMap(config => zio.http.ServerConfig.live(http.ServerConfig.default.port(config.get.port))),
        Server.live,
        PostgresUserUserRepository.live,
        DbConfig.layer,
        ConnectionPool.live,
        DbConfig.connectionPoolConfig
      )
  } yield ()

  def run = main
}
