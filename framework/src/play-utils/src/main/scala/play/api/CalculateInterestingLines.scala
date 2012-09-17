package play.api
import scalax.io.JavaConverters._

trait CalculateInterestingLines {
  self: PlayException.ExceptionSource =>

  def interestingLines(border: Int = 4): InterestingLines = {
      val i: Option[InterestingLines] = for (f <- Option(input);  
           val (first, last) = f.split('\n').splitAt(line - 1); 
           focus <- last.headOption) yield {
        val before = first.takeRight(border)
        val after = last.drop(1).take(border)
        val firstLine = line - before.size
        val errorLine = before.size
        new InterestingLines(firstLine, ((before :+ focus) ++ after).toArray, errorLine)
      }
      i.getOrElse(null)
    }
}