package com.simulation;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RoadIntersectionSocket extends Entity {
	float x, y, width, height;
	public RoadIntersectionSocket(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(x, y, width, height);
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
}
