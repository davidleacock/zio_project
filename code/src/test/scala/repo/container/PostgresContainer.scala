package repo.container

import zio.ZLayer
import com.dimafeng.testcontainers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import zio._
import zio.sql.ConnectionPoolConfig
import java.util.Properties

object PostgresContainer {

  val createContainer: ZLayer[Any, Throwable, PostgreSQLContainer] =
    ZLayer.scoped {
      ZIO.acquireRelease {
        ZIO.attemptBlocking {
          val acquiredContainer = new PostgreSQLContainer(
            dockerImageNameOverride =
              Option("postgres:alpine").map(DockerImageName.parse)
          ).configure(container => container.withInitScript("init.sql"))
          acquiredContainer.start()
          acquiredContainer
        }
      }(containerToRelease => ZIO.attemptBlocking(containerToRelease.stop()).orDie)
    }

  val connectionPoolConfigLayer
    : ZLayer[PostgreSQLContainer, Throwable, ConnectionPoolConfig] = {
    def connectionProperties(user: String, password: String): Properties = {
      val properties = new Properties
      properties.setProperty("user", user)
      properties.setProperty("password", password)
      properties
    }

    ZLayer(
      for {
        sqlContainer <- ZIO.service[PostgreSQLContainer]
        container = sqlContainer.container
      } yield ConnectionPoolConfig(
        container.getJdbcUrl,
        connectionProperties(container.getUsername, container.getPassword)
      )
    )
  }
}
