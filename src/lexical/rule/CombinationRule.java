package lexical.rule;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.stereotype.ConnectStereotypeState;
import lexical.rule.base.BaseRule;
import lexical.rule.base.ComplicatedRule;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合规则，多条规则的集合，此规则和计数规则为特例规则（匹配方式转变）
 */
public class CombinationRule extends ComplicatedRule {

	private List<BaseRule> rules = new ArrayList<>();

	@Override
	public BaseStereotypeDiagram generateDiagram() {
		return new ConnectStereotypeState(this);
	}

	@Override
	public String getRuleString() {
		StringBuilder result = new StringBuilder("(");

		for  (BaseRule rule : rules) {
			result.append(rule.getRuleString());
		}
		result.append(")");
		return result.toString();
	}

	public void addRule(BaseRule rule) {
		rules.add(rule);
	}

	public List<BaseRule> getRules() {
		return rules;
	}
}
