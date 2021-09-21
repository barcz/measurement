import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Framing, Source}
import akka.util.ByteString

import java.io.File
import java.nio.file.Path
import scala.concurrent.ExecutionContextExecutor

object Main extends App {

  def filesInDir(path: String): List[Path] = {
    val dir = new File(path)
    if (dir.exists() && dir.isDirectory) {
      dir.listFiles().filter(_.getPath.endsWith(".csv")).map(fn => Path.of(fn.getPath)).toList
    } else {
      throw new IllegalArgumentException(s"$path is not a valid directory")
    }
  }

  def readSensors(p: Path) = {
    FileIO.fromPath(p).via(Framing.delimiter(ByteString("\r\n"), maximumFrameLength = 100, allowTruncation = true)).map(_.utf8String).drop(1)
      .map(Sensor.toSensor)
  }

  def calculate: Flow[Sensor, State, NotUsed] = Flow[Sensor].fold(State(Map.empty[String, (Double, Double, Double, Long)], 0, 0))((state, sensor) => State.updateState(sensor, state))

  if (args.length < 1) {
    println("usage: program <directory containing csv files>")
    sys.exit(1)
  }

  implicit val system: ActorSystem = ActorSystem("MEASUREMENTS")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val files: Seq[Path] = filesInDir(args(0))
  if (files.size == 0) {
    throw new IllegalArgumentException("No csv files")
  }
  Source(files).flatMapConcat(readSensors).via(calculate).runForeach(state => println(Result.resultString(state, files.size))).onComplete(_ => system.terminate())
}