package lexical.token;

import lexical.global.TokenTag;

public class TokenID extends BaseToken {

	public TokenID(String value) {
		super(TokenTag.ID);
		this.value = value;
	}
}
