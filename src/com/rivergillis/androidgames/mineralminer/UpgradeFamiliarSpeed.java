package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeFamiliarSpeed extends Upgrade{
	public static final int UPGRADE_NUMBER = 11;
	
	public float speed;
	public float nextSpeed;
	public float maxSpeed = 0.1f;

	public UpgradeFamiliarSpeed(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Increase Familiar Speed";
		description = "Increases the attack";
		lowerDescription = "speed of each familiar";
		maxLevel = 35;
		speed = 5f;
		nextSpeed = 4.8f;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			speed = nextSpeed;
			
			if (speed <= 0.7)
				nextSpeed = speed - 0.05f;
			else if (speed <= 1.5)
				nextSpeed = speed - 0.1f;
			else
				nextSpeed = speed - 0.2f;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 1.4) {
			priceIncrease -= 0.2;		// used to balance the prices
		}
	}
}
