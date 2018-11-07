package com.simulation;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Vehicle {
	private Path currentPath;
	private float x, y;
	private float width, height;
	private float speed;
	private float cameraX, cameraY, cameraWidth, cameraHeight;
	private int vehicleId;
	private boolean finished;
	private float maxSpeed;
	
	public Vehicle(float x, float y, float width, float height, int vehicleId) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//speed = 1;
		this.vehicleId = vehicleId;
	}
	
	public void brake() {
		speed -= 0.025f;
		if(speed < 0) speed = 0;
	}
	
	public void accelerate() {
		speed += 0.025f;
		if(speed > maxSpeed) speed = maxSpeed;
	}
	
	public float getSpeed() {
		if(speed > 1) return 1;
		if(speed < 0) return 0;
		return speed;
	}
	
	public void drawInfo(SpriteBatch spriteBatch, BitmapFont font) {
		Core.glyphLayout.setText(font, vehicleId+"");
		font.draw(spriteBatch, vehicleId+"", x+15-(Core.glyphLayout.width/2), y+Core.glyphLayout.height+8);
		Core.glyphLayout.setText(font, String.format("%.1f", speed));
		font.draw(spriteBatch, String.format("%.1f", speed), x+15-(Core.glyphLayout.width/2), y+Core.glyphLayout.height+5+height);
	}
	
	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.rect(x, y, width, height);
	}
	
	public void drawCamera(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(0.79f, 0.70f, 0.85f, 1);
		shapeRenderer.rect(cameraX, cameraY, cameraWidth, cameraHeight);
	}
	
	
	
	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
		speed = maxSpeed;
	}

	int previousDirection;
	
	public void setX(float x) {
		if(x > this.x) {
			cameraX = x+width;
			cameraY = y;
			cameraWidth = 40;
			cameraHeight = height;
		} else if(x < this.x){
			cameraX = x-40;
			cameraY = y;
			cameraWidth = 40;
			cameraHeight = height;
		}
		if(currentPath!= null) {
			if(currentPath.getDirection() == 2 && x > this.x && x > currentPath.getTarget().getX()) {
				this.x = currentPath.getTarget().getX();
				if(currentPath.getTarget().getStartingPathList().size() > 0) {
					previousDirection = currentPath.getDirection();
					currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
					// Reset speed during curve to give more realism
					if(currentPath.getDirection() != previousDirection) {
						speed = 0.2f + (float)(Math.random() * 0.3f);
					}
				} else {
					finished = true;
					currentPath = null;
				}
			} else if(currentPath.getDirection() == 0 && x < this.x && x < currentPath.getTarget().getX()) {
				this.x = currentPath.getTarget().getX();
				if(currentPath.getTarget().getStartingPathList().size() > 0) {
					currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
					// Reset speed during curve to give more realism
					speed = 0;
				} else {
					finished = true;
					currentPath = null;
				}
			} else {
				this.x = x;
			}
		}
	}
	
	public void setY(float y) {
		if(y > this.y) {
			cameraX = x;
			cameraY = y+height;
			cameraWidth = width;
			cameraHeight = 40;
		} else if(y < this.y){
			cameraX = x;
			cameraY = y-40;
			cameraWidth = width;
			cameraHeight = 40;
		}
		if(currentPath.getDirection() == 3 && y > this.y && y > currentPath.getTarget().getY()) {
			this.y = currentPath.getTarget().getY();
			if(currentPath.getTarget().getStartingPathList().size() > 0) {
				currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
				// Reset speed during curve to give more realism
				speed = 0;
			} else {
				currentPath = null;
				finished = true;
			}
		} else {
			this.y = y;
		}
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Path getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(Path currentPath) {
		this.currentPath = currentPath;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
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
	
	public float getCameraX() {
		return cameraX;
	}

	public void setCameraX(float cameraX) {
		this.cameraX = cameraX;
	}

	public float getCameraY() {
		return cameraY;
	}

	public void setCameraY(float cameraY) {
		this.cameraY = cameraY;
	}

	public float getCameraWidth() {
		return cameraWidth;
	}

	public void setCameraWidth(float cameraWidth) {
		this.cameraWidth = cameraWidth;
	}

	public float getCameraHeight() {
		return cameraHeight;
	}

	public void setCameraHeight(float cameraHeight) {
		this.cameraHeight = cameraHeight;
	}
}
