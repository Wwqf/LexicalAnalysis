package lexical;

import lexical.global.GlobalMark;
import lexical.logic.LexicalAnalysis;

public class Application {

	public static void main(String[] args) {
		LexicalAnalysis analysis = new LexicalAnalysis();
		while (!GlobalMark.stopLexicalAnalysis) {
			System.out.println(analysis.getToken());
		}
	}
}
