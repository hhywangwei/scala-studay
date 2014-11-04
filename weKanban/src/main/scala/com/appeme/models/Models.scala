package com.appeme.models

import org.squeryl.PrimitiveTypeMode._
import KanbanSchema._
/**
 * Created by weiwang on 14/11/4.
 */
class Story(val number:String, val title:String, val phase:String) {

  private [this] def validate = {
    if(number.isEmpty || title.isEmpty){
      throw new ValidateException("Both number and title are required")
    }
    if(!stories.where(a => a.number === number).isEmpty){
      throw new ValidateException("The story number is not unique")
    }
  }

  def save() : Either[Throwable, String] = {
    tx{
      try{
        validate
        stories.insert(this)
        Right("Story is create success")
      }catch {
        case exception: Throwable => Left(exception)
      }
    }
  }
}

object Story{
  def apply(number:String, title:String) ={
    new Story(number, title, "ready")
  }
}

class ValidateException(message:String) extends RuntimeException(message)
