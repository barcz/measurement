
class SensorTests extends munit.FunSuite {

  test("should update sensor value with valid value") {
    val underTest = Sensor("testSensor", 11)
    val sensors = Map("testSensor" -> (1.0,1.0,1.0,1L))
    val updatedSensors = Sensor.updateSensorValue(underTest, sensors)
    val (min, sum, max, counter) = updatedSensors("testSensor")
    assert(min == 1.0, "min updated")
    assert(sum == 12.0, "sum updated")
    assert(max == 11.0, "max updated")
    assert(counter == 2L, "counter updated")
  }

  test("should replace invalid sensor value with valid value") {
    val underTest = Sensor("testSensor", 11)
    val sensors = Map("testSensor" -> (Double.NaN,Double.NaN,Double.NaN,1L))
    val updatedSensors = Sensor.updateSensorValue(underTest, sensors)
    val (min, sum, max, counter) = updatedSensors("testSensor")
    assert(min == 11.0, "min updated")
    assert(sum == 11.0, "sum updated")
    assert(max == 11.0, "max updated")
    assert(counter == 1L, "counter updated")
  }

}
