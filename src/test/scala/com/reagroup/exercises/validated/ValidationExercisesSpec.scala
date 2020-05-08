package com.reagroup.exercises.validated

import org.specs2.mutable.Specification
import ValidationExercises._
import cats.data.NonEmptyList
import cats.data.Validated._

class ValidationExercisesSpec extends Specification {

  "passwordLengthValidation" should {
    "return the valid password when the password is not too short" in {
      passwordLengthValidation("abcdef123") must_=== Valid("abcdef123")
    }

    "return an error when the password is too short" in {
      passwordLengthValidation("crim3a") must_=== Invalid(NonEmptyList.of(PasswordTooShort))
    }
  }

  "passwordStrengthValidation" should {
    "return the valid password when the password is not too weak" in {
      passwordStrengthValidation("abcdef123") must_=== Valid("abcdef123")
    }

    "return an error when password is too weak" in {
      passwordStrengthValidation("crimeaasd") must_=== Invalid(NonEmptyList.of(PasswordTooWeak))
    }
  }

  "passwordValidation" should {
    "return the valid password when the password is not too short and not too weak" in {
      passwordValidation("abcdef123") must_=== Valid("abcdef123")
    }

    "return two errors when the password is too short and too weak" in {
      passwordValidation("") must_=== Invalid(NonEmptyList.of(PasswordTooWeak, PasswordTooShort))
    }
  }

  "nameValidation" should {
    "return the valid name when the name is not empty" in {
      nameValidation("bob", "firstName") must_=== Valid("bob")
    }

    "return an error when given an empty name" in {
      nameValidation("", "someLabel") must_=== Invalid(NonEmptyList.of(NameIsEmpty("someLabel")))
    }
  }

  "validatePerson" should {
    "return a valid person if all fields are valid" in {
      validatePerson("bob", "smith", "abc1234567") must_=== Valid(Person("bob", "smith", "abc1234567"))
    }

    "return all errors when given empty names and no password" in {
      validatePerson("", "", "") must_=== Invalid(NonEmptyList.of(NameIsEmpty("firstName"), NameIsEmpty("lastName"), PasswordTooWeak, PasswordTooShort))
    }
  }

  "validatePeople" should {
    "return all valid people if there are no errors" in {
      val inputs = List(("jimm", "smith", "hunter1234567"), ("hulk", "smash", "abcc33332"))

      val errsOrPeople = validatePeople(inputs)
      errsOrPeople must_===  Valid(List(Person("jimm", "smith", "hunter1234567"), Person("hulk", "smash", "abcc33332")))
    }

    "return all errors if there is any" in {
      val inputs = List(("jimm", "", ""), ("", "smith", "abcc33332"))

      val errsOrPeople = validatePeople(inputs)
      errsOrPeople must_=== Invalid(NonEmptyList.of(NameIsEmpty("lastName"), PasswordTooWeak, PasswordTooShort, NameIsEmpty("firstName")))
    }
  }

}
