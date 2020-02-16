package lexical.diagram.stereotype;


import lexical.diagram.base.BaseClosureStereotypeDiagram;
import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.global.ClosureAttrType;
import lexical.rule.CharacterRule;
import lexical.rule.base.BaseRule;

/**
 * 传入基本规则，生成一个正闭包有限状态自动机
 */
public class PositiveClosureState extends BaseClosureStereotypeDiagram {

	public PositiveClosureState(BaseRule rule) {
		super(rule);

		this.closureAttrType = ClosureAttrType.POSITIVE_CLOSURE;
	}

	@Override
	protected void connectClosure() {
		BaseStereotypeDiagram diagram = rule.generateDiagram();

		start.addConvertFunc(new CharacterRule(), diagram.getStart());

		diagram.getAccept().addConvertFunc(new CharacterRule(), accept);
		diagram.getAccept().addConvertFunc(new CharacterRule(), diagram.getStart());
	}
}
