package com.reagroup.exercises.circe

import org.specs2.mutable.Specification
import CirceExercises._
import io.circe.{DecodingFailure, Json, ParsingFailure}
import io.circe.syntax._
import io.circe.literal._

class CirceExercisesSpec extends Specification {

  "strToJson" should {

    "parse valid Json" in {
      val json = json"""{"name": "scala"}"""
      val errOrJson = strToJson(json.noSpaces)
      errOrJson must_=== Right(json)
    }

    "return error for invalid Json" in {
      val errOrJson = strToJson("""{"scala"}""")
      errOrJson must beLeft
    }

  }

  "encodePerson" should {

    "convert Person to Json" in {
      val person = Person("scala", 20)
      val actual = encodePerson(person)
      val expected = Json.obj("name" -> "scala".asJson, "age" -> 20.asJson)

      actual must_=== expected
    }

  }

  "encodePersonDifferently" should {
    "convert Person to Json" in {
      val person = Person("scala", 20)
      val actual = encodePersonDifferently(person)
      val expected = Json.obj("different_name" -> "scala".asJson, "different_age" -> 20.asJson)

      actual must_=== expected
    }
  }

  "encodePersonSemiAuto" should {

    "convert Person to Json" in {
      val person = Person("scala", 20)
      val actual = encodePersonSemiAuto(person)
      val expected = Json.obj("name" -> "scala".asJson, "age" -> 20.asJson)

      actual must_=== expected
    }

  }

  "decodePerson" should {

    "convert valid Json to Person" in {
      val json = Json.obj("name" -> "scala".asJson, "age" -> 20.asJson)
      val errOrPerson = decodePerson(json)

      errOrPerson must_=== Right(Person("scala", 20))
    }

    "convert invalid Json to error" in {
      val json = Json.obj("foo" -> "bar".asJson)
      val errOrPerson = decodePerson(json)

      errOrPerson must beLeft
    }

  }

  "decodePersonSemiAuto" should {

    "convert valid Json to Person" in {
      val json = Json.obj("name" -> "scala".asJson, "age" -> 20.asJson)
      val errOrPerson = decodePersonSemiAuto(json)

      errOrPerson must_=== Right(Person("scala", 20))
    }

    "convert invalid Json to error" in {
      val json = Json.obj("foo" -> "bar".asJson)
      val errOrPerson = decodePersonSemiAuto(json)

      errOrPerson must beLeft
    }

  }

  "strToPerson" should {

    "convert valid Json String to Person" in {
      val jsonStr = Json.obj("name" -> "scala".asJson, "age" -> 20.asJson).noSpaces
      val errOrPerson = strToPerson(jsonStr)

      errOrPerson must_=== Right(Person("scala", 20))
    }

    "convert invalid Json String to ParsingFailure" in {
      val invalidJsonStr = "..."
      val errOrPerson = strToPerson(invalidJsonStr)

      errOrPerson match {
        case Left(ParsingFailure(_, _)) => ok
        case other => ko(s"Expected ParsingFailure, but received: $other")
      }
    }

    "convert valid Json String that doesn't contain correct info to DecodingFailure" in {
      val invalidJsonStr = """{"name": 12345}"""
      val errOrPerson = strToPerson(invalidJsonStr)

      errOrPerson match {
        case Left(DecodingFailure(_, _)) => ok
        case other => ko(s"Expected DecodingFailure, but received: $other")
      }
    }

  }

}
