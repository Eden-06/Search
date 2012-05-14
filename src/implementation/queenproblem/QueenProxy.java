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

public class QueenProxy implements IQueenGame {

	private QueenGame proxy=null;
	private IQueenGame source=null;
	private int x,y=-1;
		
	public QueenProxy(IQueenGame state,int x, int y) {
		if (state==null) throw new IllegalArgumentException("source may never be null");
		if (x<0 || x>state.size()) throw new IllegalArgumentException("x is out of bound");
		if (y<0 || y>state.size()) throw new IllegalArgumentException("y is out of bound");		
		this.source=state;
		this.x=x;
		this.y=y;		
	}

	private QueenGame getProxy(){
		if (proxy==null){
			proxy=new QueenGame(source);
			proxy.setQueen(x,y);
		}
		return proxy;
	}
	
	@Override
	public int collisions() {	return getProxy().collisions();	}
	@Override
	public boolean hasQueen(int x, int y) {	return getProxy().hasQueen(x, y);	}
	@Override
	public void initialize() {
		if (proxy==null) proxy=new QueenGame(source.size());
		proxy.initialize();
	}
	@Override
	public boolean isSolved() {	return getProxy().isSolved(); }
	@Override
	public int positionOf(int x) {	return getProxy().positionOf(x);	}
	@Override
 public void random() {
		if (proxy==null) proxy=new QueenGame(source.size());
		proxy.random();
	}
 @Override
	public void randomFull() {
		if (proxy==null) proxy=new QueenGame(source.size());
		proxy.randomFull();
	}
	@Override
	public void setQueen(int x, int y) {	getProxy().setQueen(x, y); }
	@Override
	public int size() {	return source.size(); }
	@Override
	public int[] getGrid() {	return getProxy().getGrid();	}
	@Override
	public int compareTo(IQueenGame o) {	return getProxy().compareTo(o); }
	@Override
	public String toString() {	return getProxy().toString();	}
	@Override
	public int hashCode() {	return getProxy().hashCode(); }	
	public boolean equals(QueenGame g) {	return getProxy().equals(g); }
	@Override
	public boolean equals(Object o) {	return getProxy().equals(o);	}

}
