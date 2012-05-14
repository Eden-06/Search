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
 * This is the base of all problems which can be solved in a tree based search.
 * <br><br>
 * Additional to the simple Problem a TreeProblem consists of a method to check how deep
 * a given state is in the search tree.
 * <br>
 * If you implement this interface, your problem can additionally be solved by the following search algorithms:
 * <ul>
 * <li>basic.DepthLimitedSearch</li>
 * <li>basic.IterativDeepeningSearch</li>
 * </ul>
 * 
 * @author eden06
 *
 * @see core.Problem
 * @param <T> the type of the states used throughout the search
 */
public interface TreeProblem<T> extends Problem<T> {
	/**
	 * Returns the depth of the given state in the search tree.<br> 
	 * Note: Only initial states should have a depth of 0.
	 * 
	 * @param state the state to be inspected
	 * @return a positive integer indicating the depth of a state.
	 */
 public int depth(T state);
}
