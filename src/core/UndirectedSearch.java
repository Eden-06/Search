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

import java.util.Random;

/**
 * This subclass of core.Search is the abstraction for every undirected search algorithm
 * such as extended.HillClimbing or extended.SimulatedAnnealing.
 * Inherent for all such algorithms is that they look for an optimum
 * in a search space. According to that a undirected search can
 * either search for a maximum or a minimum optimum.
 * If a subclass does not change the behavior you have to give the search direction
 * as argument to the constructor of this class.<br>
 * (Most of the subclasses use the minimum search as default.) 
 * <br>
 * If you want to change the search direction after creation you only have to use
 * one of the following methods before initializing the search.<br>  
 * <ul>
 * <li>minimum search: <code>searchMinimum()</code></li>
 * <li>maximum search: <code>searchMaximum()</code></li>
 * </ul>
 * These methods will only return true if the new search direction has been changed.
 * <hr>
 * If you implement your own undirected search make it a subclass of this Class
 * in order to provide a transparent semantic for the search directions.<br>
 * <b>Note:</b> There is a static field holding an instance of the java.util.Random,
 * which should be used as random value generator.
 * 
 * @param <T>
 *  the specific type of the states in the search space.
 *  
 * @see extended.HillClimbing
 * @see extended.SimulatedAnnealing
 * 
 * @author Eden_06
 */
public abstract class UndirectedSearch<T> extends Search<T>{
	/**
	 * holds a global random value generator, which should be used by all subclasses
	 */
	public static final Random random=new Random(System.currentTimeMillis());
	
	/**
  * holds a switch determine whether the search looks for minimum values or not
  */
	private boolean minimum=true;

	
	/**
	 * Creates a new undirected search in the desired direction.<br>
	 * If the argument is true the search looks for a minimum other wise it looks for a maximum.
	 * 
	 * @param minimum
	 *  flag indicating whether the search looks for minimum values or not
	 */
	public UndirectedSearch(boolean minimum) {
		super();
		this.minimum = minimum;
	}
	
	/**
	 * This method tells the search to look for minimum values.
	 * <b>Note:</b> This operation is effective only before initialization.
	 * 
	 * @return
	 *  true if the search direction is set to minimum
	 */
	public final boolean searchMinimum(){
		if (!initialized()) minimum=true;
		return minimum;
	}
	/**
	 * This method tells the search to look for maximum values.
	 * <b>Note:</b> This operation is effective only before initialization.
	 * 
	 * @return
	 *  true if the search direction is set to maximum
	 */
	public final boolean searchMaximum(){
		if (!initialized()) minimum=false;
		return !minimum;
	}	
	/**
	 * This method tells the subclasses whether it should search for minimum
	 * or maximum values.
	 * 
	 * @return 
	 *  true if the search looks for minimum values<br>
	 *  false if the search looks for maximum values
	 */
	public final boolean minimum(){
		return minimum;
	}
	
	
	
}
