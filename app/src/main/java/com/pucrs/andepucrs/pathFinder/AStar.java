package com.pucrs.andepucrs.pathFinder;

import android.graphics.Point;

import com.pucrs.andepucrs.heuristic.AStarHeuristic;
import com.pucrs.andepucrs.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;


public class AStar {
	private AreaMap map;
	private AStarHeuristic heuristic;

	private ArrayList<Node> closedList;
	private SortedNodeList openList;
	private ArrayList<Point> shortestPath;
	Logger log = new Logger();

	public AStar(AreaMap map, AStarHeuristic heuristic) {
		this.map = map;
		this.heuristic = heuristic;
		closedList = new ArrayList<Node>();
		openList = new SortedNodeList();
	}

	public ArrayList<Point> calcShortestPath(int startX, int startY, int goalX, int goalY) {
		map.setStartLocation(startX, startY);
		map.setGoalLocation(goalX, goalY);

		if (map.getNode(goalX, goalY).isObstacle) {
			return null;
		}

		map.getStartNode().setDistanceFromStart(0);
		closedList.clear();
		openList.clear();
		openList.add(map.getStartNode());

		while(openList.size() != 0) {

			Node current = openList.getFirst();

			if(current.getX() == map.getGoalLocationX() && current.getY() == map.getGoalLocationY()) {
				return reconstructPath(current);
			}

			openList.remove(current);
			closedList.add(current);

			for(Node neighbor : current.getNeighborList()) {
				boolean neighborIsBetter;

				if (closedList.contains(neighbor))
					continue;

				if (!neighbor.isObstacle) {

					// calculate how long the path is if we choose this neighbor as the next step in the path 
					float neighborDistanceFromStart = (current.getDistanceFromStart() + map.getDistanceBetween(current, neighbor));

					if(!openList.contains(neighbor)) {
						openList.add(neighbor);
						neighborIsBetter = true;
						//if neighbor is closer to start it could also be better
					} else if(neighborDistanceFromStart < current.getDistanceFromStart()) {
						neighborIsBetter = true;
					} else {
						neighborIsBetter = false;
					}
					if (neighborIsBetter) {
						neighbor.setPreviousNode(current);
						neighbor.setDistanceFromStart(neighborDistanceFromStart);
						neighbor.setHeuristicDistanceFromGoal(heuristic.getEstimatedDistanceToGoal(neighbor.getPoint(), map.getGoalPoint()));
					}
				}

			}
		}
		return null;
	}

	private ArrayList<Point> reconstructPath(Node node) {
		ArrayList<Point> path = new ArrayList<Point>();
		while(!(node.getPreviousNode() == null)) {
			path.add(0,node.getPoint());
			node = node.getPreviousNode();
		}
		this.shortestPath = path;
		return path;
	}

	private class SortedNodeList {

		private ArrayList<Node> list = new ArrayList<Node>();

		public Node getFirst() {
			return list.get(0);
		}

		public void clear() {
			list.clear();
		}

		public void add(Node node) {
			list.add(node);
			Collections.sort(list);
		}

		public void remove(Node n) {
			list.remove(n);
		}

		public int size() {
			return list.size();
		}

		public boolean contains(Node n) {
			return list.contains(n);
		}
	}

}
