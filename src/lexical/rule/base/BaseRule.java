package lexical.rule.base;

import lexical.diagram.base.BaseStereotypeDiagram;

/**
 * 规则类基类，一个转换函数包括一条规则，A状态通过匹配一条规则到达B状态
 */
public abstract class BaseRule {

	/**
	 * 为当前规则生成有限状态自动机
	 * @return
	 */
	public abstract BaseStereotypeDiagram generateDiagram();

	/**
	 * 返回规则的字符串形式
	 * @return
	 */
	public abstract String getRuleString();
}
