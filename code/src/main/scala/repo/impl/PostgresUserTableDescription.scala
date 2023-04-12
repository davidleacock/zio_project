package repo.impl

import domain.User
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresUserTableDescription extends PostgresJdbcModule {

  val users = defineTableSmart[User]

  val (userId, userName, userEmail) = users.columns
}
