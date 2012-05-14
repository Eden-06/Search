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

import java.util.ArrayList;
import java.util.List;

/**
 * A hill climbing strategy which selects the first successor which is better then the current state.
 * It is one of the standard HillClimbingStrategy implementations usable in the hill climbing search.<br>
 * <br>
 * It obeys all constraints from the interface definition, and is able to return
 * equal states whenever no better state can be found.<br>
 * This strategy is faster then the other because it does not look at all the successors.
 * On the other hand the hill climbing itself will need more steps to find an optimum.
 * 
 * @see extended.HillClimbing
 * @see extended.HillClimbingStrategy
 * @see extended.BestChoiceStrategy
 * 
 * @author eden06
 * 
 * @param <T> the specific type of all elements in the search domain.
 *
 */
public class FirstChoiceStrategy<T> implements HillClimbingStrategy<T> {

	/**
	 * Creates a new FirstChoiceStrategy which can be employed in the hill climbing search.
	 */
	public FirstChoiceStrategy(){
		
	}
	
	/**
	 * This method selects the first state in the extension list
	 * which is better then the current state.<br>
	 * It starts at a random position and looks for a state which is better then the current state
	 * (<code>hillclimbing.compare(current,state) &gt; 0</code>).
	 * If there is a state which is equal (in fact <code>hillclimbing.compare(current,state) = 0</code>)
	 * to the current state it is remembered, for the case that no better state can be found.
	 * 
	 * @see extended.HillClimbingStrategy#select(extended.HillClimbing, java.util.List, java.lang.Object)
	 */
	@Override
	public T select(HillClimbing<T> hc, List<T> list, T current) {
		int size=list.size(),
  base=HillClimbing.random.nextInt(size),
  compare=0;
		ArrayList<T> array=new ArrayList<T>(list);
		T next=null,
		  alternative=array.get(base);
		for (int i=0;i<size;i++){
			next=array.get((base+i) % size);
			compare=hc.compare(current,next);
			if ( compare<0 )
			 return next;
			else
				if (compare==0)
					alternative=next;				
		}
		return alternative;
	}

}
