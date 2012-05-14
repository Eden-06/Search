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

package basic;

import java.util.LinkedList;

import core.DirectedSearch;
import core.Problem;

/**
 * A slow implementation of the depth first search algorithm which uses the general search algorithm
 * implemented in the core.DirectedSearch. This depth first search algorithm is a blind search
 * able to solve any problem implementing the core.Problem interface.<br>
 * <br>
 * <b>Note:</b> This implementation is only for completeness and should not be used.
 * Use the basic.DepthFirstSearch whenever you need to use a depth first search.
 * 
 * @see basic.DepthFirstSearch
 * 
 * @author eden06
 *
 * @param <T>  the specific type of all elements in the search domain.
 */
public class SlowDepthFirstSearch<T> extends DirectedSearch<T> {
 /**
  * holds the queue used during the search
  */
	private LinkedList<T> queue=new LinkedList<T>();
	
	/**
  * This method creates a new BreadthFirstSearch
  * where implicit duplicate duplicate handling is enabled.
  * 
	 * @param problem
	 */
	public SlowDepthFirstSearch(Problem<T> problem) {	
		super(problem);	
	}
	
	/**
	 * This method creates a new SlowDepthFirstSearch.
  * According to the given flag the search will 
  * turn off the internal duplicate handling mechanism or not.<br>
  * 
	 * @param problem to be solved
	 * @param noHash flag indicating that duplicates should not be handled
	 */
	public SlowDepthFirstSearch(Problem<T> problem,boolean noHash) {
		super(problem,noHash);
	}	
	
 /**
  * @see core.DirectedSearch#add(java.lang.Object)
  */
	@Override
	protected boolean add(T state) {
		queue.addFirst(state);
		return true;
	}

	/**
	 * @see core.DirectedSearch#clear() 
	 */
	@Override
	protected void clear() {
  queue.clear();
	}
	
 /**
  * @see core.DirectedSearch#empty()
  */
	@Override
	protected boolean empty() {
		return queue.isEmpty();
	}
	
 /**
  * @see core.DirectedSearch#pop()
  */
	@Override
	protected T pop() {
		return queue.pop();
	}

}
