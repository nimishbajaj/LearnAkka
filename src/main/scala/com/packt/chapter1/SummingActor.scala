package com.packt.chapter1

import akka.actor.Actor

class SummingActor extends Actor{
  var sum = 0

  override def receive: Receive = {
    case x: Int => sum = sum + x
      println(s"my state as sum is $sum")

    case _ => println("I don't know what are you " +
      "talking about")
  }
}
