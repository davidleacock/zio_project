package http

import zhttp.http._
import zio.Random

object Routes {

  // TODO get info from route and return correct payload
  private val getRoute: Http[Any, Nothing, Request, Response] =
    Http.collect[Request] { case Method.GET -> !! / "user" =>
      Response.text("returning user...")
    }

  // TODO get info from post and create correct payload
  private val postRoute: Http[Any, Nothing, Request, Response] =
    Http.collectZIO[Request] { case Method.POST -> !! / "user" =>
      Random
        .nextIntBetween(1, 100)
        .map(number => Response.text(s"You got $number"))
    }

  val combinedRoutes: Http[Any, Nothing, Request, Response] = getRoute ++ postRoute
}
