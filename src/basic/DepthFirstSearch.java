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
import core.Problem;

/**
 * The depth first search algorithm is a blind search able
 * to solve any problem implementing the core.Problem interface.
 * This class is a subclass of core.Search because it uses
 * recursion rather then the general search algorithm employed in core.DirectedSearch.<br>
 * <br>
 * The depth first search uses a recursive function to expand all states
 * in the search tree. This function calls itself for each successor of the given state, and
 * terminates only if a goal has been found or all successors have been explored.<br>
 * This strategy grants a time complexity of <code>O(b^d)</code> and
 * a space complexity of <code>O(b*d)</code>
 * (where b is the branching factor and d the depth of the search tree).<br>
 * In contrast to other algorithms depth first search is complete only if
 * there are no infinite branches in the search domain.
 * In most cases this is no problem because we can limit the maximal search depth
 * by using the basic.DepthLimitedSearch.<br>
 * Despite the fact that this strategy can not generate optimal solutions,
 * it can solve big problems in a memory saving way.<br>
 * <br>
 * In general this algorithm is only applicable to search domains with a finite
 * search tree, because otherwise this algorithm would not terminate.<br>
 * <b>Note:</b> You should prefer basic.IterativeDeepeningSearch whenever
 * you need an optimal solution or have to deal with an infinite search tree.<br>
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
 * class MyProblem&lt;T&gt; extends Problem&lt;T&gt;{
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
 * Problem problem = new MyProblem();
 * //Turn of the implicit duplicate handling mechanism
 * Search search = new DepthFirstSearch(problem, false);
 * </code></pre>
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class DepthFirstSearch<T> extends Search<T> {
 /**
  * holds a reference to the problem to be solved 
  */
	protected Problem<T> problem=null;
	/**
	 * holds the set used for duplicate handling
	 */
	protected Set<T> hash=null;
	/**
	 * Creates a new DepthFirstSearch with the given problem.
  * The search will use implicit duplicate handling.
	 * 
	 * @param problem problem the core.Poblem to be solved
	 */
	public DepthFirstSearch(Problem<T> problem) {
		this(problem,false);
	}	
	/**
	 * Create a new DepthFirstSearch with the given problem
  * and disabled hashing if the noHash flag is true.
	 * 
  * @param problem the core.Poblem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
	 */
	public DepthFirstSearch(Problem<T> problem,boolean noHash) {
		super();
  if (problem!=null) this.problem=problem;
  else throw new IllegalArgumentException("problem should never be null!");
  hash= (noHash ? new DummySet<T>() : new HashSet<T>(1024));
	}
 /**
  * This method implements the recursive depth first search.
  * It expands the given state and calls itself for each successor state.
  * 
  * @param state to be checked and further expanded
  */
	private final void recursiveSearch(T state){
		if (result==null && running()){
		 if (problem.isGoal(state)){
		 	result=state;
		 }else{
			 neededSteps++;
			 if (! hash.add(state)) return;
			 for (T node: problem.expand(state) )
			 	recursiveSearch(node);
		 }
		}
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
	 * by calling a recursive function which expands a state and calls itself for all successor states.<br>
	 * <b>Note:</b> A more accurate description is given in basic.DepthFirstSearch.
	 * 
	 * @see core.Search#search()
	 */
	@Override
	protected final void search() {
	 recursiveSearch( problem.initial() );
	}	
 /**
	 * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
  */
	public final Problem<T> getProblem() {
		return problem;
	}


}
