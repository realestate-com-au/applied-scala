package com.reagroup.appliedscala.urls.savemovie

/**
  * The difference between this type and `NewMovieRequest`
  * is that this model is validated, based on the business rules
  * we have in our system.
  */
case class ValidatedMovie(name: String, synopsis: String)
