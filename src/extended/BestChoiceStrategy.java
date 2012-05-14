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
 * 
 * A hill climbing strategy which selects the best successor upon all successor states.
 * It is one of the standard HillClimbingStrategy implementations usable in the hill climbing search.<br>
 * <br>
 * In general this strategy will produce good result in view search steps,
 * but is more likely to trip into local optimum during search.
 * In fact to compare all successor states is slow for large lists and
 * will produce a leak of performance for big optimization problems. 
 * 
 * @see extended.HillClimbing
 * @see extended.HillClimbingStrategy
 * @see extended.FirstChoiceStrategy
 * 
 * @author eden06
 * 
 * @param <T> the specific type of all elements in the search domain.
 *
 */
public class BestChoiceStrategy<T> implements HillClimbingStrategy<T> {

	/**
	 * Creates a new BestChoiceStrategy which can be employed in the hill climbing search.
	 */
	public BestChoiceStrategy(){
		
	}
	
	/**
	 * This method selects the best state of all states in the extension list.<br>
	 * It walks over the howl list from a random starting point and tries to find the 
	 * best state according to the HillClimbing#compare method.
	 * 
	 * @see extended.HillClimbingStrategy#select(extended.HillClimbing, java.util.List, java.lang.Object)
	 */
	@Override
	public T select(HillClimbing<T> hc, List<T> list, T current) {
		int size=list.size(),
      base=HillClimbing.random.nextInt(size);
		ArrayList<T> array=new ArrayList<T>(list);
		T best=array.get(base),  
				next=null;
		for (int i=1;i<size;i++){
			next=array.get((base+i) % size);
			if ( hc.compare(best,next)<0 )
			 best=next;			 
		}
		return best;
	}

}
