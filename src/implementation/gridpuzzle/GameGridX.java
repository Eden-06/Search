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

package implementation.gridpuzzle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Eden_06
 *
 */
public class GameGridX{
	private boolean legal=false;
	private int[] grid;
	private int height;
	private int width;
	private Point zero;
	
	public GameGridX(int width, int height){
		this.width=width;
		this.height=height;
		grid=new int[width*height];
	}
	
	public GameGridX(GameGridX game){
		this.width=game.width;
		this.height=game.height;
		this.legal=game.legal;
		if (legal)
		 this.zero=new Point(game.zero);
  grid=Arrays.copyOf(game.grid,width*height);
	}
	
	public static int distance(int x1, int y1,int x2, int y2){
		return Math.abs(x1-x2)+Math.abs(y1-y2);
	}
	
	public int get(int x,int y){
		if ((x>=0) && (y>=0) && (x<width) && (y<height)){
			return grid[y*width+x];
		}else{
			return -1;
		}
	}
	
	public void set(int x,int y,int value){
		if ((x>=0) && (y>=0) && (x<width) && (y<height)){
			if ((value>-1) && (value<width*height)){
				grid[y*width+x]=value;
				legal=false;
			}
		}			
	}

	public boolean isLegal(){
		if (! legal){
			int[] a=Arrays.copyOf(grid,width*height);
			Arrays.sort(a);
			legal=true;
			for (int i=0;i<width*height;i++){
				if (a[i]!=i){
					legal=false;
					break;
				}
			}
			if (legal){
			 for (int i=0;i<width*height;i++){
		   if (grid[i]==0){
		   	zero=new Point(i%width,i/width);
		   	break;
		   }
 		 }
			}
		}
		return legal;
	}
	
	public int highestNumber(){
		return width*height-1;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Point positionOf(int value){
	 Point result=null;
		if (isLegal()){
	 	if (value==0){
	 		result=new Point(zero);
	 	}else{
	 		for (int i=0;i<width*height;i++){
	 			if (grid[i]==value){
	 				result=new Point(i%width,i/width);
	 				break;
	 			}
	 		}
	 	}
	 }
	 return result;
	}	
	
	public boolean move(int x,int y){
		boolean result=false;
		if (isLegal()){
			int value=get(x,y);
			if ((value>=0) && (GameGrid.distance(x,y,zero.x,zero.y)==1)){
				set(x,y,0);
				set(zero.x,zero.y,value);
			 zero.x=x;
			 zero.y=y;
			 result=true;
			}
		}
		return result;
	}
	
	public List<Point> movements(){
		List<Point> result=new LinkedList<Point>();
		if (isLegal()){
			if (zero.x+1<width){
				result.add(new Point(zero.x+1,zero.y));
			}				
			if (zero.x-1>=0){
				result.add(new Point(zero.x-1,zero.y));
			}
			if (zero.y+1<height){
				result.add(new Point(zero.x,zero.y+1));
			}
			if (zero.y-1>=0){
				result.add(new Point(zero.x,zero.y-1));
			}
		}
		return result;
	}

	public void randomize(){
		List<Integer> a=new ArrayList<Integer>();
		for (int i=0;i<width*height;i++){
			a.add(i);
		}
		Collections.shuffle(a);
		for (int i=0;i<width*height;i++){
			grid[i]=a.get(i);
		}
		legal=true;
	 for (int i=0;i<width*height;i++){
   if (grid[i]==0){
   	zero=new Point(i%width,i/width);
   	break;
   }
	 }
	}
	
	public void initialize(){
		for (int i=0;i<width*height-1;i++){
			grid[i]=i+1;
		}
		grid[width*height-1]=0;
		legal=true;
		zero=new Point(width-1,height-1);
	}
	
	public boolean comparable(GameGridX g){
		return (g.width==width) && (g.height==height);
	}
	
	public boolean equals(GameGridX g){
		if ((g.width==width) && (g.height==height)){
			return Arrays.equals(grid, g.grid);
		}else{
			return false;
		}			
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
  if (o instanceof GameGridX){
  	return equals((GameGridX) o);
  }else{
		 return false;
  }
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		//grid=null;
		grid=null;
		super.finalize();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(grid);
	}
	
	protected int[] toArray(){
		return grid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result="";
		for (int y=0;y<height;y++){
			for (int x=0;x<width;x++){
				result+=String.format("%3d",grid[y*width+x]);
			}
			if (y!=height-1) result+="\n";			
		}
		return result;
	}
	
}
