package repo.impl

import domain.User
import zio.schema.DeriveSchema
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresUserTableDescription extends PostgresJdbcModule {

  implicit val userSchema = DeriveSchema.gen[User]

  val users = defineTableSmart[User]

  val (userId, userName, userEmail) = users.columns
}
