package com.packt.chapter1

import akka.actor.{Props, PoisonPill, ActorSystem, Actor}

case object Stop

class ShutdownActor extends Actor {
  override def receive: Receive = {
    case msg:String => println(s"$msg")
    case Stop => context.stop(self)
  }
}

object ShutdownApp extends App {
  val actorSystem = ActorSystem("HelloAkka")
  val shotdownActor1 = actorSystem.actorOf(Props[ShutdownActor],
  "shutdownActor1")

  shotdownActor1 ! "hello"
  shotdownActor1 ! PoisonPill
  shotdownActor1 ! "Are you there?"

  val shutdownActor2 = actorSystem.actorOf(Props[ShutdownActor],
  "shutdownActor2")

  shutdownActor2 ! "hello"
  shutdownActor2 ! Stop
  shutdownActor2 ! "Are you there"

}
