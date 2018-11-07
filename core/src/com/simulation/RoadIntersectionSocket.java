package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RoadIntersectionSocket extends Entity {
	Intersection intersection;
	float x, y, width, height;
	int currentColor = -1;
	int mode;
	float config[] = null;
	float timer;
	
	public RoadIntersectionSocket(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		timer = 0f;
	}
	
	//0 - Green
	//1 - Yellow
	//2 - Red
	
	public void draw(ShapeRenderer shapeRenderer) {
		if(currentColor == 0) {
			shapeRenderer.setColor(0, 1, 0, 1);
		} else if(currentColor == 1) {
			shapeRenderer.setColor(1, 195f/255f, 11f/255f, 1);
		} else if(currentColor == 2) {
			shapeRenderer.setColor(1, 0, 0, 1);
		} else {
			shapeRenderer.setColor(0, 0, 0, 1);
		}
		
		shapeRenderer.rect(x, y, width, height);
	}
	
	public void update() {
		if(currentColor != -1) {
			timer += Gdx.graphics.getDeltaTime();
			if(timer >= config[currentColor]) {
				timer = 0;
				currentColor++;
				if(currentColor == 3) {
					currentColor = 0;
				}
			}
		}
		
	}
	public Intersection getIntersection() {
		return intersection;
	}

	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
	}
	public float getTimer() {
		return timer;
	}
	public void setTimer(float timer) {
		this.timer = timer;
	}
	public int getCurrentColor() {
		return currentColor;
	}
	public void setCurrentColor(int currentColor) {
		this.currentColor = currentColor;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public float[] getConfig() {
		return config;
	}
	public void setConfig(float[] config) {
		this.config = config;
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
