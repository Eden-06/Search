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

import core.Search;
import junit.framework.TestCase;

public class TestSearch extends TestCase {
 
 private class EmptySearch extends Search<Integer>{
		@Override
		protected boolean canPrepare() {	return false;	}
		@Override
		protected void prepare() { fail("prepare should never be called!");	}
		@Override
		protected void search() { fail("search should never be called!");	}
 }

 private class SimpleSearch extends Search<Integer>{
		@Override
		protected boolean canPrepare() {	return true;	}
		@Override
		protected void prepare() {	}
		@Override
		protected void search() { 
			assertTrue("during search running should always be true",running());	neededSteps++; result=1; 
		}
 }
 
 private class StopSearch extends Search<Integer>{
		@Override
		protected boolean canPrepare() {	return true;	}
		@Override
		protected void prepare() {	}
		@Override
		protected void search() {
			neededSteps++; result=1;	stop();
			assertFalse("running should be false if stop has been called",running());
		}		
	}
	
 private EmptySearch emptySearch;
 private SimpleSearch simpleSearch;
 private StopSearch stopSearch;
	
	public TestSearch() {
		super("Test cases for the Search");
	}

	protected void setUp() throws Exception {
		super.setUp();
		emptySearch=new EmptySearch();
		simpleSearch=new SimpleSearch();
		stopSearch=new StopSearch();
	}

	protected void tearDown() throws Exception {
		emptySearch=null;
		simpleSearch=null;
		stopSearch=null;
		super.tearDown();		
	}

	public void testInitialize() {
		assertFalse("initialize should return false if canPrepare returns false",emptySearch.initialize());
		assertTrue("initialize should return true if canPrepare returns true",simpleSearch.initialize());
  simpleSearch.run(); //reset search
	}

	public void testInitialized() {
		assertFalse("initialized should be false",emptySearch.initialized());
		emptySearch.initialize();
		assertFalse("initialized should be false",emptySearch.initialized());
		
		assertFalse("initialized should be false",simpleSearch.initialized());
		simpleSearch.initialize();
		assertTrue("initialized should be true",simpleSearch.initialized());
		simpleSearch.run(); //reset search

	}

	public void testRun() {
	 emptySearch.initialize();
		emptySearch.run(); //never do a search
		emptySearch.run(); //never do a search
		
		simpleSearch.initialize();
		simpleSearch.run();
	 simpleSearch.run();
	 		
	}

	public void testStop() {
		stopSearch.initialize();
		stopSearch.run(); //never fail
		stopSearch.run(); //never fail
	}

	public void testRunning() {
		simpleSearch.initialize();
		simpleSearch.run();
	 simpleSearch.run();
	}

	public void testGetResult() {
		emptySearch.initialize();
		assertTrue("getResult should always return null",emptySearch.getResult()==null);
		emptySearch.run();
		assertTrue("getResult should always return null",emptySearch.getResult()==null);
		emptySearch.run();
	 assertTrue("getResult should always return null",emptySearch.getResult()==null);
		
		simpleSearch.initialize();
		assertTrue("getResult should return null after initializing",simpleSearch.getResult()==null);
		simpleSearch.run();
		assertTrue("getResult should return 1 after running",simpleSearch.getResult()==1);
	 simpleSearch.run();
	 assertTrue("getResult should return 1 after the second running",simpleSearch.getResult()==1);
	 	 
	 stopSearch.initialize();
	 assertTrue("getResult should return null after initializing",stopSearch.getResult()==null);
		stopSearch.run();
		assertTrue("getResult should return 1 after running",stopSearch.getResult()==1);
		stopSearch.run();
		assertTrue("getResult should return 1 after the second running",stopSearch.getResult()==1);
	}

	public void testNeededSteps() {
		emptySearch.initialize();
		assertTrue("neededSteps should always return 0",emptySearch.neededSteps()==0);
		emptySearch.run();
		assertTrue("neededSteps should always return 0",emptySearch.neededSteps()==0);
		emptySearch.run();
	 assertTrue("neededSteps should always return 0",emptySearch.neededSteps()==0);
		
		simpleSearch.initialize();
		assertTrue("neededSteps should return 0 after initializing",simpleSearch.neededSteps()==0);
		simpleSearch.run();
		assertTrue("neededSteps should return 1 after running",simpleSearch.neededSteps()==1);
	 simpleSearch.run();
	 assertTrue("neededSteps should return 1 after second running",simpleSearch.neededSteps()==1);
	 
	 stopSearch.initialize();
	 assertTrue("neededSteps should return 0 after initializing",stopSearch.neededSteps()==0);
		stopSearch.run();
		assertTrue("neededSteps should return 1 after running",stopSearch.neededSteps()==1);
		stopSearch.run();
		assertTrue("neededSteps should return 1 after second running",stopSearch.neededSteps()==1);
	}

}
