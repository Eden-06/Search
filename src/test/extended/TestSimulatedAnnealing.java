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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import extended.SimulatedAnnealing;
import extended.SimulatedAnnealingProblem;

import junit.framework.TestCase;

public class TestSimulatedAnnealing extends TestCase {
	private class Foo{
		private double p[];
		public Foo(double p[]){this.p=p;}
		public double of(double x){ //Horner shema
		 double y=0, u=0; 
		 for(int i=0;i<p.length;i++){y=u+p[i];u=x*y;}
			return y;
		}
	}
	
	private class PProblem implements SimulatedAnnealingProblem<Double>{
  private Double i=null;
  private Foo f=null;
 	public PProblem(Double i,Foo f) {		super();	this.i=i;	this.f=f;}
		@Override
		public List<Double> expand(Double state) {
			List<Double> r=new ArrayList<Double>();
			if(Math.abs(state)<10000.0){	r.add(state-1.0);	r.add(state+1.0);	}
			return r;
		}
		@Override
		public Double initial() {	return i;	}
		public void setI(Double i) { this.i=i;	}
		@Override
		public boolean isGoal(Double state) {	fail("isGoal should never get called");	return false;	}
		@Override
		public double difference(Double a, Double b) {	return f.of(a)-f.of(b);	}

	}
		
	PProblem l;

	SimulatedAnnealing<Double> smin, smax;
	
	protected void setUp() throws Exception {
		super.setUp();
		l=new PProblem(0.0,new Foo(new double[]{0.5,0.0}));
		smin=new SimulatedAnnealing<Double>(l);
	}

	protected void tearDown() throws Exception {
		l=null;
		smin=null;
		super.tearDown();
	}

	private boolean isIn(final double element,final double[] set){
		double e=Math.rint(element*10);
		for (int i=0;i<set.length;i++)
			if (Math.rint(set[i]*10)==e)	return true;
		return false;
	}
	
	private void subSearch(boolean m,PProblem p,double[] r){
		SimulatedAnnealing<Double> s=new SimulatedAnnealing<Double>(p,m);
  for(double x1=-10.0;x1<10.0;x1+=1.0){
  	p.setI(x1);
  	s.initialize();
  	s.run();
  	assertFalse("The search should have found a result",s.getResult()==null);
  	assertTrue("The search should have find "+Arrays.toString(r)+" but found "+p.f.of(s.getResult()),isIn(p.f.of(s.getResult()),r));
  }	 
	}
	
	public void testSearch() {
		SimulatedAnnealing<Double> s;
		//problem !=null   g=l  b=null
		PProblem[] problem = {l,null};
		//temp > 0         g=13 b=0
  double[] temp = {13,0};
		//fact > 0 & < 1   g=.6 b=0,1
  double[] fact = {.6,0,1};
		//steps > 0        g=10 b=0
  int[] steps = {10,0};
  
		//Should creating an instance with wrong values fail?
		// YES
  int count=2;
  String ask;
  //test constructor(Problem<T>)
  for (int i=0;i<count;i++){
  	ask=String.format("problem=%s",problem[i%2]);
  	try{
  		s=new SimulatedAnnealing<Double>(problem[i%2]);
  		if (i!=0) fail("If one argument is bad the construction should fail but excepts "+ask);
  		s.initialize();
  	}catch(IllegalArgumentException e){
  		if (i==0) fail("All arguments where set properly with "+ask+" but the construction failed");
  	}  
  }
  count=2;
  //test constructor(Problem<T>,boolean)
  for (int i=0;i<count;i++){
  	ask=String.format("problem=%s",problem[i%2]);
  	try{
  		s=new SimulatedAnnealing<Double>(problem[i%2],true);
  		if (i!=0) fail("If one argument is bad the construction should fail but excepts "+ask);
  		s.initialize();
  	}catch(IllegalArgumentException e){
  		if (i==0) fail("All arguments where set properly with "+ask+" but the construction failed");
  	}  
  }
  count=2*2*3*2;
  //test constructor(Problem<T>,boolean,double,double,int)
  for (int i=0;i<count;i++){
  	ask=String.format("problem=%s,temperature=%.2f,factor=%.2f,maxSteps=%d",problem[i%2],temp[(i/2)%2],fact[(i/4)%3],steps[(i/12)%2]);
  	try{
  		s=new SimulatedAnnealing<Double>(problem[i%2],true,temp[(i/2)%2],fact[(i/4)%3],steps[(i/12)%2]);
  		if (i!=0) fail("If one argument is bad the construction should fail but excepts "+ask);
  		s.initialize();
  	}catch(IllegalArgumentException e){
  		if (i==0) fail("All arguments where set properly with "+ask+" but the construction failed");
  	}  
  }
  count=2*2*3;
  //test constructor(Problem<T>,boolean,double,double)
  for (int i=0;i<count;i++){
  	try{
   	ask=String.format("problem=%s,temperature=%.2f,factor=%.2f",problem[i%2],temp[(i/2)%2],fact[(i/4)%3]);
  		s=new SimulatedAnnealing<Double>(problem[i%2],true,temp[(i/2)%2],fact[(i/4)%3]);
  		if (i!=0) fail("If one argument is bad the construction should fail");
  		s.initialize();
  	}catch(IllegalArgumentException e){
  		if (i==0) fail("All arguments where set properly but the construction failed");
  	}  
  }
  //test constructor(Problem<T>,double,double)
  count=2*2*3;
  for (int i=0;i<count;i++){
  	ask=String.format("problem=%s,temperature=%.2f,factor=%.2f",problem[i%2],temp[(i/2)%2],fact[(i/4)%3]);
  	try{
  		s=new SimulatedAnnealing<Double>(problem[i%2],temp[(i/2)%2],fact[(i/4)%3]);
  		if (i!=0) fail("If one argument is bad the construction should fail");
  		s.initialize();
  	}catch(IllegalArgumentException e){
  		if (i==0) fail("All arguments where set properly but the construction failed");
  	}  
  }

  //Test MinimumSearch with f1  		
		//test if it can find the correct values
		double[] results={-(7.0/3.0),0.0,-(1.0/3.0)};
		double[] poly={1/6.0,0.0,-1.25,0.0,2.0,0.5,0.0};
  PProblem f1=new PProblem(0.0, new Foo(poly));
		//f1(x)=1/6 x^6 - 5/4 x^4 + 2 x^2 + 0.5 x
  subSearch(true,f1,results);
  
  //Test MaximumSearch with f2
  double[] iresults={(7.0/3.0),0.0,(1.0/3.0)};
		double[] ipoly={-1/6.0,0.0,+1.25,0.0,-2.0,-0.5,0.0};
  PProblem f2=new PProblem(0.0, new Foo(ipoly));
  //f2(x)=-f1(x)= -1/6 x^6 + 5/4 x^4 - 2 x^2 - 0.5 x  
  subSearch(false,f2,iresults);
	}
		
//	public void testDouble(){
//		double a=Double.NaN;
//		assertFalse(a>0);
//		assertFalse(a<0);
//		assertFalse(a==a);
//		assertTrue(Double.isNaN(2/a));
//		assertTrue(Double.isNaN(a*0.5));
//	}
	
	public void testInitialize(){
		l.setI(null);
		assertFalse("initialize should fail if the inital value is null",smin.initialize());
		l.setI(0.0);
	}
	
	public void testGetters(){
		SimulatedAnnealing<Double> s;
		PProblem prob = new PProblem( 0.0, new Foo(new double[]{2.0}) );
  double temp  =13, dtemp =10;
  double fact  =.6, dfact =.5;
  int    steps =10, dsteps=100;
  //test constructor(Problem<T>)
  s=new SimulatedAnnealing<Double>(prob);
  assertTrue(s.minimum());
  assertTrue(s.getInitialTemperature()==dtemp);
  assertTrue(s.getFactor()==dfact);
  assertTrue(s.getMaxSame()==dsteps);
  assertTrue(s.getProblem()==prob);
  s.initialize();
  //test constructor(Problem<T>,boolean)
  s=new SimulatedAnnealing<Double>(prob,false);
  assertTrue(! s.minimum());
  assertTrue(s.getInitialTemperature()==dtemp);
  assertTrue(s.getFactor()==dfact);
  assertTrue(s.getMaxSame()==dsteps);
  assertTrue(s.getProblem()==prob);
  s.initialize();
  //test constructor(Problem<T>,boolean,double,double,int)
  s=new SimulatedAnnealing<Double>(prob,false,temp,fact,steps);
  assertTrue(! s.minimum());
  assertTrue(s.getInitialTemperature()==temp);
  assertTrue(s.getFactor()==fact);
  assertTrue(s.getMaxSame()==steps);
  assertTrue(s.getProblem()==prob);
  s.initialize();
  //test constructor(Problem<T>,boolean,double,double)
  s=new SimulatedAnnealing<Double>(prob,false,temp,fact);
  assertTrue(! s.minimum());
  assertTrue(s.getInitialTemperature()==temp);
  assertTrue(s.getFactor()==fact);
  assertTrue(s.getMaxSame()==dsteps);
  assertTrue(s.getProblem()==prob);
  s.initialize();
  //test constructor(Problem<T>,double,double)
  s=new SimulatedAnnealing<Double>(prob,temp,fact);
  assertTrue(s.minimum());
  assertTrue(s.getInitialTemperature()==temp);
  assertTrue(s.getFactor()==fact);
  assertTrue(s.getMaxSame()==dsteps);
  assertTrue(s.getProblem()==prob);
  s.initialize();
  
	}
	
	public void testSetInitialTemperature() {
		double[] values = 
		{10,0,-1,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,Double.MIN_VALUE,Double.NaN};
		boolean[] results =
		{true,false,false,false,true,true,false};
		for (int i=0; i<values.length;i++){
			smin.setInitialTemperature(values[i]);
			if (results[i]){
				assertTrue("setInitialTemperature() should have changed to "+values[i],smin.getInitialTemperature()==values[i]);
			}else{
				assertFalse("setInitialTemperature() should have not changed to "+values[i],smin.getInitialTemperature()==values[i]);				
			}			
		}
		assertFalse("setInitialTemperatur(NaN) should not have effect",Double.isNaN(smin.getInitialTemperature()));
		smin.initialize();
		for (int i=0; i<values.length;i++){
			smin.setInitialTemperature(values[i]);
			if (results[i]){
				assertTrue("After initializing setInitialTemperature() should have changed to "+values[i],smin.getInitialTemperature()==values[i]);
			}else{
				assertFalse("After initializing setInitialTemperature() should have not changed to "+values[i],smin.getInitialTemperature()==values[i]);				
			}			
		}		
		smin.run();
		smin.setInitialTemperature(10);
	}

	public void testSetFactor() {
			
		double[] values = 
		{10.0,1.0,0.0,-1.0,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,Double.MIN_VALUE,Double.NaN,0.5,0.99999999999};
		boolean[] results =
		{false,false,false,false,false,false,true,false,true,true};
		for (int i=0; i<values.length;i++){
			smin.setFactor(values[i]);
			if (results[i]){
				assertTrue("setFactor() should have changed to "+values[i],smin.getFactor()==values[i]);
			}else{
				assertFalse("setFactor() should have not changed to "+values[i],smin.getFactor()==values[i]);				
			}			
		}
		assertFalse("setFactor(NaN) should not have effect",Double.isNaN(smin.getFactor()));
		smin.initialize();
		for (int i=0; i<values.length;i++){
			smin.setFactor(values[i]);
			if (results[i]){
				assertTrue("After initializing setFactor() should have changed to "+values[i],smin.getFactor()==values[i]);
			}else{
				assertFalse("After initializing setFactor() should have not changed to "+values[i],smin.getFactor()==values[i]);				
			}			
		}		
		smin.run();
		smin.setFactor(0.5);			
	}

	public void testSetMaxSame() {
		int[] values=
		{-100,-1,0,1,100,Integer.MIN_VALUE,Integer.MAX_VALUE};
		boolean[] results=
		{false,false,false,true,true,false,true};
		for (int i=0; i<values.length;i++){
			smin.setMaxSame(values[i]);
			if (results[i]){
				assertTrue("setMaxSame() should have changed to "+values[i],smin.getMaxSame()==values[i]);
			}else{
				assertFalse("setMaxSame() should have not changed to "+values[i],smin.getMaxSame()==values[i]);				
			}			
		}
		smin.initialize();
		for (int i=0; i<values.length;i++){
			smin.setMaxSame(values[i]);
			if (results[i]){
				assertTrue("After initializing setMaxSame() should have changed to "+values[i],smin.getMaxSame()==values[i]);
			}else{
				assertFalse("After initializing setMaxSame() should have not changed to "+values[i],smin.getMaxSame()==values[i]);				
			}			
		}		
		smin.setMaxSame(100);
		smin.run();		
	}
	
}
