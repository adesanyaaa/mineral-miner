package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeFamiliarDamage extends Upgrade{
	public static final int UPGRADE_NUMBER = 10;
	
	public int damage;
	public int nextDamage;

	public UpgradeFamiliarDamage(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Increase Familiar Damage";
		description = "Increases the damage";
		lowerDescription = "of each familiar";
		maxLevel = 200;
		damage = 1;
		nextDamage = 3;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			damage = nextDamage;
			
			if (level >= 100)
				nextDamage = damage + 10000;
			else if (level >= 19)
				nextDamage = damage + 100;
			else if (level >= 9)
				nextDamage = damage + 25;
			else if (level >= 5)
				nextDamage = damage + 15;
			else
				nextDamage = damage + 5;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 2.0) {
			priceIncrease -= 0.2;		// used to balance the prices
		}
	}
}
