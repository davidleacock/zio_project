package domain

import zio.schema.{DeriveSchema, Schema}

// TODO should these get moved?
case class User(id: String, name: String, email: String)

object User {
  implicit val userSchema: Schema.Record[User] = DeriveSchema.gen[User]
}


sealed trait UserError extends Throwable
object UserError {
  final case class UserNotFound(message: String) extends UserError
  final case class InvalidData(message: String) extends UserError
}



sealed trait AppError extends Throwable

object AppError {
  final case class RepositoryError(cause: Throwable) extends AppError
  final case class DecodingError(message: String) extends AppError
}