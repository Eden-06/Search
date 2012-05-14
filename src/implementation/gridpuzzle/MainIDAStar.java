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

import util.SystemInput;
import core.Search;
import basic.IterativeDeepeningAStar;

public class MainIDAStar {

	public static void buildEasy(GameGrid target){
		target.set(0,0,1);
		target.set(1,0,14);
		target.set(2,0,3);
		target.set(3,0,4);
		target.set(0,1,5);
		target.set(1,1,10);
		target.set(2,1,2);
		target.set(3,1,8);
		target.set(0,2,9);
		target.set(1,2,0);
		target.set(2,2,15);
		target.set(3,2,11);
		target.set(0,3,13);
		target.set(1,3,7);
		target.set(2,3,6);
		target.set(3,3,12);
	}
	
	public static void buildDifficult(GameGrid target){
		target.set(0,0,9);
		target.set(1,0,15);
		target.set(2,0,12);
		target.set(3,0,6);
		target.set(0,1,1);
		target.set(1,1,5);
		target.set(2,1,0);
		target.set(3,1,7);
		target.set(0,2,14);
		target.set(1,2,13);
		target.set(2,2,11);
		target.set(3,2,4);
		target.set(0,3,3);
		target.set(1,3,10);
		target.set(2,3,8);
		target.set(3,3,2);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ask;
 	do{
			System.out.println("Welcome to the 15-Puzzle solver");
			System.out.println("         (using IDA*)");
			System.out.println("===============================");
			System.out.println("Which Puzzle should be solved");
			System.out.println("1 - An easy example [default]");
			System.out.println("2 - A Random generated puzzle");
			System.out.println("3 - A difficult example");
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
			GameGrid target=new GameGrid(4,4);
			target.initialize();
			GameGrid source=new GameGrid(4,4);
		 switch (sel){
		  case 2 : source.randomize(); break;
		  case 3 : buildDifficult(source); break;
		  default: buildEasy(source);
		 }
		 System.out.println("Trying to solve this Puzzle:");
		 System.out.println(source.toString());
		 GridProblem problem=new GridProblem(source,target, new HeuristicThree());
		  
	  Search<GridState> puzzle=new IterativeDeepeningAStar<GridState>(problem);
	  puzzle.initialize();
	  long time=System.currentTimeMillis();
	  puzzle.run();
	  System.out.format("Milliseconds: %d\n",System.currentTimeMillis()-time);
	  System.out.format("Number of branched nodes: %d\n",puzzle.neededSteps());
	  GridState result=puzzle.getResult();
	  
	  //print solution
	  if (result!=null){
	  	List<Point> path=result.getPath();
	  	System.out.format("Number of moves: %d\n",path.size());
	  	GameGrid current=new GameGrid(source);
	  	for (Point p: path){
	  		System.out.println(current.toString());
	  		System.out.format("move (%d,%d)\n",p.x,p.y);
					current.move(p.x,p.y);
				}
	  	System.out.println(current.toString());
	  }else{
	  	System.out.println(source.toString());
	  	System.out.println("Impossible to solve");
	  }
	  System.out.println();
	  System.out.println("Do you wish to solve another puzzle? (y/n)");
	  ask=SystemInput.readLn();
		}while(ask.equalsIgnoreCase("y"));

	}

}
