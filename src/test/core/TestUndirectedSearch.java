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

import core.UndirectedSearch;
import junit.framework.TestCase;

public class TestUndirectedSearch extends TestCase {
 
	private class IntSearch extends UndirectedSearch<Integer>{
		public IntSearch(boolean m) {	super(m);	}
		@Override
		protected boolean canPrepare() {	return true;	}
		@Override
		protected void prepare() {	}
		@Override
		protected void search() { result=(minimum() ? -1 : 1);	}	
		public boolean m() { return minimum(); }
	}
	
	private IntSearch minSearch;
	private IntSearch maxSearch;
	
	public TestUndirectedSearch() {
		super("Test cases for the UndirectedSearch");
	}

	protected void setUp() throws Exception {
		super.setUp();
		minSearch=new IntSearch(true);
		maxSearch=new IntSearch(false);
	}

	protected void tearDown() throws Exception {
		minSearch=null;
		maxSearch=null;
		super.tearDown();
	}

	public void testSearchMinimum() {
		assertTrue(minSearch.searchMinimum());
		assertTrue(minSearch.m());
		
		maxSearch.initialize();
		assertFalse(maxSearch.searchMinimum());
		assertFalse(maxSearch.m());
		maxSearch.run();
		assertTrue(maxSearch.searchMinimum());
		assertTrue(maxSearch.m());
		maxSearch.searchMaximum();
	}

	public void testSearchMaximum() {
		assertTrue(maxSearch.searchMaximum());
		assertFalse(maxSearch.m());
		
		minSearch.initialize();
		assertFalse(minSearch.searchMaximum());
		assertTrue(minSearch.m());
		minSearch.run();
		assertTrue(minSearch.searchMaximum());
		assertFalse(minSearch.m());
		minSearch.searchMinimum();
	}

	public void testMinimum() {
		assertTrue(minSearch.m());
		minSearch.initialize();
		assertTrue(minSearch.m());
		minSearch.run();
		assertTrue(minSearch.m());
		
		assertFalse(maxSearch.m());
		maxSearch.initialize();
		assertFalse(maxSearch.m());
		maxSearch.run();
		assertFalse(maxSearch.m());	
	}

}
