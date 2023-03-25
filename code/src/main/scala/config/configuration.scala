package config

import zio._
import zio.config._
import zio.config.ConfigDescriptor._
import zio.config.typesafe._
import zio.config.magnolia._
import com.typesafe.config.ConfigFactory
import zio.sql.ConnectionPoolConfig

import java.util.Properties


trait TypesafeConfig {
  def typesafeConfig: Task[ConfigSource] = {
    ZIO
      .attempt(
        TypesafeConfigSource.fromTypesafeConfig(
          ZIO.attempt(ConfigFactory.defaultApplication())
        )
      )
  }
}

final case class ServerConfig(port: Int)

object ServerConfig extends TypesafeConfig {

  private val serverConfigDesc = nested("server-config") {
    int("port").default(8090)
  }.to[ServerConfig]

  val layer: ZLayer[Any, Nothing, ServerConfig] = ZLayer(
    typesafeConfig
      .map(source => serverConfigDesc from source)
      .flatMap(config => read(config))
      .orDie
  )
}

final case class DbConfig(
    host: String,
    port: String,
    dbName: String,
    url: String,
    user: String,
    password: String,
    driver: String,
    connectThreadPoolSize: Int
)

object DbConfig extends TypesafeConfig {

  private val dbConfigDescriptor = nested("db-config")(descriptor[DbConfig])

  val layer: ZLayer[Any, Nothing, DbConfig] = ZLayer(
    typesafeConfig
      .map(source => dbConfigDescriptor from source)
      .flatMap(config => read(config))
      .orDie
  )

  val connectionPoolConfig: ZLayer[DbConfig, Throwable, ConnectionPoolConfig] =
    ZLayer(
      for {
        serverConfig <- ZIO.service[DbConfig]
      } yield (ConnectionPoolConfig(
        serverConfig.url,
        connProperties(serverConfig.user, serverConfig.password)
      ))
    )

  private def connProperties(user: String, password: String): Properties = {
    val props = new Properties
    props.setProperty("user", user)
    props.setProperty("password", password)
    props
  }
}
