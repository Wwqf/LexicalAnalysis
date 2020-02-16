package lexical;

import lexical.logic.LexicalAnalysis;

public class Application {

	public static void main(String[] args) {
		LexicalAnalysis analysis = new LexicalAnalysis();
		System.out.println(analysis.getToken());
	}
}
