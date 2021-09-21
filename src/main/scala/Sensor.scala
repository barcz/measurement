case class Sensor(id: String, value: Double)

object Sensor {
  def toSensor(input: String): Sensor = {
    val parts = input.split(",")
    Sensor(parts(0), parts(1).toDouble)
  }

  def updateSensorValue(sensor: Sensor, sensors: Map[String, (Double, Double, Double, Long)]): Map[String, (Double, Double, Double, Long)] = {
    val (min, sum, max, counter) = sensors(sensor.id)
    if (sensor.value.isNaN) sensors else {
      val newMin = if (min.isNaN || sensor.value < min) sensor.value else min
      val newSum = if (sum.isNaN) sensor.value else sum + sensor.value
      val newMax = if (max.isNaN || sensor.value > max) sensor.value else max
      val newCounter = if (sum.isNaN) counter else counter + 1
      sensors.updated(sensor.id, (newMin, newSum, newMax, newCounter))
    }
  }

}
