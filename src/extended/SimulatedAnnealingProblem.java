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

import core.Problem;
/**
 * This is the base of all problems which can be solved with the simulated annealing search.<br>
 * <br>
 * Additional to the simple Problem a SimulatedAnnealingProblem consists of a method
 * which computes the difference between two states.
 * As described in the method documentation this operation 
 * assumes a total ordering over the elements in the search domain,
 * so it is possible to find a smallest or a biggest element according to the difference.
 * This is exactly what the simulated annealing is trying to do.
 * <br>
 * If you implement this interface, your problem can be solved by the following search algorithms:
 * <ul>
 * <li>extended.SimulatedAnnealing</li>
 * </ul>
 * In the following there are some important hints how to augment your simulated annealing problem:
 * <ol> 
 * <li>
 * It is in general a good idea to create the initial state at random.
 * Simply if you want to port your problem to the hill climbing algorithm
 * </li>
 * <li>
 * Make sure the SimulatedAnnealingProblem#difference() method is damn fast,
 * otherwise the simulated annealing will get stuck in computing the difference between two states.
 * </li>
 * <li>
 * Because this search selects one successor of a state at random,
 * the list of successors must not be large (But at least half of the <code>maxSame</code> property).
 * It is more important to provide significant improvements to the current state,
 * instead of a complete set of all successors.<br>
 * For example if all successors of each state are 1.0 better or 1.0 worse then the current state,
 * the search is granted to take years and yet does not find a good example. 
 * </li>
 * <li>
 * <b>Do not assume</b> that the core.Problem#expand(java.lang.Object) method is
 * called for the current state several times.
 * In fact the list of successors is maintained until the current state gets changed.
 * </li>
 * <li>
 * Whenever you have to deal with a large number of successor states for a given state,
 * think about using a proxy class which generates the successor state only if necessary.
 * </li>
 * </ol> 
 * <b>Note:</b> The simulated annealing will not use the core.Problem#isGoal(java.lang.Object) method,
 * to determine if a solution has been found. 
 * In fact the annealing process stops when there has been no improvements to the result for some time.
 * The so found result may not be the optimal solution, but is granted to be a good approximation.<br>
 * For compatibility with other search algorithms you should implement the isGoal method in a proper way.<br>
 * 
 * 
 * @see extended.SimulatedAnnealing
 * 
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain
 */
public interface SimulatedAnnealingProblem<T> extends Problem<T> {
 /**
  * Computes the difference between two states in the search domain.
	 * Returns a double value which indicates the difference between these states.
	 * For example assume an evaluation function <code>f</code>
	 * which assigns a double value to each state <code>s</code>.
	 * Then the difference of two states can be computed with the difference of the function values:
	 * <pre><code>
	 * function double f(T state){ ... }
	 * 
	 * function double difference(T x, T y){
	 * 	return f(x) - f(y)
	 * }
	 * </code></pre>
	 * In detail the difference must obey the following rules for all states x and y:
	 * <ul>
	 * <li><code>difference(x,y) == -(difference(y,x)</code></li>
	 * <li><code>( difference(x,y)&gt;0 and difference(y,z)&gt;0 ) implies difference(x,z) &gt; 0</code> <i>(The relation must be transitive.)</i></li>
	 * <li><code>( difference(x,y)&gt;0 and difference(y,z)&gt;0 ) implies difference(x,z) &gt; 0</code> <i>(The relation must be transitive.)</i></li>
	 * <li><code>difference(x,y) == 0 implies that difference(x,z) == difference(y,z) for all states z</code></li>
	 * </ul>
	 * 
	 * @param x the operand from which the difference is computed
	 * @param y the operand to which the difference is computed
	 * 
	 * @return a negative double, zero, or a positive double which indicates the difference of state x to state y.
  */
	public double difference(T x,T y);
}
