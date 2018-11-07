package com.simulation;

public class Path {
	
	RoadIntersectionSocket socket = null;
	PathPoint start, target;
	int direction;
	
	public Path(PathPoint start, PathPoint target, int direction, RoadIntersectionSocket socket) {
		super();
		this.start = start;
		start.getStartingPathList().add(this);
		this.target = target;
		target.getEndingPathList().add(this);
		this.direction = direction;
		this.socket = socket;
	}
		
		/*  0 = left
		 *  1 = down
		 *  2 = right
		 *  3 = up
		 */
 
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public PathPoint getStart() {
		return start;
	}
	public void setStart(PathPoint start) {
		this.start = start;
	}
	public PathPoint getTarget() {
		return target;
	}
	public void setTarget(PathPoint target) {
		this.target = target;
	}
	public RoadIntersectionSocket getSocket() {
		return socket;
	}
	public void setSocket(RoadIntersectionSocket socket) {
		this.socket = socket;
	}	
}
