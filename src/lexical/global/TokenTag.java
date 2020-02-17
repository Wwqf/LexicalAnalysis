package lexical.global;

public enum TokenTag {
	None("None"),
	WS("WS"),
	DELIMITER("DELIMITER"),
	NUMBER("NUMBER"),
	ID("ID"),
	INT("INT"),
	FLOAT("FLOAT"),
	DOUBLE("DOUBLE"),
	STRING("STRING"),
	TEXT("TEXT"),
	ARRAY("ARRAY"),
	IF("IF"),
	ELSE("ELSE"),
	RELOP("RELOP");

	private String str;
	TokenTag(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	public static TokenTag match(String str) {
		for (TokenTag tag:
		     TokenTag.values()) {
			if (str.equals(tag.str)) return tag;
		}
		return None;
	}
}
