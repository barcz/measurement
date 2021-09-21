
class StateTests extends munit.FunSuite {

  test("should update state with valid data") {

    val underTest = State(sensors = Map("sensor1" -> (1.0, 4.0, 3.0, 2L)), processed = 2, failed =  0)

    val newState = State.updateState(Sensor("sensor1", 5.0), underTest)

    val (min, sum, max, count) = newState.sensors("sensor1")

    assert(min == 1.0, "min")
    assert(sum == 9.0, "sum")
    assert(max == 5.0, "max")
    assert(count == 3L, "count")
    assert(newState.processed == 3, "processed values")
    assert(newState.failed == 0, "failed values")
  }

  test("should update state with invalid data") {

    val underTest = State(sensors = Map("sensor1" -> (1.0, 4.0, 3.0, 2L)), processed = 2, failed =  0)

    val newState = State.updateState(Sensor("sensor1", Double.NaN), underTest)

    val (min, sum, max, count) = newState.sensors("sensor1")

    assert(min == 1.0, "min")
    assert(sum == 4.0, "sum")
    assert(max == 3.0, "max")
    assert(count == 2L, "count")
    assert(newState.processed == 3, "processed values")
    assert(newState.failed == 1, "failed values")
  }

  test("should add new sensor data") {

    val underTest = State(sensors = Map("sensor1" -> (1.0, 4.0, 3.0, 2L)), processed = 2, failed =  0)

    val newState = State.updateState(Sensor("sensor2", 5.0), underTest)

    val (min1, sum1, max1, count1) = newState.sensors("sensor1")

    assert(min1 == 1.0, "min")
    assert(sum1 == 4.0, "sum")
    assert(max1 == 3.0, "max")
    assert(count1 == 2L, "count")
    assert(newState.processed == 3, "processed values")
    assert(newState.failed == 0, "failed values")

    val (min2, sum2, max2, count2) = newState.sensors("sensor2")

    assert(min2 == 5.0, "min")
    assert(sum2 == 5.0, "sum")
    assert(max2 == 5.0, "max")
    assert(count2 == 1L, "count")
    assert(newState.processed == 3, "processed values")
    assert(newState.failed == 0, "failed values")
  }

  test("should update sensor data with valid data") {

    val underTest = State(sensors = Map("sensor1" -> (Double.NaN, Double.NaN, Double.NaN, 1L)), processed = 1, failed =  1)

    val newState = State.updateState(Sensor("sensor1", 5.0), underTest)

    val (min1, sum1, max1, count1) = newState.sensors("sensor1")

    assert(min1 == 5.0, "min")
    assert(sum1 == 5.0, "sum")
    assert(max1 == 5.0, "max")
    assert(count1 == 1L, "count")
    assert(newState.processed == 2, "processed values")
    assert(newState.failed == 1, "failed values")
  }


}
