package lexical.diagram.stereotype;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.rule.CharacterRule;
import lexical.rule.ConjunctionSymRule;

/**
 * 单字符有限状态自动机, 也适用于连词符规则
 */
public class SingleStereotypeState extends BaseStereotypeDiagram {

	public SingleStereotypeState() {
		this(new CharacterRule());
	}

	public SingleStereotypeState(CharacterRule rule) {
		start.addConvertFunc(rule, accept);
	}

	public SingleStereotypeState(ConjunctionSymRule rule) {
		start.addConvertFunc(rule, accept);
	}
}
