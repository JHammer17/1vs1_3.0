package de.onevsone.enums;

public enum BestOfsPrefs {

	BESTOF1("First win"),
	BESTOF3("Best of 3"),
	BESTOF5("Best of 5");
	
	private String name;
	
	BestOfsPrefs(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
