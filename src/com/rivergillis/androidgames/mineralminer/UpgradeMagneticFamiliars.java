package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeMagneticFamiliars extends Upgrade {
	public static final int UPGRADE_NUMBER = 12;
	
	public float goldPerSecond;
	public float nextGoldPerSecond;
	

	public UpgradeMagneticFamiliars(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Magnetic Familiars";
		description = "Gain gold each second";
		lowerDescription = "for each familiar";
		maxLevel = 300;
		goldPerSecond = 0;
		nextGoldPerSecond = 50;
		
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			goldPerSecond = nextGoldPerSecond;
			
			if (level <= 20)
				nextGoldPerSecond += 50;
			else if (level <= 50)
				nextGoldPerSecond += 100;
			else if (level <= 75)
				nextGoldPerSecond += 200;
			else if (level <= 100)
				nextGoldPerSecond += 300;
			else if (level <= 150)
				nextGoldPerSecond += 500;
			else if (level <= 200)
				nextGoldPerSecond += 1000;
			else
				nextGoldPerSecond += 2000;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 1.6) {
			priceIncrease -= 0.1;		// used to balance the prices
		}
	}
}
