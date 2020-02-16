package lexical.utils;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.unit.State;
import lexical.diagram.unit.StateType;
import lexical.diagram.unit.TransitionFunc;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 处理有限状态自动机的序号
 */
public class DiagramSequence {

	public static void adjustSequence(int startTag, BaseStereotypeDiagram diagram) {
		adjustSequence(startTag, diagram.getStart());
	}

	public static void adjustSequence(int startTag, State startState) {
		startState.setType(StateType.START_STATE);

		Queue<State> queue = new LinkedList<>();
		queue.add(startState);

		while (!queue.isEmpty()) {
			State s = queue.poll();

			if (s.getId() == 0) s.setId(startTag++);
			else continue;

			if (s.getTransitionFuncList().size() == 0) {
				s.setType(StateType.ACCEPT_STATE);
				continue;
			}

			for (TransitionFunc func : s.getTransitionFuncList()) {
				queue.add(func.getNextState());
			}
		}
	}
}
