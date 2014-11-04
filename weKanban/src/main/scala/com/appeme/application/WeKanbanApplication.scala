package com.appeme.application

import scalaz.http._
import response._
import request._
import servlet._
import Slinky._

final class WeKanbanApplication extends StreamStreamServletApplication {

  val application = new ServletApplication[Stream, Stream] {
    def application(implicit servlet: HttpServlet, servletRequest: HttpServletRequest, request: Request[Stream]) = {
      def found(x: Iterator[Byte]) : Response[Stream] = OK << x.toStream
      HttpServlet.resource(found, NotFound.xhtml)
    }
  }
}