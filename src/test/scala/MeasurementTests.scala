
class MeasurementTests extends munit.FunSuite {

  test("converting measurements and ordering") {
    val sensors: Map[String, (Double, Double, Double, Long)] = Map(
      ("s1" -> (Double.NaN, Double.NaN, Double.NaN, 1L)),
      ("s2" -> (1, 11, 10, 2L)),
      ("s3" -> (100, 100, 100, 1L))
    )


    val measurements = Measurement.toMeasurements(sensors)

    val m1 = measurements(0)
    val m2 = measurements(1)
    val m3 = measurements(2)

    assert(m1.id == "s3", "m1 id")
    assert(m1.min == 100, "m1 min")
    assert(m1.avg == 100, "m1 avg")
    assert(m1.max == 100, "m1 max")

    assert(m2.id == "s2", "m2 id")
    assert(m2.min == 1, "m2 min")
    assert(m2.avg == 5.5, "m2 avg")
    assert(m2.max == 10, "m2 max")

    assert(m3.id == "s1", "m3 id")
    assert(m3.min.isNaN, "m3 min")
    assert(m3.avg.isNaN, "m3 avg")
    assert(m3.max.isNaN, "m3 max")

  }

}
