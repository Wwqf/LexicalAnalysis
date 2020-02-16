package lexical.algorithm;

import lexical.pattern.*;
import lexical.rule.CombinationRule;
import lexical.rule.PointRule;
import lexical.rule.base.BaseRule;
import lexical.structure.Production;
import lexical.structure.TransitionGraph;

import java.util.Map;

public class Thompson {

	// 产生式头部对应的由此产生式主体构建的有限状态自动机
	private Map<String, TransitionGraph> transitionGraphs;
	// 产生式头部，产生式体
	private Production production;

	public Thompson(Map<String, TransitionGraph> transitionGraphs, Production production) {
		this.transitionGraphs = transitionGraphs;
		this.production = production;
	}

	public TransitionGraph execute() {
		CombinationRule combinationRule = new CombinationRule();
		BaseRule preRule = null;

		char c;
		while (production.hasNext()) {
			c = production.skipSpaces();

			if (c == '(' || c == '{' || c == '[') {
				// 这三个要检测是否有闭包属性

				TransitionGraph transitionGraph = null;
				TransitionGraph closureTransitionGraph = null;

				if (c == '(') {
					transitionGraph = new ParenthesisStrategy(transitionGraphs).construct(production);
				} else if (c == '{') {
					transitionGraph = new BraceStrategy(transitionGraphs, preRule).construct(production);
				} else { // '['
					transitionGraph = new OrStrategy().construct(production);
				}

				closureTransitionGraph = new ClosureStrategy(transitionGraph).construct(production);
				preRule = closureTransitionGraph.rule;
				combinationRule.addRule(closureTransitionGraph.rule);
			} else {
				if (c == '.') {
					PointRule pr = new PointRule();
					combinationRule.addRule(pr);
					preRule = pr;
					production.offset(1);
				} else if (c == '*' || c == '+' || c == '?') {
					TransitionGraph transitionGraph = new TransitionGraph(preRule);
					TransitionGraph closureTransitionGraph = new ClosureStrategy(transitionGraph).construct(production);;

					// remove last element
					combinationRule.getRules().remove(combinationRule.getRules().size() - 1);

					preRule = closureTransitionGraph.rule;
					combinationRule.addRule(closureTransitionGraph.rule);
				} else {
					TransitionGraph transitionGraph = new CharacterStrategy().construct(production);
					preRule = transitionGraph.rule;
					combinationRule.addRule(transitionGraph.rule);
				}
			}
		}

		// 防止多嵌套规则
		TransitionGraph result = null;
		if (combinationRule.getRules().size() == 1) {
			result = new TransitionGraph(combinationRule.getRules().get(0));
		} else if (combinationRule.getRules().size() > 1) {
			result = new TransitionGraph(combinationRule);
		} else {
			throw new RuntimeException("Transition graph can't be built.");
		}

		return result;
	}
}
