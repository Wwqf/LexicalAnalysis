package lexical.rule;


import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.stereotype.ConnectStereotypeState;
import lexical.rule.base.BaseRule;
import lexical.rule.base.ComplicatedRule;

/**
 * More like the combination ClosureRule and CombinationRule;
 * 计数规则的生成有限状态自动机还未完成
 * {1, 6}
 */
public class CountingRule extends ComplicatedRule {

	public BaseRule rule;
	public int least, most;

	public CountingRule(BaseRule rule, int least, int most) {
		this.rule = rule;
		this.least = least;
		this.most = most;
	}

	@Override
	public BaseStereotypeDiagram generateDiagram() {
		return new ConnectStereotypeState(this);
	}

	@Override
	public String getRuleString() {
		return rule.getRuleString() + "{" + least + ", " + most + "}";
	}
}
