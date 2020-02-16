package lexical.rule;


/**
 * . 规则, 意为除换行符以外的所有字符
 */
public class PointRule extends CharacterRule {

	public PointRule() {
		super('.');
	}

	@Override
	public boolean match(char matchCharacter) {
		return matchCharacter != '\n';
	}

	@Override
	public String getRuleString() {
		return ".";
	}
}
