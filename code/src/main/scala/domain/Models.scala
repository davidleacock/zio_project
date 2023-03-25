package domain

case class User(id: String, name: String, email: String)

sealed trait AppError extends Throwable

object AppError {
  final case class RepositoryError(cause: Throwable) extends AppError
}