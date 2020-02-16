package lexical.logic;

import lexical.algorithm.SubsetConstruction;
import lexical.diagram.unit.State;
import lexical.diagram.unit.StateType;
import lexical.global.GlobalMark;
import lexical.structure.NondeterministicFiniteAutomaton;
import lexical.structure.TransitionGraph;
import lexical.token.BaseToken;
import lexical.token.TokenID;
import log.Log;

import java.util.*;

public class LexicalAnalysis {

	private BufferIO buffer;
	private ReadRegular regular;
	private Map<String, TransitionGraph> transitionGraphs;

	private List<String> testcase;

	public LexicalAnalysis() {
		regular = new ReadRegular(GlobalMark.sampleFilename);
		regular.generateDiagram();
		this.transitionGraphs = regular.getTransitionGraphs();

		testcase = regular.getTestcaseFilename();
		buffer = new BufferIO
				.Builder()
				.setFilePath(testcase.get(0))
				.build();
	}


	public BaseToken getToken() {
		State startState = new TransitionGraphSet(transitionGraphs).getStartState();
		NondeterministicFiniteAutomaton automaton = new NondeterministicFiniteAutomaton(startState);

		char c = buffer.nextChar();
		Set<State> sets = new LinkedHashSet<>(), preSets;
		sets.add(startState);
		preSets = sets;

		while (c != BufferIO.EOF) {
			sets = automaton.move(sets, c);

			if (sets.size() == 0) {
				// 没有匹配或者匹配过了, 检查preSets是否存在可接受的状态

				List<State> possibleStates = new ArrayList<>();
				for (State s : preSets) {
					if (s.getType() == StateType.ACCEPT_STATE) {
						possibleStates.add(s);
					}
				}

				if (possibleStates.size() == 0) {
					// no
					Log.debug("error!");
				} else {
					// has
					State acceptState = possibleStates.get(possibleStates.size() - 1);
					System.out.println(buffer.nextMorpheme());
				}

			}
			c = buffer.nextChar();
			preSets = sets;
		}

		return null;
	}
}
