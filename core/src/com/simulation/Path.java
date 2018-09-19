package com.simulation;

public class Path {
	
	Intersection intersection = null;
	PathPoint start, target;
	int direction;
	
	public Path(PathPoint start, PathPoint target, int direction, Intersection intersection) {
		super();
		this.start = start;
		start.getStartingPathList().add(this);
		this.target = target;
		target.getEndingPathList().add(this);
		this.direction = direction;
		this.intersection = intersection;
		
		/*  0 = left
		 *  1 = down
		 *  2 = right
		 *  3 = up
		 */
	}
	
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public Intersection getIntersection() {
		return intersection;
	}
	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
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
	
	
}
