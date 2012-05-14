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

import java.util.Comparator;

import core.Problem;
/**
 * This is the base of all problems which can be solved with the hill climbing search.
 * <br><br>
 * Additional to the simple Problem a HillClimbingProblem consists of a comparison
 * method which imposes a total ordering over the elements in the search domain.
 * As a conclusion it is now possible to find a smallest or a biggest element in the search domain,
 * and this is exactly what the hill climbing is trying to do.
 * <br>
 * If you implement this interface, your problem can be solved by the following search algorithms:
 * <ul>
 * <li>extended.HillClimbingSearch</li>
 * </ul>
 * In the following there are some important hints how to augment your hill climbing problem:
 * <ol> 
 * <li>
 * In order to use HillClimbing with random restart 
 * <i>(as explained in the description of the HillClimbing class)</i>,
 * you should return a random generated state whenever the core.Problem#initial() method is called.
 * </li>
 * <li>
 * Make sure the HillClimbingProblem#compare() method is damn fast,
 * otherwise the hill climbing search will get stuck in comparing two states.
 * </li>
 * <li>
 * Whenever you have to deal with a large number of successor states for a given state,
 * think about using a proxy class which generates the successor state only if necessary.
 * Look at the QueenProxy class in implementation.queenproblem as an example.
 * </li>
 * <li>
 * <i>
 * For the skilled, it is possible to create your own List implementation,
 * which is created with a given state and creates the successor states only if the are requested.
 * You have to take good care on how to change the semantic of the list interface!
 * </i>
 * </li>
 * </ol> 
 * <b>Note:</b> The hill climbing search will not use the core.Problem#isGoal(java.lang.Object) method,
 * to determine if a solution has been found. 
 * In fact the hill climbing search assumes that a state is a goal if it has no better successor states.<br>
 * For compatibility with other search algorithms you should implement the isGoal method in a proper way.<br>
 *  
 * @see java.util.Comparator
 * @see extended.HillClimbing
 * 
 * @author eden06
 *
 * @param <T> the type of the states used throughout the search 
 */
public interface HillClimbingProblem<T> extends Problem<T>, Comparator<T> {

}
