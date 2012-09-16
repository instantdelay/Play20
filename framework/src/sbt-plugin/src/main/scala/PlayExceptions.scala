package sbt

import play.api._
import play.core._

import java.io.ByteArrayInputStream


trait PlayExceptions {

  def filterAnnoyingErrorMessages(message: String): String = {
    val overloaded = """(?s)overloaded method value (.*) with alternatives:(.*)cannot be applied to(.*)""".r
    message match {
      case overloaded(method, _, signature) => "Overloaded method value [" + method + "] cannot be applied to " + signature
      case msg => msg
    }
  }

  case class CompilationException(problem: xsbti.Problem) extends PlayException.ExceptionSource  (
    "Compilation error", filterAnnoyingErrorMessages(problem.message)) with CalculateInterestingLines{
    def line = problem.position.line.map(m => m.asInstanceOf[Int]).getOrElse(0)
    def position = problem.position.pointer.map(m => m.asInstanceOf[Int]).getOrElse(0)
    def input = problem.position.sourceFile.map(p=> new ByteArrayInputStream(scalax.file.Path(p).bytes.toArray)).getOrElse(null)
    def sourceName = problem.position.sourceFile.map(_.getAbsolutePath).getOrElse(null)
    override def toString = "in " + Option(sourceName).getOrElse("") + " - " + super.toString()
  }

  case class TemplateCompilationException(source: File, message: String, atLine: Int, column: Int) extends PlayException.ExceptionSource(
    "Compilation error", message) with CalculateInterestingLines {
    def line = atLine
    def position = column
    def input = new ByteArrayInputStream(scalax.file.Path(source).bytes.toArray)
    def sourceName = source.getAbsolutePath
    override def toString = "in " + source.getAbsolutePath + " - " + super.toString()
  }

  case class RoutesCompilationException(source: File, message: String, atLine: Option[Int], column: Option[Int]) extends PlayException.ExceptionSource (
    "Compilation error", message) with CalculateInterestingLines {
    def line = atLine.getOrElse(0)
    def position = column.getOrElse(0)
    def input = new ByteArrayInputStream(scalax.file.Path(source).bytes.toArray)
    def sourceName = source.getAbsolutePath
    override def toString = "in " + source.getAbsolutePath + " - " + super.toString()
  }

  case class AssetCompilationException(source: Option[File], message: String, atLine: Int, atColumn: Int) extends PlayException.ExceptionSource(
    "Compilation error", message) with CalculateInterestingLines{
    def line = atLine
    def position = atColumn
    def input = source.filter(_.exists()).map(p=> new ByteArrayInputStream(scalax.file.Path(p).bytes.toArray)).getOrElse(null)
    def sourceName = source.map(_.getAbsolutePath).getOrElse(null)
    override def toString = "in " + Option(sourceName).getOrElse("") + " - " + super.toString()

  }

}

object PlayExceptions extends PlayExceptions

