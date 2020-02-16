package lexical.logic;

import lexical.diagram.unit.State;
import lexical.diagram.unit.StateType;
import lexical.rule.CharacterRule;
import lexical.structure.TransitionGraph;
import lexical.utils.DiagramSequence;

import java.util.Map;

public class TransitionGraphSet {

	private Map<String, TransitionGraph> transitionGraphs;
	public TransitionGraphSet(Map<String, TransitionGraph> transitionGraphs) {
		this.transitionGraphs = transitionGraphs;
	}

	public State getStartState() {
		State start = new State();
		for (String key : transitionGraphs.keySet()) {
			TransitionGraph tg = transitionGraphs.get(key);
			if (tg.diagram == null) {
				tg.generateDiagram();
			}

			start.addConvertFunc(new CharacterRule(), tg.diagram.getStart());
			// 只可更改序号，不可以更改属性类型
			tg.diagram.getAccept().setType(StateType.ACCEPT_STATE);
			tg.diagram.getAccept().setTypeCanModify(false);

		}
		DiagramSequence.adjustSequence(0, start);
		return start;
	}
}
