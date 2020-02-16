package lexical.global;

public enum TokenTag {
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
}
