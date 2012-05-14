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

package implementation.dijkstra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import core.HeuristicProblem;

public class DijkstraProblem implements HeuristicProblem<DijkstraState>{

	private Graph graph=null;
	private int start;
	private Map<Integer,DijkstraState> nodes=new HashMap<Integer,DijkstraState>();

	public DijkstraProblem(Graph graph,int start) {
		super();
		if (graph==null) throw new IllegalArgumentException("graph should never be null");
		else	this.graph = graph;
		this.start = start;
		for (Integer i : graph.getNodes()){
			nodes.put( i, new DijkstraState(i,i==start) );
		}
	}
	
	public Map<Integer, DijkstraState> getResult() {	return nodes; }
	@Override
	public double g(DijkstraState state) { return state.getCost(); }
 @Override
	public double h(DijkstraState state) {	return 0.0; }
	@Override
	public int depth(DijkstraState state) {	return state.getDepth();	}
	@Override
	public DijkstraState initial() {	return nodes.get(start);	}
	@Override
	public boolean isGoal(DijkstraState state) {	return false; }
	@Override
	public List<DijkstraState> expand(DijkstraState state) {
		List<DijkstraState> result=new LinkedList<DijkstraState>();
		for ( Integer i : graph.successors(state.getId()) ){
			double cost = graph.getCost(state.getId(),i);
			DijkstraState oldState = nodes.get(i);
			if ( (oldState!=null) && (oldState.update(state,cost)) ) {
					result.add(oldState);
			}
		}
		return result;
	}

}
