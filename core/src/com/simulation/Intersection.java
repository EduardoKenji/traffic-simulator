package com.simulation;

import java.util.ArrayList;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Intersection extends Entity implements Cloneable {
	private int i, j;
	private float x, y, width, height;
	private int trafficLights[];
	private float hitboxX, hitboxY, hitboxWidth, hitboxHeight;
	ArrayList<PathPoint> pathPointList;
	
	//private float trafficLightsConfig[];
	//private int mode;
	private float timer;
	//private float yellowLightStartTime = 3.5f;
	
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
			westSockets[i].setIntersection(this);
			eastSockets[i] = new RoadIntersectionSocket(x+width-1, y+(30*i), 2, 30);
			eastSockets[i].setIntersection(this);
		}
		for(i = 0; i < columns; i++) {
			southSockets[i] = new RoadIntersectionSocket(x+(30*i), y-1, 30, 2);
			southSockets[i].setIntersection(this);
			northSockets[i] = new RoadIntersectionSocket(x+(30*i), y+height-1, 30, 2);
			northSockets[i].setIntersection(this);
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
	
	public void update() {
		for(i = 0; i < lines; i++) {
			westSockets[i].update();
			eastSockets[i].update();
		}
		for(i = 0; i < columns; i++) {
			southSockets[i].update();
			northSockets[i].update();
		}
	}
	
	public void drawPathPoints(ShapeRenderer shapeRenderer) {
		for(i=0; i<pathPointList.size(); i++) {
			pathPointList.get(i).draw(shapeRenderer);
		}
	}
	
	public RoadIntersectionSocket[] getWestSockets() {
		return westSockets;
	}

	public void setWestSockets(RoadIntersectionSocket[] westSockets) {
		this.westSockets = westSockets;
	}

	public RoadIntersectionSocket[] getEastSockets() {
		return eastSockets;
	}

	public void setEastSockets(RoadIntersectionSocket[] eastSockets) {
		this.eastSockets = eastSockets;
	}

	public RoadIntersectionSocket[] getSouthSockets() {
		return southSockets;
	}

	public void setSouthSockets(RoadIntersectionSocket[] southSockets) {
		this.southSockets = southSockets;
	}

	public RoadIntersectionSocket[] getNorthSockets() {
		return northSockets;
	}

	public void setNorthSockets(RoadIntersectionSocket[] northSockets) {
		this.northSockets = northSockets;
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
