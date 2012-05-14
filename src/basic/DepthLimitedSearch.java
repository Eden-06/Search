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

import java.util.HashSet;
import java.util.Set;

import util.DummySet;
import core.Search;
import core.TreeProblem;

/**
 * The depth limited search algorithm is a blind search
 * able to solve any problem implementing the core.TreeProblem interface.
 * Depth limited search is a subclass of core.Search because it uses
 * recursion rather then the general search algorithm employed in core.DirectedSearch.<br>
 * <br>
 * The depth limited search uses a recursive function to expand all states
 * in the search tree. This function calls itself for each successor of the given state, and
 * terminates only if a goal has been found, the depth limit is reached
 * or all successors have been explored.<br>
 * This strategy grants a time complexity of <code>O(b^l)</code> and
 * a space complexity of <code>O(b*l)</code>
 * (where b is the branching factor and l is the selected depth limit).<br>
 * In contrast to other algorithms depth limited search is complete only if
 * there is a solution s with <code>problem.depth(s) &lt; limit</code>.
 * The biggest problem now is to estimate the depth limit.<br>
 * Despite the fact that this strategy can not generate optimal solutions,
 * it can solve big problems in a memory saving way.<br>
 * In general this algorithm is only applicable to search domains
 * where you can estimate the depth of the solution in an appropriate way.<br>
 * <b>Note:</b> You should prefer basic.IterativeDeepeningSearch whenever
 * the depth limit can not be estimated.<br>
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
 * Search search = new DepthLimitedSearch(problem, 1000, false);
 * </code></pre>
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class DepthLimitedSearch<T> extends Search<T> {
 /**
  * holds a reference to the problem to be solved 
  */
	protected TreeProblem<T> problem;
	/**
	 * holds the depth limit used for this search
	 */
	protected int limit;
	/**
	 * holds the set used for duplicate handling
	 */
	protected Set<T> hash;
	
	/**
	 * Create a new DepthLimitedSearch with the given problem,
	 * going down to the given limit.
	 * The search will use implicit duplicate handling.
	 * 
  * @param problem the core.Poblem to be solved 
	 * @param limit of the exploration depth
	 */
	public DepthLimitedSearch(TreeProblem<T> problem,int limit) {
		this(problem,limit,false);		
	}
	/**
	 * Create a new DepthLimitedSearch with the given problem,
	 * going down to the given limit
  * and disabled hashing if the noHash flag is true.
	 * 
  * @param problem the core.Poblem to be solved
	 * @param limit of the exploration depth
	 * @param noHash flag indicating that duplicates should not be handled
	 */
	public DepthLimitedSearch(TreeProblem<T> problem,int limit,boolean noHash) {
		super();
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
		if (limit>0) this.limit=limit;
		else throw new IllegalArgumentException("maximumDepth must be greater than one!");
		hash = (noHash ? new DummySet<T>() : new HashSet<T>(1024));
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
	 		neededSteps++;
	 		if (! hash.add(state)) return;
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
  * This method clears the hash used for duplicate handling
	 * to prepare it for a new search run.
	 * 
	 * @see core.Search#prepare()
  */
	@Override
	protected void prepare() {
  hash.clear();
	}
 /**
  * This method looks for a goal in the search domain,
	 * by calling a recursive function which expands a state and calls itself for all successor states,
	 * until the depth limit is reached.<br>
	 * <b>Note:</b> A more accurate description is given in basic.DepthLimitedSearch.
	 * 
	 * @see core.Search#search()
  */
	@Override
	protected void search() {
 	depthLimitedSearch(problem.initial(),limit);
	}
 
	/**
	 * This method returns the number of expanded nodes during the search.
	 * Note: This method is a alias for neededSteps() and has the same result.
  * 
  * @return
  *  the number of expanded nodes
  */
	public final int branchedNodes(){
		return neededSteps;
	}
	
 /**
	 * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
  */
	public final TreeProblem<T> getProblem() {
		return problem;
	}
	/**
	 * This method returns the depth to which the search domain will be explored 
	 * 
	 * @return the depth limit of this search
	 */
	public final int depthLimit() {
		return limit;
	}

	
	
	
}
