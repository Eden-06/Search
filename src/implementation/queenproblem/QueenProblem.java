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

package implementation.queenproblem;

import java.util.LinkedList;
import java.util.List;

import extended.HillClimbingProblem;
import extended.SimulatedAnnealingProblem;


public class QueenProblem implements HillClimbingProblem<IQueenGame>,
		SimulatedAnnealingProblem<IQueenGame> {

	private int size;
	private IQueenGame initial;
	
	public QueenProblem(int size) {
		if (size<3) throw new IllegalArgumentException();
		else	this.size = size;
		initial=new QueenGame(size); 
		initial.initialize();
	}

	@Override
	public List<IQueenGame> expand(IQueenGame state) {
		List<IQueenGame> result=new LinkedList<IQueenGame>();
		for (int x=0;x<size;x++){ //like a complete shuffle
			IQueenGame add;
			for (int y=0;y<size;y++){
			 if (state.positionOf(x)!=y){
			 	//fast implementation using the proxy pattern
			 	add=new QueenProxy(state,x,y);			 	
			 	//damn slow implementation with lots
			 	// add=new QueenGame(state);
			 	// add.setQueen(x,y);
			 	result.add(add);
			 }
			}
		}
		return result;
	}

	@Override
	public IQueenGame initial() {	return initial; }
	@Override
	public boolean isGoal(IQueenGame state) {	return state.isSolved();	}
	@Override
	public int compare(IQueenGame a, IQueenGame b) {	return a.compareTo(b);	}
	@Override
	public double difference(IQueenGame a, IQueenGame b) {	return a.collisions()-b.collisions();	}

}
