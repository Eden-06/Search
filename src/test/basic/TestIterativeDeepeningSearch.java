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
import core.Search;
import core.TreeProblem;
import basic.IterativeDeepeningSearch;

public class TestIterativeDeepeningSearch extends TestCase {

	 //TODO: detect hashing bug!
	
	private class GState{
		public Integer id;
		public GState parent=null;
		public int depth=0;
		public GState(int id,GState parent) {
			this.id=id;	this.parent=parent;	this.depth=parent.depth+1;
		}
		public GState(int id){ this.id=id;	}		
		public List<Integer> getPath(){
			List<Integer> l=new LinkedList<Integer>();
			GState p=this;
			while(p!=null){	l.add(p.id);	p=p.parent;	}
			return l;		
		}
		public boolean equals(GState s) {	return this.id==s.id;	}
		@Override
		public boolean equals(Object obj) {
			return ((obj !=null) && (obj instanceof GState)) ? equals((GState) obj) : false;
		}
		@Override
		public int hashCode() {	return id;	}		
	}
	
	private class GProblem implements TreeProblem<GState>{
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
		private List<Integer> order=new LinkedList<	Integer>();
		private Integer i,g;
  private Search<GState> search=null;		
  public GProblem(Integer i, Integer g) {	super(); this.i = i;	this.g = g;	}  
  private void mayAdd(List<GState> l,GState parent,int x,int y){	
  	if (x>=0 && x<5 && y>=0 && y<5)	l.add( new GState( x+(5*y), parent ) );	
  }
		@Override
		public GState initial() {	
			//order.clear(); 
			return (i==null ? null : new GState(i));	
		}
		@Override
		public List<GState> expand(GState state) {
		 if (state.id==0) order.clear();
			order.add(state.id);
			int x = state.id % 5, y = state.id / 5;			
			List<GState> r=new LinkedList<GState>();
			mayAdd(r,state,x+1,y);	mayAdd(r,state,x,y+1);
			mayAdd(r,state,x-1,y);	mayAdd(r,state,x,y-1);
			if (search!=null && search.neededSteps()==5)	search.stop();
			return r;
		}
		@Override
		public boolean isGoal(GState state) {	return state.id==g;	}
		public void setSearch(Search<GState> search) {	this.search = search;	}
		public List<Integer> getOrder() { return order; }
		@Override
		public int depth(GState state) { return state.depth; } //Manhatten distance		
	}
	
	private GProblem                         empty,       walk,        run,       stop,       nohash;
	private IterativeDeepeningSearch<GState> emptySearch, walkSearch,  runSearch, stopSearch, nohashSearch;
	
	public TestIterativeDeepeningSearch() {
		super("Test case for the iterative deepening search");
	}

	protected void setUp() throws Exception {
		super.setUp();
		empty=new GProblem(null,null);
		emptySearch=new IterativeDeepeningSearch<GState>(empty);
		walk=new GProblem(0,null);
		walkSearch=new IterativeDeepeningSearch<GState>(walk,1002,false);
		run=new GProblem(0,18);
		runSearch=new IterativeDeepeningSearch<GState>(run,1020);
		stop=new GProblem(0,18);
		stopSearch=new IterativeDeepeningSearch<GState>(stop,1200);
		stop.setSearch(stopSearch);
		nohash=new GProblem(0,null);
		nohashSearch=new IterativeDeepeningSearch<GState>(nohash,12,true);
	}

	protected void tearDown() throws Exception {
		empty=null;       walk=null;       run=null;       stop=null;       nohash=null;
		emptySearch=null; walkSearch=null; runSearch=null; stopSearch=null; nohashSearch=null;
		super.tearDown();
	}

	public void testIterativeDeepenignSearch() {
		try{
			IterativeDeepeningSearch<GState> test=new IterativeDeepeningSearch<GState>(null);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
		for (int i=-10;i<2;i++){
			try{
				IterativeDeepeningSearch<GState> test=new IterativeDeepeningSearch<GState>(new GProblem(0,18),i);
				fail("creating a search with a maximumDepth smaller than 0 should fail!");
				test.run();
			}catch(Exception e){
				
			}
		}
		IterativeDeepeningSearch<GState> test=new IterativeDeepeningSearch<GState>(new GProblem(0,18),2);
		test.run();
	}
	
	public void testInitialize() {
		assertFalse(emptySearch.initialize());
		assertTrue(walkSearch.initialize());
		assertTrue(runSearch.initialize());
		assertTrue(stopSearch.initialize());
		assertTrue(nohashSearch.initialize());
	}

	public void testRun() {
		emptySearch.run();
		assertTrue(String.format("%d != null",emptySearch.getResult()),emptySearch.getResult()==null);
		assertTrue(String.format("%d != 0",emptySearch.neededSteps()),emptySearch.neededSteps()==0);
		//assertTrue(String.format("%d != 0",emptySearch.branchedNodes()),emptySearch.branchedNodes()==0);
		assertTrue(empty.getOrder().isEmpty());
		
		walkSearch.run();
		//System.out.println(walk.getOrder().toString());
		assertTrue(String.format("%d != null",walkSearch.getResult()),walkSearch.getResult()==null);
		assertTrue(String.format("%d != 6798",walkSearch.neededSteps()),walkSearch.neededSteps()==6798);
		//assertTrue(walkSearch.branchedNodes()==walkSearch.neededSteps());
		assertTrue(walk.getOrder().equals( Arrays.asList(0, 1, 2, 3, 4, 9, 14, 19, 24, 23, 22, 21, 20, 15, 16, 17, 18, 13, 12, 11, 10, 5, 6, 7, 8, 6, 7, 8, 7, 8, 8, 12, 11, 10, 5, 6, 7, 11, 10, 5, 6, 10, 5, 16, 17, 18, 13, 8, 12, 7, 11, 6, 17, 18, 13, 8, 12, 7, 18, 13, 8, 18, 17, 16, 15, 10, 5, 11, 6, 12, 7, 13, 8, 13, 12, 11, 10, 5, 6, 7, 8, 8, 7, 6, 5, 8, 13, 18, 23, 22, 21, 20, 17, 16, 15, 12, 11, 10, 7, 6, 5, 7, 12, 17, 22, 21, 20, 16, 15, 11, 10, 6, 5, 6, 11, 16, 21, 20, 15, 10, 5, 5, 10, 15, 20) ));
		//secound try to show that its deterministic
		walkSearch.run();
		assertTrue(String.format("%d != null",walkSearch.getResult()),walkSearch.getResult()==null);
		assertTrue(String.format("%d != 6798",walkSearch.neededSteps()),walkSearch.neededSteps()==6798);
		//assertTrue(walkSearch.branchedNodes()==walkSearch.neededSteps());
		assertTrue(walk.getOrder().equals( Arrays.asList(0, 1, 2, 3, 4, 9, 14, 19, 24, 23, 22, 21, 20, 15, 16, 17, 18, 13, 12, 11, 10, 5, 6, 7, 8, 6, 7, 8, 7, 8, 8, 12, 11, 10, 5, 6, 7, 11, 10, 5, 6, 10, 5, 16, 17, 18, 13, 8, 12, 7, 11, 6, 17, 18, 13, 8, 12, 7, 18, 13, 8, 18, 17, 16, 15, 10, 5, 11, 6, 12, 7, 13, 8, 13, 12, 11, 10, 5, 6, 7, 8, 8, 7, 6, 5, 8, 13, 18, 23, 22, 21, 20, 17, 16, 15, 12, 11, 10, 7, 6, 5, 7, 12, 17, 22, 21, 20, 16, 15, 11, 10, 6, 5, 6, 11, 16, 21, 20, 15, 10, 5, 5, 10, 15, 20) ));
		
		runSearch.run();
		//System.out.println(run.getOrder().toString());
		assertTrue(String.format("%d != 18",runSearch.getResult().id),runSearch.getResult().id==18);
		assertTrue(String.format("%d != 6",runSearch.getResult().depth),runSearch.getResult().depth==6);
		//TODO:check if the path is correct
		assertTrue(String.format("%d != 141",runSearch.neededSteps()),runSearch.neededSteps()==141);
		//assertTrue(runSearch.branchedNodes()==runSearch.neededSteps());
		assertTrue(runSearch.getResult().getPath().equals( Arrays.asList(18, 13, 8, 3, 2, 1, 0) ));
		assertTrue(run.getOrder().equals( Arrays.asList(0, 1, 2, 3, 4, 9, 14, 8, 8, 13) ));
		//secound try to show that its deterministic
		runSearch.run();
		assertTrue(String.format("%d != 18",runSearch.getResult().id),runSearch.getResult().id==18);
		assertTrue(String.format("%d != 6",runSearch.getResult().depth),runSearch.getResult().depth==6);
		assertTrue(String.format("%d != 141",runSearch.neededSteps()),runSearch.neededSteps()==141);
		//assertTrue(runSearch.branchedNodes()==runSearch.neededSteps());
		assertTrue(runSearch.getResult().getPath().equals( Arrays.asList(18, 13, 8, 3, 2, 1, 0) ));
		assertTrue(run.getOrder().equals( Arrays.asList(0, 1, 2, 3, 4, 9, 14, 8, 8, 13) ));
		
		stopSearch.run();
		//System.out.println(stop.getOrder().toString());
		assertTrue(String.format("%d != null",stopSearch.getResult()),stopSearch.getResult()==null);
		assertTrue(String.format("%d != 5",stopSearch.neededSteps()),stopSearch.neededSteps()==5);
		//assertTrue(stopSearch.branchedNodes()==stopSearch.neededSteps());
		assertTrue(stop.getOrder().equals( Arrays.asList(0) ));
		
		nohashSearch.run();
		//System.out.println(nohash.getOrder().toString());
		assertTrue(String.format("%d != null",nohashSearch.getResult()),nohashSearch.getResult()==null);
		assertTrue(String.format("%d != 191125",nohashSearch.neededSteps()),nohashSearch.neededSteps()==191125);
		//assertTrue(nohashSearch.branchedNodes()==nohashSearch.neededSteps());
		assertTrue(nohash.getOrder().equals( Arrays.asList(0) ));
		//secound try to show that its deterministic
		nohashSearch.run();
		assertTrue(String.format("%d != null",nohashSearch.getResult()),nohashSearch.getResult()==null);
		assertTrue(String.format("%d != 191125",nohashSearch.neededSteps()),nohashSearch.neededSteps()==191125);
		//assertTrue(nohashSearch.branchedNodes()==nohashSearch.neededSteps());
		assertTrue(nohash.getOrder().equals( Arrays.asList(0) ));
		
	}
	
	public void testGetProblem() {
		assertTrue(emptySearch.getProblem()==empty);
		assertTrue(walkSearch.getProblem()==walk);
		assertTrue(runSearch.getProblem()==run);
		assertTrue(stopSearch.getProblem()==stop);
		assertTrue(nohashSearch.getProblem()==nohash);
	}
	
	public void testGetMaximumDepth(){
		assertTrue(emptySearch.getMaximumDepth()==1000);
		assertTrue(walkSearch.getMaximumDepth()==1002);
		assertTrue(runSearch.getMaximumDepth()==1020);
		assertTrue(stopSearch.getMaximumDepth()==1200);
		assertTrue(nohashSearch.getMaximumDepth()==12);
	}

	public void subtest(IterativeDeepeningSearch<GState> test){
		int d=test.getMaximumDepth();
		test.run();
		test.setMaximumDepth(10);
		assertTrue(test.getMaximumDepth()==10);
		test.initialize();
		test.setMaximumDepth(5);
		assertFalse(test.getMaximumDepth()==5);
		test.run();
		test.setMaximumDepth(d);
		assertTrue(test.getMaximumDepth()==d);		
	}
	
	public void subtest2(IterativeDeepeningSearch<GState> test){
		int d=test.getMaximumDepth();
		test.run();
		test.setMaximumDepth(10);
		assertTrue(test.getMaximumDepth()==10);
		test.initialize();
		test.setMaximumDepth(5);
		assertTrue(test.getMaximumDepth()==5);
		test.run();
		test.setMaximumDepth(d);
		assertTrue(test.getMaximumDepth()==d);		
	}
	
	public void testSetMaximumDepth(){
  subtest2(emptySearch);
  subtest(walkSearch);
  subtest(runSearch);
  subtest(stopSearch);
  subtest(nohashSearch);
	}
}
