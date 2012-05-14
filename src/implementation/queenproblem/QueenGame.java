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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * @author Eden_06
 *
 */
public class QueenGame implements IQueenGame {
 private int[] grid;
	private int size=1;
	private int collisions=-1;
	
	public QueenGame(int n){
		if (n>1)	size=n;
	 grid=new int[size];	 
	}
	
	public QueenGame(IQueenGame game){
		size=game.size();
		grid=Arrays.copyOf(game.getGrid(),game.size());
		collisions=game.collisions();
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#initialize()
	 */
	public void initialize(){
	 Arrays.fill(grid,0);
	 collisions=(size*(size-1)) / 2;
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#randomFull()
	 */
	public void randomFull(){
		Random rand=new Random();
		for(int i=0;i<size;i++){
			grid[i]=rand.nextInt(size);			
		}
		collisions=-1;
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#random()
	 */
	public void random(){
		List<Integer> a=new ArrayList<Integer>(size);
		int i;
		for(i=0;i<size;i++){
			a.add(i);
		}
		Collections.shuffle(a);
		i=0;
		for(Integer j: a){
			grid[i++]=j.intValue();
		}
		collisions=-1;
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#setQueen(int, int)
	 */
	public void setQueen(int x,int y){
		if ((x>=0) && (y>=0) && (x<size) && (y<size)){
			grid[x]=y;
			collisions=-1;
		}
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#positionOf(int)
	 */
	public int positionOf(int x){
		return ( ((x>=0) && (x<size)) ? grid[x] : -1 );
	}
		
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#hasQueen(int, int)
	 */
	public boolean hasQueen(int x,int y){
		if ((x>=0)&& (y>=0) && (x<size) && (y<size)){
			return (grid[x]==y);
		}else{
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#collisions()
	 */
	public int collisions(){
		if (collisions==-1){
		 collisions=0;
		 for(int i=0;i<size;i++){
		 	for(int j=0;j<size;j++){
		 		if (i==j) continue;
		 		if (grid[i]==grid[j]) collisions++;
		 		if (Math.abs(grid[i]-grid[j])==Math.abs(i-j)) collisions++; 
		 	}
		 }
		 collisions=collisions/2;
		}
		return collisions;
	}

	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#isSolved()
	 */
	public boolean isSolved(){
		return collisions()==0;
	}
	
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#size()
	 */
	public int size(){
		return size;
	}
	
	public boolean equals(IQueenGame g){
		return Arrays.equals(grid,g.getGrid());		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof IQueenGame){
			return equals((IQueenGame) o);
		}else{
			return false;
		}	
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(grid);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/* (non-Javadoc)
	 * @see implementation.queenproblem.IQueenGame#toString()
	 */
	@Override
	public String toString() {
		String result="";
		for (int y=0;y<size;y++){
		 for(int x=0;x<size;x++){
				if (grid[x]==y){
					result+="D";
				}else{
					result=( (x+y)%2==0 ? result+"#" : result+" ");
				}
			}
			if (y!=size-1) result+="\n";
		}
		return result;
	}

	@Override
	public int compareTo(IQueenGame o) {
		return new Integer(collisions()).compareTo(o.collisions());
	}

	@Override
	public int[] getGrid() {
		return grid;
	}
	
}

	
