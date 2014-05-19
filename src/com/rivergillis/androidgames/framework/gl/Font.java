package com.rivergillis.androidgames.framework.gl;

/**
 * This class allows us to draw text from a bitmap font file
 * The first element is ASCII code 32, the last is 126
 * @author jgillis
 *
 */
public class Font {
    public final Texture texture;	// contains the fonts glyph
    public final int glyphWidth;	
    public final int glyphHeight;	// contains the height and width of a single glyph
    public final TextureRegion[] glyphs = new TextureRegion[96];   // one texture region for each glyph

    public Font(Texture texture, 
            int offsetX, int offsetY,
            int glyphsPerRow, int glyphWidth, int glyphHeight) {        
        this.texture = texture;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        int x = offsetX;
        int y = offsetY;
        for(int i = 0; i < 96; i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);	// generate the texture regions
            x += glyphWidth;
            if(x == offsetX + glyphsPerRow * glyphWidth) {
                x = offsetX;
                y += glyphHeight;
            }
        }        
    }

    // draws a string of text to the screen
    public void drawText(SpriteBatcher batcher, String text, float x, float y) {
        int len = text.length();
        for(int i = 0; i < len; i++) {		// for each character in the string
            int c = text.charAt(i) - ' ';	// grab the value of the character minus the value of ' '
            if(c < 0 || c > glyphs.length - 1) // skip if the value is less than 0 or more than the array length
                continue;
            
            TextureRegion glyph = glyphs[c];	// grab the appropriate glyph
            batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);	// draw it
            x += glyphWidth;				// increment width by the glyph width
        }
    }
    
    // convenience method, calls drawText with an integer
    public void drawText(SpriteBatcher batcher, int text, float x, float y) {
    	drawText(batcher, Integer.toString(text), x, y);
    }
}
