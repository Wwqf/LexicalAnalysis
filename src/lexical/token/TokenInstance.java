package lexical.token;

import lexical.global.TokenTag;

public class TokenInstance extends BaseToken {
	public TokenInstance(TokenTag tag, String value) {
		super(tag);
		this.value = value;
	}
}
