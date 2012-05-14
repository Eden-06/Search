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
 * The uniform cost search algorithm is a blind search able
 * to solve any problem implementing the core.HeuristicProblem interface.<br>
 * <br>
 * The uniform cost search tries to expand those states at first
 * which are cheap to reach from the initial state.
 * This strategy grants completeness and a time and space complexity
 * of <code>O(b^n)</code> (where b is the branching factor and n the depth of the solution).<br>
 * In fact the solution found is optimal only if the following constraint holds:<br>
 * For all states <code>a</code> in the search domain and all <code>s</code> in 
 * <code>heuristicProblem.expand(a)</code> the sum of the path costs of <code>s</code>
 * is greater or equal to the sum of the path costs of <code>a</code>
 * (or short where <code>heuristicProblem.g(s) >= heuristicProblem.g(a)</code>).<br>
 * <br>
 * In all cases where two or more paths from the initial state to an other state exists
 * you have to take care of updated states.
 * To cope with this situation you can select one of the following implementation patterns:
 * <ul>
 * <li><b>Disable hashing: </b>
 * A simple solution would be to create a uniform cost search without hashing and without updating of the queue.<br>
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
 * Search search = new UniformCostSearch(problem, true, false);
 * </code>
 * </li>
 * <li>
 * <b>User defined hashing: </b>
 * Experienced users can create a uniform cost search with custom hashing and with an updating queue.<br>
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
 * Search search = new UniformCostSearch(problem, false, false);
 * </code></pre>
 * </li>
 * </ul>  
 * 
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements of the search domain.
 */
public class UniformCostSearch<T> extends BestFirstSearch<T> {
 /**
  * This method creates a new UniformCostSearch.
  * All search parameters will be set to the default values.
  * 
  * @param problem the HeuristicProblem to be solved
  */
	public UniformCostSearch(HeuristicProblem<T> problem) {	
		super(problem);	
	}
 /**
  * This method creates a new UniformCostSearch.
  * According to the given update flag the search will 
  * use either a queue which is able to update the ordering or not.<br>
  * <b>Note:</b> The performance is significantly improved,
  * if the queue must not update its elements.
  * 
  * @param problem the HeuristicProblem to be solved
  * @param update flag indicating whether the states in the queue should be updated or not.
  */
	public UniformCostSearch(HeuristicProblem<T> problem, boolean update) {	
		super(problem, update);	
	}
	/**
	 * This method creates a new UniformCostSearch.
  * According to the given flags the search will 
  * turn off the internal duplicate handling mechanism and 
  * uses a queue which updates its elements or not.<br>
  * <pre><code>
  * HeuristicProblem problem;
  * Search s1,s2,s3,s4;
  * s1 = new UniformCostSearch(problem,false,false); 
  * s2 = new UniformCostSearch(problem,true ,false);
  * s3 = new UniformCostSearch(problem,false,true );
  * s4 = new UniformCostSearch(problem,true ,true );
  * </code></pre>
  * s1 will be a search with duplicate handling and a queue without updates.<br>
  * s2 will be a search without duplicate handling and a queue without updates.<br>
  * s3 will be a search with duplicate handling and a queue which updates its elements.<br>
  * s4 will be a search without duplicate handling and a queue which updates its elements.<br>
  * <br>
  * <b>Note:</b> The performance is significantly improved,
  * if the queue must not update its elements.
  * 
  * @param problem the HeuristicProblem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
  * @param update flag indicating whether the states in the queue should be updated or not.
	 */
	public UniformCostSearch(HeuristicProblem<T> problem, boolean noHash, boolean update) {	
		super(problem, noHash, update);	
	}
	/** 
	 * This method evaluates a state according to the sum of the path costs from the initial state.
	 * <b>Note:</b> This method uses the function core.HeuristicProblem#g(java.lang.Object)
	 * to compute the sum of the path costs, and nothing else.
	 * 
	 * @see core.BestFirstSearch#f(java.lang.Object)
	 */	
	public Double f(T state) {	
		return heuristicProblem.g(state);	
	}
	

}
