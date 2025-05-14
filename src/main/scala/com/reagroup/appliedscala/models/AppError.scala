package com.reagroup.appliedscala.models

enum AppError extends Throwable {
  case EnrichmentFailure(movieName: String)
}
