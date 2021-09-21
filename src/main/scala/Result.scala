object Result {

  def resultString(state: State, numberOfFiles: Long): String = {
    val measurements = Measurement.toMeasurements(state.sensors)
      s"""|Num of processed files: $numberOfFiles
          |Num of processed measurements: ${state.processed}
          |Num of failed measurements: ${state.failed}
          |
          |Sensors with highest avg humidity:
          |
          |sensor-id,min,avg,max
          |${measurements.mkString("\n")}
      """.stripMargin('|')
  }

}
