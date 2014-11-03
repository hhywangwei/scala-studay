package com.appeme.application

import scalaz.http.request._
import scalaz.http.response._
import scalaz.http.servlet._

final class WeKanbanApplication extends StreamStreamServletApplication {

  val application = new ServletApplication[Stream,Stream] {
    def application(implicit  servlet: HttpServlet, servletRequest: HttpServletRequest, request:Request[Stream]) ={
      def found(x : Iterator[Byte] ): Response[Stream] = OK << x.toStream
      HttpServlet.resource(found, NotFound.reasonPhraseS)
    }
  }
}