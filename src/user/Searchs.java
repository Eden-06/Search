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

package user;

import java.util.Map;

import basic.BreadthFirstSearch;
import basic.IterativeDeepeningAStar;
import basic.IterativeDeepeningSearch;



import core.HeuristicProblem;
import core.Problem;
import core.Search;
import core.TreeProblem;
import extended.HillClimbing;
import extended.HillClimbingProblem;
import extended.SimulatedAnnealing;
import extended.SimulatedAnnealingProblem;

/**
 * List of properties
 * 
 * selection properties
 * 
 * -depth first search
 * -breadth first search
 * -iterative deepening
 * 
 * -uniform cost search
 * -greedy search
 * -astar search
 * -iterative deepening astar
 * 
 * -hill climbing (?)
 * -simulated annealing (?) 
 * 
 * 
 * implicit properties
 * - branching factor
 * - search depth 
 * - duplicate states
 * - hash conformity
 * 
 * for heuristics
 * - monotony
 * - optimism
 * 
 * 
 * 
 * 
 * <b>Note:</b> The BidirectionalSearch must be created explicitly,
 * and will not be created by one of these build methods.
 * 
 * @author eden06
 *
 */
public final class Searchs {

	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @return
	 */
	public static <T> Map<String,Boolean> properties(HeuristicProblem<T> problem) {
		return null;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @return
	 */
	public static <T> Map<String,Boolean> properties(TreeProblem<T> problem) {
		return null;
	}
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @return
	 */
	public static <T> Map<String,Boolean> properties(Problem<T> problem) {
		return null;
	}
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @param properties
	 * @return
	 */
	public static <T> Search<T> build(SimulatedAnnealingProblem<T> problem, Map<String,Boolean> properties) {
		return new SimulatedAnnealing<T>(problem);
	}	
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @param properties
	 * @return
	 */
	public static <T> Search<T> build(HillClimbingProblem<T> problem, Map<String,Boolean> properties) {
		return new HillClimbing<T>(problem);
	}	
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @param properties
	 * @return
	 */
	public static <T> Search<T> build(HeuristicProblem<T> problem, Map<String,Boolean> properties) {
		return new IterativeDeepeningAStar<T>(problem);
	}
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @param properties
	 * @return
	 */
	public static <T> Search<T> build(TreeProblem<T> problem, Map<String,Boolean> properties) {
		return new IterativeDeepeningSearch<T>(problem);
	}
	/**
	 * 
	 * @param <T>
	 * @param problem
	 * @param properties
	 * @return
	 */
	public static <T> Search<T> build(Problem<T> problem, Map<String,Boolean> properties) {
		return new BreadthFirstSearch<T>(problem);
	}
	
	
}
