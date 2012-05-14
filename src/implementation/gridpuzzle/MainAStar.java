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

package implementation.gridpuzzle;

import java.awt.Point;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import basic.AStarSearch;
import core.Search;

import util.SystemInput;


public class MainAStar {

	public static void buildDefault(GameGrid target){
		target.set(0,0,5);
		target.set(1,0,6);
		target.set(2,0,7);
		target.set(0,1,0);
		target.set(1,1,2);
		target.set(2,1,3);
		target.set(0,2,1);
		target.set(1,2,4);
		target.set(2,2,8);
	}
	
	public static void buildCustom(GameGrid target){
		String ask;
  while(true){
  	System.out.println("Enter custom 8-puzzle:");
  	System.out.println(" Seperate cols with [,] and rows with linebreaks.");
  	System.out.println(" Allowed are the Numbers from 0 to 8. Zero is the empty block.");
			for (int i=0;i<3;i++){
	   ask=SystemInput.readLn();
	   Pattern p=Pattern.compile("([0-8]),([0-8]),([0-8])");
	   Matcher m=p.matcher(ask);
	   if (m.matches() && (m.groupCount()>=3)){
	   	try{
	   		target.set(0,i,Integer.parseInt(m.group(1)));
	    	target.set(1,i,Integer.parseInt(m.group(2)));
	    	target.set(2,i,Integer.parseInt(m.group(3)));
	   	}catch (Exception e){
	   		System.out.println("The entered String could not be parsed as Integer.\nTry again:");
	   		i=-1;
	   	}   	
	   }else{
	   	System.out.println("The entered String could not be matched against [0-8],[0-8],[0-8].\nTry again:");
	   	i=-1;
	   }
	  }
			if (! target.isLegal()){
				System.out.println("The entered puzzle is illegal");
				System.out.println(target.toString());				
			}else{
				break;
			}
  }
  
	}
	
	public static void buildRandom(GameGrid target){
	 target.randomize();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ask;
		do{
			System.out.println("Welcome to the 8-Puzzle solver");
			System.out.println("          (Using A*)");
			System.out.println("==============================");
			System.out.println("Which Puzzle should be solved");
			System.out.println("1 - Example from the exercise [default]");
			System.out.println("2 - Random generated puzzle");
			System.out.println("3 - Custom puzzle");
			int sel;
			while(true){
				ask=SystemInput.readLn();
				try{
					if (ask.length()==0){
						sel=1;
					}else{
						sel=Integer.parseInt(ask);
					}				
				}catch(Exception e){
					sel=0;
				}
				if ((sel<1) || (sel>3)){
					System.out.println("Illegal number format");
				}else{
					break;
				}
			}
			GameGrid initial=new GameGrid(3,3);
		 switch (sel){
		  case 2 : buildRandom(initial); break;
		  case 3 : buildCustom(initial); break;
		  default: buildDefault(initial);
		 }
		 System.out.println("Which heuristic should be used");
			System.out.println("1 - Number of incorrect blocks");
			System.out.println("2 - Number of movements by direct exchange");
			System.out.println("3 - Sum of all block distances [default]");	
			while(true){
				ask=SystemInput.readLn();
				try{
					if (ask.length()==0){
						sel=3;
					}else{
						sel=Integer.parseInt(ask);
					}
				}catch(Exception e){
					sel=0;
				}
				if ((sel<1) || (sel>3)){
					System.out.println("Illegal number format");
				}else{
					break;
				}
			}
			GameGrid target=new GameGrid(3,3);
			target.initialize();
			AbstractHeuristic heuristic;
			switch (sel){
		  case 1 :	heuristic=new HeuristicOne(); break;
		  case 2 : heuristic=new HeuristicTwo(); break;
		  default: heuristic=new HeuristicThree();
		 }		
		 System.out.println("Trying to solve this Puzzle:");
		 System.out.println(initial.toString());
	  
		 GridProblem problem=new GridProblem(initial,target,heuristic);
		 Search<GridState> puzzle=new AStarSearch<GridState>(problem);
		 //search with hash and without update
		 puzzle.initialize();
	  long time=System.currentTimeMillis();
	  puzzle.run();
	  System.out.format("Millisecounds: %d\n",(System.currentTimeMillis()-time));
	  System.out.format("Number of branched nodes: %d\n",puzzle.neededSteps());//GridStates.instance().size()
	  GridState result=puzzle.getResult();
	  
	  //print solution
	  if (result!=null){
	  	List<Point> movements=result.getPath();
	  	System.out.format("Number of moves: %d\n",movements.size());
	  	GameGrid current=new GameGrid(initial);
	  	for (Point p : movements){
	  		System.out.println(current.toString());
	  		System.out.format("move (%d,%d)\n",p.x,p.y);
					current.move(p.x,p.y);
				}
	  	System.out.println(current.toString());
	  }else{
	  	System.out.println("Impossible to solve");
	  }
	  System.out.println();
	  System.out.println("Do you wish to solve another puzzle? (y/n)");
	  ask=SystemInput.readLn();
		}while(ask.equalsIgnoreCase("y"));
	}

}
