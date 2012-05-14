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

package test.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.BestFirstSearch;
import core.HeuristicProblem;
import junit.framework.TestCase;

public class TestBestFirstSearch extends TestCase {

 private class ElemProblem implements HeuristicProblem<Integer>{
  double[] space=new double[10];
 	public ElemProblem(){	for (int i=0;i<10;i++)	space[i]=0.0;	}
		@Override
		public double g(Integer state) { return space[state]; }
		@Override
		public double h(Integer state) {	if (state==4) space[4]=0.0; return 0.0;	}
		@Override
		public int depth(Integer state) {	return 0;	}
		@Override
		public List<Integer> expand(Integer state) {
   int b=(2*state);
   List<Integer> r=new LinkedList<Integer>();
   if (b+1<10) r.add(b+1);
   if (b+2<10) r.add(b+2);
			return r;
		}
		@Override
		public Integer initial() {	return 0;	}
		@Override
		public boolean isGoal(Integer state) {	return state==9;	}
		public void random() {	Random r=new Random();	for (int i=0;i<10;i++)	space[i]=r.nextDouble();	}
		public void perfect() { 
			for (int i=0;i<10;i++)	space[i]=1.0;
			space[0]=0.0;	space[1]=0.0;	space[4]=2.0;	space[9]=0.0;			
		}
 }
	
	private class BinSearch extends BestFirstSearch<Integer>{
		public BinSearch(HeuristicProblem<Integer> problem, boolean update) {	super(problem, update);	}
		public BinSearch(HeuristicProblem<Integer> problem) {	super(problem);	}
		@Override
		public Double f(Integer state) {	return heuristicProblem.g(state);	}
		@Override
		public boolean add(Integer state){
			boolean r=super.add(state);
			heuristicProblem.h(state);
			return r;
		}
	}
	
	private ElemProblem fixed, update, random;
	private BinSearch   fixedSearch,  updateSearch,  randomSearch;
	
	public TestBestFirstSearch() {
		super("Test case for the BestFirstSearch class");
	}

	protected void setUp() throws Exception {
		super.setUp();
		fixed=new ElemProblem();
		fixed.perfect();
		fixedSearch=new BinSearch(fixed,false);
		
		update=new ElemProblem();
		update.perfect();
		updateSearch=new BinSearch(update,true);
		
		random=new ElemProblem();
		random.random();
		randomSearch=new BinSearch(random);		
	}

	protected void tearDown() throws Exception {
		fixed=null; update=null; random=null;
		fixedSearch=null;  updateSearch=null;  randomSearch=null;
		super.tearDown();
	}

	public void testBestFirstSearch() {
		try{
			BinSearch test=new BinSearch(null,false);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
	}

	public void testGetProblem() {
		assertTrue(fixedSearch.getProblem() instanceof HeuristicProblem<?>);
		assertTrue(fixedSearch.getProblem()==fixed);
		assertTrue(updateSearch.getProblem()==update);
		assertTrue(randomSearch.getProblem()==random);
	}

	public void testRun() {
  fixedSearch.run();
  assertTrue(fixedSearch.getResult()==9);
  assertTrue(fixedSearch.neededSteps()==4);
  assertTrue(fixedSearch.branchedNodes()==4);
  
  updateSearch.run();
  assertTrue(updateSearch.getResult()==9);
  assertTrue(updateSearch.neededSteps()==3);
  assertTrue(updateSearch.branchedNodes()==3);
  
  randomSearch.run();
  assertTrue(randomSearch.getResult()==9);
  assertTrue(randomSearch.neededSteps()<10);
  assertTrue(randomSearch.branchedNodes()==randomSearch.neededSteps());  
	}


}
