package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeFamiliar extends Upgrade{
	public static final int UPGRADE_NUMBER = 9;

	public int numberOfFamiliars;
	
	public UpgradeFamiliar(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Add a Familiar";
		description = "Gives you a familiar";
		lowerDescription = "Each continuously attacks";
		maxLevel = 50;
		numberOfFamiliars = 0;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			numberOfFamiliars++;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void load(int level) {
		super.load(level);
		numberOfFamiliars = level;
	}
	
	@Override
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 2.0) {
			priceIncrease -= 0.1;		// used to balance the prices
		}
	}
}
