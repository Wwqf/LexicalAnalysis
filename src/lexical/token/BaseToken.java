package lexical.token;


import lexical.global.TokenTag;
import log.Log;

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
		if (tag == null) {
			Log.error("token tag is null.");
			return "";
		}

		if (value.equals("")) {
			return "<" + tag.getStr() + ">";
		}
		return "<" + tag.getStr() + ", " + value + ">";
	}
}
