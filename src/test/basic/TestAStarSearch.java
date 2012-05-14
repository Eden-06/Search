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

import basic.AStarSearch;

import core.HeuristicProblem;
import core.Search;
import junit.framework.TestCase;

public class TestAStarSearch extends TestCase {

	private class HState{
		public int id;
		public HState parent=null;
		public int depth=0;
		public double cost=0.0;
		public HState(int id,HState parent,double cost) {
			this.id=id;	this.parent=parent;	this.depth=parent.depth+1;	this.cost=parent.cost+cost;
		}
		public HState(int id){ this.id=id;	}		
		public List<Integer> getPath(){
			List<Integer> l=new LinkedList<Integer>();
			HState p=this;
			while(p!=null){	l.add(p.id);	p=p.parent;	}
			return l;
		}
	}
	
	private class HProblem implements HeuristicProblem<HState>{
		/*
		 * 5 x 5 Feld     5 x 5 Kosten
		 * 0  1  2  3  4  1.0 1.5 2.0 2.5 3.0
		 * 5  6  7  8  9  1.5 1.0 1.5 2.0 2.5 
		 *10 11 12 13 14  2.0 1.5 1.0 1.5 2.0
		 *15 16 17 18 19  3.0 2.0 1.5 1.0 1.5
		 *20 21 22 23 24  3.5 3.0 2.0 1.5 1.0
		 *
		 * i mod 5 = x    0.5*|x-y|+1.0
		 * i div 5 = y
		 * 
		 */		
		private List<Integer> order=new LinkedList<Integer>();
		private Integer i,g;
  private Search<HState> search=null;		
  public HProblem(Integer i, Integer g) {	super(); this.i=i;	this.g=g;	}
  private void mayAdd(List<HState> l,HState parent,int x,int y){	
  	if (x>=0 && x<5 && y>=0 && y<5)	l.add( new HState( x+(5*y), parent, 0.5*Math.abs(x-y)+1.0 ) );	
  }
		private int dist(int x1, int y1, int x2, int y2){ return Math.abs(x1-x2) + Math.abs(y1-y2); }
  @Override
		public HState initial() {	order.clear(); return (i==null ? null : new HState(i));	}
		@Override
		public List<HState> expand(HState state) {
			order.add(state.id);
			int x = state.id % 5, y = state.id / 5;			
			List<HState> r=new LinkedList<HState>();
			mayAdd(r,state,x+1,y);	mayAdd(r,state,x,y+1);
			if (search!=null && search.neededSteps()==5)	search.stop();
			return r;
		}
		@Override
		public boolean isGoal(HState state) {	return state.id==g;	}
		public void setSearch(Search<HState> search) {	this.search = search;	}
		public List<Integer> getOrder() { return order; }
		@Override
		public int depth(HState state) {return state.depth; }		
		@Override
		public double g(HState state) {	return state.cost;	}
		@Override
		public double h(HState state) {	return dist(g%5,g/5,state.id%5,state.id/5); }
	}
	
	private HProblem                  empty,       walk,        run,       stop;
	private AStarSearch<HState> emptySearch, walkSearch,  runSearch, stopSearch;
	
	public TestAStarSearch() {
		super("test case for the a star search");
	}

	protected void setUp() throws Exception {
		super.setUp();
		empty=new HProblem(null,null);
		emptySearch=new AStarSearch<HState>(empty);
		walk=new HProblem(0,25);
		walkSearch=new AStarSearch<HState>(walk);
		run=new HProblem(0,24);
		runSearch=new AStarSearch<HState>(run);
		stop=new HProblem(0,24);
		stopSearch=new AStarSearch<HState>(stop);
		stop.setSearch(stopSearch);
	}

	protected void tearDown() throws Exception {
		empty=null;       walk=null;       run=null;       stop=null;
		emptySearch=null; walkSearch=null; runSearch=null; stopSearch=null;
		super.tearDown();
	}

	public void testAStarSearch() {
		try{
			AStarSearch<HState> test=new AStarSearch<HState>(null);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
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
		assertTrue(String.format("%d != 251",walkSearch.neededSteps()),walkSearch.neededSteps()==251);
		assertTrue(walkSearch.branchedNodes()==walkSearch.neededSteps());
		assertTrue(walk.getOrder().equals( Arrays.asList(0, 5, 10, 1, 6, 6, 15, 11, 11, 11, 16, 16, 16, 20, 12, 12, 7, 7, 12, 12, 17, 17, 21, 21, 17, 2, 17, 16, 12, 7, 12, 22, 17, 22, 17, 22, 21, 17, 22, 17, 22, 17, 22, 22, 13, 13, 18, 18, 18, 18, 13, 18, 18, 22, 21, 18, 13, 18, 8, 23, 23, 23, 23, 23, 8, 23, 23, 23, 22, 21, 18, 22, 13, 22, 17, 13, 18, 13, 18, 13, 18, 18, 18, 18, 18, 23, 23, 23, 3, 23, 8, 23, 23, 23, 23, 23, 23, 23, 23, 22, 18, 22, 13, 18, 24, 24, 23, 23, 23, 24, 23, 19, 19, 23, 19, 19, 24, 24, 24, 19, 24, 19, 19, 24, 24, 19, 24, 8, 23, 24, 24, 24, 24, 24, 24, 14, 14, 14, 14, 22, 18, 13, 18, 24, 23, 19, 19, 24, 24, 19, 23, 24, 24, 24, 24, 24, 24, 19, 24, 24, 19, 19, 24, 24, 24, 24, 19, 24, 24, 23, 19, 19, 24, 24, 24, 23, 24, 23, 19, 19, 24, 24, 19, 24, 14, 9, 9, 22, 14, 14, 14, 19, 19, 24, 24, 19, 23, 19, 24, 24, 24, 24, 19, 24, 24, 24, 24, 23, 24, 19, 24, 14, 9, 14, 14, 19, 19, 24, 24, 19, 24, 19, 24, 24, 24, 24, 24, 23, 19, 4, 24, 24, 14, 9, 14, 19, 24, 19, 24, 24, 23, 24, 9, 14, 24, 19, 24, 14, 24, 19, 24) ));
		//secound try to show that its deterministic
		walkSearch.run();
		assertTrue(String.format("%d != null",runSearch.getResult()),walkSearch.getResult()==null);
		assertTrue(String.format("%d != 251",walkSearch.neededSteps()),walkSearch.neededSteps()==251);
		assertTrue(walkSearch.branchedNodes()==walkSearch.neededSteps());
		assertTrue(walk.getOrder().equals( Arrays.asList(0, 5, 10, 1, 6, 6, 15, 11, 11, 11, 16, 16, 16, 20, 12, 12, 7, 7, 12, 12, 17, 17, 21, 21, 17, 2, 17, 16, 12, 7, 12, 22, 17, 22, 17, 22, 21, 17, 22, 17, 22, 17, 22, 22, 13, 13, 18, 18, 18, 18, 13, 18, 18, 22, 21, 18, 13, 18, 8, 23, 23, 23, 23, 23, 8, 23, 23, 23, 22, 21, 18, 22, 13, 22, 17, 13, 18, 13, 18, 13, 18, 18, 18, 18, 18, 23, 23, 23, 3, 23, 8, 23, 23, 23, 23, 23, 23, 23, 23, 22, 18, 22, 13, 18, 24, 24, 23, 23, 23, 24, 23, 19, 19, 23, 19, 19, 24, 24, 24, 19, 24, 19, 19, 24, 24, 19, 24, 8, 23, 24, 24, 24, 24, 24, 24, 14, 14, 14, 14, 22, 18, 13, 18, 24, 23, 19, 19, 24, 24, 19, 23, 24, 24, 24, 24, 24, 24, 19, 24, 24, 19, 19, 24, 24, 24, 24, 19, 24, 24, 23, 19, 19, 24, 24, 24, 23, 24, 23, 19, 19, 24, 24, 19, 24, 14, 9, 9, 22, 14, 14, 14, 19, 19, 24, 24, 19, 23, 19, 24, 24, 24, 24, 19, 24, 24, 24, 24, 23, 24, 19, 24, 14, 9, 14, 14, 19, 19, 24, 24, 19, 24, 19, 24, 24, 24, 24, 24, 23, 19, 4, 24, 24, 14, 9, 14, 19, 24, 19, 24, 24, 23, 24, 9, 14, 24, 19, 24, 14, 24, 19, 24) ));
		
		runSearch.run();
		//System.out.println(run.getOrder().toString());
		assertFalse(String.format("%s != null",runSearch.getResult()),runSearch.getResult()==null);
		//System.out.println(runSearch.getResult().getPath().toString());
		assertTrue(String.format("%d != 24",runSearch.getResult().id),runSearch.getResult().id==24);
		assertTrue(runSearch.getResult().getPath().equals( Arrays.asList(24, 23, 18, 17, 12, 11, 6, 5, 0) ));
		assertTrue(String.format("%d != 35",runSearch.neededSteps()),runSearch.neededSteps()==35);
		assertTrue(runSearch.branchedNodes()==runSearch.neededSteps());
		assertTrue(run.getOrder().equals( Arrays.asList(0, 1, 5, 6, 6, 11, 7, 7, 12, 12, 11, 12, 12, 17, 13, 2, 13, 18, 10, 18, 17, 17, 18, 18, 13, 13, 17, 18, 18, 18, 18, 23, 16, 8, 23) ));
		//secound try to show that its deterministic
		runSearch.run();
		assertFalse(String.format("%s != null",runSearch.getResult()),runSearch.getResult()==null);
		assertTrue(String.format("%d != 24",runSearch.getResult().id),runSearch.getResult().id==24);
		assertTrue(runSearch.getResult().getPath().equals( Arrays.asList(24, 23, 18, 17, 12, 11, 6, 5, 0) ));
		assertTrue(String.format("%d != 35",runSearch.neededSteps()),runSearch.neededSteps()==35);
		assertTrue(runSearch.branchedNodes()==runSearch.neededSteps());
		assertTrue(run.getOrder().equals( Arrays.asList(0, 1, 5, 6, 6, 11, 7, 7, 12, 12, 11, 12, 12, 17, 13, 2, 13, 18, 10, 18, 17, 17, 18, 18, 13, 13, 17, 18, 18, 18, 18, 23, 16, 8, 23) ));
		
		stopSearch.run();
		//System.out.println(stop.getOrder().toString());
		assertTrue(String.format("%s != null",runSearch.getResult()),stopSearch.getResult()==null);
		assertTrue(String.format("%d != 5",stopSearch.neededSteps()),stopSearch.neededSteps()==5);
		assertTrue(stopSearch.branchedNodes()==stopSearch.neededSteps());
		assertTrue(stop.getOrder().equals( Arrays.asList(0, 1, 5, 6, 6) ));
		
	}

}
