package lexical.pattern;

import lexical.global.ClosureAttrType;
import lexical.rule.ClosureRule;
import lexical.structure.Production;
import lexical.structure.TransitionGraph;

public class ClosureStrategy implements RuleStrategy {

	private TransitionGraph preTransitionGraph;
	public ClosureStrategy(TransitionGraph preTransitionGraph) {
		this.preTransitionGraph = preTransitionGraph;
	}

	@Override
	public TransitionGraph construct(Production production) {
		// 例如 [a-zA-Z] 这句正则表达式, 如果检查末尾, 可能会下标溢出, 所以用try ... catch ...

		try {
			if (production.getChar() == '+') {
				production.offset(1);
				return new TransitionGraph(
						new ClosureRule(preTransitionGraph.rule, ClosureAttrType.POSITIVE_CLOSURE)
				);
			} else if (production.getChar() == '*') {
				production.offset(1);
				return new TransitionGraph(
						new ClosureRule(preTransitionGraph.rule, ClosureAttrType.KLEENE_CLOSURE)
				);
			} else if (production.getChar() == '?') {
				production.offset(1);
				return new TransitionGraph(
						new ClosureRule(preTransitionGraph.rule, ClosureAttrType.ZERO_ONE_CLOSURE)
				);
			}
		} catch (Exception ignored) {
		}

		return preTransitionGraph;
	}
}
