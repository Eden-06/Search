/*******************************************************************************

Copyright (c) 2007, Thomas "Eden_06" Kühn
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this 
  list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or 
  other materials provided with the distribution.
* Neither the name of the Thomas "Eden_06" Kühn nor the names of its 
  contributors may be used to endorse or promote products derived from this 
  software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*******************************************************************************/

package implementation.gridpuzzle;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import core.HeuristicProblem;



public class GridProblem implements HeuristicProblem<GridState>{
 
	private GridState initial=null;
	private GameGrid goal=null;
	private AbstractHeuristic heuristic=null;
		
	public GridProblem(GameGrid initial, GameGrid goal,	AbstractHeuristic heuristic) {
		super();
		if (initial==null) throw new IllegalArgumentException();
		else	this.initial = new GridState(initial);
		if (goal==null) throw new IllegalArgumentException();
		else this.goal = goal;
		if (heuristic==null) throw new IllegalArgumentException();
		else this.heuristic = heuristic;
	}
	@Override
	public double g(GridState state) {	return state.getDepth(); }
	@Override
	public double h(GridState state) {	return heuristic.get(state.getGrid(),goal);	}
	@Override
	public int depth(GridState state) {	return state.getDepth(); }
	@Override
	public List<GridState> expand(GridState state) {
  List<GridState> result=new LinkedList<GridState>();
  for (Point p : state.getGrid().movements()) {
  	GameGrid grid=new GameGrid(state.getGrid());
  	grid.move(p.x,p.y);  	
  	result.add( new GridState(grid,p,state) );
  }	
		return result;
	}

	@Override
	public GridState initial() {	
		return initial;	
	}

	@Override
	public boolean isGoal(GridState state) {	return goal.equals(state.getGrid()); }

	
}
