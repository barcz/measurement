import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.Await
import scala.concurrent.duration._

class CalculateTests extends munit.FunSuite {

  val testSensorValues = List(Sensor("s1", 10), Sensor("s2", 22), Sensor("s3", Double.NaN), Sensor("s1", 1), Sensor("s3", 33))

  def assertSensor(id: String, sensors: Map[String, (Double, Double, Double, Long)], min: Double, sum: Double, max: Double, counter: Long) = {
    assert(sensors(id)._1 == min, s"$id - min")
    assert(sensors(id)._2 == sum, s"$id - sum")
    assert(sensors(id)._3 == max, s"$id - max")
    assert(sensors(id)._4 == counter, "counter")
  }

  test("akka test state calculation") {
    implicit val testSystem = ActorSystem("TEST")
    implicit val ec = testSystem.dispatcher

    val state: State = Await.result(Source(testSensorValues).via(Main.calculate).toMat(Sink.head)(Keep.right).run(), 5.second)
    assert(state.failed == 1, "failed")
    assert(state.processed == 5, "processed")
    val sensors = state.sensors
    assertSensor("s1", sensors, 1, 11, 10, 2)
    assertSensor("s2", sensors, 22, 22, 22, 1)
    assertSensor("s3", sensors, 33, 33, 33, 1)
  }


}
