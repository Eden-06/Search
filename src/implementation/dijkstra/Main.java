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

package implementation.dijkstra;

import java.util.Collection;

public class Main {

	/**
	 * bsp: vl aud
	 *
	 * @param args
	 */
	public static void main(String[] args) {
  Graph g=new Graph();
  g.addEdge(1,15,8);
  g.addEdge(1,3,4);
  g.addEdge(2,5,4);
  g.addEdge(2,3,5);
  g.addEdge(3,4,5);
  g.addEdge(3,9,10);
  g.addEdge(4,8,6);
  g.addEdge(4,13,7);
  g.addEdge(5,12,6);
  g.addEdge(5,2,7);
  g.addEdge(6,3,8);
  g.addEdge(6,3,9);
  g.addEdge(6,6,7);
  g.addEdge(7,7,6);
  g.addEdge(7,11,9);
  g.addEdge(7,7,10);
    
  Dijkstra d=new Dijkstra(g);
  for ( Integer start : g.getNodes() ){
  	Collection<DijkstraState> result=d.dijkstra(start);
  	System.out.format("Dijkstra(g,%d) =>\n",start);
  	for (DijkstraState s : result){
  	 System.out.format(" %d. cost=%f, length=%d\n",s.getId(),s.getCost(),s.getDepth());
  	}
  	System.out.println();  	
  }
  
  
    
	}

}
