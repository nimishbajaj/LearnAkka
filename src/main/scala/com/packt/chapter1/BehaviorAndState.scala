package com.packt.chapter1

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration._

object BehaviorAndState extends App {
  implicit val timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem("HelloAkka")
//  println(actorSystem)

  val actor = actorSystem.actorOf(Props[FibonacciActor])

  val future = (actor ? 10).mapTo[Int]
  val fibonacciNumber = Await.result(future, 10 seconds)
  println(fibonacciNumber)
}
