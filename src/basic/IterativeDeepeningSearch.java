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

import core.Search;
import core.TreeProblem;
/**
 * The iterative deepening search algorithm is a blind search
 * able to solve any problem implementing the core.TreeProblem interface.
 * IterativeDeepeningSearch is a subclass of core.Search because it uses
 * recursion rather then the general search algorithm employed in core.DirectedSearch.<br>
 * <br>
 * The iterative deepening search starts a depth limited search with a depth limit of zero
 * and increases the depth limit slowly until a goal has been found
 * or the maximum depth is reached.<br>
 * This strategy grants completeness, a time complexity of <code>O(b^n)</code> and
 * a space complexity of <code>O(b*n)</code>
 * (where b is the branching factor and n is the depth of the solution).<br>
 * Further more the algorithm can find an optimal solution only 
 * if the path costs between two states are constant or increase by a function of the depth.<br>
 * In fact iterative deepening search is the best trade of between breadth first 
 * and depth limited search for medium and large search domains.<br> 
 * <br>
 * To avoid duplicate states during the search a HashSet is used,
 * which tries to hash all expanded states in order to inhibit further expansion.
 * In detail there are two important rules for states of type T which allow proper hashing:
 * <ul>
 * <li>T must implement a hashCode() method.</li>
 * <li>T must implement a equals() method.</li>
 * <li>
 * hashCode() and equals() must be conform to this rule:<br>
 * for all e,t <code>(e.hashCode()==t.hashCode()) and (e.equals(t))</code> implies <code>e==t</code>
 * </li>
 * </ul>   
 * In some cases it is necessary to turn of the implicit duplicate handling mechanism.
 * For example there are no duplicate states in the search domain, 
 * or special knowledge about the search domain makes customized duplicate handling
 * more feasible.<br> 
 * The following example shows how to implement a duplicate handling
 * mechanism and how to disable the implicit duplicate handling mechanism:<br>
 * <pre><code> 
 * class MyTreeProblem&lt;T&gt; extends TreeProblem&lt;T&gt;{
 * 	private Set&lt;Integer&gt; visited=new HashSet&lt;Integer&gt;();
 *     
 * 	public List<T> expand(T state){
 * 		List<T> result = new LinkedList<T>();
 * 		for (T newState : super.expand(state) ){
 * 			if ( visited.add( newState.hashCode() ) ){
 * 				result.add(newState);
 * 			}
 * 		}
 * 		return result;
 * 	}
 *     
 * } 
 * 
 * // ...
 * 
 * TreeProblem problem = new MyTreeProblem();
 * //Turn of the implicit duplicate handling mechanism
 * Search search = IterativeDeepeningSearch(problem, 1000, false);
 * </code></pre>
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class IterativeDeepeningSearch<T> extends Search<T> {
	/**
	 * holds the number of expanded nodes during an iteration 
	 */
	private int expandedNodes;
	/**
	 * holds a reference to the problem to be solved
	 */
	protected TreeProblem<T> problem;
	/**
	 * holds the maximal number of iterations allowed
	 */
	protected int maximumDepth=1000;
	/**
	 * holds the map used for duplicate handling
	 */
	protected Map<T,Integer> hash=new HashMap<T,Integer>(1024);	
	
	/**
  *	Create a new IterativeDeepeningSearch with the given problem.
	 * This search will explore at most up to a depth of 1000 and
	 * uses implicit duplicate handling.
	 * 
  * @param problem the core.Poblem to be solved
  */
	public IterativeDeepeningSearch(TreeProblem<T> problem) {
		super();
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
	}
	/**
	 * Create a new IterativeDeepeningSearch with the given problem,
	 * which needs at most the given iterations.
  * This search will use implicit duplicate handling.
	 * 
  * @param problem the core.Poblem to be solved
	 * @param maximumDepth the maximum exploration depth for the overall search
	 */
	public IterativeDeepeningSearch(TreeProblem<T> problem,int maximumDepth) {
		this(problem,maximumDepth,false);			
	}
	/**
	 * Create a new IterativeDeepeningSearch with the given problem,
	 * which needs at most the given iterations
  * and disabled hashing if the noHash flag is true.
	 * 
  * @param problem the core.Poblem to be solved
	 * @param maximumDepth the maximum exploration depth for the overall search
	 * @param noHash flag indicating that duplicates should not be handled 
	 * 
	 */
	public IterativeDeepeningSearch(TreeProblem<T> problem,int maximumDepth, boolean noHash) {
		super();
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
		if (maximumDepth>1) this.maximumDepth=maximumDepth;
		else throw new IllegalArgumentException("maximumDepth must be greater than one!");
		hash= (noHash ? new DummyMap<T,Integer>() : new HashMap<T,Integer>(1024));
	}
 
 /**
  * This method implements the recursive depth limited search.
  * It expands the given state and calls itself for each successor state,
  * until the given depth limit has been reached.
  * 
  * @param state to be checked and further expanded
  * @param limit of the exploration depth
  */
	private final void depthLimitedSearch(T state,int limit){
	 if (result==null && running()){
	 	if (problem.isGoal(state)){
	 		result=state;
	 	}else{
	 		expandedNodes++;
	 		neededSteps++;
	 		//check if this state has been expanded previously
	  	Integer depth=hash.get(state), 
	  	        newdepth=problem.depth(state);
	  	if ((depth==null) || (newdepth<depth))
	  	 hash.put(state,newdepth);
	  	else
	  		return;
	  	//expand state
	 		for (T node: problem.expand(state))
	 			if (problem.depth(node) < limit)
	 				depthLimitedSearch(node,limit);	 		
	 	} 	
	 }	
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
  * This method looks for a goal by iteratively calling a recursive depth limited search
  * with increasing depth limits. 
  * It terminates when a goal has been found or the number of expanded nodes
  * does not increase anymore.
  * 
  * @see core.Search#search()
  */
	@Override
	protected final void search() {
		int limit=1, last=0;
		T initial=problem.initial();
		while (limit<maximumDepth && result==null && running()){
			expandedNodes=0;
			hash.clear(); // do not use that expandedNodes==hash.size() because of the DummySet
  	depthLimitedSearch(initial,limit);  	
  	//System.out.format("limit=%d, nodes=%d\n",limit,expandedNodes);
  	//break if the number of expanded nodes does not increase any more
  	if (last==expandedNodes) break;
  	last=expandedNodes;
  	limit++;
  }
	}
 /**
	 * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
  */
	public TreeProblem<T> getProblem() {
		return problem;
	}
 /**
  * Returns the maximal depth to which the search tree will be explored. 
  * 
  * @return the maximum amount of iterations
  */
	public int getMaximumDepth() {
		return maximumDepth;
	}
 /**
  * Changes the maximal number of iterations to the given value. 
  * 
  * @param maximumDepth the new maximum number of iterations
  */
	public void setMaximumDepth(int maximumDepth) {
		if (! initialized()) this.maximumDepth = maximumDepth;
	}
	
}
