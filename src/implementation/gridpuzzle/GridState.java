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
import java.util.LinkedList;
import java.util.List;

public class GridState {
 private GameGrid current=null;
 private GridState parent=null;
 private int depth=0;
 private Point move=null;
 private int hash=0;
  
 public GridState(GameGrid current, Point move, GridState parent) {
		super();
		if (current==null) throw new IllegalArgumentException();
		else this.current = current;
		this.hash=current.hashCode();
		if (parent==null) throw new IllegalArgumentException();
		else this.parent = parent;
		if (move==null) throw new IllegalArgumentException();
		else this.move = move;
		this.depth = parent.depth + 1;
	}

	public GridState(GameGrid current) {
		super();
		if (current==null) throw new IllegalArgumentException();
		else this.current = current;
		this.hash=current.hashCode();
	}

	public GameGrid getGrid() {	return current; }
	public GridState getParent() {	return parent;	}
	public int getDepth() {	return depth; }
	public Point getMove() {	return move;	}

	public List<Point> getPath(){
 	LinkedList<Point> result=new LinkedList<Point>();
 	GridState p = this;
 	while (p.parent!=null){
 		result.addFirst(p.move);
 		p=p.parent;
 	}	
 	return result;
 }
	
	@Override
	public int hashCode() {	
		return hash;	
	}

	public boolean equals(GridState g) {
		return current.equals(g.getGrid());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof GridState)
			return equals((GridState)o);
		else
		 return false;
	}
	
	
	
	
}
