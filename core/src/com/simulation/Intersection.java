package com.simulation;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Intersection extends Entity implements Cloneable {
	private int i, j;
	private float x, y, width, height;
	private int trafficLights[];
	private float hitboxX, hitboxY, hitboxWidth, hitboxHeight;
	ArrayList<PathPoint> pathPointList;
	
	private float trafficLightsConfig[];
	private int mode;
	private float timer;
	private float yellowLightStartTime = 3.5f;
	
	private int lines, columns;
	
	private RoadIntersectionSocket[] westSockets, eastSockets, southSockets, northSockets;
	
	//0 = red, 1 = yellow, 2 = green, -1 = no semaphore
	
	//public Intersection(float x, float y, int trafficLights[], int mode, float trafficLightsConfig[], int lines, int columns) {
	public Intersection(float x, float y, int lines, int columns) {
		super(x, y, 30*columns, 30*lines);
		this.lines = lines;
		this.columns = columns;
		width = (30*columns);
		height = (30*lines);
		this.x = x;
		hitboxX = x+10;
		this.y = y;
		hitboxY = y+10;
		hitboxWidth = width-20;
		hitboxHeight = height-20;
		//this.trafficLights = trafficLights;
		//this.mode = mode;
		//this.trafficLightsConfig = trafficLightsConfig;
		timer = 0f;
		westSockets = new RoadIntersectionSocket[lines];
		eastSockets = new RoadIntersectionSocket[lines];
		southSockets = new RoadIntersectionSocket[columns];
		northSockets = new RoadIntersectionSocket[columns];
		for(i = 0; i < lines; i++) {
			westSockets[i] = new RoadIntersectionSocket(x-1, y+(30*i), 2, 30);
			eastSockets[i] = new RoadIntersectionSocket(x+width-1, y+(30*i), 2, 30);
		}
		for(i = 0; i < columns; i++) {
			southSockets[i] = new RoadIntersectionSocket(x+(30*i), y-1, 30, 2);
			northSockets[i] = new RoadIntersectionSocket(x+(30*i), y+height-1, 30, 2);
		}
		pathPointList = new ArrayList<PathPoint>();
		for(i = 0; i < lines; i++) {
			for(j = 0; j < columns; j++) {
				pathPointList.add(new PathPoint(x+(j*30), y+(i*30)));
			}
		}
	}
	
	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.rect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
		for(i = 0; i < lines; i++) {
			westSockets[i].draw(shapeRenderer);
			eastSockets[i].draw(shapeRenderer);
		}
		for(i = 0; i < columns; i++) {
			southSockets[i].draw(shapeRenderer);
			northSockets[i].draw(shapeRenderer);
		}
	}
	/*
	public void drawTrafficLightsTimer(SpriteBatch spriteBatch, BitmapFont font) {
		Core.glyphLayout.setText(font, String.format("%.1f", trafficLightsConfig[0]-timer));
		font.draw(spriteBatch, String.format("%.1f", trafficLightsConfig[0]-timer), x-(Core.glyphLayout.width+5), y-Core.glyphLayout.height);
	}
	
	public void updateTrafficLights(ShapeRenderer shapeRenderer, float delta) {
		timer += delta;
		if(mode == 0) { //mode = 0: Simple round-robin for 2 semaphores; config[0] is the amount of the time the semaphore is green.
			if(timer >= yellowLightStartTime && timer < trafficLightsConfig[0]) {
				for(i = 0; i < trafficLights.length; i++) {
					if(trafficLights[i] == 2) {
						trafficLights[i] = 1;
					}
				}
			}
			if(timer >= trafficLightsConfig[0]) {
				for(i = 0; i < trafficLights.length; i++) {
					if(trafficLights[i] == 1) {
						trafficLights[i] = 0;
					} else if(trafficLights[i] == 0) {
						trafficLights[i] = 2;
					}
				}
				timer = 0f;
			}
			
		}
	}
	
	public void drawTrafficLights(ShapeRenderer shapeRenderer) {
		if(trafficLights != null) {
			if(trafficLights[0] != -1) { //left
				if(trafficLights[0] == 2) shapeRenderer.setColor(0, 1, 0, 0); 
				if(trafficLights[0] == 1) shapeRenderer.setColor(1, 1, 0, 0);
				if(trafficLights[0] == 0) shapeRenderer.setColor(1, 0, 0, 0);
				shapeRenderer.line(x, y, x, y+60); 
				shapeRenderer.line(x+1, y, x+1, y+60); 
			}
			if(trafficLights[1] != -1) { //down
				if(trafficLights[1] == 2) shapeRenderer.setColor(0, 1, 0, 0); 
				if(trafficLights[1] == 1) shapeRenderer.setColor(1, 1, 0, 0);
				if(trafficLights[1] == 0) shapeRenderer.setColor(1, 0, 0, 0);
				shapeRenderer.line(x, y, x+60, y);
				shapeRenderer.line(x, y+1, x+60, y+1);
			}
			if(trafficLights[2] != -1) { //right
				if(trafficLights[2] == 2) shapeRenderer.setColor(0, 1, 0, 0); 
				if(trafficLights[2] == 1) shapeRenderer.setColor(1, 1, 0, 0);
				if(trafficLights[2] == 0) shapeRenderer.setColor(1, 0, 0, 0);
				shapeRenderer.line(x+60, y, x+60, y+60);
				shapeRenderer.line(x+59, y, x+59, y+60);
			}
			if(trafficLights[3] != -1) { //up
				if(trafficLights[3] == 2) shapeRenderer.setColor(0, 1, 0, 0); 
				if(trafficLights[3] == 1) shapeRenderer.setColor(1, 1, 0, 0);
				if(trafficLights[3] == 0) shapeRenderer.setColor(1, 0, 0, 0);
				shapeRenderer.line(x, y+60, x+60, y+60);
				shapeRenderer.line(x, y+59, x+60, y+59);
			}
		}
	}
	*/
	
	public void drawPathPoints(ShapeRenderer shapeRenderer) {
		for(i=0; i<pathPointList.size(); i++) {
			pathPointList.get(i).draw(shapeRenderer);
		}
	}
	
	public Intersection clone() throws CloneNotSupportedException {
        return (Intersection) super.clone();
	}
	
	public ArrayList<PathPoint> getPathPointList() {
		return pathPointList;
	}

	public void setPathPointList(ArrayList<PathPoint> pathPointList) {
		this.pathPointList = pathPointList;
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
	}
	
	public float getHitboxX() {
		return hitboxX;
	}

	public void setHitboxX(float hitboxX) {
		this.hitboxX = hitboxX;
	}

	public float getHitboxY() {
		return hitboxY;
	}

	public void setHitboxY(float hitboxY) {
		this.hitboxY = hitboxY;
	}

	public float getHitboxWidth() {
		return hitboxWidth;
	}

	public void setHitboxWidth(float hitboxWidth) {
		this.hitboxWidth = hitboxWidth;
	}

	public float getHitboxHeight() {
		return hitboxHeight;
	}

	public void setHitboxHeight(float hitboxHeight) {
		this.hitboxHeight = hitboxHeight;
	}

	public int[] getTrafficLights() {
		return trafficLights;
	}

	public void setTrafficLights(int[] trafficLights) {
		this.trafficLights = trafficLights;
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
}
