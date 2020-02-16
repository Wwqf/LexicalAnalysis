package lexical.pattern;


import lexical.rule.*;
import lexical.rule.base.BaseRule;
import lexical.structure.Production;
import lexical.structure.TransitionGraph;
import lexical.utils.Escape;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/*
 * 小括号策略，在小括号中可以是字符，可以是大括号策略，可以包含另外一个小括号策略，也可以是一条或策略。
 * (\.{digit}+)?
 * (E[+-]?{digit}+)?
 * ({letter_ul} | {digit})*
 * ([+-]? ({letter_ul} | {digit})?)*
 */
public class ParenthesisStrategy implements RuleStrategy {

	private Map<String, TransitionGraph> transitionGraphs;
	private BaseRule preRule;

	public ParenthesisStrategy(Map<String, TransitionGraph> transitionGraphs) {
		this.transitionGraphs = transitionGraphs;
	}

	@Override
	public TransitionGraph construct(Production production) {
		// '('
		production.offset(1);

		// 括号内是否为或规则
		boolean isOrRule = false;
		// 有限状态自动机队列
		Queue<BaseRule> queueFA = new LinkedList<>();

		StringBuilder temporary = new StringBuilder();
		char c;
		while ((c = production.skipSpaces()) != ')') {

			/*
			 * 1. (     2. {
			 * 3. [     4. ordinary char    5. |
			 */

			if (c == '(' || c == '{' || c == '[') {
				connectStringRule(queueFA, temporary);

				// 这三个要检测是否有闭包属性

				TransitionGraph transitionGraph = null;
				TransitionGraph closureTransitionGraph = null;

				if (c == '(') {
					transitionGraph = construct(production);
				} else if (c == '{') {
					transitionGraph = new BraceStrategy(transitionGraphs, preRule).construct(production);
				} else { // '['
					transitionGraph = new OrStrategy().construct(production);
				}

				closureTransitionGraph = new ClosureStrategy(transitionGraph).construct(production);
				preRule = closureTransitionGraph.rule;

				queueFA.add(closureTransitionGraph.rule);
			} else if (c == '|') {
				connectStringRule(queueFA, temporary);

				isOrRule = true;
				production.offset(1);
			} else {
				if (c == '\\') {
					temporary.append(Escape.getEscape(production));
					// 防止多加一位, 向后偏移一位
					production.offset(-1);
				} else if (c == '.') {
					connectStringRule(queueFA, temporary);

					queueFA.add(new PointRule());
				} else temporary.append(c);
				production.offset(1);
			}
		}
		connectStringRule(queueFA, temporary);

		// ')'
		production.offset(1);

		TransitionGraph result = null;
		if (isOrRule) {
			OrRule orRule = new OrRule();
			while (!queueFA.isEmpty()) {
				orRule.addRule(queueFA.poll());
			}
			result = new TransitionGraph(orRule);
		} else {
			CombinationRule combinationRule = new CombinationRule();
			while (!queueFA.isEmpty()) {
				combinationRule.addRule(queueFA.poll());
			}
			result = new TransitionGraph(combinationRule);
		}
		return result;
	}

	private void connectStringRule(Queue<BaseRule> queueFA, StringBuilder temporary) {
		if (temporary.length() > 1) {
			// is strings
			queueFA.add(new StringRule(temporary.toString()));
		} else if (temporary.length() == 1){
			// is char
			queueFA.add(new CharacterRule(temporary.charAt(0)));
		}
		temporary.delete(0, temporary.length());
	}
}
