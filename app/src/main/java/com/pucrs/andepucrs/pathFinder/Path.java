package com.pucrs.andepucrs.pathFinder;

import android.graphics.Point;

import java.util.ArrayList;

public class Path {
    private ArrayList<Node> waypoints = new ArrayList<Node>();

    public Path() {
    }

    public int getLength() {
        return waypoints.size();
    }

    public Node getWayPointNode(int index) {
        return waypoints.get(index);
    }

    public Point getWayPoint(int index) {
        return new Point(waypoints.get(index).getX(), waypoints.get(index).getY());
    }

    public int getX(int index) {
        return getWayPointNode(index).getX();
    }


    public int getY(int index) {
        return getWayPointNode(index).getY();
    }

    public void appendWayPoint(Node n) {
        waypoints.add(n);
    }

    public void prependWayPoint(Node n) {
        waypoints.add(0, n);
    }

    public boolean contains(int x, int y) {
        for (Node node : waypoints) {
            if (node.getX() == x && node.getY() == y)
                return true;
        }
        return false;
    }

}
