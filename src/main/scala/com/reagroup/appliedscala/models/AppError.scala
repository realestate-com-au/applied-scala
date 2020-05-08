package com.reagroup.appliedscala.models

sealed trait AppError extends Throwable

case class EnrichmentFailure(movieName: String) extends AppError