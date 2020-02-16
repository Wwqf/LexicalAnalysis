package lexical.rule;

import lexical.diagram.base.BaseStereotypeDiagram;
import lexical.diagram.stereotype.ConnectStereotypeState;
import lexical.rule.base.ComplicatedRule;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串规则，多个单字符规则的集合，在规则之间的转换中不需要插入空转换
 * （在生成ConnectStereotypeState时，每个规则之间需要插入一条控转换，若在这里添加则多余）
 */
public class StringRule extends ComplicatedRule {

	private String value;
	private List<CharacterRule> rules;
	public StringRule(String value) {
		this.value = value;

		rules = new ArrayList<>();
		for (char c : value.toCharArray()) {
			rules.add(new CharacterRule(c));
		}
	}

	@Override
	public BaseStereotypeDiagram generateDiagram() {
		BaseStereotypeDiagram diagram = getGenerateClone();

		if (diagram != null) {
			return diagram;
		} else {
			diagram = new ConnectStereotypeState(this);
		}

//		setGenerateClone(diagram);
		return diagram;
	}

	@Override
	public String getRuleString() {
		return value;
	}

	public List<CharacterRule> getRules() {
		return rules;
	}
}
