package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeArmorPierce extends Upgrade {
	public static final int UPGRADE_NUMBER = 2;
	public static final float PERCENT_INCREASE = 1.33f;
	
	public float percentPierce;
	public float nextPierce;
	
	public UpgradeArmorPierce(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Armor Pierce";
		description = "Pickaxe strikes through";
		lowerDescription = "more of the enemy's armor";
		maxLevel = 75;
		percentPierce = 0;
		nextPierce = percentPierce + PERCENT_INCREASE;
		nextPierce = Math.round(nextPierce * 100.0f) / 100.0f;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			percentPierce = nextPierce;
			nextPierce = percentPierce + PERCENT_INCREASE;
			nextPierce = Math.round(nextPierce * 100.0f) / 100.0f; // rounds off to 2 decimal places
			
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void load(int level) {
		super.load(level);
		percentPierce = PERCENT_INCREASE * level;
		percentPierce = Math.round(percentPierce * 100.0f) / 100.0f; 
		nextPierce = percentPierce + PERCENT_INCREASE;
		nextPierce = Math.round(nextPierce * 100.0f) / 100.0f;
	}
}
