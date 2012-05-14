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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.SystemInput;

import extended.BidirectionalProblem;
import extended.BidirectionalSearch;

public class MainBidirectional {

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
 			System.out.println("Welcome to the 8-Puzzle solver");
 			System.out.println(" (Using BidirectionalSearch)");
 			System.out.println("==============================");
 			System.out.println("Which Puzzle should be solved");
 			System.out.println("1 - example from the exercise [default]");
 			System.out.println("2 - Random generated puzzle");
 			System.out.println("3 - Custom puzzle");
 			System.out.println("4 - An easy 15-Puzzle");
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
 				if ((sel<1) || (sel>4)){
 					System.out.println("Illegal number format");
 				}else{
 					break;
 				}
 			}
 			GameGrid source=new GameGrid(3,3),
 			         target=new GameGrid(3,3);
 		 switch (sel){
 		  case 2 :	buildRandom(source); 	break;
 		  case 3 : buildCustom(source);		break;
 		  case 4 : 
 		  	source=new GameGrid(4,4);
      target=new GameGrid(4,4);
 		  	buildEasy(source);		break;
 		  default: buildDefault(source);
 		 }
		 target.initialize();
		 System.out.println("Trying to solve this Puzzle:");
		 System.out.println(source.toString());
		 BidirectionalProblem<GridState> problem=new BidirectionalGridProblem(source,target);
		  
	  BidirectionalSearch<GridState> puzzle=new BidirectionalSearch<GridState>(problem);
	  puzzle.initialize();
	  long time=System.currentTimeMillis();
	  puzzle.run();
	  System.out.format("Milliseconds: %d\n",System.currentTimeMillis()-time);
	  System.out.format("Number of branched nodes: %d\n",puzzle.neededSteps());
	  GridState hitFromTop    = puzzle.getHitFromTop();
	  GridState hitFromBottom = puzzle.getHitFromBottom();
	  
	  //print solution
	  if (hitFromTop!=null && hitFromBottom!=null){
	  	List<Point> pathToHit=hitFromTop.getPath();	  	
	  	List<Point> pathFromHit=hitFromBottom.getPath();
	  	
	  	Collections.reverse(pathFromHit);
	  	
	  	System.out.format("Number of moves: %d\n",pathToHit.size()+pathFromHit.size());
	  	GameGrid current=new GameGrid(source);
	  	for (Point p: pathToHit){
	  		System.out.println(current.toString());
	  		System.out.format("move (%d,%d)\n",p.x,p.y);
					current.move(p.x,p.y);
				}
	  	System.out.println(current.toString());
	  	System.out.println("Found equals state in both directions.");
	  	for (Point p: pathFromHit){
	  		System.out.format("move (%d,%d)\n",p.x,p.y);
	  		current.move(p.x,p.y);
	  		System.out.println(current.toString());										
	  	}
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
