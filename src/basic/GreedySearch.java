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

package basic;

import core.BestFirstSearch;
import core.HeuristicProblem;
/**
 * The greedy search algorithm is a heuristic search able
 * to solve any problem implementing the core.HeuristicProblem interface.<br>
 * <br>
 * This search tries to expand those states at first
 * which are most promising to reach a goal state.
 * To do this the HeuristicProblem#h(java.lang.Object) method is called for each state in order
 * to approximate the costs needed to reach a goal.<br> 
 * This strategy grants a time and space complexity of <code>O(b*^n)</code> 
 * (where b* is the effective branching factor and n the depth of the solution).
 * A drawback of this strategy is the incompleteness, if an infinite path exists in the search domain.
 * In general this search will not find an optimal solution,
 * but is able to generate a good approximation to the optimum in a short amount of time.<br>
 * <b>Note:</b> In some special cases the problem can be divided into subproblems
 * for which the heuristic function can compute an optimal solution. In these cases the
 * greedy search will also be optimal.<br>
 * <br>
 * In most of the cases this search can be used with enabled hashing and without an updating queue.
 * <i>(These are the default values.)</i>
 * The only case when you need to create a greedy search with an updating queue,
 * if the heuristic function returns different values for the same state.
 * <i>(For example if the goal state is moving around the search domain.)</i><br>
 * <b>Note:</b> Using an updating queue will cause massive performance penalties. 
 *  
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class GreedySearch<T> extends BestFirstSearch<T> {
 /**
  * This method creates a new GreedySearch.
  * All search parameters will be set to the default values.
  * 
  * @param problem the HeuristicProblem to be solved
  */
	public GreedySearch(HeuristicProblem<T> problem) {
		super(problem);
	}
 /**
  * This method creates a new GreedySearch.
  * According to the given update flag the search will 
  * use either a queue which is able to update the ordering or not.<br>
  * <b>Note:</b> In almost all cases using an updating queue is not necessary.
  * 
  * @param problem the HeuristicProblem to be solved
  * @param update flag indicating whether the states in the queue should be updated or not.
  */
	public GreedySearch(HeuristicProblem<T> problem, boolean update) {
		super(problem, update);
	}
	/**
  * This method creates a new GreedySearch.
  * According to the given flags the search will 
  * turn off the internal duplicate handling mechanism and 
  * uses a queue which updates its elements or not.<br>
  * <pre><code>
  * HeuristicProblem problem;
  * Search s1,s2,s3,s4;
  * s1 = new GreedySearch(problem,false,false); 
  * s2 = new GreedySearch(problem,true ,false);
  * s3 = new GreedySearch(problem,false,true );
  * s4 = new GreedySearch(problem,true ,true );
  * </code></pre>
  * s1 will be a search with duplicate handling and a queue without updates.<br>
  * s2 will be a search without duplicate handling and a queue without updates.<br>
  * s3 will be a search with duplicate handling and a queue which updates its elements.<br>
  * s4 will be a search without duplicate handling and a queue which updates its elements.<br>
  * <br>
  * <b>Note:</b> In almost all cases using an updating queue is not necessary.
  * 
  * @param problem the HeuristicProblem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
  * @param update flag indicating whether the states in the queue should be updated or not.
	 */
	public GreedySearch(HeuristicProblem<T> problem, boolean noHash, boolean update) {
		super(problem, noHash, update);	
	}
	/** 
	 * This method estimates the costs to reach a goal state from the given state.
	 * <b>Note:</b> This method uses the function core.HeuristicProblem#h(java.lang.Object)
	 * to compute this estimation, and nothing else.
	 * 
	 * @see core.BestFirstSearch#f(java.lang.Object)
	 */
	@Override
	public Double f(T state) {
		return heuristicProblem.h(state);
	}

}
