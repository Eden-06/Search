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

import java.util.LinkedList;

import core.DirectedSearch;
import core.Problem;


/**
 * The breadth first search algorithm is a blind search
 * able to solve any problem implementing the core.Problem interface.<br>
 * <br>
 * The breadth first search starts at depth zero
 * and expands all states with the same depth in the search tree before 
 * it does the same in the next depth.<br>
 * This strategy grants completeness and a time and space complexity
 * of <code>O(b^n)</code> (where b is the branching factor and n the depth of the solution).<br>
 * An optimal solution can be found only 
 * if the path costs between two states are constant or increase by a function of the depth.<br>
 * <br>
 * In general this algorithm is only applicable for small problem domains with a small
 * branching factor, because of the large amount of space needed during execution.<br>
 * <b>Note:</b> You should prefer basic.IterativeDeepeningSearch whenever
 * the BreadthFirstSearch exceeds the available amount of space.<br>
 * <br> 
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
 * Search search = new BreadthFirstSearch(problem, false);
 * </code></pre>
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class BreadthFirstSearch<T> extends DirectedSearch<T> {
 /**
  * holds the queue used during the search
  */
	private LinkedList<T> queue=new LinkedList<T>();
	
	/**
  * This method creates a new BreadthFirstSearch
  * where implicit duplicate handling is enabled.
  * 
	 * @param problem to be solved
	 */
	public BreadthFirstSearch(Problem<T> problem) {
		super(problem);
	}
	/**
	 * This method creates a new BreadthFirstSearch.
  * According to the given flag the search will 
  * turn off the internal duplicate handling mechanism or not.<br>
  * 
	 * @param problem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
	 */
	public BreadthFirstSearch(Problem<T> problem, boolean noHash) {
		super(problem,noHash);
	}	
	/**
	 * @see core.DirectedSearch#add(java.lang.Object)
	 */
 @Override
	protected boolean add(T state) {
		return queue.add(state);
	}
 /**
  * @see core.DirectedSearch#clear()
  */
	@Override
	protected void clear() {
  queue.clear();
	}
 /**
  * @see core.DirectedSearch#empty()
  */
	@Override
	protected boolean empty() {
		return queue.isEmpty();
	}
 /**
  * @see core.DirectedSearch#pop()
  */
	@Override
	protected T pop() {
		return queue.pop();
	}

}
