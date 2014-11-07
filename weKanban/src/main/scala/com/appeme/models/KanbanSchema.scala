package com.appeme.models

import java.sql.DriverManager

import org.squeryl._
import org.squeryl.adapters._
import org.squeryl.PrimitiveTypeMode._

/**
 * Created by weiwang on 14/11/4.
 */
object KanbanSchema extends Schema{

  val stories = table[Story]("STORIES")

  def init = {
    import org.squeryl.SessionFactory

    Class.forName("org.h2.Driver")
    if(SessionFactory.concreteFactory.isEmpty){
      SessionFactory.concreteFactory = Some(() =>
        Session.create(DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa",""), new H2Adapter))
    }
  }

<<<<<<< HEAD
  def tx[A](a: =>A): A = {
=======
  def tx[A](a: => A) :A = {
>>>>>>> 4ada834c582a3ce9723bcd862cf556cd176c2f34
    init
    inTransaction(a)
  }

  def main(args: Array[String]) {
    println("initializing the weKanban schema")
    init
    inTransaction { drop ; create }
  }
}
