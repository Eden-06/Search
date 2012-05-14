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

package test.basic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import core.Problem;
import core.Search;
import basic.BreadthFirstSearch;

public class TestBreadthFirstSearch extends TestCase {

	private class GProblem implements Problem<Integer>{
		/*
		 * 5 x 5 Feld
		 * 0  1  2  3  4
		 * 5  6  7  8  9 
		 *10 11 12 13 14 
		 *15 16 17 18 19
		 *20 21 22 23 24 
		 *
		 * i mod 5 = x
		 * i div 5 = y
		 * 
		 * 
		 */		
		private List<Integer> order=new LinkedList<Integer>();
		private Integer i,g;
  private Search<Integer> search=null;		
  public GProblem(Integer i, Integer g) {	super(); this.i = i;	this.g = g;	}  
  private void mayAdd(List<Integer> l,int x,int y){
			if (x>=0 && x<5 && y>=0 && y<5)	l.add( x+(5*y)  );
		}
		@Override
		public Integer initial() {	order.clear(); return i;	}
		@Override
		public List<Integer> expand(Integer state) {
			order.add(state);
			int x = state % 5, y = state / 5;			
			List<Integer> r=new LinkedList<Integer>();
			mayAdd(r,x+1,y);	mayAdd(r,x,y+1);
			if (search!=null && search.neededSteps()==5)	search.stop();
			return r;
		}
		@Override
		public boolean isGoal(Integer state) {	return state==g;	}
		public void setSearch(Search<Integer> search) {	this.search = search;	}
		public List<Integer> getOrder() { return order; }		
	}
	
	private GProblem                  empty,       walk,        run,       stop;
	private BreadthFirstSearch<Integer> emptySearch, walkSearch,  runSearch, stopSearch;
	
	public TestBreadthFirstSearch() {
		super("Test case for the breadth first search");
	}

	protected void setUp() throws Exception {
		super.setUp();
		empty=new GProblem(null,null);
		emptySearch=new BreadthFirstSearch<Integer>(empty);
		walk=new GProblem(0,null);
		walkSearch=new BreadthFirstSearch<Integer>(walk);
		run=new GProblem(0,24);
		runSearch=new BreadthFirstSearch<Integer>(run);
		stop=new GProblem(0,24);
		stopSearch=new BreadthFirstSearch<Integer>(stop);
		stop.setSearch(stopSearch);
	}

	protected void tearDown() throws Exception {
		empty=null;       walk=null;       run=null;       stop=null;
		emptySearch=null; walkSearch=null; runSearch=null; stopSearch=null;
		super.tearDown();
	}

	public void testDepthFirstSearch() {
		try{
			BreadthFirstSearch<Integer> test=new BreadthFirstSearch<Integer>(null);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
	}
	
	public void testInitialize() {
		assertFalse(emptySearch.initialize());
		assertTrue(walkSearch.initialize());
		assertTrue(runSearch.initialize());
		assertTrue(stopSearch.initialize());
	}

	public void testRun() {
		emptySearch.run();
		assertTrue(String.format("%d != null",emptySearch.getResult()),emptySearch.getResult()==null);
		assertTrue(String.format("%d != 0",emptySearch.neededSteps()),emptySearch.neededSteps()==0);
		assertTrue(String.format("%d != 0",emptySearch.branchedNodes()),emptySearch.branchedNodes()==0);
		assertTrue(empty.getOrder().isEmpty());
		
		walkSearch.run();
		//System.out.println(walk.getOrder().toString());
		assertTrue(String.format("%d != null",runSearch.getResult()),walkSearch.getResult()==null);
		assertTrue(String.format("%d != 41",walkSearch.neededSteps()),walkSearch.neededSteps()==41);
		assertTrue(walkSearch.branchedNodes()==walkSearch.neededSteps());
		assertTrue(walk.getOrder().equals( Arrays.asList(0, 1, 5, 2, 6, 10, 3, 7, 11, 15, 4, 8, 12, 16, 20, 9, 13, 17, 21, 14, 18, 22, 19, 23, 24) ));
		//secound try to show that its deterministic
		walkSearch.run();
		assertTrue(String.format("%d != null",runSearch.getResult()),walkSearch.getResult()==null);
		assertTrue(String.format("%d != 41",walkSearch.neededSteps()),walkSearch.neededSteps()==41);
		assertTrue(walkSearch.branchedNodes()==walkSearch.neededSteps());
		assertTrue(walk.getOrder().equals( Arrays.asList(0, 1, 5, 2, 6, 10, 3, 7, 11, 15, 4, 8, 12, 16, 20, 9, 13, 17, 21, 14, 18, 22, 19, 23, 24) ));
		
		runSearch.run();
		//System.out.println(run.getOrder().toString());
		assertTrue(String.format("%d != 24",runSearch.getResult()),runSearch.getResult()==24);
		assertTrue(String.format("%d != 39",runSearch.neededSteps()),runSearch.neededSteps()==39);
		assertTrue(runSearch.branchedNodes()==runSearch.neededSteps());
		assertTrue(run.getOrder().equals( Arrays.asList(0, 1, 5, 2, 6, 10, 3, 7, 11, 15, 4, 8, 12, 16, 20, 9, 13, 17, 21, 14, 18, 22, 19, 23) ));
		//secound try to show that its deterministic
		runSearch.run();
		assertTrue(String.format("%d != 24",runSearch.getResult()),runSearch.getResult()==24);
		assertTrue(String.format("%d != 39",runSearch.neededSteps()),runSearch.neededSteps()==39);
		assertTrue(runSearch.branchedNodes()==runSearch.neededSteps());
		assertTrue(run.getOrder().equals( Arrays.asList(0, 1, 5, 2, 6, 10, 3, 7, 11, 15, 4, 8, 12, 16, 20, 9, 13, 17, 21, 14, 18, 22, 19, 23) ));
		
		stopSearch.run();
		//System.out.println(stop.getOrder().toString());
		assertTrue(String.format("%d != null",runSearch.getResult()),stopSearch.getResult()==null);
		assertTrue(String.format("%d != 0",stopSearch.neededSteps()),stopSearch.neededSteps()==5);
		assertTrue(stopSearch.branchedNodes()==stopSearch.neededSteps());
		assertTrue(stop.getOrder().equals( Arrays.asList(0,1,5,2,6) ));
		
	}
	
	public void testGetProblem() {
		assertTrue(emptySearch.getProblem()==empty);
		assertTrue(walkSearch.getProblem()==walk);
		assertTrue(runSearch.getProblem()==run);
		assertTrue(stopSearch.getProblem()==stop);
	}

}
