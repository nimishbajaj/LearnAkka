package com.packt.chapter1

import java.util.concurrent.ConcurrentLinkedQueue
import akka.actor.{Props, Actor, ActorSystem, ActorRef}
import akka.dispatch.{MailboxType, ProducesMessageQueue,
Envelope, MessageQueue}
import com.typesafe.config.Config

class MyMessageQueue extends MessageQueue{
  private final val queue = new
    ConcurrentLinkedQueue[Envelope]()

  //these should be implemented; queue used as example
  override def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
    if(handle.sender.path.name == "MyActor") {
      handle.sender ! "hey dude, how are you?, I know your name, processing" +
        "your request"
      queue.offer(handle)
    } else handle.sender ! "i don't talk to strangers, I can't process" +
      "your request"
  }

  override def dequeue(): Envelope = queue.poll()

  override def numberOfMessages: Int = queue.size()

  override def hasMessages: Boolean = !queue.isEmpty

  override def cleanUp(owner: ActorRef, deadLetters: MessageQueue): Unit = {
    while (hasMessages) {
      deadLetters.enqueue(owner, dequeue())
    }
  }

}

class MyUnboundedMailbox extends MailboxType
  with ProducesMessageQueue[MyMessageQueue] {

  def this(settings: ActorSystem.Settings,
           config: Config) = this()

  final override def create(owner: Option[ActorRef], system:
  Option[ActorSystem]): MessageQueue = new MyMessageQueue()
}


class MySpecialActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(msg)
  }
}

class MyActor extends Actor {
  override def receive: Receive = {
    case (msg: String, actorRef: ActorRef) =>
      actorRef ! msg
    case msg => println(msg)
  }
}

object CustomMailbox extends App {


  val actorSystem = ActorSystem("HelloAkka")
  val actor = actorSystem.actorOf(Props[MySpecialActor]
    .withDispatcher("custom-dispatcher"))

  val actor1 = actorSystem.actorOf(Props[MyActor],"xyz")
  val actor2 = actorSystem.actorOf(Props[MySpecialActor], "MyActor")

  actor1 ! ("Hello", actor)
  actor2 ! ("Hello", actor)

}
