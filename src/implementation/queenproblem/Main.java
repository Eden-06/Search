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

package implementation.queenproblem;

import core.UndirectedSearch;
import extended.HillClimbing;
import extended.SimulatedAnnealing;
import extended.HillClimbing.EStrategy;
import util.SystemInput;

public class Main {

	/**
	 * @param args
	 */
		public static void main(String[] args) {
			String ask;
			int sel;
			long time;
			QueenProblem problem;
			UndirectedSearch<IQueenGame> search;
			
			do{
				System.out.println("Welcome to the n-Queen problem solver");
				System.out.println("=====================================");
				System.out.println("Enter the size of the problem:");
				System.out.println(" Allowed are values higher three. The default value is the eight.");
				while(true){
					ask=SystemInput.readLn();
					try{
						if (ask.length()==0){
							sel=8;
						}else{
							sel=Integer.parseInt(ask);
						}				
					}catch(Exception e){
						sel=0;
					}
					if (sel<4){
						System.out.println("Illegal number format! Try again...");
					}else{
						break;
					}
				}
				problem=new QueenProblem(sel);
				
				System.out.println("Which Algorithm should be used");
				System.out.println("1 - Simulated Annealing");
				System.out.println("2 - Hill Climbing [default]");
				while(true){
					ask=SystemInput.readLn();
					try{
						if (ask.length()==0){
							sel=2;
						}else{
							sel=Integer.parseInt(ask);
						}				
					}catch(Exception e){
						sel=0;
					}
					if ((sel<1) || (sel>2)){
						System.out.println("Illegal number format! Try again...");
					}else{
						break;
					}
				}	
				if (sel==1){ //Simulated Annealing
					
					double temp,fact;
					int max;
					System.out.println("Enter the initial temperatur:");
					System.out.println(" The value must be a positiv number. The default value is 10.");
					while(true){
						ask=SystemInput.readLn();
						try{
							if (ask.length()==0){
								temp=10.0;
							}else{
								temp=Double.parseDouble(ask);
							}				
						}catch(Exception e){
							temp=-1;
						}
						if (temp<0){
							System.out.println("Illegal number format! Try again...");
						}else{
							break;
						}
					}

					System.out.println("Enter the cooling factor:");
					System.out.println(" The value must be a value beetween 0 and 1. The default value is 0.95");
					while(true){
						ask=SystemInput.readLn();
						try{
							if (ask.length()==0){
								fact=0.95;
							}else{
								fact=Double.parseDouble(ask);
							}				
						}catch(Exception e){
							fact=0;
						}
						if (fact<=0 || fact>=1){
							System.out.println("Illegal number format! Try again...");
						}else{
							break;
						}
					}
					
					System.out.println("Enter the maximal number of steps without improvement:");
					System.out.println(" The value must be positive number. The default value is 100");
					while(true){
						ask=SystemInput.readLn();
						try{
							if (ask.length()==0){
								max=100;
							}else{
								max=Integer.parseInt(ask);
							}				
						}catch(Exception e){
							max=0;
						}
						if (max<=0){
							System.out.println("Illegal number format! Try again...");
						}else{
							break;
						}
					}
					
					search=new SimulatedAnnealing<IQueenGame>(problem,true,temp,fact,max);
					System.out.println("Starting SimulatedAnnealing");
					
				}else{ //Hill Climbing
					
					EStrategy strategy=null;
					
					System.out.println("Which Hillclimbing variant should be used");
					System.out.println("1 - First-Choice-Hillclimbing");
					System.out.println("2 - Best-Choice-Hillclimbing [default]");
					while(true){
						ask=SystemInput.readLn();
						try{
							if (ask.length()==0){
								sel=2;
							}else{
								sel=Integer.parseInt(ask);
							}				
						}catch(Exception e){
							sel=0;
						}
						if ((sel<1) || (sel>2)){
							System.out.println("Illegal number format! Try again...");
						}else{
							break;
						}
					}
					switch (sel){
					 case 1 : strategy = EStrategy.FIRST_CHOICE; break;
					 default: strategy = EStrategy.BEST_CHOICE; 
					}		
					
					System.out.println("Enter the number of allowed Sidesteps:");
					System.out.println(" The value must be zero or a positiv number. The default value is zero.");
					while(true){
						ask=SystemInput.readLn();
						try{
							if (ask.length()==0){
								sel=0;
							}else{
								sel=Integer.parseInt(ask);
							}				
						}catch(Exception e){
							sel=-1;
						}
						if (sel<0){
							System.out.println("Illegal number format! Try again...");
						}else{
							break;
						}
					}					
					search=new HillClimbing<IQueenGame>(problem,true,strategy,sel);
					System.out.println("Starting Hillclimbing");					
				}
				
				search.initialize();
				time=System.currentTimeMillis();
				search.run();
				time=(System.currentTimeMillis()-time);
				System.out.println(search.getResult());
				System.out.format("Milliseconds: %d\n",time);
				if ((search.getResult()!=null) && (search.getResult().collisions()==0))
					System.out.println("Problem: solved");
				else
					System.out.println("Problem: not solved");
				System.out.format("Steps: %d\n",search.neededSteps());
				System.out.format("Collisions: %d\n",search.getResult().collisions());
				
			 System.out.println();
		  System.out.println("Do you wish to solve another puzzle? (y/n)");
		  ask=SystemInput.readLn();
		 }while(ask.equalsIgnoreCase("y"));
		}

}



