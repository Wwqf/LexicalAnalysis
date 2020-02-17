package lexical.structure;

import lexical.algorithm.SubsetConstruction;
import lexical.diagram.unit.State;
import lexical.global.GlobalMark;

import java.util.*;

/**
 * 存在连词符规则简化NFA，很难做NFA - DFA的转换，所以只有一个模板，也没测试
 *
 * 如果想要做NFA-DFA的转换，需要将ConjunctionRule分解成一个或定式有限状态自动机，
 * 还需将StringRule返回的形式做一定处理。
 */

@Deprecated
public class DeterministicFiniteAutomaton {

	private NondeterministicFiniteAutomaton NFA;
	private SubsetConstruction subsetConstruction;

	private Map<StateSet, Map<String, StateSet>> Dstates = new HashMap<>();

	public DeterministicFiniteAutomaton(NondeterministicFiniteAutomaton NFA) {
		this.NFA = NFA;
		this.subsetConstruction = new SubsetConstruction();
	}

	public DeterministicFiniteAutomaton covert() {
		Set<State> states = subsetConstruction.epsilon_closure(NFA.getStartState());

		Queue<StateSet> queue = new LinkedList<>();
		int id = 1;
		queue.add(new StateSet(id++, states));

		while (!queue.isEmpty()) {
			StateSet stateSet = queue.poll();

			if (stateSet.isMark) continue;
			stateSet.isMark = true;

			for (String terminal : NFA.getTerminals()) {
				if (terminal.equals("ε")) continue;

				states = subsetConstruction.epsilon_closure(subsetConstruction.move(stateSet.states, terminal));

				StateSet ss = existDstates(states);
				if (ss == null) {
					ss = new StateSet(id++, states);
					Map<String, StateSet> values = new HashMap<>();

					values.put(terminal, ss);
					Dstates.put(stateSet, values);

					queue.add(ss);
				} else {
					Map<String, StateSet> values = Dstates.get(stateSet);
					if (values == null) values = new HashMap<>();

					values.put(terminal, ss);
					Dstates.put(stateSet, values);
				}
			}

		}

		return this;
	}

	public StateSet existDstates(Set<State> states) {
		for (StateSet ss : Dstates.keySet()) {
			if (ss.states.equals(states)) return ss;
		}
		return null;
	}

	final class StateSet {
		final int id;
		boolean isMark = false;
		Set<State> states;

		StateSet(int id, Set<State> states) {
			this.id = id;
			this.states = states;
		}
	}
}
