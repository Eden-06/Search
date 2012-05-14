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

import java.util.HashMap;
import java.util.Map;

import util.DummyMap;

import core.HeuristicProblem;
import core.Search;

/**
 * The iterative deepening A* algorithm is a heuristic search
 * able to solve any problem implementing the core.HeuristicProblem interface.
 * IterativeDeepeningAStar is a subclass of core.Search because it uses
 * recursion rather then the general search algorithm employed in core.DirectedSearch.<br>
 * <br>
 * The iterative deepening A* search uses a modified depth limited search to iteratively
 * explore the search domain.
 * The modified depth limited search uses the core.HeuristicProblem#g(java.lang.Object) and core.HeuristicProblem#h(java.lang.Object) methods
 * methods to approximate the overall costs needed to reach a goal
 * from the initial state for each state.
 * This approximation for a state can be mathematically described with the following formula:
 * <code>f(s) = g(s) + h(s)</code><br>
 * Where g computes the path costs to reach state s and h estimates the costs needed to reach a goal.<br>
 * Using this evaluation function the depth limited search is modified 
 * so that only states <code>s</code> where <code>f(s) &lt;= f-limit</code> will be expanded.<br>
 * The first iteration starts with an <code>f-limit</code> equal to <code>f(problem.initial())</code>.
 * For the next iteration the <code>f-limit</code> will be set to the lowest <code>f</code> value
 * seen during the last iteration.<br>
 * This strategy grants completeness and a worst case time complexity of <code>O(b* ^ n)</code> and a space complexity
 * of <code>O(b* * n)</code> (where b* is the effective branching factor and n the depth of the solution).
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
 * In general this search should be used without implicit duplicate handling.
 * (These is <b>not</b> the default.)<br>
 * Because in all cases where two or more paths from the initial state to an other state exists
 * you have to take care of updated states.<br>
 * If your search domain has constant path costs between each state and its successors,
 * you do not have to care about duplicates.
 * Because the first occurrence of a state <code>s</code> implies
 * that each later occurrence of this state has a greater sum of path costs.
 * Thats why we can discard every state which has already been seen.
 * As a conclusion you can use the implicit duplicate handling mechanism.
 * In detail there are two important rules for states of type T which allow proper hashing:
 * <ul>
 * <li>T must implement a hashCode() method.</li>
 * <li>T must implement a equals() method.</li>
 * <li>
 * hashCode() and equals() must be conform to this rule:<br>
 * for all e,t <code>(e.hashCode()==t.hashCode()) and (e.equals(t))</code> implies <code>e==t</code>
 * </li>
 * </ul>
 * <b>Note:</b> Due to its implementation problems, it is not possible
 * to use user defined hashing. 
 * The simple problem is that before each run the hash must be cleared,
 * but there is no method which can be called for that purpose.
 * (The assumption that the basic.HeuristicProblem#initial() method is
 * called before each iteration, will not hold.)<br> 
 * <u>Example:</u><br> 
 * <code>
 * HeuristicProblem problem = new MyHeuristicProblem();<br>
 * //General search domain
 * Search search = new IterativeDeepeningAStar(problem,true);
 * //Special search domain
 * Search search = new IterativeDeepeningAStar(problem);
 * </code>
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class IterativeDeepeningAStar<T> extends Search<T> {
 /**
  * holds a reference to the problem to be solved
  */
	protected HeuristicProblem<T> problem;
	/**
	 * holds the map used for duplicate handling
	 */
	protected Map<T,Integer> hash;
	/**
	 * Create a new IterativeDeepeningAStar with the given problem.
	 * This search will use implicit duplicate handling.
	 * 
  * @param problem the core.Poblem to be solved 
	 */
	public IterativeDeepeningAStar(HeuristicProblem<T> problem) {
  this(problem,false);
	}
	/**
	 * Create a new IterativeDeepeningAStar with the given problem
  * and disabled hashing if the noHash flag is true.
	 * 
  * @param problem the core.Poblem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
	 */
	public IterativeDeepeningAStar(HeuristicProblem<T> problem, boolean noHash) {
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
		hash=(noHash?new DummyMap<T,Integer>():new HashMap<T,Integer>(1024));		
	}
	/**
	 * This method checks if the given problem returns a valid initial state.
	 * 
	 * @see core.Search#canPrepare()
	 */
	@Override
	protected boolean canPrepare() {
		return problem.initial()!=null;
	}
 /**
  * This method does nothing.
  * 
  * @see core.Search#prepare()
  */
	@Override
	protected void prepare() {

	}
 /**
  * This method looks for a goal by iteratively calling a recursive f-limited search
  * with increasing f-limits. 
  * It terminates when a goal has been found or the search domain has been fully explored.
  * 
  * @see core.Search#search()
  */
	@Override
	protected final void search() {
		T initial=problem.initial();
		double flimit=f(initial);
		while ( result==null && running() && (! Double.isInfinite(flimit)) ){
			hash.clear();			
  	flimit=costLimitedSearch(initial,flimit);
  	//System.out.format("f-limit:%f\n",flimit);
  }
	}
 /**
  * This method implements the recursive f-limited search.
  * It expands the given state and calls itself for each successor state,
  * until the limit for the evaluation function <code>f</code> has been reached.
  * 
  * @param state to be checked and further expanded
  * @param flimit to which the search domain will be explored
  * @return the new f-limit used for the next iteration
  */
	private final double costLimitedSearch(T state,double flimit){
	 //search has been terminated
		if (! running()) return flimit;
		//goal found so terminate search
		if (result!=null) return flimit;
	 //state is above the f-limit
	 double newlimit=f(state);
 	if ( newlimit > flimit)	return newlimit;
		//state is a goal value
	 if (problem.isGoal(state)){
	 	result=state;
	 	return flimit;
	 }
 	//check if this state has been expanded previously
 	Integer depth=hash.get(state), 
 	        newdepth=problem.depth(state);
 	if ((depth==null) || (newdepth<depth))
 	 hash.put(state,newdepth);
 	else
 	 return Double.POSITIVE_INFINITY; 	
	 //expand the state
 	newlimit=Double.POSITIVE_INFINITY;
 	neededSteps++;
 	for (T node: problem.expand(state))
 		newlimit=Math.min(newlimit,	costLimitedSearch(node,flimit) );	 		
 	return newlimit;	 
	}

	/**
	 * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
	 */
	public final HeuristicProblem<T> getProblem() {
		return problem;
	}
	/**
	 * This method estimates the costs to reach a goal state from the initial state
	 * over the given state.
	 * <b>Note:</b> This method uses the function core.HeuristicProblem#g(java.lang.Object)
	 * and core.HeuristicProblem#h(java.lang.Object)to compute this estimation, and nothing else.
  *    
  * @param state to be evaluated
  * @return the function value
	 */
	public final double f(T state){
		return problem.g(state) + problem.h(state);
	}
	
}
