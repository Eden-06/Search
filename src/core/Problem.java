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

import java.util.List;

/**
 * This is the base of all problems which can be solved with the Search framework.
 * <br><br>
 * A simple Problem consists of a starting state, a test if a state is a goal or not and
 * a way to generate the successor states of a known state.
 * <br>
 * If you implement this interface, your problem can be solved by the following search algorithms:
 * <ul>
 * <li>basic.DepthFirstSearch</li>
 * <li>basic.BreadthFirstSearch</li>
 * <li><i>(basic.SlowDepthFirstSearch)</i></li>
 * </ul>
 * 
 * @see basic.DepthFirstSearch
 * @see basic.BreadthFirstSearch
 * @see basic.SlowDepthFirstSearch
 * 
 * @author eden06
 *
 * @param <T> the type of the states used throughout the search
 */
public interface Problem<T> {
 /**
  * Returns the starting state of the search.<br>
  * <b>Note:</b> In some cases (e.g. HillClimbing) it is feasible to
  * return a random generated state, instead of one single starting state. 
  * 
  * @return the initial state of your search domain 
  */
	public T initial();
	/**
	 * Checks whether a given state is a goal of the search or not.
	 *  
	 * @param state to be checked
	 * @return true only if the state is one of the goal states
	 */
 public boolean isGoal(T state);
 /**
  * Generates all successor states of the given state.<br>
  * This method should never return null.
  * If the state can not be expanded than you should return an empty List.<br>
  * <b>Note:</b> Most search algorithms have the ability to check for duplicate states,
  * but if you want to implement your own duplicate handling 
  * than this method is the place to put it in.
  *  
  * @param state to be expanded
  * @return a List of all successor states
  */
 public List<T> expand(T state);
}
