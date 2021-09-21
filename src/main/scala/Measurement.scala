case class Measurement(id: String, min: Double, avg: Double, max: Double) {
  override def toString: String = s"$id,$min,$avg,$max"
}

object Measurement {
  implicit val measurementOrdering: Ordering[Measurement] = new Ordering[Measurement] {
    override def compare(r1: Measurement, r2: Measurement): Int = {
      if (r1.avg.isNaN) 1 else {
        if (r2.avg.isNaN) -1 else {
          if (r1.avg > r2.avg) -1 else 1
        }
      }
    }
  }

  def toMeasurements(sensors: Map[String, (Double, Double, Double, Long)]): List[Measurement] =
    sensors.map { case (k, v) => Measurement(k, v._1, v._2 / v._4, v._3) }.toList.sorted
}
