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

package core;

/**
 * This Class is the abstraction of every search algorithm in a generic domain.
 * It is typically used to pass searches around and manipulate
 * them where maximum generality is desired (like the Collection interface).<br>
 * In general a user can run each search in two equivalent ways:
 * <pre><code>
 * Search&lt;Integer&gt; search = new MySearch(5);
 * search.run();
 * search.getResult(); // should return 42 
 * </code></pre>
 * or:
 * <pre><code>
 * Search&lt;Integer&gt; search = new MySearch(5);
 * if (search.initialize()){
 *  search.run();
 *  search.getResult(); // should return 42 
 * }else{
 *  System.out.println("The search could not be initialized!");
 * } 
 * </code></pre>
 * <b>Note:</b> The search will only be executed if it can be initialized().
 * So an uninitialized search will first try to initialize itself before execution.
 * <br>
 * <hr>
 * To implement a new search you just have to create a subclass of Search
 * and implement the abstract methods in an appropriate way.<br>
 * There are some important information for developers of a new subclasses:
 * <ul>
 * <li>
 * <b>Calling order:</b> The three hook methods will be called in a fixed order.
 * First Search#canPrepare() will be called. If it returned true Search#prepare()
 * will be called immediately afterwards. 
 * After that the Search#search() method will be called to execute the search algorithm.
 * </li>
 * <li>
 * <b>Special fields:</b> The are two special fields defined.<br>
 * First the <code>result</code> is a place holder for the solution found by your search.
 * After initializing a search this field will be set to <code>null</code>.
 * The developer must explicitly assign the solution to this field, 
 * in order to allow the user to access the result after execution of your search algorithm.<br>
 * Second the <code>neededSteps</code> is a counter for the number of iterations your 
 * search algorithm needed.
 * After initializing a search this field will be reset to zero.
 * You must explicitly increment this field during your search, to allow users
 * instant access to the progress of your search.
 * The semantic of <code>neededSteps</code> is not fixed and may differ in some subclasses. 
 * </li>
 * </ul>
 * There are further implementation patterns for each subclass of Search:
 * <ol>
 * <li>
 * <b>Checking preconditions:</b>
 * Before a search can be executed Search#canPrepare() is checked. 
 * This method should be used to check all preconditions of a search algorithm
 * so that if Search#canPrepare() returned true, Search#prepare() and Search#search()
 * will be executed without any exceptions.<br>
 * <i>Example:</i> <code>return ( problem.initial() != null )</code>
 * </li>
 * <li>
 * <b>Prepare a search:</b>
 * Before a search is executed Search#prepare() is called to prepare your search.
 * In general this method should be used for time consuming preparation tasks,
 * such as creating an index or clearing a hash, which should not effect the execution time
 * of the Search#run() method it self.<br>
 * Which initialization should be done in the prepare() method and
 * which inside the search() method depends only on the developer.<br>
 * <i>Example:</i> <code>hash.clear()</code>
 * </li>
 * <li>
 * <b>Incrementing the step counter:</b>
 * This class contains a special field named <code>neededSteps</code>.
 * This field will be automatically set to zero whenever a search is initialized.
 * You should increment this field during execution of the Search#search() method
 * so users can see the progress of the your search.<br>
 * <i>Example:</i> <code>neededSteps++;</code>
 * </li>
 * <li>
 * <b>Stopping a search:</b><br>
 * To enable your subclass to terminate the execution whenever the user called Search#stop()
 * you must check the Search#running() method during the execution of your search.
 * This method will return <code>false</code> whenever the execution should be terminated.<br>
 * <i>Example:</i> <code>if (! running() ) return;</code> 
 * </li>
 * <li>
 * <b>Yielding the solution:</b>
 * This class contains a special field named <code>result</code>.
 * If your search finds a solution then you should store this solution of Type T
 * into this field (by assigning the solution to it) before you terminate your search.
 * The <code>result</code> will hold the solution until the Search#initialize() method
 * is called again.<br> 
 * <i>Example:</i> <code>if ( problem.isGoal(state) ){ result=state; return; }</code>
 * </li>
 * </ol>
 * The following simple example shows most of the basic implementation patterns:
 * <pre><code>
 * public class MySearch extends Search<Integer>{
 *  private Integer initial=null;
 *  public MySearch(Integer i) {
 *   initial=i; 
 *  }
 *  protected void search(){
 *   Integer current=initial;
 *   while(running()){                // (4.)
 *    neededSteps++;                  // (3.)
 *    current++;
 *    if (current % 42 == 0){         // (5.)
 *     result = i;
 *     break; 
 *    }
 *   }
 *  }
 *  protected void canPrepare(){ 
 *   return initial!=null;            // (1.)
 *  }
 *  protected void prepare() { 
 *   if (initial<0) initial=-initial; // (2.)
 *  } 
 * } 
 * </code></pre>
 * 
 * 
 *  
 * 
 * @see core.DirectedSearch 
 * @see core.UndirectedSearch 
 *
 * @param <T> the specific type of all elements of the search domain.
 * 
 * @author Eden_06
 */
public abstract class Search<T> implements Runnable {
	/**
	 * declaration of the states a search can have 
	 */
	private enum ESearchState { Terminated, Initialized, Running	}
	/**
  * holding the current state of the search
  */
	private ESearchState state=ESearchState.Terminated;
 /**
  * holding the number of steps needed for the search
  */
	protected int neededSteps=0;
	/**
  * holding the result of the search or null if nothing has been found yet
  */
	protected T result=null;
	
	/**
	 * Creates a new instance of an abstract search.
	 */
	public Search(){

	}
	
	/**
  * This method initializes the search if possible and if it is not already initialized.
  * In detail it uses the method canPrepare to determine if the search can really be prepared.
  * Afterwards it uses the method prepare to initialize the search.
  *  
  * @return true if the search is successfully initialized otherwise false
  */
	public final boolean initialize(){
		if ( ESearchState.Initialized.equals(state) ) return true;
		if ( canPrepare() ){
			neededSteps=0;
			result=null;
			prepare();
		 state=ESearchState.Initialized;
		}
		return ESearchState.Initialized.equals(state);
	}
	/**
	 * This method tells you whether a search is initialized (and can be performed) or not.
	 * 
	 * @return true if the search has been initialized
	 */
	public final boolean initialized(){
		return ESearchState.Initialized.equals(state);
	}
	/**
	 * This method runs the search if it can be initialized.
	 * After running this method initialized is set to false again.<br>
	 * <b>Note:</b> This method will also call the initialize() method,
	 * so you do not have to initialize a search. 
	 */
 public final void run(){
 	if (initialize()){
 		state=ESearchState.Running;
 		search();
 		state=ESearchState.Terminated;
 	}
 }
 /**
  * This method stops a running search.
  * In detail it sets an internal flag, which tells the search that it should terminate.
  */
 public final void stop(){
 	if (ESearchState.Running.equals(state))
 	 state=ESearchState.Terminated; 	
 }
 /**
  * This method returns true only if the search has been started and is not finished or stopped.<br>
  * <b>Note:</b> Every Subclass should call the running() method subsequently during a search 
  * and abort the search if it returned false. 
  *  
  * @return true only if the search has been started and is neither stopped nor finished
  */
 public final boolean running(){
 	return ESearchState.Running.equals(state);
 }
 /**
  * This method returns the generic solution in the domain of the search.
  * or null if the search was not yet successful.
  * 
  * @return a solution of the generic type T or null
  */
 public final T getResult(){
 	return result;
 }
 /**
  * This method returns the number of steps the search needed to find a solution
  * or zero if the search was not yet successful.<br>
  * <b>Note:</b> In respective to some search algorithms
  * it may not return the number of cycles during the computation.
  * 
  * @return the number of steps or zero
  */
 public final int neededSteps(){
 	return neededSteps;
 } 
	/**
	 * This method is the hook for any concrete implementation of
	 * a search algorithm and will be called from inside the run method.<br>
	 * Note: The variable result and neededSteps must be set in this
	 * method in an appropriate way,
	 * to ensure that the result contain the solution or null if no solution was found.  
	 */
 protected abstract void search();
 /**
  * This method is the hook for the preparation of the search and 
  * determines if the search can be initialized.
  * Use this method only to check the constraints for your search.<br>
  * <b>Note:</b> Do not make preparation for the search in this method use the prepare method.
  * 
  * @return true if the search can be prepared
  */
 protected abstract boolean canPrepare();
 /**
  * This method is the hook for the preparation of the search and 
  * will be called from inside the initialize method.<br>
  * <b>Note:</b> The contract of the implementation of the initialize method ensures
  * that prepare is only called when canPrepare returns true previously.
  * So you must not check any constraints from the canPrepare method.<br>
  * Warning: It is prohibited for this method to throw any kind of Exception!
  */
 protected abstract void prepare();
 /**
  * This method frees the search.
  */
 @Override
	protected void finalize() throws Throwable {
  result=null;
  state=null;
		super.finalize();
	}
	
}

