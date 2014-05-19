package com.rivergillis.androidgames.mineralminer;

public abstract class Upgrade {
	
	public long initialPrice;
	public long price;
	public float priceIncrease;
	public int level;
	public int maxLevel;
	public String name;
	public String description;
	public String lowerDescription;
	
	public Upgrade(long initialPrice, float priceIncrease) {
		this.initialPrice = initialPrice;
		this.priceIncrease = priceIncrease;
		price = this.initialPrice;
		level = 0;
	}
	
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 1.3) {
			priceIncrease -= 0.1;		// used to balance the prices
		}
	}
	
	public void buyUpgrade() {
		Wallet.coins -= price;
		increasePrice();
		++level;
	}
	
	public void load(int level) {
		for (int i = 0; i < level; i++) {
			increasePrice();
		}
		this.level = level;
	}
}
