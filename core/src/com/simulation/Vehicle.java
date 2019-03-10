package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Marie
 *
 */
public class Vehicle {
	private Path currentPath;
	private float x, y;
	private float width, height;
	private float speed;
	private float cameraX, cameraY, cameraWidth, cameraHeight;
	private int vehicleId;
	private boolean finished;
	private float maxSpeed;
	private float leftLateralCameraX, leftLateralCameraY, leftLateralCameraWidth, leftLateralCameraHeight;
	private float rightLateralCameraX, rightLateralCameraY, rightLateralCameraWidth, rightLateralCameraHeight;
	private float longRangeCameraX, longRangeCameraY, longRangeCameraWidth, longRangeCameraHeight;
	private int state;
	private float timeSinceDirectionChanged;
	private float idleTime, currentIdleTimer;
	private float timerToRemoveFromMarkedList;
	private float aliveTime;
	private boolean onlySameDirection;
	
	//state = 0: stopped
	//state = -1: braking
	//state = 1: accelerating;
	
	public Vehicle(float x, float y, float width, float height, int vehicleId) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//speed = 1;
		this.vehicleId = vehicleId;
		timeSinceDirectionChanged = 0f;
		idleTime = 0.5f;
		onlySameDirection = false;
	}
	
	public void brake() {
		if(speed > 0.45f) {
			speed -= 0.1f;
		} else {
			speed -= 0.02f;
		}
			
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
		/*
		shapeRenderer.setColor(1, 0, 1, 1);
		shapeRenderer.rect(leftLateralCameraX, leftLateralCameraY, leftLateralCameraWidth, leftLateralCameraHeight);
		shapeRenderer.rect(rightLateralCameraX, rightLateralCameraY, rightLateralCameraWidth, rightLateralCameraHeight);
		shapeRenderer.setColor(0, 0.5f, 1, 1);
		shapeRenderer.rect(longRangeCameraX, longRangeCameraY, longRangeCameraWidth, longRangeCameraHeight);
		*/
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
		speed = maxSpeed;
	}

	public void update() {
		aliveTime += Gdx.graphics.getDeltaTime();
		timeSinceDirectionChanged += Gdx.graphics.getDeltaTime();
		currentIdleTimer += Gdx.graphics.getDeltaTime();
		timerToRemoveFromMarkedList += Gdx.graphics.getDeltaTime();
	}
	
	int previousDirection;
	int lateralCameraWidth = 29;
	int lateralCameraRange = 30;
	int lateralCameraOffset = 0;
	int frontalCameraRange = 30;
	
	int i;
	
	public void setX(float x) {
		if(x > this.x) {
			cameraX = x+width;
			cameraY = y;
			cameraWidth = frontalCameraRange;
			cameraHeight = height;
			longRangeCameraX = x+width;
			longRangeCameraY = y+10;
			longRangeCameraWidth = 115;
			longRangeCameraHeight = 10;
			leftLateralCameraX = x+width-lateralCameraOffset;
			leftLateralCameraY = y+height;
			leftLateralCameraWidth = lateralCameraRange;
			leftLateralCameraHeight = lateralCameraWidth;
			rightLateralCameraX = x+width-lateralCameraOffset;
			rightLateralCameraY = y-lateralCameraWidth;
			rightLateralCameraWidth = lateralCameraRange;
			rightLateralCameraHeight = lateralCameraWidth;
		} else if(x < this.x){
			cameraX = x-frontalCameraRange;
			cameraY = y;
			cameraWidth = frontalCameraRange;
			cameraHeight = height;
			leftLateralCameraX = x-lateralCameraRange+lateralCameraOffset;
			leftLateralCameraY = y+height;
			leftLateralCameraWidth = lateralCameraRange;
			leftLateralCameraHeight = lateralCameraWidth;
			rightLateralCameraX = x-lateralCameraRange+lateralCameraOffset;
			rightLateralCameraY = y-lateralCameraWidth;
			rightLateralCameraWidth = lateralCameraRange;
			rightLateralCameraHeight = lateralCameraWidth;
		}
		if(currentPath!= null) {
			if(currentPath.getDirection() == 2 && x > this.x && x > currentPath.getTarget().getX()) {
				this.x = currentPath.getTarget().getX();
				if(currentPath.getTarget().getStartingPathList().size() > 0) {
					previousDirection = currentPath.getDirection();
					if(onlySameDirection) {
						for(i = 0; i < currentPath.getTarget().getStartingPathList().size(); i++) {
							if(currentPath.getTarget().getStartingPathList().get(i).getDirection() == 2) {
								currentPath = currentPath.getTarget().getStartingPathList().get(i);
								break;
							}
						}
					} else {
						currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
					}
					// Reset speed during curve to give more realism
					if(currentPath.getDirection() != previousDirection) {
						speed = 0.2f + (float)(Math.random() * 0.3f);
						timeSinceDirectionChanged = 0f;
					}
				} else {
					finished = true;
					currentPath = null;
				}
			} else if(currentPath.getDirection() == 0 && x < this.x && x < currentPath.getTarget().getX()) {
				this.x = currentPath.getTarget().getX();
				if(currentPath.getTarget().getStartingPathList().size() > 0) {
					previousDirection = currentPath.getDirection();
					if(onlySameDirection) {
						for(i = 0; i < currentPath.getTarget().getStartingPathList().size(); i++) {
							if(currentPath.getTarget().getStartingPathList().get(i).getDirection() == 0) {
								currentPath = currentPath.getTarget().getStartingPathList().get(i);
								break;
							}
						}
						//currentPath = currentPath.getTarget().getStartingPathList().get(0);
					} else {
						currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
					}
					// Reset speed during curve to give more realism
					if(currentPath.getDirection() != previousDirection) {
						speed = 0.2f + (float)(Math.random() * 0.3f);
						timeSinceDirectionChanged = 0f;
					}
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
			cameraHeight = frontalCameraRange;
			leftLateralCameraX = x-lateralCameraWidth;
			leftLateralCameraY = y+height-lateralCameraOffset;
			leftLateralCameraWidth = lateralCameraWidth;
			leftLateralCameraHeight = lateralCameraRange;
			rightLateralCameraX = x+width;
			rightLateralCameraY = y+height-lateralCameraOffset;
			rightLateralCameraWidth = lateralCameraWidth;
			rightLateralCameraHeight = lateralCameraRange;
		} else if(y < this.y){
			cameraX = x;
			cameraY = y-frontalCameraRange;
			cameraWidth = width;
			cameraHeight = frontalCameraRange;
			leftLateralCameraX = x-lateralCameraWidth;
			leftLateralCameraY = y-lateralCameraRange+lateralCameraOffset;
			leftLateralCameraWidth = lateralCameraWidth;
			leftLateralCameraHeight = lateralCameraRange;
			rightLateralCameraX = x+width;
			rightLateralCameraY = y-lateralCameraRange+lateralCameraOffset;
			rightLateralCameraWidth = lateralCameraWidth;
			rightLateralCameraHeight = lateralCameraRange;
		}
		if(currentPath.getDirection() == 3 && y > this.y && y > currentPath.getTarget().getY()) {
			this.y = currentPath.getTarget().getY();
			if(currentPath.getTarget().getStartingPathList().size() > 0) {
				previousDirection = currentPath.getDirection();
				if(onlySameDirection) {
					for(i = 0; i < currentPath.getTarget().getStartingPathList().size(); i++) {
						if(currentPath.getTarget().getStartingPathList().get(i).getDirection() == previousDirection) {
							currentPath = currentPath.getTarget().getStartingPathList().get(i);
							break;
						}
					}
				} else {
					currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
				}
				// Reset speed during curve to give more realism
				if(currentPath.getDirection() != previousDirection) {
					speed = 0.2f + (float)(Math.random() * 0.3f);
					timeSinceDirectionChanged = 0f;
				}
			} else {
				currentPath = null;
				finished = true;
			}
		} else if(currentPath.getDirection() == 1 && y < this.y && y < currentPath.getTarget().getY()) {
			this.y = currentPath.getTarget().getY();
			if(currentPath.getTarget().getStartingPathList().size() > 0) {
				previousDirection = currentPath.getDirection();
				if(onlySameDirection) {
					for(i = 0; i < currentPath.getTarget().getStartingPathList().size(); i++) {
						if(currentPath.getTarget().getStartingPathList().get(i).getDirection() == previousDirection) {
							currentPath = currentPath.getTarget().getStartingPathList().get(i);
							break;
						}
					}
				} else {
					currentPath = currentPath.getTarget().getStartingPathList().get((int)(Math.random() * currentPath.getTarget().getStartingPathList().size()));
				}
				// Reset speed during curve to give more realism
				if(currentPath.getDirection() != previousDirection) {
					speed = 0.2f + (float)(Math.random() * 0.3f);
					timeSinceDirectionChanged = 0f;
				}
			} else {
				currentPath = null;
				finished = true;
			}
		} else {
			this.y = y;
		}
	}
	
	public float getAliveTime() {
		return aliveTime;
	}

	public void setAliveTime(float aliveTime) {
		this.aliveTime = aliveTime;
	}

	public float getLongRangeCameraX() {
		return longRangeCameraX;
	}

	public void setLongRangeCameraX(float longRangeCameraX) {
		this.longRangeCameraX = longRangeCameraX;
	}

	public float getLongRangeCameraY() {
		return longRangeCameraY;
	}

	public void setLongRangeCameraY(float longRangeCameraY) {
		this.longRangeCameraY = longRangeCameraY;
	}

	public float getLongRangeCameraWidth() {
		return longRangeCameraWidth;
	}

	public void setLongRangeCameraWidth(float longRangeCameraWidth) {
		this.longRangeCameraWidth = longRangeCameraWidth;
	}

	public float getLongRangeCameraHeight() {
		return longRangeCameraHeight;
	}

	public void setLongRangeCameraHeight(float longRangeCameraHeight) {
		this.longRangeCameraHeight = longRangeCameraHeight;
	}

	public boolean isOnlySameDirection() {
		return onlySameDirection;
	}

	public void setOnlySameDirection(boolean onlySameDirection) {
		this.onlySameDirection = onlySameDirection;
	}

	public float getTimerToRemoveFromMarkedList() {
		return timerToRemoveFromMarkedList;
	}

	public void setTimerToRemoveFromMarkedList(float timerToRemoveFromMarkedList) {
		this.timerToRemoveFromMarkedList = timerToRemoveFromMarkedList;
	}

	public float getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(float idleTime) {
		this.idleTime = idleTime;
	}

	public float getCurrentIdleTimer() {
		return currentIdleTimer;
	}

	public void setCurrentIdleTimer(float currentIdleTimer) {
		this.currentIdleTimer = currentIdleTimer;
	}

	public float getTimeSinceDirectionChanged() {
		return timeSinceDirectionChanged;
	}

	public void setTimeSinceDirectionChanged(float timeSinceDirectionChanged) {
		this.timeSinceDirectionChanged = timeSinceDirectionChanged;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public float getLeftLateralCameraX() {
		return leftLateralCameraX;
	}

	public void setLeftLateralCameraX(float leftLateralCameraX) {
		this.leftLateralCameraX = leftLateralCameraX;
	}

	public float getLeftLateralCameraY() {
		return leftLateralCameraY;
	}

	public void setLeftLateralCameraY(float leftLateralCameraY) {
		this.leftLateralCameraY = leftLateralCameraY;
	}

	public float getLeftLateralCameraWidth() {
		return leftLateralCameraWidth;
	}

	public void setLeftLateralCameraWidth(float leftLateralCameraWidth) {
		this.leftLateralCameraWidth = leftLateralCameraWidth;
	}

	public float getLeftLateralCameraHeight() {
		return leftLateralCameraHeight;
	}

	public void setLeftLateralCameraHeight(float leftLateralCameraHeight) {
		this.leftLateralCameraHeight = leftLateralCameraHeight;
	}

	public float getRightLateralCameraX() {
		return rightLateralCameraX;
	}

	public void setRightLateralCameraX(float rightLateralCameraX) {
		this.rightLateralCameraX = rightLateralCameraX;
	}

	public float getRightLateralCameraY() {
		return rightLateralCameraY;
	}

	public void setRightLateralCameraY(float rightLateralCameraY) {
		this.rightLateralCameraY = rightLateralCameraY;
	}

	public float getRightLateralCameraWidth() {
		return rightLateralCameraWidth;
	}

	public void setRightLateralCameraWidth(float rightLateralCameraWidth) {
		this.rightLateralCameraWidth = rightLateralCameraWidth;
	}

	public float getRightLateralCameraHeight() {
		return rightLateralCameraHeight;
	}

	public void setRightLateralCameraHeight(float rightLateralCameraHeight) {
		this.rightLateralCameraHeight = rightLateralCameraHeight;
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
