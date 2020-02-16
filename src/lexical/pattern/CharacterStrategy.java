package lexical.pattern;


import lexical.rule.CharacterRule;
import lexical.rule.StringRule;
import lexical.structure.Production;
import lexical.structure.TransitionGraph;
import lexical.utils.Escape;

/**
 * 字符规则策略, 当遇到任意字符时都可调用此策略
 */
public class CharacterStrategy implements RuleStrategy {

	// 不可追加特殊字符
	private static char[] cantAppendSC = {
			'{', '[', '(', '.', '*', '+', '?'
	};

	@Override
	public TransitionGraph construct(Production production) {
		StringBuilder appendStr = new StringBuilder();

		TransitionGraph transitionGraph = null;

		char c;

		outer:
		while (production.hasNext()) {
			c = production.getChar();

			for (char item : cantAppendSC) {
				// 中断调用
				if (c == item) break outer;
			}

			switch (c) {
				case '\\':
					appendStr.append(Escape.getEscape(production));
					break;
				case '"':
					production.offset(1);
					while ((c = production.getChar()) != '"') {
						appendStr.append(c);
						production.offset(1);
					}
					production.offset(1);
					break;
				default:
					appendStr.append(c);
					production.offset(1);
					break;
			}
		}

		if (appendStr.length() == 1) {
			transitionGraph = new TransitionGraph(new CharacterRule(appendStr.charAt(0)));
		} else {
			transitionGraph = new TransitionGraph(new StringRule(appendStr.toString()));
		}
		return transitionGraph;
	}
}
