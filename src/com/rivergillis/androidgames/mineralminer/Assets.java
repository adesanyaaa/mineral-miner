package com.rivergillis.androidgames.mineralminer;

import com.rivergillis.androidgames.framework.gl.Font;
import com.rivergillis.androidgames.framework.gl.Texture;
import com.rivergillis.androidgames.framework.gl.TextureRegion;
import com.rivergillis.androidgames.framework.impl.GLGame;

/**
 * This class contains references to all of the Assets in mineral miner
 * @author jgillis
 *
 */
public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static Texture upgrades;
	public static TextureRegion upgradesRegion;

	public static Texture items;
	public static TextureRegion rock;
	public static TextureRegion rockd1;
	public static TextureRegion rockd2;
	public static TextureRegion rock2;
	public static TextureRegion rock2d1;
	public static TextureRegion rock2d2;
	
	public static TextureRegion pick;
	public static TextureRegion pickHit;
	public static TextureRegion pick2;
	public static TextureRegion pickHit2;
	
	public static TextureRegion leftButton;
	public static TextureRegion leftButtonDimmed;
	public static TextureRegion rightButton;
	public static TextureRegion rightButtonDimmed;
	
	public static TextureRegion upgradeSelection;
	public static TextureRegion buyButton;
	public static TextureRegion buyButtonDimmed;
	public static TextureRegion upgrade1;
	
	public static TextureRegion familiar1;
	public static TextureRegion familiarHit1;
	public static TextureRegion familiar2;
	public static TextureRegion familiarHit2;
	
	public static Texture droidmono11;
	public static Font font;
	
	
	public static void load(GLGame game) {
		background = new Texture(game, "background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);	// loads the background image and creates a region for it
		upgrades = new Texture(game, "upgrades.png");
		upgradesRegion = new TextureRegion(upgrades, 0, 0, 320, 480);
		
		items = new Texture(game, "items.png");		// loads the main 'items' atlas
		rock = new TextureRegion(items, 0, 0, 200, 200);
		rockd1 = new TextureRegion(items, 200, 0, 200, 200);
		rockd2 = new TextureRegion(items, 400, 0, 200, 200);
		rock2 = new TextureRegion(items, 0, 200, 200, 200);
		rock2d1 = new TextureRegion(items, 200, 200, 200, 200);
		rock2d2 = new TextureRegion(items, 400, 200, 200, 200);
		
		pick = new TextureRegion(items, 600, 0, 200, 200);
		pickHit = new TextureRegion(items, 800, 0, 200, 200);
		pick2 = new TextureRegion(items, 600, 200, 200, 200);
		pickHit2 = new TextureRegion(items, 800, 200, 200, 200);
		
		leftButton = new TextureRegion(items, 1018, 0, 32, 32);
		leftButtonDimmed = new TextureRegion(items, 1018, 32, 32, 32);
		rightButton = new TextureRegion(items, 1018, 64, 32, 32);
		rightButtonDimmed = new TextureRegion(items, 1018, 96, 32, 32);
		
		upgradeSelection = new TextureRegion(upgrades, 384, 0, 64, 64);
		buyButton = new TextureRegion(upgrades, 320, 64, 64, 32);
		buyButtonDimmed = new TextureRegion(upgrades, 384, 64, 64, 32);
		
		upgrade1 = new TextureRegion(upgrades, 320, 0, 64, 64);
		
		familiar1 = new TextureRegion(items, 1050, 0, 32, 32);
		familiarHit1 = new TextureRegion(items, 1050, 32, 32, 32);
		familiar2 = new TextureRegion(items, 1050, 64, 32, 32);
		familiarHit2 = new TextureRegion(items, 1050, 96, 32, 32);
		
		droidmono11 = new Texture(game, "droidmono11.png");
		font = new Font(droidmono11, 0, 0, 23, 11, 19);
		
	}
	
    // GLES context will get lost when the app is paused, we have to reload the textures when the app is resumed.
    public static void reload() {
        background.reload();
        items.reload();
        upgrades.reload();
        droidmono11.reload();
//        if(Settings.soundEnabled)
//            music.play();
    }
}
