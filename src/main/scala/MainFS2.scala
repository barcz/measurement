import cats.effect.{ExitCode, IO, IOApp}
import fs2._
import fs2.io.file.{Files, Path}

import java.io.File

object MainFS2 extends IOApp {

  def filesInDirectory(path: String): IO[List[Path]] = {
    val dir = new File(path)
    if (dir.exists && dir.isDirectory)
      IO.blocking(dir.listFiles.filter(f => f.getName.endsWith(".csv")).map(f => Path(f.getPath)).toList)
    else
      IO.raiseError(new IllegalArgumentException("Not a valid directory"))
  }

  def calculate: Pipe[IO, Sensor, State] = {

    def calculateSum(s: Stream[IO, Sensor], state: State): Pull[IO, State, Unit] = {
      s.pull.uncons1.flatMap {
        case Some((hd, tl)) =>
          val newState = State.updateState(hd, state)
          calculateSum(tl, newState)
        case None =>
          if (state.processed == 0) Pull.done else {
            Pull.output1(state)
          }
      }
    }

    in => calculateSum(in, State(Map.empty[String, (Double, Double, Double, Long)], 0, 0)).stream
  }

  def readFile(path: Path): Stream[IO, Sensor] =
    Files[IO]
      .readAll(path)
      .through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      .map(s => Sensor.toSensor(s))

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- if (args.length < 1) IO.raiseError(new IllegalArgumentException("Usage: program <directory of csv files>")) else IO.unit
    fileList <- filesInDirectory(args(0))
    stateResult <- Stream.iterable(fileList).flatMap(path => readFile(path)).through(calculate).compile.last
    _ <- IO.println(Result.resultString(stateResult.getOrElse(State(Map.empty[String, (Double, Double, Double, Long)], 0, 0)), fileList.length))
  } yield (ExitCode.Success)

}
