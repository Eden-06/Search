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

import java.util.Comparator;
import java.util.List;

import core.UndirectedSearch;

/**
 * The hill climbing algorithm is simple local search
 * able to solve any problem implementing the extended.HillClimbingProblem interface.<br>
 * <br>
 * This algorithm starts with the initial state,
 * selects the best successor of the current state as new current state and
 * continues until no best successor can be found.
 * The current state now holds the found solution, in fact a local minimum (or maximum).<br>
 * This strategy grants constant space and linear time complexity
 * but neither completeness nor optimality.
 * In detail it is possible to gain completeness with two simple modifications to the algorithm.<br>
 * The first is, to allow a limited amount of side steps. 
 * A side step is an iteration where a state equal (according to the comparator)
 * to the current state is selected as new current state.<br>
 * <i>This ability is directly implemented in this algorithm and can be used in the following way:</i><br>
 * <pre><code>
 * HillClimbingProblem&lt;MyState&gt; problem=new MyHillClimbingProblem();
 * HillClimbing&lt;MyState&gt; search=new HillClimbing<MyState>(problem, true, EhcStrategy.BEST_CHOICE, 100);
 * //creates a hill climbing search where 100 side steps are allowed during the search
 * search.setSideSteps(1000);
 * //changes the allowed number of side steps for this hill climbing search to 1000
 * </code></pre>
 * The second is, to run the hill climbing search <b>with random initial states</b>
 * for several times and remembering only the best solution of all runs.
 * <i>This ability is not implemented but can easily be implemented in the following way:</i><br> 
 * <pre><code>
 * int limit=10; 
 * HillClimbingProblem&lt;MyState&gt; problem=new MyHillClimbingProblem();
 * MyState best=problem.initial();
 * HillClimbing&lt;MyState&gt; search=new HillClimbing<MyState>(problem);
 * for (iteration=0; iteration<limit ; iteration++){
 * 	search.run();
 * 	if (search.getResult()!=null){
 * 		if ( search.compare(best, search.getResult()) &lt; 0 ){
 * 			best=search.getResult();
 * 		}
 * 	}
 * } 
 * </code></pre>
 * <b>Note:</b> This algorithm is based on probability so the found solution,
 * can vary on successive runs. This algorithm works best in situations,
 * where you only need a good approximation to the optimal solution in a short amount of time.<br>
 * <br> 
 * Hill climbing can use different strategies on how to select
 * a better state. There are two default strategies shipped with this implementation.
 * You must check if one of the default strategies fits best to your problem
 * or if you implement your own strategy.<br>
 * The following list guide you through the different opportunities: 
 * <ul>
 * <li>
 * <b>Best-Choice</b> selects the best successor state and 
 * if there are more then one best states choose one randomly.<br>
 * This strategy suits perfect for a search domains
 * with a small (and limited) number of successors of each state,
 * where the look up of the best states will not take long and
 * where you need a high probability of finding a solution.
 * The major problem of this strategy is the fact,
 * that you can not scale it to a bigger search domain
 * without losing lots of performance (especially time).<br>
 * The following example shows how to create or change the strategy
 * of a search to the best choice strategy:
 * <pre><code>
 * HillClimbingProblem&lt;State&gt; problem = new MyHillClimbingProblem();
 * //1.) Creates a search which uses the first choice strategy  
 * HillClimbing&lt;State&gt; search = new HillClimbingSearch&lt;State&gt;(problem, EStrategy.BEST_CHOICE);
 * //2.) Changes the search strategy of an HillClimbingSearch to the first choice strategy
 * search.setStrategy( EStrategy.BEST_CHOICE );  
 * </li>
 * <li>
 * <b>First-Choice</b> selects the first successor state
 * which is better then the current state.<br>
 * This strategy suits perfect for large search domains with a
 * large (or infinite) number of successor states.
 * The main contribution you must take is that your search
 * need more steps to finish and you can not reach a high probability
 * of finding a solution in a single run.<br>
 * The following example shows how to create or change the strategy
 * of a search to the first choice strategy:
 * <pre><code>
 * HillClimbingProblem&lt;State&gt; problem = new MyHillClimbingProblem();
 * //1.) Creates a search which uses the first choice strategy  
 * HillClimbing&lt;State&gt; search = new HillClimbingSearch&lt;State&gt;(problem, EStrategy.FIRST_CHOICE);
 * //2.) Changes the search strategy of an HillClimbingSearch to the first choice strategy
 * search.setStrategy( EStrategy.FIRST_CHOICE ); 
 * </code></pre>
 * </li>
 * <li>
 * <b>Custom</b> selects a successor state according to a user defined strategy.<br>
 * With you own strategy you can take special knowledge of the search domain into account.
 * In this way you can improve the search performance significantly despite the fact
 * that it is more complicated to create your own HillClimbingStrategy class.
 * For more information on how to create custom search strategies look at
 * the description of extended.HillClimbingStrategy.<br> 
 * You can change the hill climbing strategy in two different ways.
 * Both are shown in the following example:<br>
 * <pre><code>
 * HillClimbingProblem&lt;State&gt; problem = new MyHillClimbingProblem();
 * HillClimbingStrategy&lt;State&gt; strategy = new MyHillClimbingStrategy();
 * //1.) Creates a search which uses your custom strategy
 * HillClimbing&lt;State&gt; search = new HillClimbingSearch&lt;State&gt;(problem, true, strategy, 100);
 * //2.) Changes the search strategy of an HillClimbingSearch to your custom strategy
 * search.setStrategy( strategy );
 * </code></pre>
 * </li>
 * </ul>
 * 
 * @see UndirectedSearch
 * @see HillClimbingProblem
 * @see HillClimbingStrategy
 * @see BestChoiceStrategy
 * @see FirstChoiceStrategy
 * @see SimulatedAnnealing
 * 
 * @author Eden_06
 *
 * @param <T> the specific type of all elements in the search domain.
 */
public class HillClimbing<T> extends UndirectedSearch<T> implements Comparator<T>{
	
	/**
	 * Enumeration to select one of the two default hill climbing strategies.
	 * 
	 * @see extended.BestChoiceStrategy
	 * @see extended.FirstChoiceStrategy
	 * 
	 * @author eden06
	 */
	public enum EStrategy {
		/**
		 * selects the best choice strategy for this hill climbing search
		 */
		BEST_CHOICE,
		/**
		 * selects the first choice strategy for this hill climbing search
		 */
		FIRST_CHOICE 
	}
	
	/**
	 * the number of side steps the search actually needed
	 */
	private int neededSideSteps=0;
	/**
	 * the amount of side steps allowed during a search
	 */
	private int sideSteps=0;
	/**
	 * holds a reference to the problem to be solved
	 */
	protected HillClimbingProblem<T> problem=null;
	/**
	 * holds the strategy used during the search
	 */
	protected HillClimbingStrategy<T> strategy=null;
	
	/**
	 * Creates a new hill climbing search, with the given problem.
	 * The search will look for a global minimum with a best choice strategy and
	 * no allowed side steps.
	 * 
	 * @param problem the HillClimbingProblem to be solved
	 * 
	 * @throws IllegalArgumentException if the problem is null
	 */
	public HillClimbing(HillClimbingProblem<T> problem) {
		super(true);
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
		strategy=new BestChoiceStrategy<T>();
	}	
	
	/**
	 * Creates a new hill climbing search, with the given problem and 
	 * the direction given by the minimum flag.
	 * The search will use the selected strategy without allowing side steps.
	 * 
	 * @param problem the HillClimbingProblem to be solved
	 * @param minimum flag indicating whether the search looks for minimum values or not
	 * @param strategy enumeration indicating which of the default strategies to use
	 * 
	 * @throws IllegalArgumentException if problem or strategy is null
	 */
	public HillClimbing(HillClimbingProblem<T> problem, boolean minimum, EStrategy strategy){
		this(problem,minimum,strategy,0);
	}	
	/**
	 * Creates a new hill climbing search, with the given problem, 
	 * the direction given by the minimum flag and the allowed number of sideSteps.
	 * The search will use the selected strategy.
	 * 
	 * @param problem the HillClimbingProblem to be solved
	 * @param minimum flag indicating whether the search looks for minimum values or not
	 * @param strategy enumeration indicating which of the default strategies to use
	 * @param sideSteps the number of allowed side steps during the search
	 * 
	 * @throws IllegalArgumentException if problem or strategy is null and if sideSteps is smaller than zero
	 */
	public HillClimbing(HillClimbingProblem<T> problem, boolean minimum, EStrategy strategy, int sideSteps) {
		super(minimum);
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
		if (strategy==null) throw new IllegalArgumentException("strategy should never be null");
		else {
			switch(strategy){
				case FIRST_CHOICE: 
					this.strategy=new FirstChoiceStrategy<T>(); break;
			 default:
			 	this.strategy=new BestChoiceStrategy<T>();
			}
		}
	 if (sideSteps>=0) this.sideSteps=sideSteps;
	 else throw new IllegalArgumentException("sidesSteps must be greater or equal zero");
	}
	/**
	 * Creates a new hill climbing search, with the given problem, 
	 * the direction given by the minimum flag and the allowed number of sideSteps.
	 * The search will use the given HillClimbingStrategy during the search.
	 * 
	 * @param problem the HillClimbingProblem to be solved
	 * @param minimum flag indicating whether the search looks for minimum values or not
	 * @param strategy a custom HillClimbingStrategy instance for the search
	 * @param sideSteps the number of allowed side steps during the search
	 * 
	 * @throws IllegalArgumentException if problem or strategy is null and if sideSteps is smaller than zero
	 */
	public HillClimbing(HillClimbingProblem<T> problem, boolean minimum, HillClimbingStrategy<T> strategy, int sideSteps) {
		super(minimum);
		if (problem!=null) this.problem=problem; 
	 else throw new IllegalArgumentException("problem should never be null!");
		if (strategy!=null) this.strategy=strategy;
		else throw new IllegalArgumentException("strategy should never be null");
	 if (sideSteps>=0) this.sideSteps=sideSteps;
	 else throw new IllegalArgumentException("sidesSteps must be greater or equal zero");

	}
	
	/**
	 * Creates a new hill climbing search, with the given problem and 
	 * the selected strategy.
	 * The search will look for a global minimum without allowing side steps.
	 * 
	 * @param problem the HillClimbingProblem to be solved
	 * @param strategy enumeration indicating which of the default strategies to use
	 * 
	 * @throws IllegalArgumentException if problem or strategy is null
	 */
	public HillClimbing(HillClimbingProblem<T> problem, EStrategy strategy){
		this(problem,true,strategy,0);
	}
	/**
	 * This method returns the Comparator the HillClimbingProblem
	 * implements.
	 * 
	 * @return the comparator imposed by the HillClimbingProblem instance 
	 */
	protected Comparator<T> comparator(){
		return problem;
	}
	
	/**
	 * This method delegates the compare operation to the correct comparator and
	 * then inverts the value if necessary (if minimum search is used).<br>
	 * Note: This method must be used when your implementing your own hill climbing strategy.
	 * 
	 * @param x
	 *  left side of the comparison
	 * @param y
	 *  right side of the comparison
	 * @return -1,0 or 1 in respect to the comparator and search direction
	 */
	public final int compare(T x,T y){
		int result=problem.compare(x,y);
		return (minimum() ? -result : result);
	}
	/**
  * This method returns the problem, with which this search has been created.
	 * 
	 * @return the problem to be solved
	 */
	public final HillClimbingProblem<T> getProblem(){
		return problem;
	}
	/**
	 * This method returns the actual HillClimbingStrategy used
	 * during the search. 
	 * 
	 * @return the strategy used during the hill climbing search
	 */
	public final HillClimbingStrategy<T> getStrategy() {
		return strategy;
	}

	/**
	 * This method gives you the number of side steps needed by the last search.
	 * 
	 * @return 
	 *  the number of needed side steps
	 */
	public final int neededSideSteps(){
		return (neededSideSteps<0 ? 0 : neededSideSteps);
	}
	
	/**
	 * This method resets the number of allowed side steps
	 * to the given value.<br> 
	 * Note: This method has also effect during a running
	 * or initialized search! 
	 * @param steps
	 *  the new number of side steps
	 */
	public final void setSideSteps(int steps){
		if (steps>=0)	sideSteps=steps;
		//if ((initialized()) && (steps>=0))	sideSteps=steps;
	}
	
	/**
	 * This method replaces the used strategy by the given one.
	 * <b>Note:</b> This method has only effect if the search has not been initialized!
	 * 
	 * @param strategy the HillClimbingStrategy to set
	 */
	public final void setStrategy(HillClimbingStrategy<T> strategy) {
		if (!initialized()) this.strategy = strategy;
	}
	
	/**
	 * This method replaces the used strategy by the selected default strategy.
	 * <b>Note:</b> This method has only effect if the search has not been initialized and 
	 * the given strategy is not null.
	 * 
	 * @param strategy enumeration indicating which of the default strategies to set
	 */
	public final void setStrategy(EStrategy strategy) {
		if (!initialized() && (strategy!=null)){
			switch(strategy){
				case FIRST_CHOICE: 
					this.strategy=new FirstChoiceStrategy<T>(); break;
				default:
					this.strategy=new BestChoiceStrategy<T>();
			}			
		}
	}
 /**
  * This method delegates to the select method of the used strategy.
  * It only forwards the message if the extension and current arguments are not null
  * and not empty.
  * 
  * @param extension the list of successors of the current state
  * @param current the current state for this hill climbing step
  * @return the selected state according to the strategy or null
  */
	private final T select(List<T> extension,T current){
		if (extension==null || extension.isEmpty())		return null;
		if (current==null) return null;
		return strategy.select(this,extension,current);		
	}
	/**
	 * Returns true only there is a strategy and an initial state available.
	 */
	@Override
	protected boolean canPrepare() {
		return (strategy!=null) && (problem.initial()!=null);
	}
	/**
	 * Initializes the number of needed side steps.
	 */
	@Override
	protected void prepare() {
	 neededSideSteps=-1;		//ensure search won't stop when sideSteps==0
	}
	/**
	 * This method fills the hook which implements the actual HillClimbing algorithm.
	 * The implementation can be described in the following way:
	 * 
	 * <pre><code>
	 * current = problem.initial();
	 * while( running() ){
	 * 	next = select_best_successor( current );
	 * 	value = compare(current, next)
	 * 	if (value &gt; 0){ // there was no best successor
	 * 		result = current;
	 * 		break;
	 * 	}
	 * 	current = next;
	 * }
	 * </code></pre>
	 * 
	 */
	@Override
	protected void search() {
  T current=problem.initial();
		T next;
		int compare=0;
  while(running()){
  	next=select(problem.expand(current),current);
  	if (next==null) break;
  	neededSteps++;
  	compare=compare(current,next);
  	if (compare==0) neededSideSteps++;
  	if ((compare>0) || (neededSideSteps>=sideSteps)){
  		result=current;
  		break;
  	} 
  	current=next;
  } 				
	}

}
