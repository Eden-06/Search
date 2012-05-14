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

package extended;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import core.Search;

/**
 * The bidirectional search algorithm is a special blind search
 * able to solve any problem implementing the core.BidirectionalProblem interface.<br>
 * <br>
 * The bidirectional search tries to find the shortest path by running
 * a forward search from the initial state and a backward search from the goal state.
 * The search succeeds if a state has been found, which is reachable in the forward search
 * and also in the backward search.<br>
 * This strategy grants completeness and a time and space complexity
 * of <code>O(b^(n/2))</code> (where b is the branching factor and n the depth of the solution).<br>
 * An optimal solution can be found only 
 * if the path costs between two states are constant or increase by a function of the depth.<br>
 * <br>
 * This algorithm is only applicable if you have a distinct goal state 
 * <i>(instead of a plain description what a goal may look like)</i>
 * and the actions which lead to a successor state can be reversed.
 * Their are only a few real world examples where these two constraints hold.
 * <i>(For Example in a path finding search where no heuristic is known.)</i><br>   
 * <br>
 * Because the search terminates when ever the same state is reachable in the top down search
 * and in the bottom up search, this search returns two resulting states.
 * One can be obtained by calling BidirectionalSearch#getHitFromTop(), and
 * the other can be obtained by calling BidirectionalSearch#getHitFromBottom().
 * With the first state you can reconstruct the path from the initial state to the found hit state.
 * With the second state you can reconstruct the path from the found hit state to the goal state
 * by reversing the actions.<br>
 * In contrast to the other search algorithms this search only allows implicit duplicate handling.
 * This has a plain reason. We have to use a java.util.HashSet to check
 * whether a state in the top down search has also been seen in the bottom up search.
 * There for two important rules for states of type T must be fulfilled to allow proper hashing:
 * <ul>
 * <li>T must implement a hashCode() method.</li>
 * <li>T must implement a equals() method.</li>
 * <li>
 * hashCode() and equals() must be conform to this rule:<br>
 * for all e,t <code>(e.hashCode()==t.hashCode()) and (e.equals(t))</code> implies <code>e==t</code>
 * </li>
 * </ul>
 * <br>
 * This class is a subclass of core.Search only for convenience,
 * because it can not be used in the following way:
 * <pre><code>
 * // creates a new problem
 * BidirectionalProblem problem=new MyBidirectionalProblem();
 * //creates a new search
 * Search search=new BidirectionalSearch(problem);
 * // initializes and runs the search 
 * search.run() 
 * // will not return the goal of this search
 * search.getResult() 
 * </code></pre>
 * Instead this class should be used in the following way:<br>
 * <pre><code>
 * // creates a new problem
 * BidirectionalProblem problem=new MyBidirectionalProblem();
 * //creates a new search
 * BidirectionSearch search=new BidirectionalSearch(problem);
 * // initializes and runs the search 
 * search.run() 
 * // will return the state reached from the initial state
 * Object s = search.getHitFromTop()
 * // will return the state reached from the goal state
 * Object t = search.getHitFromBottom()
 * // the following condition is always true
 * (s==null ? t==null : s.equals(t)) 
 * </code></pre>
 * 
 * 
 * @author Eden_06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class BidirectionalSearch<T> extends Search<T> {

	private Deque<T> topDown=new LinkedList<T>();
	private Map<Integer,T> topDownHash=new HashMap<Integer,T>();
	private Deque<T> bottomUp=new LinkedList<T>();
	private Map<Integer,T> bottomUpHash=new HashMap<Integer,T>();
	/**
	 * holds a reference to the problem to be solved 
	 */
	protected BidirectionalProblem<T> problem=null;
	/**
	 * holding the result of the bottom up search or null if nothing has been found yet
	 */
	protected T coresult=null;
	/**
	 * Creates a new bidirectional search, with the given problem.
	 * 	
	 * @param problem the BidirectionalProblem to be solved
	 */
 public BidirectionalSearch(BidirectionalProblem<T> problem) {
		super();
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
	}

	/**
	 * This method returns the number of expanded nodes during the search.
	 * <b>Note:</b> This method is no alias for neededSteps() and returns the actual number of branched nodes.
  * 
  * @return
  *  the number of branched nodes
  */
	public final int branchedNodes(){
		return 2 * neededSteps;
	}
	
	/**
	 * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
	 */
	public BidirectionalProblem<T> getProblem() {
		return problem;
	}

	/**
	 * Returns the state where the top down search and the bottom up search
	 * hit each other or null if no such state could be found.
	 * In Detail this method returns a state which belongs to the 
	 * <b>top down search</b>.<br>
	 * Please read the class description for further information.
	 * 
	 * @see BidirectionalSearch#getHitFromBottom()
	 * 
	 * @return the state where top down and bottom up search hit each other or null
	 */
	public T getHitFromTop(){ //TODO: rename
		return result;
	}
	/**
	 * Returns the state where the top down search and the bottom up search
	 * hit each other or null if no such state could be found.
	 * In Detail this method returns a state which belongs to the 
	 * <b>bottom up search</b>.<br> 
	 * Please read the class description for further information.
	 * 
	 * @see BidirectionalSearch#getHitFromTop()
	 * 
	 * @return the state where top down and bottom up search hit each other or null
	 */
	public T getHitFromBottom(){ //TODO: rename
		return coresult;
	}	

	/**
	 * This method checks if the given problem returns valid initial and goal states.
	 * 
	 * @see core.Search#canPrepare()
	 */
	@Override
	protected final boolean canPrepare() {
		return (problem.initial()!=null && problem.goal()!=null);
	}
 /**
  * This method clears the hashes and queues
	 * to prepare them for a new search run. 
  * 
  * @see core.Search#prepare()
  */
	@Override
	protected final void prepare() {
	 coresult=null;
		topDown.clear();
		topDownHash.clear();
		bottomUp.clear();
	 bottomUpHash.clear();
	}
 /**
  * This method implements the bidirectional search algorithm,
  * which can be described in the following way:
  * <ul>
  * <li>add problem.initial() to top down queue</li>
  * <li>add problem.goal() to bottom up queue</li>
  * <li>
  * <b>until</b> top down queue and bottom up queue is not empty <b>do</b>
  * <ul>
  * <li>pop first element from top down queue and assign it to upper</li>
  * <li>pop first element from bottom up queue and assign it to lower</li>
  * <li>terminate the search <b>if</b> upper can be found in the bottom up queue</li>
  * <li>terminate the search <b>if</b> lower can be found in the top down queue</li>
  * <li><b>for each</b> node problem.expand(upper) <b>do</b> add node to top down queue</li>
  * <li><b>for each</b> node problem.implode(lower) <b>do</b> add node to bottom up queue</li>
  * </ul>
  * </li>
  * </ul>
  * 
  * 
  * @see core.Search#search()
  */
	@Override
	protected final void search() {
  T upper=problem.initial(),
    lower=problem.goal(),
    fetched;
		topDown.add(upper);
  topDownHash.put(upper.hashCode(),upper);
  bottomUp.add(lower);
  bottomUpHash.put(lower.hashCode(),lower);
		while((! topDown.isEmpty()) && (! bottomUp.isEmpty()) && running()){
			//remove head of both queues
			upper=topDown.removeFirst();
			lower=bottomUp.removeFirst();
			//check if we found a state in the rand
			fetched=bottomUpHash.get(upper.hashCode());
			if ( fetched!=null && fetched.equals(upper) ){
				result=upper;
				coresult=fetched;
				break;
			}
			fetched=topDownHash.get(lower.hashCode());
			if ( fetched!=null && fetched.equals(lower) ){
			 result=fetched;
			 coresult=lower;				
			 break;
			}
			neededSteps++;
			//expand the states upper and lower
			for (T node:problem.expand(upper)){
				if (! node.equals(topDownHash.get(node.hashCode())) ){
				 topDown.addLast(node);
				 topDownHash.put(node.hashCode(),node);
				}
			}			
			for (T node:problem.implode(lower)){
				if (! node.equals(bottomUpHash.get(node.hashCode())) ){
				 bottomUp.addLast(node);
				 bottomUpHash.put(node.hashCode(),node);
				}
			}
		}		
	}

	/**
	 * Clears and Frees the queues and Hashes used in this search. 
	 * 
	 * @see core.Search#finalize()
	 */
	@Override
	protected final void finalize() throws Throwable {
		topDown.clear();
		topDownHash.clear();
		bottomUp.clear();
	 bottomUpHash.clear();
	 
		problem=null;
  topDown=null;
 	topDownHash=null;
 	bottomUp=null;
 	bottomUpHash=null;
		super.finalize();
	}
}
