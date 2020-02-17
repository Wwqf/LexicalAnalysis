package lexical.token;

import lexical.global.TokenTag;

public class NoneToken extends BaseToken {

	public NoneToken(String value) {
		super(TokenTag.None);
		this.value = value;
	}
}
