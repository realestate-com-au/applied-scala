package com.reagroup.exercises.circe

import io.circe.Decoder.Result
import io.circe._
import io.circe.syntax._

/**
  * Circe (pronounced SUR-see, or KEER-kee in classical Greek, or CHEER-chay in Ecclesiastical Latin) is a JSON library for Scala.
  *
  * We like Circe as opposed to other libraries because it is functional, type-safe and very idiomatic.
  * It integrates very well with the Cats ecosystem.
  *
  * For more comprehensive docs on Circe:
  * https://circe.github.io/circe/
  *
  * There are 3 parts to these exercises.
  *
  * 1. Parsing (`String => Json`)
  * 2. Encoding (`A => Json`)
  * 3. Decoding (`Json => A`)
  */
object CirceExercises {

  /**
    * Json Parsing
    *
    * Hint: `parser.parse` is already in scope (imported through `io.circe._`)
    *
    * Why is the return type an `Either`?
    */
  def strToJson(str: String): Either[ParsingFailure, Json] = {
    ???
  }

  /**
    * Try make a syntax error in the following Json document and compile.
    * What happens?
    */
  val validJson: Json = {
    import io.circe.literal._

    json"""
      {
        "someKey": "someValue",
        "anotherKey": "anotherValue"
      }
    """
  }

  case class Person(name: String, age: Int)

  /**
    * Defining encoders and decoders in the companion object means that Scala will always be able to find them.
    *
    * Note: they may be "shadowed" by a higher priority implicit
    */
  object Person {

    /**
      * Create an `Encoder` instance for `Person` by implementing the `apply` method below.
      *
      * Make `personEncoder` an `implicit` to avoid having to pass the `Encoder` instance
      * into `asJson` explicitly.
      *
      * Bonus content (if you have time):
      *
      * You can read the code below as "Person is an instance of the Encoder type class"
      *
      * More info on type classes:
      *
      * - https://typelevel.org/cats/typeclasses.html
      * - https://www.parsonsmatt.org/2017/01/07/how_do_type_classes_differ_from_interfaces.html
      */
    implicit val personEncoder: Encoder[Person] = (p: Person) => {
      ???
    }

    /**
      * Sometimes you might want several encoders for the same type.
      *
      * Why can't we define this as implicit as well? How would Scala know which one to pick?
      */
    val differentPersonEncoder: Encoder[Person] = (p: Person) => {
      Json.obj(
        "different_name" -> p.name.asJson,
        "different_age" -> p.age.asJson
      )
    }
  }

  /**
    * Scala will look for an implicit `Encoder[Person]` in the following places:
    *
    * - The current scope (current method, class, file)
    * - Imports
    * - The companion object of `Encoder`
    * - The companion object of `Person` (bingo!)
    */
  def encodePerson(person: Person): Json = {
    person.asJson
  }

  /**
    * Use `differentPersonEncoder` explicitly to encode the person
    */
  def encodePersonDifferently(person: Person): Json = {
    person.asJson(???)
  }

  /**
    * Sick of writing custom encoders? You can use "semiauto derivation"
    * to create an `Encoder` instance for you using a Scala feature called macros.
    *
    * The downside to this is the keys of your `Json` are now tightly coupled with
    * how you have named the fields inside `Person`
    *
    * Hint: Use `deriveEncoder`
    *
    * For more comprehensive examples:
    * https://circe.github.io/circe/codecs/semiauto-derivation.html
    */
  def encodePersonSemiAuto(person: Person): Json = {
    import io.circe.generic.semiauto._

    implicit val personEncoder: Encoder[Person] = ???
    person.asJson
  }

  /**
    * Decoding
    */

  /**
    * Remember: `Result[A]` is an alias for `Either[DecodingFailure, A]`
    *
    * Question: Why is the return type an `Either`?
    *
    * Construct a `Decoder` instance for `Person`, that uses the `HCursor` to
    * navigate through the `Json`.
    *
    * Use the provided `HCursor` to navigate through the `Json`, and try to
    * create an instance of `Person`.
    *
    * Hint: Use `cursor.downField("name")` to navigate to the `"name"` field.
    * `cursor.downField("name").as[String]` will navigate to the `"name"` field
    * and attempt to decode the value as a `String`.
    *
    * Alternatively, you can use `cursor.get[String]("name")` to do the same thing.
    *
    * Once you have retrieved the `name` and `age`, construct a `Person`!
    *
    * For more comprehensive cursor docs:
    * https://circe.github.io/circe/api/io/circe/HCursor.html
    *
    * For more comprehensive examples:
    * https://circe.github.io/circe/codecs/custom-codecs.html
    */
  def decodePerson(json: Json): Either[DecodingFailure, Person] = {
    import cats.implicits._

    implicit val personDecoder: Decoder[Person] = new Decoder[Person] {
      override def apply(cursor: HCursor): Result[Person] = ???
    }
    // note: a lot of boilerplate can be removed. Try pressing alt-enter with your
    // cursor over "new Decoder[Person]" above. This works because Decoder is a trait with
    // a single abstract method.

    // This says "Turn this Json to a Person"
    json.as[Person]
  }

  /**
    * You can use "semiauto derivation" for decoders too.
    *
    * Hint: Use deriveDecoder
    */
  def decodePersonSemiAuto(json: Json): Either[DecodingFailure, Person] = {
    import io.circe.generic.semiauto._

    implicit val personDecoder: Decoder[Person] = ???

    json.as[Person]
  }

  /**
    * Parse and then decode
    *
    * Hint: Use `parser.decode`, which does both at the same time.
    */
  def strToPerson(str: String): Either[Error, Person] = {
    import io.circe.generic.semiauto._

    implicit val personDecoder: Decoder[Person] = ???

    ???
  }

}
