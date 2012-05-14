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

/**
 * This is the base of all problems which can be solved in an heuristic search.
 * <br><br>
 * Additional to the TreeProblem a HeuristicProblem consists of two methods to evaluate
 * the sum of the path costs and the proximity to a goal for a given state.
 * These two methods enables some algorithms to direct their search towards a goal.
 * <br>
 * If you implement this interface, your problem can additionally be solved by the following search algorithms:
 * <ul>
 * <li>basic.UniformCostSearch</li>
 * <li>basic.GreedySearch</li>
 * <li>basic.AStarCostSearch</li>
 * <li>basic.IterativDeepeningAStar</li>
 * </ul>
 * <b>Note:</b> The method which computes the proximity to a goal (called heuristic)
 * is of great importance for the performance of the algorithms above, (but not for the UniformCostSearch). 
 * There are some constraints for a good heuristic function which must hold for all states s in the search domain:
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
 * 
 * @author eden06
 *
 * @param <T> the type of the states used throughout the search
 */
public interface HeuristicProblem<T> extends TreeProblem<T> {
	/**
	 * This method returns the sum of all path costs from the initial to the given state.
	 * 
	 * @param state to be inspected
	 * @return the sum of the path costs from the initial state to the given state
	 */
 public double g(T state);
 /**
  * This method computes the proximity of the given state to a goal.
  * The value returned should express how much the path 
  * from the given state to a goal state may cost.
  * <b>Note:</b> For optimality and completeness it is important 
  * to obeys the following rules for all states s in the search domain.
  * <ul>
  * <li>
  * <code>h(s) &lt;= h(s') + cost(s,s')</code> for all successors <code>s'</code> of state <code>s</code>.<br> 
  * </li>
  * <li>
  * <code>h(s) &lt;= g(t)</code> where <code>t</code> is the nearest goal to state <code>s</code><br>
  * </li>
  * </ul>
  * 
  * @param state which should be used to reach a goal
  * @return an approximation of the path costs needed to reach a goal.
  */
 public double h(T state);
}
