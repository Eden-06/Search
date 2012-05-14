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

import basic.DepthLimitedSearch;
import core.Search;
import core.TreeProblem;
import junit.framework.TestCase;

public class TestDepthLimitedSearch extends TestCase {


	private class GProblem implements TreeProblem<Integer>{
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
		@Override
		public int depth(Integer state) {return Math.abs( (state%5) + (state/5) ); } //Manhatten distance	
	}
	
	private GProblem                    empty,       walk,        run,       stop,       nohash,       limited;
	private DepthLimitedSearch<Integer> emptySearch, walkSearch,  runSearch, stopSearch, nohashSearch, limitedSearch;
	
	public TestDepthLimitedSearch() {
		super("Test case for the depth limited search");
	}

	protected void setUp() throws Exception {
		super.setUp();
		empty=new GProblem(null,null);
		emptySearch=new DepthLimitedSearch<Integer>(empty,1001);
		walk=new GProblem(0,null);
		walkSearch=new DepthLimitedSearch<Integer>(walk,1002,false);
		run=new GProblem(0,24);
		runSearch=new DepthLimitedSearch<Integer>(run,1003);
		stop=new GProblem(0,24);
		stopSearch=new DepthLimitedSearch<Integer>(stop,1004);
		stop.setSearch(stopSearch);
		nohash=new GProblem(0,null);
		nohashSearch=new DepthLimitedSearch<Integer>(nohash,1005,true);
		limited=new GProblem(0,24);
		limitedSearch=new DepthLimitedSearch<Integer>(limited,5);
	}

	protected void tearDown() throws Exception {
		empty=null;       walk=null;       run=null;       stop=null;       nohash=null;       limited=null;
		emptySearch=null; walkSearch=null; runSearch=null; stopSearch=null; nohashSearch=null; limitedSearch=null;
		super.tearDown();
	}

	public void testDepthFirstSearch() {
		try{
			DepthLimitedSearch<Integer> test=new DepthLimitedSearch<Integer>(null,10);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
		
		for (int i=0;i>-100;i--){
			try{
				DepthLimitedSearch<Integer> test=new DepthLimitedSearch<Integer>(empty,i);
				fail("creating a search with a limit of "+i+" should fail!");
				test.run();
			}catch(Exception e){
				
			}
		}
	}
	
	public void testInitialize() {
		assertFalse(emptySearch.initialize());
		assertTrue(walkSearch.initialize());
		assertTrue(runSearch.initialize());
		assertTrue(stopSearch.initialize());
		assertTrue(nohashSearch.initialize());
		assertTrue(limitedSearch.initialize());		
	}
	
	public void subRun(DepthLimitedSearch<Integer> search,
			                 GProblem problem, 
			                 Integer result, 
			                 int steps, 
			                 List<Integer> order){
		search.run();
		//System.out.println(walk.getOrder().toString());
		assertTrue(String.format("%d != "+result,search.getResult()),search.getResult()==result);
		assertTrue(String.format("%d != "+steps,search.neededSteps()),search.neededSteps()==steps);
		assertTrue(search.branchedNodes()==search.neededSteps());
		assertTrue(problem.getOrder().equals( order ));
		//secound try to show that its deterministic
		search.run();
		assertTrue(String.format("%d != "+result,search.getResult()),search.getResult()==result);
		assertTrue(String.format("%d != "+steps,search.neededSteps()),search.neededSteps()==steps);
		assertTrue(search.branchedNodes()==search.neededSteps());
		assertTrue(problem.getOrder().equals( order ));	
		
	}

	public void testRun() {
		emptySearch.run();
		assertTrue(String.format("%d != null",emptySearch.getResult()),emptySearch.getResult()==null);
		assertTrue(String.format("%d != 0",emptySearch.neededSteps()),emptySearch.neededSteps()==0);
		assertTrue(String.format("%d != 0",emptySearch.branchedNodes()),emptySearch.branchedNodes()==0);
		assertTrue(empty.getOrder().isEmpty());
		
		
		subRun(walkSearch,walk,null,41,Arrays.asList(0, 1, 2, 3, 4, 9, 14, 19, 24, 8, 13, 18, 23, 7, 12, 17, 22, 6, 11, 16, 21, 5, 10, 15, 20));
		
		subRun(runSearch,run,24,8,Arrays.asList(0,1,2,3,4,9,14,19));
				
		stopSearch.run();
		//System.out.println(stop.getOrder().toString());
		assertTrue(String.format("%d != null",runSearch.getResult()),stopSearch.getResult()==null);
		assertTrue(String.format("%d != 5",stopSearch.neededSteps()),stopSearch.neededSteps()==5);
		assertTrue(stopSearch.branchedNodes()==stopSearch.neededSteps());
		assertTrue(stop.getOrder().equals( Arrays.asList(0,1,2,3,4) ));
		
		subRun(nohashSearch,nohash,null,251,Arrays.asList(0, 1, 2, 3, 4, 9, 14, 19, 24, 8, 9, 14, 19, 24, 13, 14, 19, 24, 18, 19, 24, 23, 24, 7, 8, 9, 14, 19, 24, 13, 14, 19, 24, 18, 19, 24, 23, 24, 12, 13, 14, 19, 24, 18, 19, 24, 23, 24, 17, 18, 19, 24, 23, 24, 22, 23, 24, 6, 7, 8, 9, 14, 19, 24, 13, 14, 19, 24, 18, 19, 24, 23, 24, 12, 13, 14, 19, 24, 18, 19, 24, 23, 24, 17, 18, 19, 24, 23, 24, 22, 23, 24, 11, 12, 13, 14, 19, 24, 18, 19, 24, 23, 24, 17, 18, 19, 24, 23, 24, 22, 23, 24, 16, 17, 18, 19, 24, 23, 24, 22, 23, 24, 21, 22, 23, 24, 5, 6, 7, 8, 9, 14, 19, 24, 13, 14, 19, 24, 18, 19, 24, 23, 24, 12, 13, 14, 19, 24, 18, 19, 24, 23, 24, 17, 18, 19, 24, 23, 24, 22, 23, 24, 11, 12, 13, 14, 19, 24, 18, 19, 24, 23, 24, 17, 18, 19, 24, 23, 24, 22, 23, 24, 16, 17, 18, 19, 24, 23, 24, 22, 23, 24, 21, 22, 23, 24, 10, 11, 12, 13, 14, 19, 24, 18, 19, 24, 23, 24, 17, 18, 19, 24, 23, 24, 22, 23, 24, 16, 17, 18, 19, 24, 23, 24, 22, 23, 24, 21, 22, 23, 24, 15, 16, 17, 18, 19, 24, 23, 24, 22, 23, 24, 21, 22, 23, 24, 20, 21, 22, 23, 24));
		
		subRun(limitedSearch,limited,null,21,Arrays.asList(0, 1, 2, 3, 4, 8, 7, 12, 6, 11, 16, 5, 10, 15, 20));
				
	}
	
	public void testGetProblem() {
		assertTrue(emptySearch.getProblem()==empty);
		assertTrue(walkSearch.getProblem()==walk);
		assertTrue(runSearch.getProblem()==run);
		assertTrue(stopSearch.getProblem()==stop);
		assertTrue(nohashSearch.getProblem()==nohash);
		assertTrue(limitedSearch.getProblem()==limited);
		
	}
	
	public void testDepthLimit() {
		assertTrue(emptySearch.depthLimit()==1001);
		assertTrue(walkSearch.depthLimit()==1002);
		assertTrue(runSearch.depthLimit()==1003);
		assertTrue(stopSearch.depthLimit()==1004);
		assertTrue(nohashSearch.depthLimit()==1005);
		assertTrue(limitedSearch.depthLimit()==5);		
	}

}
