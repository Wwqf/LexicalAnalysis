package lexical.diagram.stereotype;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.rule.CharacterRule;
import lexical.rule.OrRule;
import lexical.rule.base.BaseRule;

/**
 * 或定式
 */
public class OrStereotypeState extends BaseStereotypeDiagram {

	public OrStereotypeState(OrRule orRule) {
		solveOrRule(orRule);
	}

	private void solveOrRule(OrRule orRule) {
		if (orRule.getRules().size() == 1) {

			BaseStereotypeDiagram diagram = orRule.getRules().get(0).generateDiagram();
			start = diagram.getStart();
			accept = diagram.getAccept();

			return ;
		}

		for (BaseRule rule : orRule.getRules()) {
			BaseStereotypeDiagram diagram = rule.generateDiagram();
			start.addConvertFunc(new CharacterRule(), diagram.getStart());
			diagram.getAccept().addConvertFunc(new CharacterRule(), accept);
		}
	}
}
