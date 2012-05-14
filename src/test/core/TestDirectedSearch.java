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

import core.DirectedSearch;
import core.Problem;
import junit.framework.TestCase;

public class TestDirectedSearch extends TestCase {

	//TODO: extend test to noHash uses
	
	private class EmptyProblem implements Problem<Integer>{
		@Override
		public List<Integer> expand(Integer state) { fail("should never get called");	return null;	}
		@Override
		public Integer initial() {	return null;	}
		@Override
		public boolean isGoal(Integer state) {	fail("should never get called"); return false;	}		
	}

	private class IntProblem implements Problem<Integer>{
		@Override
		public List<Integer> expand(Integer state) {	List<Integer> r=new LinkedList<Integer>();r.add(state+1);return r;	}
		@Override
		public Integer initial() {	return 1;	}
		@Override
		public boolean isGoal(Integer state) {	return state==10;	}		
	}
	
	private class StopProblem implements Problem<Integer>{
		private DirectedSearch<Integer> search=null;
		public void setSearch(DirectedSearch<Integer> search) {	this.search = search;	}
		@Override
		public List<Integer> expand(Integer state) {	List<Integer> r=new LinkedList<Integer>();r.add(state+1);return r;	}
		@Override
		public Integer initial() {	return 1;	}
		@Override
		public boolean isGoal(Integer state) {	if (state==10) search.stop(); return false;	}
	}
	
	private class HashProblem implements Problem<Integer>{
		private IntSearch search=null;
		public void setSearch(IntSearch search) {	this.search = search;	}
		@Override
		public List<Integer> expand(Integer state) {	List<Integer> r=new LinkedList<Integer>();r.add(state+1);return r;	}
		@Override
		public Integer initial() {	return 1;	}
		@Override
		public boolean isGoal(Integer state) {	if (search.hashsize()==5) search.stop(); return state==10;	}		
	}
	
	private class IntSearch extends DirectedSearch<Integer>{
  private int i=999;
		private int is=999;
  public IntSearch(Problem<Integer> problem) {	super(problem);	}
  public int hashsize() {	return hash.size();	}
		public IntSearch(Problem<Integer> problem,boolean nohash) {	super(problem,nohash);	}
		@Override
		public boolean add(Integer state) {	i++; is=state;	return true;	}
		@Override
		public void clear() { i=-1;	}
		@Override
		public boolean empty() {	return i==-1;	}
		@Override
		public Integer pop() {	i--; return is;	}
		public Integer i() { return i; }
		public Integer is() { return is; }
	}
	
	private IntSearch emptySearch, intSearch, stopSearch, hashSearch, nohashSearch;
	private Problem<Integer> empty, integer;
	private StopProblem stop;
	private HashProblem hash, nohash;
	
	
	public TestDirectedSearch() {
		super("Test case for the DirectedSearch");
	}

	protected void setUp() throws Exception {
		super.setUp();
		empty=new EmptyProblem();
		emptySearch=new IntSearch(empty);
		integer=new IntProblem();
		intSearch=new IntSearch(integer);
		stop=new StopProblem();
		stopSearch=new IntSearch(stop);
		stop.setSearch(stopSearch);
		hash=new HashProblem();
		hashSearch=new IntSearch(hash);
		hash.setSearch(hashSearch);
		nohash=new HashProblem();
		nohashSearch=new IntSearch(nohash,true);
		nohash.setSearch(nohashSearch);
		
	}

	protected void tearDown() throws Exception {
		emptySearch=null; intSearch=null; stopSearch=null; hashSearch=null; nohashSearch=null;
		empty=null; integer=null; stop=null; hash=null; nohash=null;
		super.tearDown();
	}

	public void testInitialize() {
		assertFalse(emptySearch.initialize());
		assertTrue(emptySearch.i()==999);
		assertTrue(emptySearch.is()==999);
		assertTrue(intSearch.initialize());
		assertTrue(intSearch.i()==-1);
		assertTrue(intSearch.is()==999);
		intSearch.run(); //rest

	}

	public void testRun() {
	 emptySearch.run();
	 assertTrue(emptySearch.getResult()==null);
	 assertTrue(emptySearch.neededSteps()==emptySearch.branchedNodes());
	 assertTrue(emptySearch.neededSteps()==0);
	 assertTrue(0==emptySearch.branchedNodes());
	 
	 intSearch.run();
	 assertTrue(intSearch.getResult()==10);
	 assertTrue(intSearch.neededSteps()==intSearch.branchedNodes());
	 assertTrue(intSearch.neededSteps()==9);
	 assertTrue(9==intSearch.branchedNodes());
	 assertTrue(intSearch.i()==-1);
	 assertTrue(intSearch.is()==10);
	 
	 stopSearch.run();
	 assertTrue(stopSearch.getResult()==null);
	 assertTrue(stopSearch.neededSteps()==stopSearch.branchedNodes());
	 assertTrue(stopSearch.neededSteps()==10);
	 assertTrue(10==stopSearch.branchedNodes());
	 assertTrue(stopSearch.i()==0);
	 assertTrue(stopSearch.is()==11);
	 
	 hashSearch.run();
	 assertTrue(hashSearch.getResult()==null);
	 assertTrue(hashSearch.neededSteps()==hashSearch.branchedNodes());
	 assertTrue(String.format("%d",hashSearch.neededSteps() ),hashSearch.neededSteps()==6);
	 assertTrue(hashSearch.i()==0);
	 assertTrue(hashSearch.is()==7);
	 
	 nohashSearch.run();
	 assertTrue(nohashSearch.getResult()==10);
	 assertTrue(nohashSearch.neededSteps()==nohashSearch.branchedNodes());
	 assertTrue(nohashSearch.neededSteps()==9);
	 assertTrue(nohashSearch.i()==-1);
	 assertTrue(nohashSearch.is()==10);
	}

	public void testDirectedSearch() {
		try{
			IntSearch test=new IntSearch(null);
			fail("creating a search without a problem should fail!");
			test.run();
		}catch(Exception e){
			
		}
	}
	
 public void testGetProblem() {
		assertTrue(emptySearch.getProblem()==empty);
		assertTrue(intSearch.getProblem()==integer);
		assertTrue(stopSearch.getProblem()==stop);
		assertTrue(hashSearch.getProblem()==hash);
		assertTrue(nohashSearch.getProblem()==nohash);
	}

}
