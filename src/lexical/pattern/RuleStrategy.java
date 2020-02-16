package lexical.pattern;

import lexical.structure.Production;
import lexical.structure.TransitionGraph;

/**
 * 规则策略, 在Thompson算法中，根据字符选择不同的策略，然后返回该策略构建的有限状态自动机。
 */
public interface RuleStrategy {
	TransitionGraph construct(Production production);
}
