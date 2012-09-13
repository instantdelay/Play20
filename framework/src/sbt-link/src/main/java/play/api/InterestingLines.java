package play.api;

public class InterestingLines {
	
	public final int firstLine;
	public final int errorLine;
	public final String[] focus;

	public InterestingLines(int firstLine, String[] focus, int errorLine){
		this.firstLine = firstLine;
		this.errorLine = errorLine;
		this.focus = focus;
	}

}