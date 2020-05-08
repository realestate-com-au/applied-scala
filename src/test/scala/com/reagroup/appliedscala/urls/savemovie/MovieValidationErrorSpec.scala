package com.reagroup.appliedscala.urls.savemovie

import cats.data._
import cats.implicits._
import org.specs2.mutable.Specification

class MovieValidationErrorSpec extends Specification {

  "show" should {
    "stringify MovieNameTooShort" in {
      val result = MovieValidationError.show(MovieNameTooShort)

      result must_=== "MOVIE_NAME_TOO_SHORT"
    }

    "return NewMovie" in {
      val result = MovieValidationError.show(MovieSynopsisTooShort)

      result must_=== "MOVIE_SYNOPSIS_TOO_SHORT"
    }
  }

}
