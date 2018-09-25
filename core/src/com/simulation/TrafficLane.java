package com.simulation;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TrafficLane extends Entity implements Cloneable {
	private int i;
	private float x, y, width, height;
	private ArrayList<PathPoint> pathPointList;
	
	public TrafficLane(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		pathPointList = new ArrayList<PathPoint>();
		pathPointList.add(new PathPoint(x, y));
		if(height == 30) {
			pathPointList.add(new PathPoint(x+width-30, y));
		} else if(width == 30) {
			pathPointList.add(new PathPoint(x, y+height-30));
		}
	}
	
	public TrafficLane clone() throws CloneNotSupportedException {
        return (TrafficLane) super.clone();
	}
	
	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.rect(x, y, width, height);
	}
	
	public void drawPathPoints(ShapeRenderer shapeRenderer) {
		for(i=0; i<pathPointList.size(); i++) {
			pathPointList.get(i).draw(shapeRenderer);
		}
	}
	
	public ArrayList<PathPoint> getPathPointList() {
		return pathPointList;
	}

	public void setPathPointList(ArrayList<PathPoint> pathPointList) {
		this.pathPointList = pathPointList;
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
