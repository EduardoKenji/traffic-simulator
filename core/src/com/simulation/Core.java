package com.simulation;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

public class Core extends ApplicationAdapter implements InputProcessor {
	
	public static GlyphLayout glyphLayout;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	ArrayList<Vehicle> vehicleList;
	ArrayList<TrafficLane> trafficLaneList;
	ArrayList<PathPoint> pathPointList;
	ArrayList<Intersection> intersectionList;
	ArrayList<Path> pathList;
	ArrayList<Integer> markedIdForLateralCollisionList; //if the vehicleId is in this list, the car can't be included in a lateralVehicleCollision
	
	TrafficLane editModeCreateHorizontalTrafficLane;
	TrafficLane editModeCreateVerticalTrafficLane;
	Intersection editModeCreateIntersection;
	Intersection newIntersection;
	TrafficLane newTrafficLane;
	
	int i, j;
	int vehicleId;
	boolean vehicleHaveToBrake;
	BitmapFont blackFont, whiteFont;
	Button editButton;
	ArrayList<Button> buttonList;
	int editMode;
	
	float mouseClickX, mouseClickY;
	float mouseCurrentX, mouseCurrentY;
	int mouseButton;
	int scrolled;
	
	OrthographicCamera camera;
	float difX, difY;
	Matrix4 currentProjectionMatrix;
	float currentZoom;
	
	String test;
	
	int numberOfCarsThatDespawned;
	float aliveTimeSum;
	
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
		markedIdForLateralCollisionList = new ArrayList<Integer>();

		intersectionList.add(new Intersection(440, 100, 2, 2));
				
		TrafficLane leftLane1 = new TrafficLane(200, 100, 240, 30);
		TrafficLane leftLane2 = new TrafficLane(200, 130, 240, 30);	
		// 0 and 1
		trafficLaneList.add(leftLane1);
		trafficLaneList.add(leftLane2);
		// 2 and 3
		trafficLaneList.add(new TrafficLane(440, -80, 30, 180));
		trafficLaneList.add(new TrafficLane(470, -80, 30, 180));
		// 4 and 5
		trafficLaneList.add(new TrafficLane(500, 100, 240, 30));
		trafficLaneList.add(new TrafficLane(500, 130, 240, 30));
		// 6 and 7
		trafficLaneList.add(new TrafficLane(440, 160, 30, 200));
		trafficLaneList.add(new TrafficLane(470, 160, 30, 200));
		intersectionList.add(new Intersection(740, 100, 2, 2));
		// 8 and 9
		trafficLaneList.add(new TrafficLane(800, 100, 240, 30));
		trafficLaneList.add(new TrafficLane(800, 130, 240, 30));
		// 10 and 11
		trafficLaneList.add(new TrafficLane(740, 160, 30, 200));
		trafficLaneList.add(new TrafficLane(770, 160, 30, 200));	
		// 12 and 13
		trafficLaneList.add(new TrafficLane(740, -80, 30, 180));
		trafficLaneList.add(new TrafficLane(770, -80, 30, 180));
		intersectionList.add(new Intersection(440, -140, 2, 2));
		// 14 and 15
		trafficLaneList.add(new TrafficLane(200, -140, 240, 30));
		trafficLaneList.add(new TrafficLane(200, -110, 240, 30));
		// 16 and 17
		trafficLaneList.add(new TrafficLane(440, -320, 30, 180));
		trafficLaneList.add(new TrafficLane(470, -320, 30, 180));
		// 18 and 19
		trafficLaneList.add(new TrafficLane(500, -110, 240, 30));
		trafficLaneList.add(new TrafficLane(500, -140, 240, 30));
		intersectionList.add(new Intersection(740, -140, 2, 2));
		// 20 and 21
		trafficLaneList.add(new TrafficLane(800, -110, 240, 30));
		trafficLaneList.add(new TrafficLane(800, -140, 240, 30));
		// 22 and 23
		trafficLaneList.add(new TrafficLane(740, -320, 30, 180));
		trafficLaneList.add(new TrafficLane(770, -320, 30, 180));
		
		//pathList.add(new Path(leftLane1.getPathPointList().get(0), trafficLaneList.get(8).getPathPointList().get(1), 2, intersectionList.get(0).getWestSockets()[0]));
		pathList.add(new Path(leftLane1.getPathPointList().get(0), intersectionList.get(0).getPathPointList().get(1), 2, intersectionList.get(0).getWestSockets()[0]));
		pathList.add(new Path(leftLane2.getPathPointList().get(0), intersectionList.get(0).getPathPointList().get(2), 2, intersectionList.get(0).getWestSockets()[1]));
		
		//pathList.add(new Path(intersectionList.get(0).getPathPointList().get(0), trafficLaneList.get(6).getPathPointList().get(1), 3, null));
		
		pathList.add(new Path(intersectionList.get(0).getPathPointList().get(1), intersectionList.get(1).getPathPointList().get(1), 2, intersectionList.get(1).getWestSockets()[0]));
		pathList.add(new Path(intersectionList.get(0).getPathPointList().get(1), intersectionList.get(2).getPathPointList().get(1), 1, intersectionList.get(2).getNorthSockets()[1]));
		
		pathList.add(new Path(intersectionList.get(0).getPathPointList().get(2), intersectionList.get(1).getPathPointList().get(2), 2, intersectionList.get(1).getWestSockets()[1]));
		pathList.add(new Path(intersectionList.get(0).getPathPointList().get(2), trafficLaneList.get(6).getPathPointList().get(1), 3, null));
		
		pathList.add(new Path(intersectionList.get(1).getPathPointList().get(2),trafficLaneList.get(10).getPathPointList().get(1), 3, null));
		pathList.add(new Path(intersectionList.get(1).getPathPointList().get(2),trafficLaneList.get(9).getPathPointList().get(1), 2, null));
		
		pathList.add(new Path(intersectionList.get(1).getPathPointList().get(1), intersectionList.get(3).getPathPointList().get(1), 1, intersectionList.get(3).getNorthSockets()[1]));
		pathList.add(new Path(intersectionList.get(1).getPathPointList().get(1), trafficLaneList.get(8).getPathPointList().get(1), 2, null));
		
		pathList.add(new Path(trafficLaneList.get(20).getPathPointList().get(1), intersectionList.get(3).getPathPointList().get(2), 0, intersectionList.get(3).getEastSockets()[1]));
		pathList.add(new Path(trafficLaneList.get(21).getPathPointList().get(1), intersectionList.get(3).getPathPointList().get(1), 0, intersectionList.get(3).getEastSockets()[0]));
		
		pathList.add(new Path(intersectionList.get(3).getPathPointList().get(1), trafficLaneList.get(23).getPathPointList().get(0), 1, null));
		pathList.add(new Path(intersectionList.get(3).getPathPointList().get(1), intersectionList.get(2).getPathPointList().get(1), 0, intersectionList.get(2).getEastSockets()[0]));
		
		pathList.add(new Path(intersectionList.get(2).getPathPointList().get(1), trafficLaneList.get(17).getPathPointList().get(0), 1, null));
		pathList.add(new Path(intersectionList.get(2).getPathPointList().get(1), trafficLaneList.get(14).getPathPointList().get(0), 0, null));
		
		pathList.add(new Path(intersectionList.get(3).getPathPointList().get(2), intersectionList.get(2).getPathPointList().get(2), 0, intersectionList.get(2).getEastSockets()[1]));
		pathList.add(new Path(intersectionList.get(3).getPathPointList().get(2), intersectionList.get(1).getPathPointList().get(2), 3, intersectionList.get(1).getSouthSockets()[0]));
		
		pathList.add(new Path(intersectionList.get(2).getPathPointList().get(2), intersectionList.get(0).getPathPointList().get(2), 3, intersectionList.get(0).getSouthSockets()[0]));
		pathList.add(new Path(intersectionList.get(2).getPathPointList().get(2), trafficLaneList.get(14).getPathPointList().get(0), 0, null));
		
		pathList.add(new Path(intersectionList.get(2).getPathPointList().get(1), trafficLaneList.get(17).getPathPointList().get(0), 1, null));
		
		//Vertical spawners
		
		pathList.add(new Path(trafficLaneList.get(7).getPathPointList().get(1), intersectionList.get(0).getPathPointList().get(1), 1, intersectionList.get(0).getNorthSockets()[1]));
		pathList.add(new Path(trafficLaneList.get(11).getPathPointList().get(1), intersectionList.get(1).getPathPointList().get(1), 1, intersectionList.get(1).getNorthSockets()[1]));
		
		pathList.add(new Path(trafficLaneList.get(16).getPathPointList().get(0), intersectionList.get(2).getPathPointList().get(2), 3, intersectionList.get(2).getSouthSockets()[0]));
		pathList.add(new Path(trafficLaneList.get(22).getPathPointList().get(0), intersectionList.get(3).getPathPointList().get(2), 3, intersectionList.get(3).getSouthSockets()[0]));
		
		leftLane1.getPathPointList().get(0).setMode(3);
		leftLane2.getPathPointList().get(0).setMode(3);
			
		trafficLaneList.get(20).getPathPointList().get(1).setMode(3);
		trafficLaneList.get(21).getPathPointList().get(1).setMode(3);
		
		trafficLaneList.get(7).getPathPointList().get(1).setMode(3);
		trafficLaneList.get(11).getPathPointList().get(1).setMode(3);
		
		trafficLaneList.get(16).getPathPointList().get(0).setMode(3);
		trafficLaneList.get(22).getPathPointList().get(0).setMode(3);
		
		float respawnTimer = 4f;
		
		leftLane1.getPathPointList().get(0).setSpawnTimer(respawnTimer);
		leftLane2.getPathPointList().get(0).setSpawnTimer(respawnTimer);
		
		trafficLaneList.get(20).getPathPointList().get(1).setSpawnTimer(respawnTimer);
		trafficLaneList.get(21).getPathPointList().get(1).setSpawnTimer(respawnTimer);
		
		trafficLaneList.get(7).getPathPointList().get(1).setSpawnTimer(respawnTimer);
		trafficLaneList.get(11).getPathPointList().get(1).setSpawnTimer(respawnTimer);
		
		trafficLaneList.get(16).getPathPointList().get(0).setSpawnTimer(respawnTimer);
		trafficLaneList.get(22).getPathPointList().get(0).setSpawnTimer(respawnTimer);
		
		for(i = 0; i < trafficLaneList.size(); i++) {
			pathPointList.add(trafficLaneList.get(i).getPathPointList().get(0));
			pathPointList.add(trafficLaneList.get(i).getPathPointList().get(1));
		}
		
		for(i = 0; i < intersectionList.size(); i++) {
			for(j = 0; j < intersectionList.get(i).getPathPointList().size(); j++) {
				pathPointList.add(intersectionList.get(i).getPathPointList().get(j));
			}
		}

		//config[0] = tempo para sinal verde
		//config[1] = tempo para sinal amarelo
		//config[2] = tempo para sinal vermelho
		float config[] = {3.5f, 1.5f, 5f};
		//float config2[] = {3.5f, 1.5f, 6.5f};
		
		intersectionList.get(0).getWestSockets()[0].setCurrentColor(0);
		intersectionList.get(0).getWestSockets()[0].setConfig(config);
		intersectionList.get(0).getWestSockets()[0].setMode(1);
		intersectionList.get(0).getWestSockets()[0].setState(1);
		intersectionList.get(0).getWestSockets()[0].setRelatedSocket(intersectionList.get(0).getSouthSockets()[0]);
		System.out.println(intersectionList.get(0).getWestSockets()[0].getRelatedSocket());
		intersectionList.get(0).getWestSockets()[1].setCurrentColor(0);
		intersectionList.get(0).getWestSockets()[1].setConfig(config);
		intersectionList.get(0).getWestSockets()[1].setMode(1);
		intersectionList.get(0).getWestSockets()[1].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);
		
		intersectionList.get(1).getWestSockets()[0].setCurrentColor(0);
		intersectionList.get(1).getWestSockets()[0].setConfig(config);
		intersectionList.get(1).getWestSockets()[0].setMode(1);
		intersectionList.get(1).getWestSockets()[0].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);
		intersectionList.get(1).getWestSockets()[1].setCurrentColor(0);
		intersectionList.get(1).getWestSockets()[1].setConfig(config);
		intersectionList.get(1).getWestSockets()[1].setMode(1);
		intersectionList.get(1).getWestSockets()[1].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);

		intersectionList.get(2).getEastSockets()[0].setCurrentColor(0);
		intersectionList.get(2).getEastSockets()[0].setConfig(config);
		intersectionList.get(2).getEastSockets()[0].setMode(1);
		intersectionList.get(2).getEastSockets()[0].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);
		intersectionList.get(2).getEastSockets()[1].setCurrentColor(0);
		intersectionList.get(2).getEastSockets()[1].setConfig(config);
		intersectionList.get(2).getEastSockets()[1].setMode(1);
		intersectionList.get(2).getEastSockets()[1].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);
		
		intersectionList.get(3).getEastSockets()[0].setCurrentColor(0);
		intersectionList.get(3).getEastSockets()[0].setConfig(config);
		intersectionList.get(3).getEastSockets()[0].setMode(1);
		intersectionList.get(3).getEastSockets()[0].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);
		intersectionList.get(3).getEastSockets()[1].setCurrentColor(0);
		intersectionList.get(3).getEastSockets()[1].setConfig(config);
		intersectionList.get(3).getEastSockets()[1].setMode(1);
		intersectionList.get(3).getEastSockets()[1].setSocketToMirror(intersectionList.get(0).getWestSockets()[0]);
		
		intersectionList.get(0).getSouthSockets()[0].setCurrentColor(2);
		intersectionList.get(0).getSouthSockets()[0].setConfig(config);
		intersectionList.get(0).getSouthSockets()[0].setRelatedSocket(intersectionList.get(0).getWestSockets()[0]);
		intersectionList.get(0).getSouthSockets()[0].setMode(1);
		
		intersectionList.get(1).getSouthSockets()[0].setCurrentColor(2);
		intersectionList.get(1).getSouthSockets()[0].setConfig(config);
		intersectionList.get(1).getSouthSockets()[0].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(1).getSouthSockets()[0].setMode(1);
		
		intersectionList.get(2).getSouthSockets()[0].setCurrentColor(2);
		intersectionList.get(2).getSouthSockets()[0].setConfig(config);
		intersectionList.get(2).getSouthSockets()[0].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(2).getSouthSockets()[0].setMode(1);
		
		intersectionList.get(3).getSouthSockets()[0].setCurrentColor(2);
		intersectionList.get(3).getSouthSockets()[0].setConfig(config);
		intersectionList.get(3).getSouthSockets()[0].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(3).getSouthSockets()[0].setMode(1);
		
		intersectionList.get(0).getNorthSockets()[1].setCurrentColor(2);
		intersectionList.get(0).getNorthSockets()[1].setConfig(config);
		intersectionList.get(0).getNorthSockets()[1].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(0).getNorthSockets()[1].setMode(1);
		
		intersectionList.get(1).getNorthSockets()[1].setCurrentColor(2);
		intersectionList.get(1).getNorthSockets()[1].setConfig(config);
		intersectionList.get(1).getNorthSockets()[1].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(1).getNorthSockets()[1].setMode(1);
		
		intersectionList.get(2).getNorthSockets()[1].setCurrentColor(2);
		intersectionList.get(2).getNorthSockets()[1].setConfig(config);
		intersectionList.get(2).getNorthSockets()[1].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(2).getNorthSockets()[1].setMode(1);
		
		intersectionList.get(3).getNorthSockets()[1].setCurrentColor(2);
		intersectionList.get(3).getNorthSockets()[1].setConfig(config);
		intersectionList.get(3).getNorthSockets()[1].setSocketToMirror(intersectionList.get(0).getSouthSockets()[0]);
		intersectionList.get(3).getNorthSockets()[1].setMode(1);
		
		editMode = 0;
		editButton = new Button(100, 520, 100, 50, "Edit");
		editModeCreateHorizontalTrafficLane = new TrafficLane(450, 620, 140, 30);
		editModeCreateVerticalTrafficLane = new TrafficLane(650, 550, 30, 140);
		editModeCreateIntersection = new Intersection(500, 510, 2, 2);
		buttonList.add(editButton);
		
		camera = new OrthographicCamera(960, 720);
		camera.position.x = 480;
		camera.position.y = 360;
		difX = 0;
		difY = 0;
		currentZoom = 1;
		currentProjectionMatrix = spriteBatch.getProjectionMatrix();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		handleInput();
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		
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
			trafficLaneList.get(i).drawPathPoints(shapeRenderer);
		}
		for(i = 0; i < intersectionList.size(); i++) {
			intersectionList.get(i).draw(shapeRenderer);
			intersectionList.get(i).drawPathPoints(shapeRenderer);
		}
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		for(i = 0; i < buttonList.size(); i++) {
			buttonList.get(i).drawOutline(shapeRenderer);
		}
		shapeRenderer.end();
		
		spriteBatch.begin();
		for(i = 0; i < vehicleList.size(); i++) {
			vehicleList.get(i).drawInfo(spriteBatch, whiteFont);
		}
		for(i = 0; i < buttonList.size(); i++) {
			buttonList.get(i).drawText(spriteBatch, whiteFont);
		}
		
		whiteFont.setColor(Color.YELLOW);
		
		for(i = 0; i < trafficLaneList.size(); i++) {	
			whiteFont.draw(spriteBatch, ""+i, trafficLaneList.get(i).getX() + 5, trafficLaneList.get(i).getY() + 16);
		}
		
		whiteFont.setColor(Color.WHITE);
		whiteFont.draw(spriteBatch, "Zoom: "+currentZoom, 100, 700);
		whiteFont.draw(spriteBatch, mouseCurrentX+", "+mouseCurrentY, 100, 660);
		whiteFont.draw(spriteBatch, difX+", "+difY, 100, 620);
		whiteFont.draw(spriteBatch, "Average time alive (car): "+(aliveTimeSum/numberOfCarsThatDespawned), 180, 300);
		whiteFont.draw(spriteBatch, "Test: "+test, 180, 260);
		spriteBatch.end();
		
		// Edit Mode
		if(editMode != 1) { // Out of edit mode
			update();
		} else { // With edit mode
			try {
				updateEditMode();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			drawEditMode();	
		}
		
		if(isPointInsideEntity(mouseClickX, mouseClickY, editButton)) {
			if(editMode == 0) {
				editMode = 1;
			} else {
				newIntersection = null;
				newTrafficLane = null;
				editMode = 0;
			}
		}
		
		editButton.setText("Edit("+editMode+")");
		
		mouseClickX = -1;
		mouseClickY = -1;
		mouseButton = -1;
		scrolled = 0;
	}
	
	int flag;
	int random;
	
	public void update() {
		// Update
		
		for(i = 0; i < pathPointList.size(); i++) {
			pathPointList.get(i).update();
			if(pathPointList.get(i).getMode() == 2 || pathPointList.get(i).getMode() == 3) {
				
				flag = 0;
				for(j = 0; j < vehicleList.size(); j++) {
					if(vehicleInsideSpawnPoint(vehicleList.get(j), pathPointList.get(i))) { //Se há veículo dentro do spawn point
						flag = 1;
					}
				}
				
				if(flag == 0) { // Se não há veículos dentro do spawn point
					if(pathPointList.get(i).getCurrentTimer() >= pathPointList.get(i).getSpawnTimer()) {
						pathPointList.get(i).setCurrentTimer(0f);
						pathPointList.get(i).setSpawnTimer(((float)Math.random() * 2) + 1.5f);
						vehicleList.add(new Vehicle(pathPointList.get(i).getX(), pathPointList.get(i).getY(), 30, 30, vehicleId));
						if(pathPointList.get(i).getMode() == 3) {
							vehicleList.get(vehicleList.size() - 1).setOnlySameDirection(true);
						}
						vehicleList.get(vehicleList.size() - 1).setCurrentPath(pathPointList.get(i).getStartingPathList().get(0));
						vehicleList.get(vehicleList.size() - 1).setMaxSpeed(0.7f);
						vehicleId++;
					}
				}
			} 
		}
		
		for(i = 0; i < intersectionList.size(); i++) {
			intersectionList.get(i).update();
		}
		
		for(i = 0; i < vehicleList.size(); i++) {	
			vehicleList.get(i).update();
			if(vehicleList.get(i).isFinished()) {
				numberOfCarsThatDespawned++;
				aliveTimeSum += vehicleList.get(i).getAliveTime();
				vehicleList.remove(i);
			}
		}
		
	
		for(i = 0; i < vehicleList.size(); i++) {	
			vehicleHaveToBrake = false;
			// Check if vehicles should brake or accelerate (checking collision)
			//Vehicle-vehicle collision
			
			for(j = 0; j < vehicleList.size(); j++) {
				if(i == j) continue;
				if(areVehiclesColliding(vehicleList.get(i), vehicleList.get(j))) {
					vehicleHaveToBrake = true;
				} 
			}
			
		
			// If the traffic light is red, the car should brake before it, so we need to check
			if(vehicleList.get(i).getCurrentPath() != null && vehicleList.get(i).getCurrentPath().getSocket() != null) {
				
				// Check if traffic light in the path is red
				
				if(vehicleList.get(i).getCurrentPath().getSocket().getCurrentColor() == 2 ||
						(vehicleList.get(i).getCurrentPath().getSocket().getCurrentColor() == 1 &&
						vehicleList.get(i).getCurrentPath().getSocket().getConfig()[1] - 
						vehicleList.get(i).getCurrentPath().getSocket().timer <= 0.5f)) {
					// Check for collision to brake the car before the red traffic light
					if(vehicleSocketCollision(vehicleList.get(i), vehicleList.get(i).getCurrentPath().getSocket())) {
						vehicleHaveToBrake = true;
					}
					if(vehicleList.get(i).getCurrentPath().getDirection() == 2 && 
							vehicleList.get(i).getX() + vehicleList.get(i).getWidth() > vehicleList.get(i).getCurrentPath().getSocket().getX()) {
						vehicleHaveToBrake = false;
					} else if(vehicleList.get(i).getCurrentPath().getDirection() == 0 && 
							vehicleList.get(i).getX() < vehicleList.get(i).getCurrentPath().getSocket().getX()) {
						vehicleHaveToBrake = false;
					} else if(vehicleList.get(i).getCurrentPath().getDirection() == 3 && 
							vehicleList.get(i).getY() + vehicleList.get(i).getHeight() > vehicleList.get(i).getCurrentPath().getSocket().getY()) {
						vehicleHaveToBrake = false;
					} else if(vehicleList.get(i).getCurrentPath().getDirection() == 1 && 
							vehicleList.get(i).getY() < vehicleList.get(i).getCurrentPath().getSocket().getY()) {
						vehicleHaveToBrake = false;
					} 
					/*
					for(j = 0; j < vehicleList.size(); j++) {
						if(i == j) continue;
						if(vehicleList.get(i).getCurrentPath().getDirection() == 2 && areVehiclesCollidingByLongRange(vehicleList.get(i), vehicleList.get(j))) {
							vehicleHaveToBrake = true;
							break;
						}
					}
					*/
				}
				
			}
			
			
			// Check if vehicles should brake or accelerate (checking collision)
			for(j = 0; j < vehicleList.size(); j++) {
				if(i == j) continue;
				if(areVehiclesColliding(vehicleList.get(i), vehicleList.get(j))) {
					vehicleHaveToBrake = true;
				} 
				if(areVehiclesCollidingByLateral(vehicleList.get(i), vehicleList.get(j)) && areVehiclesCollidingByLateral(vehicleList.get(j), vehicleList.get(i))) {
					if(!markedIdForLateralCollisionList.contains(vehicleList.get(i).getVehicleId()) && !markedIdForLateralCollisionList.contains(vehicleList.get(j).getVehicleId())) {
						if(vehicleList.get(i).getCurrentPath() != null && vehicleList.get(j).getCurrentPath() != null) {
							if(Math.abs(vehicleList.get(i).getCurrentPath().getDirection() - vehicleList.get(j).getCurrentPath().getDirection()) == 1) {
								if(vehicleList.get(i).getSpeed() != 0 && vehicleList.get(j).getSpeed() != 0) {
									if(vehicleList.get(i).getState() == 1 && vehicleList.get(j).getState() == 1) {
										if(vehicleList.get(i).getTimeSinceDirectionChanged() > 0.7f && vehicleList.get(j).getTimeSinceDirectionChanged() > 0.7f) {
											test = vehicleList.get(i).getVehicleId()+" "+vehicleList.get(j).getVehicleId();
											random = (int)(Math.random() * 2);
											if(random == 0) {
												vehicleList.get(i).setCurrentIdleTimer(0f);
											} else {
												vehicleList.get(j).setCurrentIdleTimer(0f);
											}
											vehicleList.get(i).setTimerToRemoveFromMarkedList(0f);
											vehicleList.get(j).setTimerToRemoveFromMarkedList(0f);
											markedIdForLateralCollisionList.add(vehicleList.get(i).getVehicleId());
											markedIdForLateralCollisionList.add(vehicleList.get(j).getVehicleId());
											editMode = 1; //this line pauses the simulation
											test = i+" "+j;
										}	
									}
								}
							}
						}
					}	
				}
			} // for
			
			
			//If idle timer is on, the car must stop
			if(vehicleList.get(i).getCurrentIdleTimer() < vehicleList.get(i).getIdleTime()) {
				vehicleHaveToBrake = true;
			}
			
			if(vehicleList.get(i).getTimerToRemoveFromMarkedList() > 2f) {
				if(markedIdForLateralCollisionList.contains(vehicleList.get(i).getVehicleId())) {
					markedIdForLateralCollisionList.remove(Integer.valueOf(vehicleList.get(i).getVehicleId()));
				}	
			}
			
			if(vehicleHaveToBrake) {
				vehicleList.get(i).brake();
				vehicleList.get(i).setState(-1);
			} else {
				vehicleList.get(i).accelerate();
				vehicleList.get(i).setState(1);
			}
			
			// Move vehicles
			if(vehicleList.get(i).getCurrentPath() != null) {
				if(vehicleList.get(i).getCurrentPath().getDirection() == 2) {
					vehicleList.get(i).setX(vehicleList.get(i).getX() + vehicleList.get(i).getSpeed());
				} else if(vehicleList.get(i).getCurrentPath().getDirection() == 3) {
					vehicleList.get(i).setY(vehicleList.get(i).getY() + vehicleList.get(i).getSpeed());
				} else if(vehicleList.get(i).getCurrentPath().getDirection() == 0) {
					vehicleList.get(i).setX(vehicleList.get(i).getX() - vehicleList.get(i).getSpeed());
				} else if(vehicleList.get(i).getCurrentPath().getDirection() == 1) {
					vehicleList.get(i).setY(vehicleList.get(i).getY() - vehicleList.get(i).getSpeed());
				}
			}
		} // for end
	}
	
	public boolean vehicleInsideSpawnPoint(Vehicle a, PathPoint b) {
		if(a.getX() == b.getX() && (Math.abs(a.getY() - b.getY()) < 45)) {
			return true;
		}
		if(a.getY() == b.getY() && (Math.abs(a.getX() - b.getX()) < 45)) {
			return true;
		}
		return false;
	}
	
	public void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-3, 0, 0);
			difX -= 3;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(3, 0, 0);
			difX += 3;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -3, 0);
			difY -= 3;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 3, 0);
			difY += 3;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if(editMode == 0) {
				editMode = 1;
			} else {
				editMode = 0;
			}
		}
		/*
		if(scrolled == 1) {
			camera.zoom += 0.02;
			currentZoom += 0.02;
		} 
		if(scrolled == -1) {
			camera.zoom -= 0.02;
			currentZoom -= 0.02;
		}
		*/
	}
	
	public void updateEditMode() throws CloneNotSupportedException {
		if(newTrafficLane != null) {
			newTrafficLane.setX(mouseCurrentX - (newTrafficLane.getWidth()/2) + difX);
			newTrafficLane.setY(mouseCurrentY - (newTrafficLane.getHeight()/2) + difY);
		}
		if(newIntersection != null) {
			newIntersection.setX(mouseCurrentX - (newIntersection.getWidth()/2) + difX);
			newIntersection.setY(mouseCurrentY - (newIntersection.getHeight()/2) + difY);
		}
		if(isPointInsideEntity(mouseClickX, mouseClickY, editModeCreateHorizontalTrafficLane)) {
			newIntersection = null;
			newTrafficLane = editModeCreateHorizontalTrafficLane.clone();
		} else if(isPointInsideEntity(mouseClickX, mouseClickY, editModeCreateVerticalTrafficLane)) {
			newIntersection = null;
			newTrafficLane = editModeCreateVerticalTrafficLane.clone();
		} else if(isPointInsideEntity(mouseClickX, mouseClickY, editModeCreateIntersection)) {
			newTrafficLane = null;
			newIntersection = editModeCreateIntersection.clone();
		} else if(mouseButton == 0) {
			if(newIntersection != null) {
				intersectionList.add(newIntersection);
				newIntersection = null;
			}
			if(newTrafficLane != null) {
				trafficLaneList.add(newTrafficLane);
				newTrafficLane = null;
			}
		}
		if(mouseButton == 1 && (newIntersection != null || newTrafficLane != null)) {
			newIntersection = null;
			newTrafficLane = null;
		}
		
	}
	
	public void drawEditMode() {
		spriteBatch.begin();
			//whiteFont.draw(spriteBatch, "Streets/Roads: "+car.getX(), 200, 620);
		spriteBatch.end();
		
		shapeRenderer.begin(ShapeType.Line);
			editModeCreateHorizontalTrafficLane.draw(shapeRenderer);
			editModeCreateVerticalTrafficLane.draw(shapeRenderer);
			editModeCreateIntersection.draw(shapeRenderer);
			if(newTrafficLane != null) {
				newTrafficLane.draw(shapeRenderer);
			}
			if(newIntersection != null) {
				newIntersection.draw(shapeRenderer);
			}
		shapeRenderer.end();
	}
	
	public boolean isPointInsideEntity(float x, float y, Entity b) {
		return (x + difX >= b.getX()  && x + difX <= b.getX() + b.getWidth() &&
				y + difY >= b.getY() && y + difY <= b.getY() + b.getHeight());
	}
	
	public boolean isCollidingWithIntersection(Vehicle a, Intersection b) {
		return (Math.abs(a.getCameraX() - b.getHitboxX()) * 2 < (a.getCameraWidth() + b.getHitboxWidth())) &&
		         (Math.abs(a.getCameraY() - b.getHitboxY()) * 2 < (a.getCameraHeight() + b.getHitboxHeight()));
	}
	
	public boolean vehicleSocketCollision(Vehicle a, RoadIntersectionSocket b) {
		/*
		return (Math.abs(a.getCameraX() - b.getX()) * 2 < (a.getCameraWidth() + b.getWidth())) &&
		         (Math.abs(a.getCameraY() - b.getY()) * 2 < (a.getCameraHeight() + b.getHeight()));
		*/
		return (a.getCameraX() < b.getX() + b.getWidth() && a.getCameraX() + a.getCameraWidth() > b.getX() &&
				a.getCameraY() < b.getY() + b.getHeight() && a.getCameraY() + a.getCameraHeight() > b.getY());
	}
	
	public boolean areVehiclesColliding(Vehicle a, Vehicle b) {
		/*
		return (Math.abs(a.getCameraX() - b.getX()) * 2 < (a.getCameraWidth() + b.getWidth())) &&
		         (Math.abs(a.getCameraY() - b.getY()) * 2 < (a.getCameraHeight() + b.getHeight()));
		*/
		return (a.getCameraX() < b.getX() + b.getWidth() && a.getCameraX() + a.getCameraWidth() > b.getX() &&
				a.getCameraY() < b.getY() + b.getHeight() && a.getCameraY() + a.getCameraHeight() > b.getY());
	}
	
	public boolean areVehiclesCollidingByLongRange(Vehicle a, Vehicle b) {
		return (Math.abs(a.getLongRangeCameraX() - b.getX()) * 2 < (a.getLongRangeCameraWidth() + b.getWidth())) &&
		         (Math.abs(a.getLongRangeCameraY() - b.getY()) * 2 < (a.getLongRangeCameraHeight() + b.getHeight()));
	}
	
	public boolean areVehiclesCollidingByLateral(Vehicle a, Vehicle b) {
		/*
		boolean leftCollision = (a.getCameraX() < b.getX() + b.getWidth() && a.getCameraX() + a.getCameraWidth() > b.getX() &&
				a.getCameraY() < b.getY() + b.getHeight() && a.getCameraY() + a.getCameraHeight() > b.getY());
		boolean rightCollision = (Math.abs(a.getRightLateralCameraX() - b.getX()) * 2 < (a.getRightLateralCameraWidth() + b.getWidth())) &&
		         (Math.abs(a.getRightLateralCameraY() - b.getY()) * 2 < (a.getRightLateralCameraHeight() + b.getHeight()));
		*/
		boolean leftCollision = (a.getLeftLateralCameraX() < b.getX() + b.getWidth() && a.getLeftLateralCameraX() + a.getLeftLateralCameraWidth() > b.getX() &&
				a.getLeftLateralCameraY() < b.getY() + b.getHeight() && a.getLeftLateralCameraY() + a.getLeftLateralCameraHeight() > b.getY());
		boolean rightCollision = (a.getRightLateralCameraX() < b.getX() + b.getWidth() && a.getRightLateralCameraX() + a.getRightLateralCameraWidth() > b.getX() &&
				a.getRightLateralCameraY() < b.getY() + b.getHeight() && a.getRightLateralCameraY() + a.getRightLateralCameraHeight() > b.getY());
		return leftCollision || rightCollision;
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
		mouseClickX = screenX;
		mouseClickY = Gdx.graphics.getHeight()-screenY;
		mouseButton = button;
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
		mouseCurrentX = screenX;
		mouseCurrentY = Gdx.graphics.getHeight()-screenY;
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		scrolled = amount;
		return true;
	}
}
