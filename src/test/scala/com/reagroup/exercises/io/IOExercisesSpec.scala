package com.reagroup.exercises.io

import cats.effect.IO
import com.reagroup.exercises.io.IOExercises._
import org.specs2.mutable.Specification

class IOExercisesSpec extends Specification {

  "immediatelyExecutingIO" should {
    "return an IO that would return number 43" in {
      val result = immediatelyExecutingIO()

      result.unsafeRunSync === 43
    }
  }

  "helloWorld" should {
    "not execute immediately" in {
      val logger = new TestLogger
      helloWorld(logger)

      logger.loggedMessages === List.empty
    }

    "return an IO that would log 'hello world' using the `logger` provided" in {
      val logger = new TestLogger
      val result = helloWorld(logger)

      result.unsafeRunSync
      logger.loggedMessages.toList === List("hello world")
    }
  }

  "alwaysFailingTask" should {
    "return an IO containing an Exception" in {
      alwaysFailingTask().attempt.unsafeRunSync match {
        case Left(_: Exception) => ok
        case otherwise => ko(s"Expected a Left(Exception()) but received a $otherwise")
      }
    }
  }

  "logMessageOrFailIfEmpty" should {
    "run `logger` if `msg` is not empty" in {
      val logger = new TestLogger
      val msg = "message"

      val program = logMessageOrFailIfEmpty(msg, logger)
      logger.loggedMessages === List.empty

      program.unsafeRunSync

      logger.loggedMessages.toList === List(msg)

    }

    "return AppException if `msg` is empty" in {
      val logger = new TestLogger
      val msg = ""
      val result = logMessageOrFailIfEmpty(msg, logger).attempt.unsafeRunSync

      result === Left(AppException("Log must not be empty")) && logger.loggedMessages.toList === List()
    }
  }

  "getCurrentTempInF" should {
    "convert the current temperature to Fahrenheit" in {
      val currentTemp = IO.pure(Celsius(100))
      val result = getCurrentTempInF(currentTemp).unsafeRunSync

      result === Fahrenheit(212)
    }
  }

  "getCurrentTempInFAgain" should {
    "convert the current temperature to Fahrenheit using an external converter" in {
      val currentTemp = IO.pure(Celsius(100))
      val converter = (c: Celsius) => IO(Fahrenheit(c.value * 9 / 5 + 32)
      )
      val result = getCurrentTempInFAgain(currentTemp, converter).unsafeRunSync

      result === Fahrenheit(212)
    }
  }

  "showCurrentTempInF" should {
    "return the current temperature in a sentence" in {
      val currentTemp = IO.pure(Celsius(100))
      val converter = (c: Celsius) => IO(Fahrenheit(c.value * 9 / 5 + 32))
      val result = showCurrentTempInF(currentTemp, converter).unsafeRunSync

      result === "The temperature is 212"
    }

    "return an error if the converter fails" in {
      val currentTemp = IO.pure(Celsius(100))
      val error = new Throwable("error")
      val converter: Celsius => IO[Fahrenheit] = _ => IO.raiseError(error)
      val result = showCurrentTempInF(currentTemp, converter).unsafeRunSync

      result === error.getMessage
    }
  }

  "mkUsernameThenPrint" should {
    "print a username if it is not empty" in {
      val username = "scalauser"
      val logger = new TestLogger

      mkUsernameThenPrint(username, logger).unsafeRunSync

      logger.loggedMessages.toList === List(username)
    }

    "return UserNameError if the username is empty" in {
      val username = ""
      val logger = new TestLogger

      val result = mkUsernameThenPrint(username, logger).attempt.unsafeRunSync

      result === Left(UsernameError("Username cannot be empty"))
    }
  }

  "explain" should {
    "write logs in the correct order" in {
      val logger = new TestLogger

      explain(logger).unsafeRunSync
      logger.loggedMessages.toList ==== List("executing step 1", "executing step 2", "executing step 3")
    }
  }

  "execute" should {
    "run an IO program" in {
      val logger = new TestLogger
      val result = helloWorld(logger)

      execute(result)

      logger.loggedMessages.toList === List("hello world")
    }
  }

}

