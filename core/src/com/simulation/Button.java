package com.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Button extends Entity {
	private float x, y, width, height;
	private String text;
	
	public Button(float x, float y, float width, float height, String text) {
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}
	
	public void drawOutline(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x, y, width, height);
	}
	
	public void drawText(SpriteBatch spriteBatch, BitmapFont font) {
		Core.glyphLayout.setText(font, text);
		font.draw(spriteBatch, text, x-(Core.glyphLayout.width/2)+(width/2), y+(Core.glyphLayout.height/2)+(height/2));
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
