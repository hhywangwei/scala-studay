package com.appeme.models

import org.squeryl.PrimitiveTypeMode._
import KanbanSchema._

/**
 * Created by weiwang on 14/11/4.
 */
class Story(val number: String, val title: String, val phase: String) {

  private def phaseLimits = Map("ready" -> Some(3),
    "dev" -> Some(2), "test" -> Some(2), "deploy" -> None)

  private[this] def validate = {
    if (number.isEmpty || title.isEmpty) {
      throw new ValidateException("Both number and title are required")
    }
    if (!stories.where(a => a.number === number).isEmpty) {
      throw new ValidateException("The story number is not unique")
    }
  }

  private[this] def validateLimit = {
    val currentSize: Long = from(stories)(s => where(s.phase === phase) compute (count))
    if (currentSize == phaseLimits(phase).getOrElse(-1)) {
      throw new ValidateException("You cannot exceed the limit set for the phase!")
    }
  }

  def save(): Either[Throwable, String] = {
    tx {
      try {
        validate
        stories.insert(this)
        Right("Story is create success")
      } catch {
        case exception: Throwable => Left(exception)
      }
    }
  }

  def moveTo(phase: String): Either[Throwable, String] = {
    tx {
      try {
        validateLimit
        update(stories)(s => where(s.number === this.number) set (s.phase := phase))
        Right("Card " + this.number + " is moved to " + phase + "phase successfully")
      } catch {
        case exception: Throwable =>
          exception.printStackTrace(); Left(exception)
      }
    }
  }
}


object Story {
  def apply(number: String, title: String) = {
    new Story(number, title, "ready")
  }

  def findAllByPhase(phase: String) = tx {
    from(stories)(s => where(s.phase === phase) select (s)) map (s => s)
  }

  def findByNumber(number: String) = tx {
    stories.where(s => s.number === number).single
  }
}

class ValidateException(message: String) extends RuntimeException(message)
