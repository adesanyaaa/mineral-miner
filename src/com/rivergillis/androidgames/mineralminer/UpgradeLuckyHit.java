package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeLuckyHit extends Upgrade{
	public static final int UPGRADE_NUMBER = 4;
	public static final int CHANCE_INCREASE = 2;
	public static final float PERCENT_INCREASE = 0.1f;
	
	public float percentChance;
	public float nextChance;
	public float damagePercent;
	public float nextDamage;
	
	public UpgradeLuckyHit(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Lucky Hit";
		description = "Chance to hit harder";
		lowerDescription = "(scales with pickaxe)";
		maxLevel = 35;
		percentChance = 0;
		nextChance = percentChance + CHANCE_INCREASE;
		damagePercent = 1f;
		nextDamage = damagePercent + PERCENT_INCREASE;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			percentChance = nextChance;
			damagePercent = nextDamage;
			nextChance = percentChance + CHANCE_INCREASE;
			nextDamage = damagePercent + PERCENT_INCREASE;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void load(int level) {
		super.load(level);
		percentChance = CHANCE_INCREASE * level;
		damagePercent = 1 + PERCENT_INCREASE * level;
		nextChance = percentChance + CHANCE_INCREASE;
		nextDamage = damagePercent + PERCENT_INCREASE;
	}
}
