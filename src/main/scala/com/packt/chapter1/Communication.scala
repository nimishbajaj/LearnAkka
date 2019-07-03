package com.packt.chapter1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}


import scala.util.Random._
import Messages._

object Messages {
  case class Done(randomNumber: Int)
  case object GiveMeRandomNumber
  case class Start(actorRef: ActorRef)
}

class RandomNumberGeneratorActor extends Actor {
  override def receive: Receive = {
    case GiveMeRandomNumber =>
      println("Recieved a message to generate a random integer")
      val randomNumber = nextInt()
      sender() ! Done(randomNumber)
  }
}

class QueryActor extends Actor{
  override def receive: Receive = {
    case Start(actorRef) => println("Send me the next random " +
      "number")
      actorRef ! GiveMeRandomNumber
    case Done(x) => println(s"received a random number"+
    s" ${x}")
  }
}

object Communication extends App {
  val actorSystem = ActorSystem("HelloAkka")
  val randomNumberGenerator =
    actorSystem.actorOf(Props[RandomNumberGeneratorActor])
  val queryActor = actorSystem.actorOf(Props[QueryActor])

  queryActor ! Start(randomNumberGenerator)
}