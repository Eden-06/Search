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

import java.util.List;
import core.Problem;
/**
 * This is the base of all problems which can be solved in the bidirectional search.
 * <br><br>
 * Additional to the simple Problem a BidirectionalProblem consists of a methods
 * to return the goal state and to create predecessor states from a given one.
 * <br>
 * If you implement this interface, your problem can be solved by the following search algorithms:
 * <ul>
 * <li>extended.BidirectionalSearch</li>
 * </ul>
 * <b>Note:</b> The bidirectional search will not use the core.Problem#isGoal(java.lang.Object) method,
 * to determine if a solution has been found. In fact a BidirectionalProblem must know exactly one goal state.
 * For compatibility with other search algorithms you should implement the isGoal method in the following way:<br>
 * <pre><code>
 * public boolean isGoal(T state){
 * 	return (goal()==null ? false : goal().equals(state) );
 * }
 * </code></pre>  
 * 
 * @see extended.BidirectionalSearch
 *  
 * @author eden06
 *
 * @param <T> the type of the states used throughout the search
 */
public interface BidirectionalProblem<T> extends Problem<T> {
 /**
  * Returns the goal state of the search.
  * <b>Note:</b> This state will be used as the starting state of the backward 
  * search in extended.BidirectionalSearch. 
  * 
  * @return the goal state of your search domain 
  */
	public T goal();
	/**
  * Generates all predecessor states of the given state.<br>
  * This method should never return null.
  * If the state can not be backtracked than you should return an empty List.<br>
  * <b>Note:</b> Most search algorithms have the ability to check for duplicate states,
  * but if you want to implement your own duplicate handling 
  * than this method is the place to put it in.
  *  
  * @param state to be backtracked
  * @return a List of all predecessor states
	 */
 public List<T> implode(T state);
}
