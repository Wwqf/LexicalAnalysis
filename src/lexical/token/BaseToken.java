package lexical.token;


import lexical.global.TokenTag;

public abstract class BaseToken {
	private final TokenTag tag;
	protected String value;

	public BaseToken(TokenTag tag) {
		this.tag = tag;
	}

	public TokenTag getTag() {
		return tag;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (value.equals("")) {
			return "<" + tag.getStr() + ">";
		}
		return "<" + tag.getStr() + ", " + value + ">";
	}
}
