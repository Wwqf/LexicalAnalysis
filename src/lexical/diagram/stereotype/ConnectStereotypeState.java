package lexical.diagram.stereotype;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.unit.State;
import lexical.rule.CharacterRule;
import lexical.rule.CombinationRule;
import lexical.rule.CountingRule;
import lexical.rule.StringRule;
import lexical.rule.base.BaseRule;
import log.Log;

import java.util.List;

/**
 * 连接状态定式, 为多条规则生成一个有限状态自动机
 */
public class ConnectStereotypeState extends BaseStereotypeDiagram {

	public ConnectStereotypeState(StringRule rule) {
		solveStringRule(rule);
	}

	public ConnectStereotypeState(CombinationRule rule) {
		solveCombinationRule(rule);
	}

	public ConnectStereotypeState(CountingRule rule) {
		solveCountRule(rule);
	}

	private void solveStringRule(StringRule rule) {
		State current = start;
		for (CharacterRule r : rule.getRules()) {
			State next = new State();
			current.addConvertFunc(r, next);

			current = next;
		}
		accept = current;
	}

	private void solveCombinationRule(CombinationRule rule) {
		StringBuilder toOneRule = new StringBuilder();
		List<BaseRule> rules = rule.getRules();

		ConnectStereotypeState state = null;
		if (rules.get(0) instanceof CharacterRule) {
			toOneRule.append(((CharacterRule) rules.get(0)).getCharacter());
		} else {
			state = new ConnectStereotypeState(rules.get(0).generateDiagram());
		}

		for (int i = 1; i < rules.size(); i++) {
			if (rules.get(i) instanceof CharacterRule) {
				toOneRule.append(((CharacterRule) rules.get(i)).getCharacter());
			} else {
				state = processConnectCharacter(toOneRule, state);

				if (state == null) {
					state = new ConnectStereotypeState(rules.get(i).generateDiagram());
				} else {
					state.addDiagram(rules.get(i).generateDiagram());
				}

			}
		}

		state = processConnectCharacter(toOneRule, state);
		this.start = state.start;
		this.accept = state.accept;
	}

	private ConnectStereotypeState processConnectCharacter(StringBuilder toOneRule, ConnectStereotypeState state) {
		if (toOneRule.length() > 1) {
			if (state == null) {
				state = new ConnectStereotypeState(new StringRule(toOneRule.toString()));
			} else {
				state.addDiagram(new StringRule(toOneRule.toString()).generateDiagram());
			}

			toOneRule.delete(0, toOneRule.length());
		} else if (toOneRule.length() == 1) {
			if (state == null) {
				state = new ConnectStereotypeState(new SingleStereotypeState(new CharacterRule(toOneRule.charAt(0))));
			} else {
				state.addDiagram(new SingleStereotypeState(new CharacterRule(toOneRule.charAt(0))));
			}
			toOneRule.delete(0, toOneRule.length());
		}
		return state;
	}

	private void solveCountRule(CountingRule rule) {
		BaseRule item = rule.rule;
		int least = rule.least, most = rule.most;
		if (least >= most) Log.error("countingRule has wrong!");

		State acceptTemporary = new State();

		// init
		BaseStereotypeDiagram diagram = rule.rule.generateDiagram();
		start = diagram.getStart();
		accept = diagram.getAccept();

		for (int i = 1; i < least; i++) {
			diagram = rule.generateDiagram();
			accept.addConvertFunc(new CharacterRule(), diagram.getStart());
			accept = diagram.getAccept();
		}

		for (int i = least; i < most; i++) {
			diagram = rule.generateDiagram();
			accept.addConvertFunc(new CharacterRule(), diagram.getStart());
			accept = diagram.getAccept();
			accept.addConvertFunc(new CharacterRule(), acceptTemporary);
		}

		accept = acceptTemporary;
	}

	/* 在CombinationRule时使用这个构造函数和addDiagram方法 */

	private ConnectStereotypeState(BaseStereotypeDiagram initDiagram) {
		start = initDiagram.getStart();
		accept = initDiagram.getAccept();
	}

	private ConnectStereotypeState addDiagram(BaseStereotypeDiagram diagram) {
		// 两条规则连接时需要用空转换
		accept.addConvertFunc(new CharacterRule(), diagram.getStart());
		accept = diagram.getAccept();
		return this;
	}
}
