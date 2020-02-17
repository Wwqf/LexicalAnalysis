package lexical.diagram.unit;


import lexical.global.TokenTag;
import lexical.rule.base.BaseRule;
import lexical.rule.base.SimpleRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {
	private int id = 0;
	private StateType type = StateType.NORMAL_STATE;
	private List<TransitionFunc> transitionFuncList = new ArrayList<>();

	private boolean typeCanModify = true;

	public TokenTag token;

	public State() { }

	public State(StateType type) {
		this.type = type;
	}

	public void addConvertFunc(SimpleRule rule, State nextState) {
		transitionFuncList.add(new TransitionFunc(rule, nextState));
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setType(StateType type) {
		if (!typeCanModify) return ;
		this.type = type;
	}

	public StateType getType() {
		return type;
	}

	public void setTypeCanModify(boolean typeCanModify) {
		this.typeCanModify = typeCanModify;
	}

	public List<TransitionFunc> getTransitionFuncList() {
		return transitionFuncList;
	}

}
