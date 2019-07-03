package com.packt.chapter1

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.ActorSystem.Settings
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.Config

class MyPriorityActor extends Actor {
  override def receive: Receive = {

    // Int messages
    case x: Int => println(x)

    // String messages
    case x: String => println(x)

    // Long messages
    case x: Long => println(x)

    // Other messages
    case x => println(x)
  }
}

class MyPriorityActorMailbox
(settings: Settings, config: Config) extends UnboundedPriorityMailbox (
  PriorityGenerator {
    case x: Int => 1
    case x: String => 0
    case x: Long => 2
    case _ => 3
  }
)

object PriorityActorMailboxApp extends App {
  val actorSystem = ActorSystem("HelloAkka")

  val myPriorityActor = actorSystem.actorOf(
    Props[MyPriorityActor].withDispatcher("prio-dispatcher"))

  myPriorityActor ! 6.0
  myPriorityActor ! 1
  myPriorityActor ! 5.0
  myPriorityActor ! 3
  myPriorityActor ! "Hello"
  myPriorityActor ! 5
  myPriorityActor ! "I am priority actor"
  myPriorityActor ! "I process string messages first,theninteger, long and others"
}