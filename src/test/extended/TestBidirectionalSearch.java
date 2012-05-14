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

package test.extended;

import java.util.LinkedList;
import java.util.List;

import extended.BidirectionalProblem;
import extended.BidirectionalSearch;
import junit.framework.TestCase;

public class TestBidirectionalSearch extends TestCase {

	private class BiProblem implements BidirectionalProblem<Integer>{
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
  private Integer i,g;
  private BidirectionalSearch<Integer> search=null;
  public BiProblem(Integer i, Integer g) {	super(); this.i = i;	this.g = g;	}
		@Override
		public Integer goal() {	return g;	}
		@Override
		public Integer initial() {	return i;	}
		@Override
		public List<Integer> implode(Integer state) {
			int x = state % 5, y = state / 5;			
			List<Integer> r=new LinkedList<Integer>();
			mayAdd(r,x-1,y);	mayAdd(r,x,y-1);
			return r;
		}
		private void mayAdd(List<Integer> l,int x,int y){
			if (x>=0 && x<5 && y>=0 && y<5)
				l.add( x+(5*y)  );
		}
		@Override
		public List<Integer> expand(Integer state) {
			int x = state % 5, y = state / 5;			
			List<Integer> r=new LinkedList<Integer>();
			mayAdd(r,x+1,y);	mayAdd(r,x,y+1);
			if (search!=null && state==6)	search.stop();
			return r;
		}
		@Override
		public boolean isGoal(Integer state) {
   fail("should never get called in binary search");
			return state==g;
		}
		public void setSearch(BidirectionalSearch<Integer> search) {	this.search = search;	}	
	}

	private BiProblem                    empty1,       empty2,       bi,       stop;
	private BidirectionalSearch<Integer> empty1Search, empty2Search, biSearch, stopSearch;
	
	public TestBidirectionalSearch() {
		super("Test case for the Bidirectional Search");
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		empty1=new BiProblem(null,24);
		empty1Search=new BidirectionalSearch<Integer>(empty1);
		empty2=new BiProblem(0,null);
		empty2Search=new BidirectionalSearch<Integer>(empty2);
		bi=new BiProblem(0,24);
		biSearch=new BidirectionalSearch<Integer>(bi);
		stop=new BiProblem(0,24);
		stopSearch=new BidirectionalSearch<Integer>(stop);
		stop.setSearch(stopSearch);
	}

	protected void tearDown() throws Exception {
		empty1=null;
		empty2=null;
		bi=null;
		stop=null;
		empty1Search=null;
		empty2Search=null;
		biSearch=null;
		stopSearch=null;
		super.tearDown();
	}
	
	public void testCreate(){
		try{
			BidirectionalSearch<Integer> test=new BidirectionalSearch<Integer>(null);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
	}

	public void testInitialize() {
		assertFalse(empty1Search.initialize());
		assertFalse(empty2Search.initialize());
		assertTrue(biSearch.initialize());
		assertTrue(stopSearch.initialize());
	}

	public void testRun() {
		empty1Search.run();
		assertTrue(empty1Search.getHitFromTop()==null);
		assertTrue(empty1Search.getHitFromBottom()==null);
		assertTrue(empty1Search.neededSteps()==0);
		assertTrue(empty1Search.branchedNodes()==0);
		
		empty2Search.run();
		assertTrue(empty2Search.getHitFromTop()==null);
		assertTrue(empty2Search.getHitFromBottom()==null);
		assertTrue(empty2Search.neededSteps()==0);
		assertTrue(empty2Search.branchedNodes()==0);
		
		biSearch.run();
		assertTrue(String.format("%d != 4",biSearch.getHitFromTop()),biSearch.getHitFromTop()==4);
		assertTrue(String.format("%d != 4",biSearch.getHitFromBottom()),biSearch.getHitFromBottom()==4);
		assertTrue(String.format("%d != 10",biSearch.neededSteps()),biSearch.neededSteps()==10);
		assertTrue(String.format("%d != 20",biSearch.branchedNodes()),biSearch.branchedNodes()==20);
		assertTrue(biSearch.branchedNodes()==2*biSearch.neededSteps());
		//secound try to show that its deterministic
		biSearch.run();
		assertTrue(String.format("%d != 4",biSearch.getHitFromTop()),biSearch.getHitFromTop()==4);
		assertTrue(String.format("%d != 4",biSearch.getHitFromBottom()),biSearch.getHitFromBottom()==4);
		assertTrue(String.format("%d != 10",biSearch.neededSteps()),biSearch.neededSteps()==10);
		assertTrue(String.format("%d != 20",biSearch.branchedNodes()),biSearch.branchedNodes()==20);
		assertTrue(biSearch.branchedNodes()==2*biSearch.neededSteps());
		
		stopSearch.run();
		assertTrue(stopSearch.getHitFromTop()==null);
		assertTrue(stopSearch.getHitFromBottom()==null);
		assertTrue(String.format("%d != 0",stopSearch.neededSteps()),stopSearch.neededSteps()==5);
		assertTrue(String.format("%d != 0",stopSearch.branchedNodes()),stopSearch.branchedNodes()==10);
		assertTrue(stopSearch.branchedNodes()==2*stopSearch.neededSteps());
		
	}

 public void testGetProblem(){
 	assertTrue(empty1Search.getProblem() instanceof BidirectionalProblem<?>);
		assertTrue(empty1Search.getProblem()==empty1);
		assertTrue(empty2Search.getProblem()==empty2);
		assertTrue(biSearch.getProblem()==bi);
		assertTrue(stopSearch.getProblem()==stop);
	}

}
