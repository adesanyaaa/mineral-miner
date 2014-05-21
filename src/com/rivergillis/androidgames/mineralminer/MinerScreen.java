package com.rivergillis.androidgames.mineralminer;

import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.rivergillis.androidgames.framework.Game;
import com.rivergillis.androidgames.framework.Input.TouchEvent;
import com.rivergillis.androidgames.framework.gl.Camera2D;
import com.rivergillis.androidgames.framework.gl.SpriteBatcher;
import com.rivergillis.androidgames.framework.impl.GLScreen;
import com.rivergillis.androidgames.framework.math.OverlapTester;
import com.rivergillis.androidgames.framework.math.Rectangle;
import com.rivergillis.androidgames.framework.math.Vector2;

public class MinerScreen extends GLScreen {
	
	public static final int NUMBER_OF_ROCKS = 50;
	
	Camera2D guiCam;
	Vector2 touchPoint;
	SpriteBatcher batcher;
	Rectangle rockBounds;
	Rectangle leftBounds;
	Rectangle rightBounds;
	Rectangle upgradeButtonBounds;
	Rectangle upgradeBounds[];
	Rock rock;
	Random rand;
	int touchedFrames;
	int familiarHitFrames;
	long damageToDo;
	long damageReduction;
	
	boolean inUpgradeScreen;
	boolean inOptions;
	int optionsStage = 1;
	int selectedUpgrade = 1;
	float timeSinceLastFamiliarAttack = 0;
	float timeAtLastFamiliarAttack = 0;
	float timeSinceLastGoldGain = 0;
	float timeAtLastGoldGain = 0;
	
	MinerRenderer renderer;
	
	public MinerScreen(Game game) {
		super(game);
		Wallet.setUpWallet(game.getFileIO());
		guiCam = new Camera2D(glGraphics, 320, 480);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 1000);
		getNewRock();
		rockBounds = new Rectangle(160 - 100, 240 - 100, 200, 200);
		leftBounds = new Rectangle(20 - 16, 20 - 16, 32, 32);
		rightBounds = new Rectangle(300 - 16, 20 - 16, 32, 32);
		upgradeButtonBounds = new Rectangle(300 - 16, 460 - 16, 32, 32);
		rand = new Random();
		renderer = new MinerRenderer();
		
		upgradeBounds = new Rectangle[12];
		int x, y;
		x = 49;
		y = 400;
		for (int i = 0; i < upgradeBounds.length; i++) {
			upgradeBounds[i] = new Rectangle(x - 32, y - 32, 64, 64);
			if (i == 3 || i == 7 || i == 11) {
				y -= 74;
				x = 49;
			} else
				x += 74;
		}
	}

	@Override
	public void update(float deltaTime) {
		checkTimes(deltaTime);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type != TouchEvent.TOUCH_UP)
				continue;
			
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			if (!inUpgradeScreen && !inOptions) {
				if (OverlapTester.pointInRectangle(rockBounds, touchPoint)) {
					hitRock();
				}
				
				if (OverlapTester.pointInRectangle(leftBounds, touchPoint)) {
					if (Wallet.currentRock > 0) {
						--Wallet.currentRock;
						getNewRock();
						Log.d("MinerScreen", rock.name + " just got created, current hp now at " + rock.health);
					}
				}
				
				if (OverlapTester.pointInRectangle(rightBounds, touchPoint)) {
					if (Wallet.currentRock < NUMBER_OF_ROCKS - 1) {
						if (Wallet.defeatedRocks[Wallet.currentRock]) {
							++Wallet.currentRock;
							getNewRock();
							Log.d("MinerScreen", rock.name + " just got created, current hp now at " + rock.health);
						}
					}
				}
				
				if (OverlapTester.pointInRectangle(upgradeButtonBounds, touchPoint)) {
					inUpgradeScreen = true;
				}
			} else if (inOptions) {
				if (OverlapTester.pointInRectangle(MinerRenderer.deleteBounds, touchPoint)) {
					if (optionsStage < 2) {
						++optionsStage;
					} else {
						Wallet.deleteAndStartOver(game.getFileIO());
						getNewRock();
						inOptions = false;
						inUpgradeScreen = false;
					}
				} else {
					inOptions = false;
				}
			} else {
				if (OverlapTester.pointInRectangle(MinerRenderer.exitBounds, touchPoint)) {
					inUpgradeScreen = false;
				}
				if (OverlapTester.pointInRectangle(MinerRenderer.buyBounds, touchPoint)) {
					Wallet.buySelection(selectedUpgrade);
					Wallet.save(game.getFileIO());
				}
				for (int x = 0; x < upgradeBounds.length; x++) {
					if (OverlapTester.pointInRectangle(upgradeBounds[x], touchPoint))
						selectedUpgrade = x + 1;
				}
				if (OverlapTester.pointInRectangle(MinerRenderer.optionsBOunds, touchPoint)) {
					inOptions = true;
					optionsStage = 1;
				}
			}
		}
	}
	
	public void checkTimes(float deltaTime) {
		timeSinceLastFamiliarAttack = ((System.nanoTime() / 1000000000.0f) - timeAtLastFamiliarAttack) + deltaTime; // in seconds
		if (timeSinceLastFamiliarAttack > Wallet.familiarSpeed.speed) {	
			familiarAttack();
			timeSinceLastFamiliarAttack = 0f;
			timeAtLastFamiliarAttack = (System.nanoTime() / 1000000000.0f) + deltaTime;
		}
		
		timeSinceLastGoldGain = ((System.nanoTime() / 1000000000.0f) - timeAtLastGoldGain) + deltaTime; // in seconds
		if (timeSinceLastGoldGain > 1.0f) {
			Wallet.coins += (Wallet.magneticFamiliars.goldPerSecond * Wallet.addFamiliar.numberOfFamiliars);
			timeSinceLastGoldGain = 0f;
			timeAtLastGoldGain = (System.nanoTime() / 1000000000.0f) + deltaTime;
		}
	}
	
	public void familiarAttack() {
		if (Wallet.addFamiliar.level <= 0)
			return;
		familiarHitFrames = 5;
		
		damageToDo = (long)(Wallet.familiarDamage.damage * Wallet.addFamiliar.numberOfFamiliars);
		if (Wallet.armorPierce.percentPierce != 0f)
			damageReduction = (long)(rock.armor - (rock.armor * (Wallet.armorPierce.percentPierce * 0.01)));
		else
			damageReduction = rock.armor;
		
		if (damageToDo > damageReduction)
			rock.health -= damageToDo - damageReduction;
		
		Log.d("Familiar", Wallet.addFamiliar.numberOfFamiliars + " familiars hit " + rock.name + " for " + (damageToDo - damageReduction) + " damage");
		
		if ((float)rock.health / (float)rock.initialHealth < 0.7) {
			if ((float)rock.health / (float)rock.initialHealth < 0.3)
				rock.breakLevel = 2;
			else
				rock.breakLevel = 1;
		}
		
		if (rock.health <= 0)
			killRock();
	}
	
	public void hitRock() {
		if (Wallet.luckyHit.percentChance > (rand.nextDouble() * 100)) {
			damageToDo = (long)(Wallet.pick.damage * Wallet.luckyHit.damagePercent);
			Log.d("MinerScreen", "Lucky Hit, " + Wallet.luckyHit.damagePercent + "% for " + damageToDo);
		} else
			damageToDo = Wallet.pick.damage;
		
		if (Wallet.sharpenedPick.level > 0) {
			damageToDo += (rock.health * (Wallet.sharpenedPick.percentDamage * 0.01));
			Log.d("MinerScreen", "Adding " + (rock.health * (Wallet.sharpenedPick.percentDamage * 0.01)) + " to make " + damageToDo);
		}
		
		if (Wallet.armorPierce.percentPierce != 0f)
			damageReduction = (long)(rock.armor - (rock.armor * (Wallet.armorPierce.percentPierce * 0.01)));
		else
			damageReduction = rock.armor;
		if (damageToDo > damageReduction)
			rock.health -= damageToDo - damageReduction;
		
		if ((float)rock.health / (float)rock.initialHealth < 0.7) {
			if ((float)rock.health / (float)rock.initialHealth < 0.3)
				rock.breakLevel = 2;
			else
				rock.breakLevel = 1;
		}
		touchedFrames = 5;
		if (rock.health <= 0)
			killRock();
		else
			if (Wallet.armorChip.chance > (rand.nextDouble() * 100)) {
				rock.armor -= Wallet.armorChip.chip;
				if (rock.armor < 0)
					rock.armor = 0;
			}
	}
	
	public void killRock() {
		if (Wallet.mineralMerchant.percentChance > (rand.nextDouble() * 100)) 
			Wallet.coins += (rock.worth * Wallet.mineralMerchant.percentGained);
		else {
			Wallet.coins += rock.worth;
		}
		Log.d("MinerScreen", rock.name + " killed, gold now at " + Wallet.coins);
		
		if (!Wallet.defeatedRocks[Wallet.currentRock]) {
			Wallet.defeatedRocks[Wallet.currentRock] = true;
			if (Wallet.currentRock < NUMBER_OF_ROCKS - 1)
				++Wallet.currentRock;
		}
		getNewRock();
		Log.d("MinerScreen", rock.name + " just got created, current hp now at " + rock.health);
	}
	
	private void getNewRock() {
		Rock rock;
		switch (Wallet.currentRock) {
		case 0:
			rock = new Rock("Rock", 10, 100, 0);
			break;
		case 1:
			rock = new Rock("Rock2", 20, 500, 0);
			break;
		case 2:
			rock = new Rock("Rock3", 30, 1050, 1);
			break;
		case 3:
			rock = new Rock("Rock4", 50, 2000, 3);
			break;
		case 4:
			rock = new Rock("Rock5", 100, 3500, 5);
			break;
		case 5:
			rock = new Rock("Rock6", 150, 5500, 10);
			break;
		case 6:
			rock = new Rock("Rock7", 300, 7500, 15);
			break;
		case 7:
			rock = new Rock("Rock8", 600, 10000, 0);
			break;
		case 8:
			rock = new Rock("Rock9", 500, 12000, 30);
			break;
		case 9:
			rock = new Rock("Rock10", 30, 25000, 100);
			break;
		case 10:
			rock = new Rock("Rock11", 1000, 15000, 35);
			break;
		case 11:
			rock = new Rock("Rock12", 1250, 25000, 35);
			break;
		case 12:
			rock = new Rock("Rock13", 1500, 35000, 40);
			break;
		case 13:
			rock = new Rock("Rock14", 2000, 50000, 45);
			break;
		case 14:
			rock = new Rock("Rock15", 10000, 70500, 0);
			break;
		case 15:
			rock = new Rock("Rock16", 5000, 60000, 50);
			break;
		case 16:
			rock = new Rock("Rock17", 7000, 70000, 55);
			break;
		case 17:
			rock = new Rock("Rock18", 8500, 83000, 55);
			break;
		case 18:
			rock = new Rock("Rock19", 10000, 100000, 60);
			break;
		case 19:
			rock = new Rock("Rock20", 13000, 98550, 65);
			break;
		case 20:
			rock = new Rock("Rock21", 15000, 108000, 70);
			break;
		case 21:
			rock = new Rock("Rock22", 20000, 135000, 70);
			break;
		case 22:
			rock = new Rock("Rock23", 25000, 148000, 75);
			break;
		case 23:
			rock = new Rock("Rock24", 30000, 159500, 80);
			break;
		case 24:
			rock = new Rock("Rock25", 38000, 172000, 85);
			break;
		case 25:
			rock = new Rock("Rock26", 100000, 1000000, 0);
			break;
		case 26:
			rock = new Rock("Rock27", 50000, 400000, 90);
			break;
		case 27:
			rock = new Rock("Rock28", 65000, 570000, 90);
			break;
		case 28:
			rock = new Rock("Rock29", 80000, 750000, 95);
			break;
		case 29:
			rock = new Rock("Rock30", 100000, 1700000, 100);
			break;
		case 30:
			rock = new Rock("Rock31", 123000, 5000000, 100);
			break;
		case 31:
			rock = new Rock("Rock32", 240000, 6500000, 130);
			break;
		case 32:
			rock = new Rock("Rock33", 350000, 7500000, 175);
			break;
		case 33:
			rock = new Rock("Rock34", 465000, 9750000, 250);
			break;
		case 34:
			rock = new Rock("Rock35", 588000, 12000000, 375);
			break;
		case 35:
			rock = new Rock("Rock36", 600000, 18000000, 420);
			break;
		case 36:
			rock = new Rock("Rock37", 740000, 23000000, 550);
			break;
		case 37:
			rock = new Rock("Rock38", 800000, 50000000, 780);
			break;
		case 38:
			rock = new Rock("Rock39", 950000, 75000000, 895);
			break;
		case 39:
			rock = new Rock("Rock40", 1000000, 100000000, 1000);
			break;
		case 40:
			rock = new Rock("Rock41", 1500000, 150000000, 1500);
			break;
		case 41:
			rock = new Rock("Rock42", 1750000, 175500000, 1700);
			break;
		case 42:
			rock = new Rock("Rock43", 2800000, 175000000, 2000);
			break;
		case 43:
			rock = new Rock("Rock44", 4250000, 200000000, 2100);
			break;
		case 44:
			rock = new Rock("Rock45", 6500000, 350000000, 3500);
			break;
		case 45:
			rock = new Rock("Rock46", 8000000, 400000000, 4000);
			break;
		case 46:
			rock = new Rock("Rock47", 9500000, 450000000, 4500);
			break;
		case 47:
			rock = new Rock("Rock48", 12000000, 600000000, 6000);
			break;
		case 48:
			rock = new Rock("Rock49", 14500000, 750000000, 7500);
			break;
		case 49:
			rock = new Rock("rock50", 20000000, 1000000000, 10000);
			break;
		default:
			rock = new Rock("Rock", 10, 100, 0);
			break;
		}
		
		this.rock = rock;
		getNewBackground();
		Wallet.save(game.getFileIO());
	}
	
	private void getNewBackground() {
		if (Wallet.currentRock < 5) {
			Wallet.currentBackground = 0;
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		guiCam.setViewportAndMatrices();
		
		batcher.beginBatch(Assets.background);
		renderer.drawBackground(batcher);	// first 2 args are the center
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		
		renderer.drawRock(batcher, rock.breakLevel);
		
		if (Wallet.addFamiliar.level > 0) {
			if (familiarHitFrames > 0) {
				--familiarHitFrames;
				renderer.drawFamiliarsHit(batcher);
			} else
				renderer.drawFamiliars(batcher);
		}
		
		if (touchedFrames > 0) {
			renderer.drawPickHit(batcher);
			touchedFrames -= 1;
		} else
			renderer.drawPick(batcher);
	
		if (!inUpgradeScreen)
			renderer.drawButtons(batcher);
		
		batcher.endBatch();
		
		if (inUpgradeScreen) {
			batcher.beginBatch(Assets.upgrades);
			renderer.drawUpgradeScreen(batcher, selectedUpgrade, Wallet.canBuy(selectedUpgrade));
			batcher.endBatch();
		}
		
		batcher.beginBatch(Assets.droidmono11);
		if (inUpgradeScreen) {
			renderer.drawUpgradeText(batcher, selectedUpgrade);
		} else
			renderer.drawGameText(batcher, rock);
		batcher.endBatch();
		
		if (inOptions) {
			batcher.beginBatch(Assets.upgrades);
			renderer.drawOptions(batcher);
			batcher.endBatch();
			
			batcher.beginBatch(Assets.droidmono11);
			renderer.drawOptionsText(batcher, optionsStage);
			batcher.endBatch();
		}
		
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
