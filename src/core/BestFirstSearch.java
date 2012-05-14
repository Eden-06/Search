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

package core;

import java.util.PriorityQueue;
import java.util.Queue;

import util.SortedQueue;

import java.util.Comparator;

/**
 * This subclass of DirectedSearch is the abstraction of all best first search algorithms
 * like greedy search or A* search.<br>
 * The basic idea behind this search algorithm is, to expand the promising states at first.
 * To estimate how promising a state is a specific evaluation function <code>f</code> is used.
 * Each subclass has a different implementation for this function, 
 * but all use the core.HeuristicProblem interface.<br>
 * <i>(For details on the evaluation function lock in to the concrete subclasses of BestFirstSearch.)</i><br>
 * <br>
 * This search algorithm has two modes for evaluation of the states in the search space.<br>
 * The first mode evaluates each state when the state is enqueued. <i>(This is the default behavior.)</i>
 * The second mode evaluates each state when ever a state is popped from the queue.
 * You can create a best first search with this behavior with the following code:<br>
 * <code>BestFirstSearch search = new MyBestFirstSearch(problem, true);</code><br>
 * So what is the different between these two modes and when should which be used?<br>
 * The first mode is only applicable if there are no two paths from the initial state to a state <code>b</code>,
 * so that <code>f(b)</code> would evaluate to two different values. For examples look into the documentations of the subclasses.
 * This simple constraint allows the search to use a java.util.PriorityQueue which grants high performance.<br>
 * The second mode is applicable in all cases (also in cases where the evaluation of a state my change over time).  
 * But this generality costs a lot of performance during the search,
 * because we have to use the util.SortedQueue instead. 
 * <hr>
 * If you want to implement your own best first search algorithm
 * you only have to inherit from this class and implement the evaluation function <code>f</code>
 * according to the following rules:<br>
 * <ul>
 * <li>f must be defined for all states of type <code>T</code> (but not for <code>null</code>).</li>
 * <li>if state <code>a</code> is better then state <code>b</code>,
 * <code>f(a)</code> must be smaller then <code>f(b)</code>.</li>
 * </ul>
 * All subclasses should provide at least one constructor,
 * where the user can decide whether to disable automatic duplicate handling
 * and whether to update the states in the queue.<br>
 * <b>Note:</b> No subclass of BestFirstSearch should hide on of these configurations to the user.
 * 
 * @see basic.GreedySearch
 * @see basic.UniformCostSearch
 * @see basic.AStarSearch
 * @author eden06
 *
 * @param <T> the specific type of all elements of the search domain.
 */
public abstract class BestFirstSearch<T> extends DirectedSearch<T> {
 
	private class BFSComparator implements Comparator<T>{
		@Override
		public final int compare(T a, T b) {
   return f(a).compareTo(f(b));
		} 	
 }
	
	/**
	 * holds a reference to the problem to be solved of the actual type 
	 */
	protected HeuristicProblem<T> heuristicProblem=null;
 /**
  * holds the instance of the queue used during the search. 
  */
	protected Queue<T> queue=null;
 /**
  * This method creates a new BestFirstSearch.
  * All search parameters will be set to the default values.
  * 
  * @param problem the HeuristicProblem to be solved
  */
	public BestFirstSearch(HeuristicProblem<T> problem) {
		this(problem,false);
	}
 /**
  * This method creates a new BestFirstSearch.
  * According to the given update flag the search will 
  * use either a queue which is able to update the ordering or not.<br>
  * <b>Note:</b> The performance is significantly improved,
  * if the queue must not update its elements.
  * 
  * @param problem the HeuristicProblem to be solved
  * @param update flag indicating whether the states in the queue should be updated or not.
  */
	public BestFirstSearch(HeuristicProblem<T> problem,boolean update) {
		super(problem,false);
		heuristicProblem=problem;
		queue=(update ? new SortedQueue<T>(new BFSComparator()) : new PriorityQueue<T>(1024,new BFSComparator()));
	}
	/**
	 * This method creates a new BestFirstSearch.
  * According to the given flags the search will 
  * turn off the internal duplicate handling mechanism and 
  * uses a queue which updates its elements or not.<br>
  * <pre><code>
  * HeuristicProblem problem;
  * Search s1,s2,s3,s4;
  * s1 = new BestFirstSearch(problem,false,false); 
  * s2 = new BestFirstSearch(problem,true ,false);
  * s3 = new BestFirstSearch(problem,false,true );
  * s4 = new BestFirstSearch(problem,true ,true );
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
	public BestFirstSearch(HeuristicProblem<T> problem, boolean noHash, boolean update) {
		super(problem,noHash);
		heuristicProblem=problem;
		queue=(update ? new SortedQueue<T>(new BFSComparator()) : new PriorityQueue<T>(1024,new BFSComparator()));
	}	
	
	/**
	 * @see core.DirectedSearch#getProblem()
	 */
	@Override
	public HeuristicProblem<T> getProblem() {
		return heuristicProblem;
	}

	/**
	 * This method adds a new state to the queue.
	 * 
	 * @see core.DirectedSearch#add(java.lang.Object)
	 */
	@Override
	protected boolean add(T state) {	return queue.add(state);	}

	/**
	 * This method removes all elements from the queue.
	 * 
	 * @see core.DirectedSearch#clear()
	 */
	@Override
	protected void clear() { queue.clear(); }

	/**
	 * This method tells whether the queue is empty or not.
	 * 
	 * @see core.DirectedSearch#empty()
	 */
	@Override
	protected boolean empty() {	return queue.isEmpty();	}

	/**
	 * This method returns the best state according to the function value.<br>
	 * Mathematically speaking if <code>pop()</code> returned a state <code>a</code> then there is no state <code>b</code> in the queue with <code>f(b)&lt;f(a)</code>
	 * 
	 * @see core.DirectedSearch#pop()
	 */
	@Override
	protected T pop() {	return queue.remove(); }
 /**
  * This method is the hook for evaluating states during insertion into the queue.<br>
  * This method should return a value indicating how good a state is.<br>
  * This means if state <code>a</code> is better than state <code>b</code> 
  * then <code>f(a)</code> should be smaller <code>f(b)</code>.<br>
  *    
  * @param state to be evaluated
  * @return the function value
  */
	public abstract Double f(T state);	
		
}
