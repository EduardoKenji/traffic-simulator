package com.simulation;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Core extends ApplicationAdapter implements InputProcessor {
	
	public static GlyphLayout glyphLayout;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	Vehicle car, car2;
	ArrayList<Vehicle> vehicleList;
	ArrayList<TrafficLane> trafficLaneList;
	ArrayList<PathPoint> pathPointList;
	ArrayList<Intersection> intersectionList;
	ArrayList<Path> pathList;
	int i, j;
	int vehicleId;
	boolean vehicleHaveToBrake;
	BitmapFont blackFont, whiteFont;
	Button editButton;
	ArrayList<Button> buttonList;
	int editMode;
	float mouseX, mouseY;
	//Texture img;
	
	public Core() {
		
	}
	
	@Override
	public void create () {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		parameter.color = Color.BLACK;
 		blackFont = generator.generateFont(parameter); // font size 12 pixels
 		parameter.borderColor = Color.BLACK;
 		parameter.borderWidth = 1;
 		parameter.color = Color.WHITE;
 		parameter.size = 14;
 		whiteFont = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		glyphLayout = new GlyphLayout();
		
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		vehicleList = new ArrayList<Vehicle>();
		trafficLaneList = new ArrayList<TrafficLane>();
		pathPointList = new ArrayList<PathPoint>();
		intersectionList = new ArrayList<Intersection>();
		pathList = new ArrayList<Path>();
		buttonList = new ArrayList<Button>();
		
		TrafficLane trafficLaneList1 = new TrafficLane(200, 100, 300, 30);
		TrafficLane trafficLaneList2 = new TrafficLane(200, 130, 300, 30);
		trafficLaneList.add(trafficLaneList1);
		trafficLaneList.add(trafficLaneList2);
		trafficLaneList.add(new TrafficLane(500, 0, 30, 100));
		trafficLaneList.add(new TrafficLane(530, 0, 30, 100));
		trafficLaneList.add(new TrafficLane(560, 100, 200, 30));
		trafficLaneList.add(new TrafficLane(560, 130, 200, 30));
		trafficLaneList.add(new TrafficLane(500, 160, 30, 200));
		trafficLaneList.add(new TrafficLane(530, 160, 30, 200));
		
		car = new Vehicle(200, 100, 30, 30, vehicleId);
		vehicleId++;
		car2 = new Vehicle(300, 100, 30, 30, vehicleId);
		vehicleId++;
		vehicleList.add(car);
		vehicleList.add(car2);
		
		PathPoint westPathPoint1 = new PathPoint(200, 100);
		PathPoint westPathPoint2 = new PathPoint(200, 130);
		PathPoint northPathPoint1 = new PathPoint(500, 330);
		PathPoint northPathPoint2 = new PathPoint(530, 330);
		PathPoint southPathPoint1 = new PathPoint(500, 0);
		PathPoint southPathPoint2 = new PathPoint(530, 0);
		PathPoint eastPathPoint1 = new PathPoint(730, 100);
		PathPoint eastPathPoint2 = new PathPoint(730, 130);
		PathPoint intersectionPathPoint1 = new PathPoint(500, 100);
		PathPoint intersectionPathPoint2 = new PathPoint(530, 100);
		PathPoint intersectionPathPoint3 = new PathPoint(500, 130);
		PathPoint intersectionPathPoint4 = new PathPoint(530, 130);
		pathPointList.add(westPathPoint1);
		pathPointList.add(westPathPoint2);
		pathPointList.add(southPathPoint1);
		pathPointList.add(southPathPoint2);
		pathPointList.add(eastPathPoint1);
		pathPointList.add(eastPathPoint2);
		pathPointList.add(northPathPoint1);
		pathPointList.add(northPathPoint2);
		pathPointList.add(intersectionPathPoint1);
		pathPointList.add(intersectionPathPoint2);
		pathPointList.add(intersectionPathPoint3);
		pathPointList.add(intersectionPathPoint4);
		
		//left, down, right, up
		//0 = red, 1 = yellow, 2 = green, -1 = no semaphore
		int trafficLightsState[] = {0, -1, -1, 2};
		
		//public Intersection(float x, float y, int trafficLights[], int mode, float trafficLightsConfig[])
		//mode = 0: Simple round-robin for 2 semaphores; config[0] is the amount of the time the semaphore is green.
		float trafficLightsConfig[] = {5f};
		intersectionList.add(new Intersection(500, 100, trafficLightsState, 0, trafficLightsConfig, 2, 2));
		
		pathList.add(new Path(westPathPoint1, eastPathPoint1, 2, intersectionList.get(0)));
		pathList.add(new Path(westPathPoint1, intersectionPathPoint3, 2, intersectionList.get(0)));
		pathList.add(new Path(westPathPoint1, intersectionPathPoint4, 2, intersectionList.get(0)));
		pathList.add(new Path(westPathPoint2, eastPathPoint2, 2, intersectionList.get(0)));
		pathList.add(new Path(westPathPoint2, intersectionPathPoint1, 2, intersectionList.get(0)));
		pathList.add(new Path(westPathPoint2, intersectionPathPoint2, 2, intersectionList.get(0)));
		pathList.add(new Path(intersectionPathPoint3, northPathPoint1, 3, intersectionList.get(0)));
		pathList.add(new Path(intersectionPathPoint4, southPathPoint2, 3, intersectionList.get(0)));
		
		
		car2.setCurrentPath(pathList.get(1));
		car.setCurrentPath(pathList.get(0));
		
		
		
		//img = new Texture("badlogic.jpg");
		editMode = 0;
		editButton = new Button(100, 520, 100, 50, "Edit");
		buttonList.add(editButton);
		vehicleId = 0;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Filled);
		for(i = 0; i < vehicleList.size(); i++) {
			vehicleList.get(i).draw(shapeRenderer);
		}
		shapeRenderer.end();
	
		shapeRenderer.begin(ShapeType.Line);
		for(i = 0; i < vehicleList.size(); i++) {
			vehicleList.get(i).drawCamera(shapeRenderer);
		}
		for(i = 0; i < trafficLaneList.size(); i++) {
			trafficLaneList.get(i).draw(shapeRenderer);
		}
		for(i = 0; i < intersectionList.size(); i++) {
			intersectionList.get(i).draw(shapeRenderer);
		}
		for(i = 0; i < pathPointList.size(); i++) {
			pathPointList.get(i).draw(shapeRenderer);
		}
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		for(i = 0; i < intersectionList.size(); i++) {
			intersectionList.get(i).drawTrafficLights(shapeRenderer);
		}
		for(i = 0; i < buttonList.size(); i++) {
			buttonList.get(i).drawOutline(shapeRenderer);
		}
		shapeRenderer.end();
		
		spriteBatch.begin();
		for(i = 0; i < intersectionList.size(); i++) {
			intersectionList.get(i).drawTrafficLightsTimer(spriteBatch, blackFont);
		}
		for(i = 0; i < vehicleList.size(); i++) {
			vehicleList.get(i).drawInfo(spriteBatch, whiteFont);
		}
		for(i = 0; i < buttonList.size(); i++) {
			buttonList.get(i).drawText(spriteBatch, whiteFont);
		}
		whiteFont.draw(spriteBatch, mouseX+", "+mouseY, 100, 620);
		spriteBatch.end();
		
		if(editMode != 1) {
			update();
		}
			
		
		if(isMouseInsideButton(mouseX, mouseY, editButton)) {
			if(editMode == 0) {
				editMode = 1;
			} else {
				editMode = 0;
			}
		}
		
		editButton.setText("Edit("+editMode+")");
		
		mouseX = -1;
		mouseY = -1;
	}
	
	public void update() {
		// Update
		for(i = 0; i < intersectionList.size(); i++) {
			intersectionList.get(i).updateTrafficLights(shapeRenderer, Gdx.graphics.getDeltaTime());
		}
		
		for(i = 0; i < vehicleList.size(); i++) {	
			vehicleHaveToBrake = false;
			// Check if vehicles should brake or accelerate (checking collision)
			for(j = 0; j < vehicleList.size(); j++) {
				if(i == j) continue;
				if(areVehiclesColliding(vehicleList.get(i), vehicleList.get(j))) {
					vehicleHaveToBrake = true;
				} 
			}
		
			// If the traffic light is red, the car should brake before it, so we need to check
			if(vehicleList.get(i).getCurrentPath() != null && vehicleList.get(i).getCurrentPath().getIntersection() != null) {
				// Check if traffic light in the path is red
				if(vehicleList.get(i).getCurrentPath().getIntersection().getTrafficLights()[(vehicleList.get(i).getCurrentPath().getDirection()+2)%4] == 0) {
					// Check for collision to brake the car before the red traffic light
					if(isCollidingWithIntersection(vehicleList.get(i), vehicleList.get(i).getCurrentPath().getIntersection())) {
						vehicleHaveToBrake = true;
					}
				}
			} 
			
			if(vehicleHaveToBrake) {
				vehicleList.get(i).brake();
			} else {
				vehicleList.get(i).accelerate();
			}
			
			// Move vehicles
			if(vehicleList.get(i).getCurrentPath() != null) {
				if(vehicleList.get(i).getCurrentPath().getDirection() == 2) {
					vehicleList.get(i).setX(vehicleList.get(i).getX() + vehicleList.get(i).getSpeed());
				} else if(vehicleList.get(i).getCurrentPath().getDirection() == 3) {
					vehicleList.get(i).setY(vehicleList.get(i).getY() + vehicleList.get(i).getSpeed());
				}
			}
		} // for end
	}
	
	public boolean isMouseInsideButton(float x, float y, Button b) {
		return (x >= b.getX() && x <= b.getX() + b.getWidth() && y >= b.getY() && y <= b.getY() + b.getHeight());
	}
	
	public boolean isCollidingWithIntersection(Vehicle a, Intersection b) {
		return (Math.abs(a.getCameraX() - b.getHitboxX()) * 2 < (a.getCameraWidth() + b.getHitboxWidth())) &&
		         (Math.abs(a.getCameraY() - b.getHitboxY()) * 2 < (a.getCameraHeight() + b.getHitboxHeight()));
	}
	
	public boolean areVehiclesColliding(Vehicle a, Vehicle b) {
		return (Math.abs(a.getCameraX() - b.getX()) * 2 < (a.getCameraWidth() + b.getWidth())) &&
		         (Math.abs(a.getCameraY() - b.getY()) * 2 < (a.getCameraHeight() + b.getHeight()));
		
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		//img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		mouseX = screenX;
		mouseY = Gdx.graphics.getHeight()-screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}