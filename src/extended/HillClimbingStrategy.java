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

/**
 * This is the base of all strategies for the hill climbing search.
 * If you want to make your hill climbing more efficient you can
 * implement your own strategy by implementing this interface.<br>
 * <br>
 * The only thing you must implement is the select method, 
 * which chooses the best state from a list of successor states according
 * to the comparator.
 * The state returned must obey the following rules:
 * <ul>
	 * <li>
	 * In general it must be better then the current state
	 * according to the HillClimbing#compare method.<br>
	 * <u>Formally:</u> There exists <code>result</code> in <code>extension</code> with <code>hillclimbing.compare(current,result) &gt; 0</code>
	 * </li>
	 * <li>
	 * It must be equal to the current state according to the the HillClimbing#compare method
	 * only if there is no state better then the current state in the list of the successors.<br>
	 * <u>Formaly:</u> If there is no <code>x</code> in <code>extension</code> 
	 * with <code>hillclimbing.compare(current,x) &gt; 0</code><br>
	 * then there is a <code>result</code> in <code>extension</code> 
	 * with <code>hillclimbing.compare(current,result) &gt;= 0</code> 
	 * </li>
	 * <li>
	 * It should only be worse then the current state 
	 * if there is absolutely no better or equal state in the list of successors.<br>
	 * <u>Formally:</u> If there is no <code>x</code> in <code>extension</code>
	 * with <code>hillclimbing.compare(current,result) &gt;= 0</code><br>
	 * then select a random <code>result</code> in <code>extension</code>
	 * </li>
	 * <li>
	 * It should never be null or the current state itself.
	 * </li>
	 * <li>
	 * The way a state will be selected must always be nondeterministic.<br>
	 * For example by starting at a random position of the list looking for a better state. 
	 * </li>
 * </ul>
 * Every implementation of this interface should rely solely on the comparison method 
 * shipped with the HillClimbing class, to allow proper minimum and maximum searches
 * with this strategy.<br> 
 * <b>Note:</b> For further information on how to create a HillClimbing search 
 * which uses a custom HillClimbingStrategy see the description of the HillClimbing class.
 * .
 * @author eden06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public interface HillClimbingStrategy<T> {
	/**
	 * This method selects the best state from the given list of successors
	 * according to the current state and a comparator.<br>
	 * This method will only be called with non null and non empty arguments and
	 * should obey the following constraints:
	 * <ul>
	 * <li>
	 * In general state returned must be better then the current state
	 * according to the HillClimbing#compare method.<br>
	 * <code>hillclimbing.compare(current,result) &gt; 0</code>
	 * </li>
	 * <li>
	 * If no better state can be found then an equal state according to
	 * the HillClimbing#compare method should be returned. <br>
	 * <code>hillclimbing.compare(current,result) &gt;= 0</code>
	 * </li>
	 * <li>
	 * Only in the case when there is no better or equal state then a random
	 * state should be returned.
	 * </li>
	 * <li>
	 * The way a state will be selected must always be nondeterministic.<br>
	 * For example by starting at a random position of the list looking for a better state. 
	 * </li>
	 * <li>
	 * This method should never return null or the current state itself.
	 * </li>
	 * </ul>
	 * <b>Hint:</b> This method is the template for all hill climbing strategies.
	 * 
	 * @param hillclimbing the hill climbing search itself (which must be used as a comparator)
	 * @param extension the list of successors of the current state
	 * @param current the current state of the hill climbing search
	 * 
	 * @return the selected best state if any exists or a random successor. 
	 */
	public T select(HillClimbing<T> hillclimbing, List<T> extension, T current);

}
