package lexical.logic;

import lexical.diagram.unit.State;
import lexical.diagram.unit.StateType;
import lexical.global.GlobalMark;
import lexical.global.TokenTag;
import lexical.structure.NondeterministicFiniteAutomaton;
import lexical.structure.TransitionGraph;
import lexical.table.TransitionTable;
import lexical.token.BaseToken;
import lexical.token.NoneToken;
import lexical.token.TokenID;
import lexical.token.TokenInstance;

import java.util.*;

public class LexicalAnalysis {

	private BufferIO buffer;
	private ReadRegular regular;
	private Map<String, TransitionGraph> transitionGraphs;

	private List<String> testcase;

	private final State startState;
	private NondeterministicFiniteAutomaton NFA;

	public LexicalAnalysis() {
		regular = new ReadRegular(GlobalMark.sampleFilename);
		regular.generateDiagram();
		this.transitionGraphs = regular.getTransitionGraphs();

		testcase = regular.getTestcaseFilename();
		buffer = new BufferIO
				.Builder()
				.setFilePath(testcase.get(0))
				.build();

		startState = new UnifiedCollection(transitionGraphs).getStartState();
		NFA = new NondeterministicFiniteAutomaton(startState);

		TransitionTable transitionTable = new TransitionTable(startState);
//		System.out.println(transitionTable.process().toString());
	}


	public BaseToken getToken() {
		SAVE save = new SAVE();

		Set<State> states = new LinkedHashSet<>();
		states.add(startState);

		char c = buffer.nextChar();
		while (c != BufferIO.EOF) {
			states = NFA.move(states, c);
			if (states.isEmpty()) {
				State acceptState = save.getAcceptState();

				if (acceptState != null) {
					acceptState = save.getAcceptState(acceptState);
					assert acceptState != null;

					String tokenValue = buffer.nextMorpheme();
					return new TokenInstance(acceptState.token, tokenValue);
				}

				return null;
			} else save.saveAcceptState(states);

			c = buffer.nextChar();
		}

		State acceptState = save.getAcceptState();
		NoneToken token = null;
		if (acceptState != null) {
			String tokenValue = buffer.nextMorpheme();
			token = new NoneToken(tokenValue);
		}

		return token;
	}

	public final class SAVE {
		List<List<State>> lists;

		SAVE() {
			lists = new ArrayList<>();
		}

		void saveAcceptState(Set<State> sets) {
			List<State> save = new ArrayList<>();
			for (State s : sets) {
				if (checkHasEndState(s)) {
					save.add(s);
				}
			}

			if (!save.isEmpty())
				lists.add(save);
		}

		State getAcceptState() {
			if (lists.isEmpty()) return null;

			List<State> items = lists.get(lists.size() - 1);
			return items.get(items.size() - 1);
		}

		boolean checkHasEndState(State s) {
			if (s.getType() == StateType.ACCEPT_STATE) return true;

			Set<State> rec = NFA.move(s, 'ε');
			for (State item : rec) {
				if (item.getType() == StateType.ACCEPT_STATE) return true;
			}
			return false;
		}

		State getAcceptState(State s) {
			if (s.getType() == StateType.ACCEPT_STATE) return s;

			Set<State> rec = NFA.move(s, 'ε');
			for (State item : rec) {
				if (item.getType() == StateType.ACCEPT_STATE) return item;
			}
			return null;
		}
	}
}
