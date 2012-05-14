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

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import extended.BestChoiceStrategy;
import extended.FirstChoiceStrategy;
import extended.HillClimbing;
import extended.HillClimbingProblem;
import extended.HillClimbingStrategy;


public class TestHillClimbing extends TestCase {
	
	private class Foo{
		private double p[];
		public Foo(double p[]){this.p=p;}
		public double of(double x){ //Horner shema
		 double y=0, u=0; 
		 for(int i=0;i<p.length;i++){y=u+p[i];u=x*y;}
			return y;
		}
	}
	
	private class PProblem implements HillClimbingProblem<Double>{
  private Double i=null;
  private Foo f=null;
  private HillClimbing<Double> hc=null;
 	public PProblem(Double i,Foo f) {		super();	this.i=i;	this.f=f;}
		@Override
		public List<Double> expand(Double state) {
			if ((hc!=null) && (state>20.0)) hc.stop();
			List<Double> r=new ArrayList<Double>(2);	r.add(state-1.0);	r.add(state+1.0);	return r;
		}
		@Override
		public Double initial() {	return i;	}
		public void setI(Double i) { this.i=i;	}
		public void setHc(HillClimbing<Double> hc) { this.hc=hc; }
		@Override
		public boolean isGoal(Double state) {	fail("isGoal should never get called");	return false;	}
		@Override
		public int compare(Double a, Double b) { return new Double(f.of(a)).compareTo(f.of(b));	}

	}
	
	private class Dat{
		public double init,max;
		public int steps;
		public Dat(double i,double m,int s){init=i;max=m;steps=s;}		
	}
	
	PProblem c;
	PProblem l;
		
	HillClimbingStrategy<Double> hcBestFirst, hcFirstBest;
	HillClimbing<Double> lmin, lmax, cmin, cmax;	
	Dat cases[]=new Dat[10];
	
	protected void setUp() throws Exception {
		super.setUp();
		l=new PProblem(0.0,new Foo(new double[]{2.0}));
		c=new PProblem(0.0,new Foo(new double []{0.2,0.0,-(10.0/3.0),0.0,9.0,0.0}));
		
		hcBestFirst=new BestChoiceStrategy<Double>();
		hcFirstBest=new FirstChoiceStrategy<Double>();
		
		lmin=new HillClimbing<Double>(l,true,hcBestFirst,0);
		lmax=new HillClimbing<Double>(l,true,hcBestFirst,0);
		lmax.searchMaximum();
		
		cmin=new HillClimbing<Double>(c,true,hcBestFirst,0);
		cmax=new HillClimbing<Double>(c,true,hcBestFirst,0);
		cmax.searchMaximum();
		
		// test cases for maximum search
		cases[0]=new Dat(-20.0,14.4,18);
		cases[1]=new Dat(-10.0,14.4,8);
		cases[2]=new Dat(-5.0,14.4,3);
		cases[3]=new Dat(-3.0,14.4,1);
		cases[4]=new Dat(-2.0,14.4,2);
		cases[5]=new Dat(-1.0,14.4,3);
		cases[6]=new Dat(0.0,5.86,2);
		cases[7]=new Dat(1.0,5.86,1);
		cases[8]=new Dat(2.0,5.86,2);
		cases[9]=new Dat(3.0,5.86,3);
	}

	protected void tearDown() throws Exception {
		c=null;
		l=null;
		cmin=null;
		cmax=null;
		lmin=null;
		lmax=null;
		hcBestFirst=null; hcFirstBest=null;
		super.tearDown();
	}
	
	public void testHillClimbing(){
  // non of this should fail
		HillClimbing<Double> h;
		h=new HillClimbing<Double>(l,true,HillClimbing.EStrategy.BEST_CHOICE,0);
		h.initialize();
		h=new HillClimbing<Double>(l,false,HillClimbing.EStrategy.BEST_CHOICE);
		h.initialize();
		h=new HillClimbing<Double>(l,HillClimbing.EStrategy.BEST_CHOICE);
		h.initialize();
		h=new HillClimbing<Double>(l);
		h.initialize();
  // all of this should fail
  
		//check sidesteps
		for (int i=-10;i>0;i++){
			try {
				h=new HillClimbing<Double>(l,true,HillClimbing.EStrategy.BEST_CHOICE,i);
				h.initialize();
				fail(String.format("sidesteps=%d should raise an exception",i));
			}catch (IllegalArgumentException e) {	}
		}
		
		//check problem
		try {
			h=new HillClimbing<Double>(null,true,HillClimbing.EStrategy.BEST_CHOICE,0);
			h.initialize();
			fail("problem=null should raise an exception");
		}catch (IllegalArgumentException e) {	}
		try {
		 h=new HillClimbing<Double>(null,false,HillClimbing.EStrategy.BEST_CHOICE);
		 h.initialize();
		 fail("problem=null should raise an exception");
	 }catch (IllegalArgumentException e) {	}
	 try {
	 	h=new HillClimbing<Double>(null,HillClimbing.EStrategy.BEST_CHOICE);
		 h.initialize();
		 fail("problem=null should raise an exception");
  }catch (IllegalArgumentException e) {	}
  try {
	 	h=new HillClimbing<Double>(null);
		 h.initialize();
		 fail("problem=null should raise an exception");
  }catch (IllegalArgumentException e) {	}
  
  //check strategy
  try {
			HillClimbing.EStrategy strategy=null;
  	h=new HillClimbing<Double>(l,true,strategy,0);
			h.initialize();
			fail("strategy=null should raise an exception");
		}catch (IllegalArgumentException e) {	}
  try {
			HillClimbingStrategy<Double> strategy=null;
  	h=new HillClimbing<Double>(l,true,strategy,0);
			h.initialize();
			fail("strategy=null should raise an exception");
		}catch (IllegalArgumentException e) {	}
		try {
		 h=new HillClimbing<Double>(l,false,null);
		 h.initialize();
		 fail("strategy=null should raise an exception");
	 }catch (IllegalArgumentException e) {	}
	 try {
	 	h=new HillClimbing<Double>(l,null);
		 h.initialize();
		 fail("strategy=null should raise an exception");
  }catch (IllegalArgumentException e) {	}


	}
	
	private boolean cmp(double x,double y){
		return Math.rint(x*10)==Math.rint(y*10);
	}

	
// uses this to test wheter the horner algorithm works fine
//	public void testFoo(){
//		double poly[]={0.2,0.0,-(10.0/3.0),0.0,9.0,0.0};
//		Foo f=new Foo(0,poly);
//		double y=0;
//		for (double i=0;i<10;i+=1){
//			f.x(i);
//			y=.2*Math.pow(i,5)-(10.0/3.0)*Math.pow(i,3)+9*i;
//			assertTrue("Horner algorithm did not work "+y+" but was "+f.function(),cmp(f.function(),y));
//		}		
//	}
	
	public void testSearch() {
		for (int i=0;i<1000;i++){
			lmin.setSideSteps(i);
			lmin.run();
			assertFalse("running a linear search for minimum should not return null",lmin.getResult()==null);
			assertTrue("running a linear search for minimum should return 2.0 but was "+l.f.of(lmin.getResult()),l.f.of(lmin.getResult())==2.0);
			assertTrue("running a linear search for minimum should need "+(i+1)+" steps",lmin.neededSteps()==i+1);
			lmin.searchMaximum();
			lmin.run();
			assertFalse("running a linear search for maximum should not return null",lmin.getResult()==null);
			assertTrue("running a linear search for minimum should return 2.0",l.f.of(lmin.getResult())==2.0);
			assertTrue("running a linear search for maximum should need "+(i+1)+" steps",lmin.neededSteps()==i+1);
		}
		lmin.setSideSteps(0);
		lmin.searchMinimum();

		//TODO:test minimum search against test cases		


	}
	
	public void testSearchBestFirst(){
		assertTrue(cmax.getStrategy()==hcBestFirst);
		c.setHc(cmax);
		for (int i=0;i<cases.length;i++){
			c.setI(cases[i].init);
		 cmax.run();
		 if (cmax.getResult()!=null){
		 	System.out.format("case[%d] - %f =?= %f  BestFirst\n",i,cases[i].max,c.f.of(cmax.getResult()));
		 	assertTrue("Maximum search neededSteps() should be "+cases[i].steps+" but was "+cmax.neededSteps(),cmax.neededSteps()==cases[i].steps);
		  assertTrue("Maximum search neededSideSteps() should be 0 but was "+cmax.neededSideSteps(),cmax.neededSideSteps()<=0);
		  assertTrue("Maximum search getResult() should be "+cases[i].max+" but was "+c.f.of(cmax.getResult()),cmp(c.f.of(cmax.getResult()),cases[i].max));
		 }else{
	  	assertTrue("only test case[9] may not find a result",i==9);
	  }
		}		
		c.setHc(null);
	}

	
	public void testSearchFirstBest(){
		assertTrue(cmax.getStrategy()==hcBestFirst);
		cmax.setStrategy(hcFirstBest);
		c.setHc(cmax);
		for (int i=0;i<cases.length;i++){
			c.setI(cases[i].init);
		 cmax.run();
		 if (cmax.getResult()!=null){
		 	System.out.format("case[%d] - %f =?= %f  FirstBest\n",i,cases[i].max,c.f.of(cmax.getResult()));
		 	assertTrue("Maximum search neededSteps() should be "+cases[i].steps+" but was "+cmax.neededSteps(),cmax.neededSteps()==cases[i].steps);
	 	 assertTrue("Maximum search neededSideSteps() should be 0 but was "+cmax.neededSideSteps(),cmax.neededSideSteps()<=0);
		  assertTrue("Maximum search getResult() should be "+cases[i].max+" but was "+c.f.of(cmax.getResult()),cmp(c.f.of(cmax.getResult()),cases[i].max));
		 }else{
		 	assertTrue("only test case[9] may not find a result",i==9);
		 }
 	}
		c.setHc(null);
		cmax.setStrategy(hcBestFirst);
	}
	
	public void testInitialized() {
		
		l.setI(null);
		assertFalse("initialize should fail if the inital value is null",lmin.initialize());
		l.setI(0.0);
		
		lmin.setStrategy((HillClimbingStrategy<Double>) null);
		assertFalse("initialize should fail if the inital value is null",lmin.initialize());
		lmin.setStrategy(hcBestFirst);
		
		lmin.setSideSteps(10);
		lmin.initialize();
		lmin.run();
		assertTrue("neededSideSteps should be reset on prepare()"+lmin.neededSideSteps(),lmin.neededSideSteps()==10);
		lmin.initialize();
		assertTrue("neededSideSteps should be reset on prepare()",lmin.neededSideSteps()==0);
		lmin.setSideSteps(0);
	}

	public void testGetStrategy(){
		assertTrue(lmin.getStrategy()==hcBestFirst);
		assertTrue(lmax.getStrategy()==hcBestFirst);
		assertTrue(cmin.getStrategy()==hcBestFirst);
		assertTrue(cmax.getStrategy()==hcBestFirst);
	}
	
	public void testSetStrategy(){
		assertTrue(lmin.getStrategy()==hcBestFirst);
		lmin.setStrategy(hcFirstBest);
		assertTrue(lmin.getStrategy()==hcFirstBest);
	 lmin.initialize();
	 lmin.setStrategy(hcBestFirst);
	 assertTrue(lmin.getStrategy()==hcFirstBest);
		lmin.run();
		lmin.setStrategy(hcBestFirst);
	 assertTrue(lmin.getStrategy()==hcBestFirst);
	}
	
	public void testSetSideSteps() {
		try{
			lmin.setSideSteps(Integer.MAX_VALUE);
		 lmin.setSideSteps(1);
		 lmin.initialize();lmin.run();
		 assertTrue("setSideSteps(1) should set the sideSteps to 1",lmin.neededSideSteps()==1);
		 lmin.setSideSteps(0);
		 lmin.initialize();lmin.run();
		 assertTrue("setSideSteps(0) should set the sideSteps to 0",lmin.neededSideSteps()==0);
		 lmin.setSideSteps(10);
		 lmin.setSideSteps(-1);
		 lmin.initialize();lmin.run();
		 assertTrue("setSideSteps(-1) should not effect the sideSteps",lmin.neededSideSteps()==10);
		 lmin.setSideSteps(Integer.MIN_VALUE);
		 lmin.initialize();lmin.run();
		 assertTrue("setSideSteps(-infinit) should not effect the sideSteps",lmin.neededSideSteps()==10);
		}catch(Exception e){		
   fail("no setSideSteps() operation should fail:\n"+e.getMessage());
	 }
	}

	public void testNeededSideSteps() {

		lmin.setSideSteps(0);
		lmin.initialize();
		assertTrue("after initializing neededSideSteps() should return zero",lmin.neededSideSteps()==0);
		for(int i=0;i<1000;i++){
			lmin.setSideSteps(i);
			lmin.initialize();
			lmin.run();
			assertTrue("after running neededSideSteps() should return "+i,lmin.neededSideSteps()==i);
			assertTrue("after running linear search should be neededSideSteps()+1==neededSteps()",lmin.neededSideSteps()+1==lmin.neededSteps());
		}
		lmin.setSideSteps(0);
	}

	public void testGetProblem(){
		assertTrue(lmin.getProblem()==l);
		assertTrue(lmax.getProblem()==l);
		assertTrue(cmin.getProblem()==c);
		assertTrue(cmax.getProblem()==c);		
	}

		
}
