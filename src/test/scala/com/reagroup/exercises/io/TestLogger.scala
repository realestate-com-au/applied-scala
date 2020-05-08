package com.reagroup.exercises.io

import scala.collection.mutable.ListBuffer

class TestLogger extends (String => Unit) {

  val loggedMessages: ListBuffer[String] = ListBuffer[String]()

  override def apply(v1: String): Unit = {
    loggedMessages += v1
    ()
  }

}