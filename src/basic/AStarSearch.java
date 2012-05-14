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
 * The A* search algorithm is a heuristic search able
 * to solve any problem implementing the core.HeuristicProblem interface.<br>
 * <br>
 * This search tries to expand those states at first
 * which are cheap to reach from the initial state and are most promising to reach a goal state.
 * To do this the core.HeuristicProblem#g(java.lang.Object) and core.HeuristicProblem#h(java.lang.Object) methods
 * are called for each state in order to approximate the overall costs needed
 * to reach a goal from the initial state.
 * This approximation for a state can be mathematically described with the following formula:
 * <code>f(s) = g(s) + h(s)</code><br>
 * Where g computes the path costs to reach state s and h estimates the costs needed to reach a goal.<br> 
 * This strategy grants completeness and a worst case time and space complexity
 * of <code>O(b*^n)</code> (where b* is the effective branching factor and n the depth of the solution).
 * This search is able to generate optimal solutions whenever the following constraints hold
 * for all states <code>s</code> in the search domain:
 * <ul>
 * <li>
 * <code>h(s) &lt;= h(s') + cost(s,s')</code> for all successors <code>s'</code> of state <code>s</code>.<br>
 * This constraint is called monotony and ensures that
 * the heuristic function <code>h</code> is constantly falling.
 * </li>
 * <li>
 * <code>h(s) &lt;= g(t)</code> where <code>t</code> is the nearest goal to state <code>s</code><br>
 * This constraint makes the heuristic function optimistic and
 * ensures that the best solution is found first. 
 * </li>
 * </ul>
 * <br>
 * In general this search should be used with enabled hashing and an updating queue.
 * (These are <b>not</b> the default values.)<br>
 * Because in all cases where two or more paths from the initial state to an other state exists
 * you have to take care of updated states.<br>
 * To cope with this situation you can select one of the following implementation patterns:
 * <ul>
 * <li><b>Do not care: </b>
 * If your search domain has constant path costs between each state and its successors,
 * you do not have to care about duplicates.<br>
 * Because the first occurrence of a state s implies that each later occurrence of this state
 * has a greater sum of path costs. Thats why we can discard every state which has already been seen.<br>
 * As a conclusion our search can now use the fast java.util.PriorityQueue and
 * the implicit duplicate handling mechanism.<br>
 * <u>Pros:</u> Its probably the best solution.<br>
 * <u>Cons:</u> Its only applicable for certain problems.<br>
 * <u>Example:</u><br> 
 * <code>
 * HeuristicProblem problem = new MyHeuristicProblem();<br>
 * Search search = new AStarSearch(problem);
 * </code>
 * </li>
 * <li><b>Disable hashing: </b>
 * A simple solution would be to create an A* search without hashing and without updating of the queue.<br>
 * In this way all states will be inserted into the queue and ordered according to their current sum of path costs
 * without checking whether they have already been visited or not.
 * So if we revisit a state with lower path costs, we can simply add it again to the queue.
 * Because the queue will not update its elements the state with the lowest path costs will
 * be placed before its duplicates.<br>
 * <u>Pros:</u> The major benefit of this solution is its simplicity and the fact that it uses the
 * fast java.util.PriorityQueue instead of the slow util.SortedQueue.
 * An other benefit is that you do not need to take care of duplicate states by your self.<br>
 * <u>Cons:</u> If there exists no shortest path in the search domain, this solution
 * produces lots of unnecessary expansions of already expanded notes.<br>
 * In fact this makes the search more space and time consuming.<br>
 * <u>Example:</u><br> 
 * <code>
 * HeuristicProblem problem = new MyHeuristicProblem();<br>
 * Search search = new AStarSearch(problem, true, false);
 * </code>
 * </li>
 * <li>
 * <b>User defined hashing: </b>
 * Experienced users can create an A* search with custom hashing and with an updating queue.<br>
 * The idea is that you keep track of all the visited states in a map,
 * where you can simply update each visited state whenever you found a better path.
 * You only have to make sure that the HeuristicProblem#expand(java.lang.Object) will only
 * return a list of states not visited before.<br>
 * The util.SortedQueue implementation ensures, that the queue is updated whenever a state in it has changed.
 * <i>(Please note that the sorted queue works slightly different, as described here!)</i><br>    
 * <u>Pros: </u> This solution saves lots of memory by prohibiting expansion of already visited states.
 * Further more it is a general solution applicable for all problems with alternative paths in the search space.
 * <br> 
 * <u>Cons: </u> This solution is more time consuming because it must frequently update the queue
 * and the states itself.
 * In fact the util.SortedQueue is damn slow compared to the java.util.PriorityQueue.<br>
 * <u>Example:</u><br>
 * <pre><code> 
 * class MyCustomProblem&lt;T&gt; extends MyHeuristicProblem&lt;T&gt;{
 * 	private Map&lt;Integer,T&gt; visited=new HashMap&lt;Integer,T&gt;();
 *     
 * 	public List<T> expand(T state){
 * 		List<T> result = new LinkedList<T>();
 * 		for (T newState : super.expand(state) ){
 * 			oldState = visited.get( newState.hashCode() );
 * 			if (oldState == null){
 * 				result.add(newState);
 * 				visited.put(newState.hashCode(), newState);
 * 			}else{
 * 				update(oldState, newState);
 * 			}
 * 		}
 * 		return result;
 * 	}
 *  
 * 	public update(T oldState,T newState){
 * 		if ( g(oldState) > g(newState) ){
 * 			// update the state of the oldState Object
 * 			// ...
 * 		}
 * 	}
 *   
 * } 
 * 
 * // ...
 * 
 * HeuristicProblem problem = new MyCustomProblem();
 * Search search = new AStarSearch(problem, false, false);
 * </code></pre>
 * </li>
 * </ul> 
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements of the search domain.
 */
public class AStarSearch<T> extends BestFirstSearch<T> {
 /**
  * Creates a new AStarSearch with the given problem.
  * The search will use implicit duplicate handling and no updating queue.
  * 
  * @param problem the HeuristicProblem to be solved
  */
	public AStarSearch(HeuristicProblem<T> problem) {	
		super(problem); 
	}
 /**
  * Create a new AStarSearch with the given problem
  * and a queue corresponding to the given update flag.
  * The search will also use implicit duplicate handling.
  * 
  * @param problem the HeuristicProblem to be solved
  * @param update flag indicating whether the states in the queue should be updated or not.
  */
	public AStarSearch(HeuristicProblem<T> problem, boolean update) {	
		super(problem, update);	
	}
 /**
  * Create a new AStarSearch with the given problem,
  * disabled hashing if the noHash flag is true
  * and a queue corresponding to the given update flag.
  * 
  * @param problem the HeuristicProblem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
  * @param update flag indicating whether the states in the queue should be updated or not.
  */
	public AStarSearch(HeuristicProblem<T> problem, boolean noHash, boolean update) {
		super(problem, noHash, update);	
	}
	/** 
	 * This method estimates the costs to reach a goal state from the initial state
	 * over the given state.
	 * <b>Note:</b> This method uses the function core.HeuristicProblem#g(java.lang.Object)
	 * and core.HeuristicProblem#h(java.lang.Object)to compute this estimation, and nothing else.
	 * 
	 * @see core.BestFirstSearch#f(java.lang.Object)
	 */
	@Override
	public Double f(T state) {
		return heuristicProblem.g(state) + heuristicProblem.h(state);
		//TODO: make sure the function is monotonic
		//return max( f(heuristicProblem.parent(state)), heuristicProblem.g(state) + heuristicProblem.h(state) );
	}

}
