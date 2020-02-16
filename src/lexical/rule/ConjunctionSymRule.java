package lexical.rule;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.stereotype.SingleStereotypeState;
import lexical.rule.base.SimpleRule;

/**
 * 连词符的应用规则
 * [a-z] [0-9]
 */
public class ConjunctionSymRule extends SimpleRule {

	private char left, right;
	public ConjunctionSymRule(char left, char right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean match(char matchCharacter) {
		return (matchCharacter >= left) && (matchCharacter <= right);
	}

	@Override
	public BaseStereotypeDiagram generateDiagram() {
		return new SingleStereotypeState(this);
	}

	@Override
	public String getRuleString() {
		return left + "-" + right;
	}

	public char getLeft() {
		return left;
	}

	public char getRight() {
		return right;
	}
}
