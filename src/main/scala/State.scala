case class State(sensors: Map[String, (Double, Double, Double, Long)], processed: Long, failed: Long)

object State {
  def updateState(sensor: Sensor, state: State): State = {
    if (state.sensors.contains(sensor.id)) {
      state.copy(sensors = Sensor.updateSensorValue(sensor, state.sensors), processed = state.processed + 1, if (sensor.value.isNaN) state.failed + 1 else state.failed)
    } else state.copy(sensors = state.sensors + (sensor.id -> (sensor.value, sensor.value, sensor.value, 1)), processed = state.processed + 1, failed = if (sensor.value.isNaN) state.failed + 1 else state.failed)
  }
}
