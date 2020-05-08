package com.reagroup.appliedscala.urls.fetchallmovies

import cats.effect.IO
import com.reagroup.appliedscala.models._
import org.specs2.mutable.Specification

class FetchAllMoviesServiceSpec extends Specification {

  "fetchAllMovies" should {

    "return all movies" in {

      val movie1 = Movie("Batman Returns", "Bats are cool!", Vector.empty[Review])
      val movie2 = Movie("Titanic", "Can't sink this!", Vector.empty[Review])
      val allMovies = Vector(movie1, movie2)

      val repo = IO.pure(allMovies)

      val service = new FetchAllMoviesService(repo)

      val actual = service.fetchAll

      actual.unsafeRunSync() must_=== allMovies

    }

  }

}
