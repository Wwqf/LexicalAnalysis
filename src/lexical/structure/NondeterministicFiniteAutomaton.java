package lexical.structure;

import lexical.diagram.unit.State;
import lexical.diagram.unit.StateType;
import lexical.diagram.unit.TransitionFunc;
import lexical.global.GlobalMark;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NondeterministicFiniteAutomaton {

	private Set<State> states;
	private Set<String> terminals;
	private Set<TransitionFunc> transitionFuncs;
	private State startState;
	private Set<State> accepts;

	public NondeterministicFiniteAutomaton(State startState) {
		states = new HashSet<>();
		terminals = new HashSet<>();
		transitionFuncs = new HashSet<>();
		accepts = new HashSet<>();

		this.startState = startState;
		convert(startState);
	}

	private void convert(State state) {
		if (states.contains(state)) return ;

		states.add(state);

		if (state.getTransitionFuncList().isEmpty()) {
			accepts.add(state);
			return ;
		}

		List<TransitionFunc> funcs = state.getTransitionFuncList();
		for (TransitionFunc item : funcs) {
			transitionFuncs.add(item);
			terminals.add(item.getRule().getRuleString());
			convert(item.getNextState());
		}
	}

	public Set<State> getStates() {
		return states;
	}

	public Set<String> getTerminals() {
		return terminals;
	}

	public Set<TransitionFunc> getTransitionFuncs() {
		return transitionFuncs;
	}

	public State getStartState() {
		return startState;
	}

	public Set<State> getAccepts() {
		return accepts;
	}

	/* 像子集构造法，但是NFA的只判断能到哪些状态，子集构造法是将经过路径的状态都加到集合 */


	public Set<State> move(Set<State> stateSets, char matchCharacter) {
		Set<State> result = new HashSet<>();

		for (State state : stateSets) {
			Set<State> rec = recursiveQuery(state, matchCharacter);
			result.addAll(rec);
		}

		return result;
	}

	private Set<State> recursiveQuery(State state, char matchChar) {
		Set<State> result = new HashSet<>();
		for (TransitionFunc func : state.getTransitionFuncList()) {
			if (func.match(matchChar)) {
				result.add(func.getNextState());
			}
			if (func.match(GlobalMark.Epsilon)) {
				result.addAll(recursiveQuery(func.getNextState(), matchChar));
			}
		}
		return result;
	}
}
