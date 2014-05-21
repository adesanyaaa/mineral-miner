package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeSharpenedPickaxe extends Upgrade{
	public static final int UPGRADE_NUMBER = 8;
	public static final float PERCENT_INCREASE = 1f;
	
	public float percentDamage;
	public float nextPercent;
	
	public UpgradeSharpenedPickaxe(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Sharpened Pickaxe";
		description = "Pickaxe damages a percent";
		lowerDescription = "of the enemy's health";
		maxLevel = 9;
		percentDamage = 0;
		nextPercent = 1f;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			percentDamage = nextPercent;
			nextPercent = percentDamage + PERCENT_INCREASE;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void increasePrice() {
		if (level == maxLevel)
			return;				// used to prevent max level from setting price of 10quad, which would overflow a long
		price = (long)(price * priceIncrease);
	}
	
	@Override
	public void load(int level) {
		super.load(level);
		percentDamage = PERCENT_INCREASE * level;
		nextPercent = percentDamage + PERCENT_INCREASE;
	}
}