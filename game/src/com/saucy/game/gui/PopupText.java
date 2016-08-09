package com.saucy.game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.rendering.Renderable;
import com.saucy.game.shop.ShopFont;

public class PopupText implements Renderable {
	
	private String text;
	private ShopFont font;
	private Vector2 center;
	
	public PopupText(String text, Vector2 center) {
		this.center = center;
		this.text = text;
		this.font = new ShopFont();
		font.setScale(.75f);
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		String[] lines = text.split("\n");
		for (int i = 0; i != lines.length; i ++)
			font.draw(batch, lines[i], new Vector2(center.x, center.y - i * 100));
	}
}