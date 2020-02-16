package lexical.diagram.unit;


import lexical.rule.base.BaseRule;
import lexical.rule.base.SimpleRule;

public class TransitionFunc {
	private SimpleRule rule;
	private State nextState;

	public TransitionFunc(SimpleRule rule, State nextState) {
		this.rule = rule;
		this.nextState = nextState;
	}

	public BaseRule getRule() {
		return rule;
	}

	public State getNextState() {
		return nextState;
	}

	public boolean match(char matchCharacter) {
		return rule.match(matchCharacter);
	}
}
