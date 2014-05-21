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
	
	public static final int NUMBER_OF_ROCKS = 30;
	
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
		Log.d("MinerScreen", "% health: " + ((float)rock.health / (float)rock.initialHealth) + " break level: " + rock.breakLevel);
		touchedFrames = 5;
		Log.d("MinerScreen", rock.name + " just lost health, current hp now at " + rock.health);
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
			rock = new Rock("Rock2", 20, 500, 1);
			break;
		case 2:
			rock = new Rock("Rock3", 30, 750, 3);
			break;
		case 3:
			rock = new Rock("Rock4", 50, 1500, 3);
			break;
		case 4:
			rock = new Rock("Rock5", 100, 2500, 5);
			break;
		case 5:
			rock = new Rock("Rock6", 150, 3500, 10);
			break;
		case 6:
			rock = new Rock("Rock7", 300, 5500, 15);
			break;
		case 7:
			rock = new Rock("Rock8", 600, 7000, 0);
			break;
		case 8:
			rock = new Rock("Rock9", 500, 9000, 30);
			break;
		case 9:
			rock = new Rock("Rock10", 1, 25000, 100);
			break;
		case 10:
			rock = new Rock("Rock11", 1000, 10000, 35);
			break;
		case 11:
			rock = new Rock("Rock12", 1250, 15000, 35);
			break;
		case 12:
			rock = new Rock("Rock13", 1500, 20000, 40);
			break;
		case 13:
			rock = new Rock("Rock14", 2000, 23000, 45);
			break;
		case 14:
			rock = new Rock("Rock15", 10000, 37500, 0);
			break;
		case 15:
			rock = new Rock("Rock16", 5000, 26000, 50);
			break;
		case 16:
			rock = new Rock("Rock17", 7000, 30000, 55);
			break;
		case 17:
			rock = new Rock("Rock18", 8500, 33000, 55);
			break;
		case 18:
			rock = new Rock("Rock19", 10000, 40000, 60);
			break;
		case 19:
			rock = new Rock("Rock20", 13000, 48550, 65);
			break;
		case 20:
			rock = new Rock("Rock21", 15000, 58000, 70);
			break;
		case 21:
			rock = new Rock("Rock22", 20000, 75000, 70);
			break;
		case 22:
			rock = new Rock("Rock23", 25000, 80000, 75);
			break;
		case 23:
			rock = new Rock("Rock24", 30000, 95000, 80);
			break;
		case 24:
			rock = new Rock("Rock25", 38000, 110000, 85);
			break;
		case 25:
			rock = new Rock("Rock26", 100000, 450000, 0);
			break;
		case 26:
			rock = new Rock("Rock27", 50000, 200000, 90);
			break;
		case 27:
			rock = new Rock("Rock28", 65000, 130000, 90);
			break;
		case 28:
			rock = new Rock("Rock29", 80000, 250000, 95);
			break;
		case 29:
			rock = new Rock("Rock30", 100000, 350000, 100);
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
