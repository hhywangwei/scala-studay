package com.appeme.application

import com.appeme.models._
import com.appeme.views._

import scalaz._
import Scalaz._
import scalaz.http._
import response._
import request._
import servlet._
import Slinky._

final class WeKanbanApplication extends StreamStreamServletApplication {
  import Request._
  import Response._
  implicit val charset = UTF8

  def param_!(name:String)(implicit request:Request[Stream]) =
    (request | name).getOrElse(List(Char)).mkString("")

  def param(name :String) (implicit request:Request[Stream]) =
    (request ! name).getOrElse(List[Char]()).mkString("")

  def handler(implicit request:Request[Stream], servletRequest:HttpServletRequest): Option[Response[Stream]] = {
    request match {
      case MethodParts(GET, "card" :: "create" :: Nil) =>
        Some(OK(ContentType, "text/html") << strict << CreateStory(param("message")))
      case MethodParts(POST, "card" :: "save" ::Nil) =>
        Some(saveStory)
      case MethodParts(GET, "card" :: "board" :: Nil) =>
        Some(OK(ContentType,"text/html") << transitional << KanbanBoard())
      case MethodParts(POST, "card" :: "move" :: Nil) =>
        Some(moveCard)
      case _ => None
    }
  }

  private def saveStory(implicit request:Request[Stream], servletRequest:HttpServletRequest) = {
    val title = param_!("title")
    val number = param_!("storyNumber")

    Story(number, title).save match {
      case Right(message) =>
        redirects[Stream,Stream] ("/card/create",("message", message))
      case Left(error) =>
        OK(ContentType,"text/html") << strict << CreateStory(error.toString)
    }
  }

  private def moveCard(implicit request:Request[Stream], servletRequest:HttpServletRequest) = {
    val number = param_!("storyNumber")
    val toPhase = param_!("phase")

    val story = Story.findByNumber(number);
    story.moveTo(toPhase) match {
      case Right(message) => OK(ContentType,"text/html") << strict << message
      case Left(error) => OK(ContentType, "text/html") << strict << error.getMessage
    }
  }

  val application = new ServletApplication[Stream, Stream] {
    def application(implicit servlet: HttpServlet, servletRequest: HttpServletRequest, request: Request[Stream]) = {
      def found(x: Iterator[Byte]) : Response[Stream] = OK << x.toStream
      handler | HttpServlet.resource(found, NotFound.xhtml)
    }
  }
}