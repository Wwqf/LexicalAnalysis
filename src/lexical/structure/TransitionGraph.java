package lexical.structure;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.rule.base.BaseRule;
import lexical.table.TransitionTable;
import lexical.utils.DiagramSequence;

public class TransitionGraph {
	public BaseRule rule;
	public BaseStereotypeDiagram diagram;

	public TransitionGraph(BaseRule rule) {
		this.rule = rule;
	}

	public TransitionGraph generateDiagram() {
		diagram = rule.generateDiagram();
		return this;
	}

	public TransitionGraph adjustSequence() {
		DiagramSequence.adjustSequence(0, diagram);
		return this;
	}

	public String getTransitionTable() {
		return new TransitionTable(diagram.getStart()).process().toString();
	}

}
