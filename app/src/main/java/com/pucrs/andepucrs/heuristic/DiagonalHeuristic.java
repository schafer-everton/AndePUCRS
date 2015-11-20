package com.pucrs.andepucrs.heuristic;

import android.graphics.Point;

/**
 * Calculate the diagonal distance to goal when 
 * a straight step costs 1 and diagonal step costs sqrt(2).
 */
public class DiagonalHeuristic implements AStarHeuristic {

	public float getEstimatedDistanceToGoal(Point start, Point goal) {		
		
		float h_diagonal = (float) Math.min(Math.abs(start.x-goal.x), Math.abs(start.y-goal.y));
		float h_straight = (float) (Math.abs(start.x-goal.x) + Math.abs(start.y-goal.y));
		float h_result = (float) (Math.sqrt(2) * h_diagonal + (h_straight - 2*h_diagonal));

		float p = (1/10000);
		h_result *= (1.0 + p);
		
		//return h_result;
		return h_diagonal;
	}

}
