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

import extended.BidirectionalProblem;

public class BidirectionalGridProblem implements BidirectionalProblem<GridState> {

	private GridState initial=null;
	private GridState goal=null;
	
	
	
	public BidirectionalGridProblem(GridState initial, GridState goal) {
		this.initial = initial;
		this.goal = goal;
	}

	public BidirectionalGridProblem(GameGrid initial, GameGrid goal) {
		this.initial = new GridState(initial);
		this.goal = new GridState(goal);
	}
	
	@Override
	public GridState goal() {	return goal; }

	@Override
	public List<GridState> implode(GridState state) {	
  List<GridState> result=new LinkedList<GridState>();
  Point zero=state.getGrid().positionOf(0);
  for (Point p : state.getGrid().movements()) {
  	GameGrid grid=new GameGrid(state.getGrid());
  	grid.move(p.x,p.y);  	
   /*
    * Add the move which will lead from the new state to the parent state.
    * This is important for the reconstruction of the path to the goal.
    */
  	result.add( new GridState(grid,zero,state) );
  }	
		return result;	
	}

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
	public GridState initial() {	return initial; }

	@Override
	public boolean isGoal(GridState state) {
		return state.equals(goal);
	}

}
