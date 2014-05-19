package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeMineralMerchant extends Upgrade {
	public static final int UPGRADE_NUMBER = 3;
	public static final float PERCENT_CHANCE_INCREASE = 2f;
	public static final float PERCENT_GAINED_INCREASE = 0.035f;
	
	public float percentChance;
	public float percentGained;
	public float nextChance;
	public float nextGained;
	
	public UpgradeMineralMerchant(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Mineral Merchant";
		description = "Increases chance to gain";
		lowerDescription = "more gold from enemies";
		maxLevel = 50;
		percentChance = 0f;
		percentGained = 1f;
		nextChance = percentChance + PERCENT_CHANCE_INCREASE;
		nextGained = percentGained + PERCENT_GAINED_INCREASE;
		nextGained = Math.round(nextGained * 1000.0f) / 1000.0f;
	}

	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			percentChance = nextChance;
			percentGained = nextGained;
			nextChance = percentChance + PERCENT_CHANCE_INCREASE;
			nextGained = percentGained + PERCENT_GAINED_INCREASE;
			nextGained = Math.round(nextGained * 1000.0f) / 1000.0f;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void load(int level) {
		super.load(level);
		percentChance = PERCENT_CHANCE_INCREASE * level;
		percentGained = 1 + PERCENT_GAINED_INCREASE * level;
		percentGained = Math.round(percentGained * 1000.0f) / 1000.0f;
		nextChance = percentChance + PERCENT_CHANCE_INCREASE;
		nextGained = percentGained + PERCENT_GAINED_INCREASE;
		nextGained = Math.round(nextGained * 1000.0f) / 1000.0f;
	}
}
