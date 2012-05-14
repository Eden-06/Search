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

import java.util.HashSet;
import java.util.Set;

import util.DummySet;

/**
 * This subclass of Search is the core implementation of most directed search algorithms  
 * and is able to perform many various directed search strategies 
 * such as depth first, breath first or best first search.<br> 
 * Inherent for all these algorithms is that the search space is explored in a tree based structure.
 * To run a DirectedSearch there must be an instance of core.Problem given. 
 * (See description for further information.)<br>
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
 * In some cases it is important to turn of the implicit hashing. 
 * This can be done by simply creating a new instance of a directed search
 * with the given argument noHash set to true.<br>
 * <i>Example:</i> <code>DirectedSearch&lt;State&gt; search = new BreadthFirstSearch&lt;State&gt;(problem, true);</code>
 * 
 * 
 * <hr>
 * This class defines the basic search algorithm which can be applied to some directed searches.
 * This algorithm can be described in the following way:
 * <ol>
 * <li>If <code>empty()</code> is true terminate the search.</li>
 * <li><code>current = pop()</code> get the next node from the queue.</li>
 * <li>If <code>isGoal(current)</code> is true then save the current state
 * and terminate the search.</li>
 * <li>For each node in <code>problem.expand(current)</code> <code>add(node)</code> to the queue.</li>
 * <li>Go to 1.</li>
 * </ol>
 * As Developer of a new directed search you only have to instantiate a queue and then
 * implement the following queuing methods:
 * <ul>
 * <li>
 * <b>add(state):</b> This method should add a new state to the queue.
 * The way you add a new state to the queue determines the kind of search.
 * For example if you always add new states to the end of the queue the resulting search
 * will behave like breadth first search.<br>
 * <i>(Because the core.DirectedSearch handles duplicates you do not have 
 * to implement your own duplicate handler in this method.)</i>
 * </li>
 * <li>
 * <b>pop():</b> This method removes the first element of the queue and returns it.
 * The search algorithm implemented here will not call this method if empty() returned true previously.
 * </li>
 * <li>
 * <b>empty():</b> This method should only return true if the queue is empty.
 * It will always be called immediately before the pop() method, to ensure
 * that pop() will never get called on an empty queue.<br>
 * According to this definition a directed search terminates whenever the queue is empty.
 * </li>
 * <li>
 * <b>clear():</b> This method should remove all elements of the queue.
 * This method will be called whenever your search gets initialized
 * to remove all states from previous runs.<br>
 * <i>(It is assumed that a cleared queue, will return true on the call of empty().)</i>
 * </li>
 * </ul>  
 * This implementation does not catch any thrown exception, 
 * so make sure your methods do not throw exceptions in most of the cases.<br>
 * <i>(Of course OutOfMemoryException's can not be avoided.)</i>
 * 
 * @see core.BestFirstSearch
 * @see basic.BreadthFirstSearch
 * @see basic.SlowDepthFirstSearch
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements of the search domain.
 */
public abstract class DirectedSearch<T> extends Search<T> {

	/**
	 * holds the problem to be solved during the search
	 */
	protected Problem<T> problem=null;
	/**
	 * holds the set used for duplicate handling
	 */
	protected Set<T> hash=null;
	/**
	 * Creates a new directed search with the given problem.
	 * 
	 * @param problem to be solved
	 * @throws IllegalArgumentException if the given problem is null
	 */
	public DirectedSearch(Problem<T> problem) throws IllegalArgumentException{
		this(problem,false);
 }
	/**
	 * Creates a new directed search with the given problem and
	 * a flag indicating whether duplicate handling should be turned off.
	 * 
	 * @param problem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
	 * @throws IllegalArgumentException if the given problem is null
	 */
	public DirectedSearch(Problem<T> problem,boolean noHash) throws IllegalArgumentException {
		super();
  if (problem!=null) this.problem=problem;
  else throw new IllegalArgumentException("problem should never be null!");
  hash=(noHash ? new DummySet<T>() : new HashSet<T>());
	}
	
	/**
	 * This method returns the number of expanded nodes during the search.<br>
	 * <b>Note:</b> This method is an alias for neededSteps() and has the same result.
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
	public Problem<T> getProblem() {
		return problem;
	}

	/**
	 * This method checks if the given problem returns a valid initial state.
	 * 
	 * @see core.Search#canPrepare()
	 */
	@Override
	protected final boolean canPrepare() {
		return problem.initial()!=null;
	}

	/**
	 * @see core.Search#finalize()
	 */
	@Override
	protected final void finalize() throws Throwable {
		clear();
		hash.clear();
		problem=null;
		hash=null;
		super.finalize();
	}

	/**
	 * This method clears the queue and the hash used for duplicate handling
	 * to prepare them for a new search run.
	 * 
	 * @see core.Search#prepare()
	 */
	@Override
	protected final void prepare() {
		clear();
		hash.clear();
	}

	/**
	 * This method looks for a goal in the search domain,
	 * by iteratively expanding the first state of the queue
	 * and adding all successor state to the queue.<br>
	 * <b>Note:</b> A more accurate description is given in core.DirectedSearch.
	 * 
	 * @see core.Search#search()
	 */
	@Override
	protected final void search() {
	 T current=problem.initial();
	 add(current);
	 while ( (!empty()) && running() ){
	 	current=pop();
	 	if (problem.isGoal(current)){
	 		result=current;
	 		break;
	 	}else{
	 		neededSteps++;
/*	 		if (neededSteps%1000 == 0 ) 
	  	 System.out.print(".");
	 		if (neededSteps%100000 == 0 ) 
	  	 System.out.format("\nbranched:%d  hashed:%d\n",neededSteps,hash.size());*/
	 		if (hash.add(current)){
	 		 for (T node: problem.expand(current) )
	 				add(node);
	 		}
	 	}
	 }		
	}
	/**
	 * This method is the hook for adding new states to the queue during the search.<br>
	 * After calling this method the state should be added to the queue.<br>
	 * <b>Note:</b> Most implementations do not check 
	 * whether a state has been successfully added or not.
	 * 
	 * @param state to be added to the queue
	 * @return true only if this state has been added.
	 */
	protected abstract boolean add(T state);
	/**
	 * This method is the hook for extracting the first element of the queue during the search.<br>
	 * It should return and remove the first element of the queue.<br>
	 * <b>Note:</b> The implementation assures that this method will only be called
	 * if empty() returned false.
	 * 
	 * @return the first element of the queue 
	 */
	protected abstract T pop();
	/**
	 * This method is the hook for clearing the queue on preparation of a new search.<br>
	 * It should remove all elements of the queue.
	 */
	protected abstract void clear();
	/**
	 * This method is the hook for the check if the queue is empty during the search.<br>
	 * It should return true whenever the queue is empty.
	 * 
	 * @return true only if the queue is empty.
	 */
	protected abstract boolean empty();
	
}
