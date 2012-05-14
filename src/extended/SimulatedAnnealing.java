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

package extended;

import java.util.List;

import core.UndirectedSearch;

/**
 * The simulated annealing search is an advanced local search
 * able to solve any problem implementing the extended.SimulatedAnnealingProblem interface.<br>
 * <br>
 * This algorithm starts with the initial state and
 * iteratively selects a random successor of the current state if
 * it is an improvement or with a probability equal to 
 * <code>Math.exp( delta / temperature )</code> 
 * <i>(where delta is the difference between the current state and the successor)</i>.
 * The temperature is decreased during the search run so the probability of selecting a bad state
 * falls down to zero. In that way the search behaves like a random walk in the beginning and an hill climbing in the end.<br>
 * The search terminates if there were no better states found since several iterations.<br>
 * This strategy grants constant space and linear time complexity but no optimal solutions.
 * In contrast to the hill climbing this algorithm is complete if the temperature falls slow enough.<br>
 * This leads to the fact that the initial temperature and the cooling factor
 * are the most important properties to find good solutions.<br> 
 * When ever the algorithm fails to produce good results, 
 * you should modify the initial temperature and cooling factor.
 * For example if your initial temperature is to high, 
 * the search starts at a good state and jumps far away from a local optimum.
 * As an other example if your cooling factor is to low, 
 * the search will easily get stuck in a local optimum.<br> 
 * <b>Note:</b> There are no easy ways to determine the best
 * search parameters, so you must run experiments until you
 * get good search solutions. 
 * A good starting point are the default values, which are an initial temperature of 10.0,
 * a cooling factor of 50 percent and an amount of same states of 100.<br>
 * Remember that this algorithm is based on probability so the found solution,
 * can vary on successive runs. This algorithm works best in situations,
 * where you only need a good approximation to the optimal solution in a short amount of time.<br>
 * <br>
 * If you want to improve the time needed for solving your problem,
 * you have to change the way in which the successor states are generated.
 * <i>(For a further discussion look into the description of the
 * extended.SimulatedAnnealingProblem.)</i>  
 *   
 * @param <T> the specific type of all elements in the search domain
 * 
 * @see core.UndirectedSearch 
 * @see extended.SimulatedAnnealingProblem
 * @see extended.HillClimbing
 * 
 * @author Eden_06
 *
 */
public class SimulatedAnnealing<T> extends UndirectedSearch<T> {
 /**
  * holds the initial temperature on which the annealing start
  */
	private double temperature=10;
 /**
  * holds the factor by which the temperature will be lowered
  */
	private double factor=0.95;
	/**
	 * holds the maximum amount of steps without improvement to the search result, before the search terminates
	 */
	private int maxSame=100;

	/**
	 * holds a reference to the problem to be solved
	 */
	protected SimulatedAnnealingProblem<T> problem=null;
		
	/**
	 * Creates a new simulated annealing search with the given problem.<br>
	 * The search look for a global minimum and uses the following default values:
	 * <ul>
	 * <li>Initial temperature = 10.0</li>
	 * <li>Cooling factor = 0.95</li>
	 * <li>maximum amount of steps without improvement = 100</li>
	 * </ul>
	 * 
	 * @param problem the SimulatedAnnleaingProblem to be solved
	 * 
	 * @throws IllegalArgumentException if the problem is null  
	 */
	public SimulatedAnnealing(SimulatedAnnealingProblem<T> problem){
	 super(true);
	 if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
 }
	
	/**
	 * Creates a new simulated annealing search, with the given problem and 
	 * the direction given by the minimum flag.<br>
	 * The search will use the following default values:
	 * <ul>
	 * <li>Initial temperature = 10.0</li>
	 * <li>Cooling factor = 0.95</li>
	 * <li>maximum amount of steps without improvement = 100</li>
	 * </ul>
	 * 
	 * @param problem the SimulatedAnnleaingProblem to be solved
	 * @param minimum flag indicating whether the search looks for minimum values or not
	 * 
	 * @throws IllegalArgumentException if the problem is null 
	 */
	public SimulatedAnnealing(SimulatedAnnealingProblem<T> problem,boolean minimum){
	 super(minimum);
	 if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
 }
	
	/**
	 * Creates a new simulated annealing search, with the given problem, 
	 * the direction given by the minimum flag, the initial temperature,
	 * the cooling factor and the maximal number of steps without improvements.
	 * 
	 * @param problem the SimulatedAnnleaingProblem to be solved
	 * @param minimum flag indicating whether the search looks for minimum values or not
	 * @param temperature a value greater 0 by which the annealing process starts
	 * @param factor a value between 0 and 1 by which the temperature will be lowered at each iteration
	 * @param maxSame the maximum amount of steps without improvements before the search terminates <i>(Must be a positive value)</i>
	 * 
	 * @throws IllegalArgumentException if the problem is null or one parameter is out of is boundaries
	 */
	public SimulatedAnnealing(SimulatedAnnealingProblem<T> problem,boolean minimum,double temperature, double factor,int maxSame)
	  throws IllegalArgumentException{
	 super(minimum);
	 if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
	 if (temperature>0) this.temperature=temperature;
	 else throw new IllegalArgumentException("The given temperature must be graeter 0");
  if ((factor>0) && (factor<1)) this.factor=factor;
  else throw new IllegalArgumentException("The given factor must be greater 0 and less then 1");
  if (maxSame>0) this.maxSame=maxSame;
  else throw new IllegalArgumentException("The given maximum number of steps (maxSame) must be greater 0");
	}
	
	
 /**
  * Creates a new simulated annealing search, with the given problem, 
	 * the initial temperature and the cooling factor.
	 * The search will look for a global minimum and
	 * allows maximal 100 steps without improvements during a search run.
	 * 
	 * 
	 * @param problem the SimulatedAnnleaingProblem to be solved
	 * @param temperature a value greater 0 by which the annealing process starts
	 * @param factor a value between 0 and 1 by which the temperature will be lowered at each iteration
	 * 
	 * @throws IllegalArgumentException if the problem is null or one parameter is out of is boundaries 
  */
 public SimulatedAnnealing(SimulatedAnnealingProblem<T> problem,double temperature, double factor){
	 this(problem,true,temperature,factor,100);
	}
	
 /**
  * Creates a new simulated annealing search, with the given problem,
  * the direction given by the minimum flag, the initial temperature and the cooling factor.
	 * The search allows maximal 100 steps without improvements during a search run.
	 * 
	 * 
	 * @param problem the SimulatedAnnleaingProblem to be solved
	 * @param minimum flag indicating whether the search looks for minimum values or not
	 * @param temperature a value greater 0 by which the annealing process starts
	 * @param factor a value between 0 and 1 by which the temperature will be lowered at each iteration
	 * 
	 * @throws IllegalArgumentException if the problem is null or one parameter is out of is boundaries
	 * 
  */
 public SimulatedAnnealing(SimulatedAnnealingProblem<T> problem,boolean minimum,double temperature, double factor){
 	this(problem,minimum,temperature,factor,100);
	}
	
	/**
	 * This method delegates the difference operation to the problem description and
	 * then inverts the result if necessary (if minimum search is used).<br>
	 *  
	 * @param x the operand from which the difference is computed
	 * @param y the operand to which the difference is computed
	 * 
	 * @return a negative double, zero, or a positive double
	 * which indicates the difference of state x to state y according to the search direction 
	 */
	protected double difference(T x,T y){
		double result=problem.difference(x, y);
		return (minimum() ? result : -result);
	}

	/**
	 * This method selects a random state from the list of successors.
	 *  
	 * @param expand the list of successor states
	 * @return the selected state or null if the list was empty
	 */
	private T select(List<T> expand) {
		if (expand.isEmpty()) return null;
		return expand.get(random.nextInt(expand.size()));
	}
	
	/**
	 * Returns the temperature at the beginning of each search run 
	 * 
	 * @return the initial temperature
	 */
	public double getInitialTemperature() {
		return temperature;
	}

	/**
	 * Returns the factor by which the temperature will be lowered on each iteration.
	 * 
	 * @return the cooling factor
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * Returns the maximum amount of steps without improvement to the search result,
	 * before the search terminates.
	 * 
	 * @return amount of steps without improvements
	 */
	public int getMaxSame() {
		return maxSame;
	}

	/**
	 * This method resets the initial temperature of the search to the given value.
	 * This operation has only effect if the value is greater 0.<br>
	 * <b>Note:</b>	Invoking this method while running a search will only effect the following
	 * search runs. 
	 * 
	 * @param temperature a value greater 0 by which the next annealing process starts
	 */
	public void setInitialTemperature(double temperature){
		if (temperature>0)
			this.temperature = temperature;
	}
	
	/**
	 * This method resets the factor by which the temperature will be lowered
	 * during a search run.
	 * This operation has only effect if the given value is greater then 0 and
	 * less then 1.<br>
	 * <b>Note:</b> This operation works also while running a search and
	 * can be used to change the scheduling of temperature decrease.
	 * 
	 * @param factor between 0 and 1 by which the temperature will be lowered
	 */
	public void setFactor(double factor){
	 if ((factor>0) && (factor<1))
	 	this.factor = factor;	  
	}
	
	/**
	 * This method resets the amount of steps without improvements to the search result.
	 * This operation has only effect if the given value is greater then 0.<br>
	 * <b>Note:</b> You can reset this property also while running a search.
	 * 
	 * @param maxSame the maximum amount of steps without improvements
	 */
	public void setMaxSame(int maxSame) {
	 if (maxSame>0)
	 	this.maxSame = maxSame;
	}

	/**
	 * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
	 */
	public SimulatedAnnealingProblem<T> getProblem() {
		return problem;
	}

	/**
	 * In detail the search algorithm can be described in the following way:
	 * 
	 * <pre><code>
	 * current = problem.initial();
	 * result = current;
	 * while( running() ){
	 * 	next = select_random_successor(current);
	 * 	delta = difference(current, next);
	 * 	if ( (delta &gt; 0) || (random.nextDouble() &lt; Math.exp( delta / temperature ) )
	 * 		current = next;
	 * 	if (difference(result,current) &gt; 0){
	 * 		result = current;
	 * 	else
	 * 		temperature = temperature * factor;
	 * }
	 * </code></pre>
	 * 
	 * @see core.Search#search()
	 */
	@Override
	protected void search() {
  T current=problem.initial();
  result=current;
		T next;
		int same=0;
		double temp=temperature;
		double delta;
		List<T> expanded=problem.expand(current);
		while(same<maxSame && running()){
			next=select(expanded);
			if (next==null) break;
  	neededSteps++;
  	delta=difference(current,next);
  	if ((delta>0) || (random.nextDouble()<Math.exp(delta/temp))){
  		current=next;	
  		expanded=problem.expand(current);
  	}
 		if (difference(result,current)>0){
 			result=current;
 			same=0;
 		}else{
 			same++;
 			temp=temp*factor;
 		}
		}		
	}

	/**
	 * This method checks if the given problem returns valid initial states.
	 * 
	 * @see core.Search#canPrepare()
	 */
	@Override
	protected boolean canPrepare() {
		return problem.initial() != null;
	}
 /**
  * This method does nothing.
	 * 
	 * @see core.Search#prepare()
  */
	@Override
	protected void prepare() {
		
	}

}
