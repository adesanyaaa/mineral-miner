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
			
			if (level <= 10)
				nextGoldPerSecond += 50;
			else if (level <= 20)
				nextGoldPerSecond += 100;
			else if (level <= 30)
				nextGoldPerSecond += 250;
			else if (level <= 40)
				nextGoldPerSecond += 500;
			else if (level <= 50)
				nextGoldPerSecond += 1000;
			else if (level <= 60)
				nextGoldPerSecond += 2000;
			else if (level <= 75)
				nextGoldPerSecond += 3000;
			else if (level <= 100)
				nextGoldPerSecond += 5000;
			else if (level <= 125)
				nextGoldPerSecond += 7000;
			else if (level <= 150)
				nextGoldPerSecond += 10000;
			else if (level <= 175)
				nextGoldPerSecond += 30000;
			else if (level <= 200)
				nextGoldPerSecond += 50000;
			else
				nextGoldPerSecond += 100000;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 1.2) {
			priceIncrease -= 0.1;		// used to balance the prices
		}
	}
}
