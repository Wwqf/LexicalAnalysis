package lexical.rule;


import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.stereotype.KleeneClosureState;
import lexical.diagram.stereotype.PositiveClosureState;
import lexical.diagram.stereotype.ZeroOneClosureState;
import lexical.global.ClosureAttrType;
import lexical.rule.base.BaseRule;
import lexical.rule.base.ComplicatedRule;

/**
 * 闭包规则，包含一个基本规则和一个闭包属性
 */
public class ClosureRule extends ComplicatedRule {

	private BaseRule rule;
	private ClosureAttrType type;

	public ClosureRule(BaseRule rule, ClosureAttrType type) {
		this.rule = rule;
		this.type = type;
	}

	@Override
	public BaseStereotypeDiagram generateDiagram() {
		BaseStereotypeDiagram diagram = getGenerateClone();

		if (diagram != null) {
			return diagram;
		} else {
			if (type == ClosureAttrType.KLEENE_CLOSURE) {
				diagram = new KleeneClosureState(rule);
			} else if (type == ClosureAttrType.POSITIVE_CLOSURE) {
				diagram = new PositiveClosureState(rule);
			} else {
				diagram = new ZeroOneClosureState(rule);
			}
		}

//		setGenerateClone(diagram);
		return diagram;
	}

	@Override
	public String getRuleString() {
		String result = "(";
		if (type == ClosureAttrType.KLEENE_CLOSURE) {
			result += rule.getRuleString();
			result += ")*";
		} else if (type == ClosureAttrType.POSITIVE_CLOSURE) {
			result += rule.getRuleString();
			result += ")+";
		} else if (type == ClosureAttrType.ZERO_ONE_CLOSURE) {
			result += rule.getRuleString();
			result += ")?";
		}
		return result;
	}
}
