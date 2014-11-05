package com.appeme.application

import com.appeme.views.CreateStory

import scalaz._
import Scalaz._
import scalaz.http._
import response._
import request._
import servlet._
import Slinky._

final class WeKanbanApplication extends StreamStreamServletApplication {
  import Request._

  implicit val charset = UTF8

  def param(name :String) (implicit request:Request[Stream]) =
    (request ! name).getOrElse(List[Char]()).mkString("")

  def handler(implicit request:Request[Stream], servletRequest:HttpServletRequest): Option[Response[Stream]] = {
    request match {
      case MethodParts(GET, "card" :: "create" :: Nil) =>
        Some(OK(ContentType, "text/html") << strict << CreateStory(param("message")))
      case _ => None
    }
  }

  val application = new ServletApplication[Stream, Stream] {
    def application(implicit servlet: HttpServlet, servletRequest: HttpServletRequest, request: Request[Stream]) = {
      def found(x: Iterator[Byte]) : Response[Stream] = OK << x.toStream
      handler | HttpServlet.resource(found, NotFound.xhtml)
    }
  }
}