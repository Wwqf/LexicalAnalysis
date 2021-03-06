package lexical.pattern;


import lexical.rule.CharacterRule;
import lexical.rule.ConjunctionSymRule;
import lexical.rule.OrRule;
import lexical.rule.PointRule;
import lexical.structure.Production;
import lexical.structure.TransitionGraph;
import lexical.utils.Escape;

import java.util.Stack;

/**
 * 或策略，如果字符为'['，则进入该策略，匹配直到']'结束。
 */
public class OrStrategy implements RuleStrategy {

	@Override
	public TransitionGraph construct(Production production) {
		// 因为扫描到'['就进入当前策略, 则需要偏移一个位置
		production.offset(1);

		OrRule orRule = new OrRule();
		if (production.getChar() == '^') {
			orRule.isComplemented = true;
			production.offset(1);
		}

		// 或规则不用管先后顺序, 为了解决连词符问题，使用栈结构暂存
		Stack<CharacterRule> ruleStack = new Stack<>();

		char c;
		while ((c = production.getChar()) != ']') {
			/*
			 * 会出现3种情况:
			 *  1. 匹配元标识转义字符 \
			 *  2. 匹配连词符 -
			 *  3. 匹配普通字符
			 *  4. .
			 */

			switch (c) {
				case '\\':
					ruleStack.push(new CharacterRule(Escape.getEscape(production)));
					break;
				case '-':
					char left = ruleStack.peek().getCharacter();
					char right = production.getChar(1);

					// 当连词符两边同时为字母或同时为数字时, 并且左边字符比右边值小
					if (Character.isLowerCase(left) && Character.isLowerCase(right)) {
						// e-s
						if (right <= left) throw new RuntimeException("Conjunction semantic error!");

						ruleStack.pop();
						orRule.addRule(new ConjunctionSymRule(left, right));
						production.offset(1);
					} else if (Character.isUpperCase(left) && Character.isUpperCase(right)) {
						// A-Z

						if (right <= left) throw new RuntimeException("Conjunction semantic error!");
						ruleStack.pop();
						orRule.addRule(new ConjunctionSymRule(left, right));
						production.offset(1);
					} else if (Character.isDigit(left) && Character.isDigit(right)) {
						// 0-9

						if (right <= left) throw new RuntimeException("Conjunction semantic error!");
						ruleStack.pop();
						orRule.addRule(new ConjunctionSymRule(left, right));
						production.offset(1);
					} else {
						// 不是连词符，当做一个字符处理
						ruleStack.push(new CharacterRule(c));
					}
					// 处理完字符，偏移一位
					production.offset(1);
					break;
				case '.':
					ruleStack.push(new PointRule());
					production.offset(1);
					break;
				default:
					ruleStack.push(new CharacterRule(c));
					// 处理完字符，偏移一位
					production.offset(1);
					break;
			}
		}

		while (!ruleStack.empty()) {
			orRule.addRule(ruleStack.pop());
		}

		// 因为扫描到']'跳出循环，则当前处于']'字符, 偏移一位到下一个字符
		production.offset(1);

		if (orRule.getRules().size() == 1) {
			return new TransitionGraph(orRule.getRules().get(0));
		}

		return new TransitionGraph(orRule);
	}
}
