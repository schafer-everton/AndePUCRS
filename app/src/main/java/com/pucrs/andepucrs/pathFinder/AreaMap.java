package com.pucrs.andepucrs.pathFinder;

import android.graphics.Point;

import com.pucrs.andepucrs.utils.Logger;

import java.util.ArrayList;


public class AreaMap {

	private int mapWith;
	private int mapHeight;
	private ArrayList<ArrayList<Node>> map;
	private int startLocationX = 0;
	private int startLocationY = 0;
	private int goalLocationX = 0;
	private int goalLocationY = 0;
	private int[][] obstacleMap = {{0}};

	private Logger log = new Logger();
	

	public AreaMap(int mapWith, int mapHeight) {
		this.mapWith = mapWith;
		this.mapHeight = mapHeight;
		
		createMap();
		log.addToLog("\tMap Created");
	}
	

	public AreaMap(int mapWith, int mapHeight, int[][] obstacleMap) {
		this.mapWith = mapWith;
		this.mapHeight = mapHeight;
		this.obstacleMap = obstacleMap;
		createMap();
		log.addToLog("\tMap Created");
	}

	private void createMap() {
		Node node;
		map = new ArrayList<ArrayList<Node>>();
		for (int x=0; x<mapWith; x++) {
			map.add(new ArrayList<Node>());
			for (int y=0; y<mapHeight; y++) {
				node = new Node(x,y,this);
				try {
					if (obstacleMap[y][x] >= 1)
						node.setObstical(true);
				} catch (Exception e) {}
				map.get(x).add(node);
			}
		}
	}

	public ArrayList<ArrayList<Node>> getNodes() {
		return map;
	}
	public void setObstacle(int x, int y, boolean isObstical) {
		map.get(x).get(y).setObstical(isObstical);
	}

	public Node getNode(int x, int y) {
		return map.get(x).get(y);
	}

	public void setStartLocation(int x, int y) {
		map.get(startLocationX).get(startLocationY).setStart(false);
		map.get(x).get(y).setStart(true);
		startLocationX = x;
		startLocationY = y;
	}

	public void setGoalLocation(int x, int y) {
		map.get(goalLocationX).get(goalLocationY).setGoal(false);
		map.get(x).get(y).setGoal(true);
		goalLocationX = x;
		goalLocationY = y;
	}

	public int getStartLocationX() {
		return startLocationX;
	}

	public int getStartLocationY() {
		return startLocationY;
	}
	
	public Node getStartNode() {
		return map.get(startLocationX).get(startLocationY);
	}

	public int getGoalLocationX() {
		return goalLocationX;
	}

	public int getGoalLocationY() {
		return goalLocationY;
	}
	
	public Point getGoalPoint() {
		return new Point(goalLocationX, goalLocationY);
	}

	public Node getGoalNode() {
		return map.get(goalLocationX).get(goalLocationY);
	}
	

	public float getDistanceBetween(Node node1, Node node2) {
		//if the nodes are on top or next to each other, return 1
		if (node1.getX() == node2.getX() || node1.getY() == node2.getY()){
			return 1;//*(mapHeight+mapWith);
		} else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
			return (float) 1.9;//*(mapHeight+mapWith);
		}
	}
	
	public int getMapWith() {
		return mapWith;
	}
	public int getMapHeight() {
		return mapHeight;
	}

	public void clear() {
		startLocationX = 0;
		startLocationY = 0;
		goalLocationX = 0;
		goalLocationY = 0;
		createMap();
	}
}
