package com.reagroup.appliedscala.models

sealed trait AppError extends Throwable

final case class EnrichmentFailure(movieName: String) extends AppError