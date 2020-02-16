package lexical.rule.base;

/**
 * 简单性规则，不用生成clone版本
 */
public abstract class SimpleRule extends BaseRule {

	public abstract boolean match(char matchingValue);
}
