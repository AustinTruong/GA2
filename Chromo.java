/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*  
*  Modifications by Austin Truong
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public String chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;
	public boolean valid;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  Set gene values to string of random chars in range [0,35)
		char pair;
		chromo = "";
		for (int i=0; i<5; i++){
			for (int j=0; j<7; j++){
				randnum = Search.r.nextDouble();
				pair = (char) ((int) (randnum*35)+'0');

				this.chromo = chromo + pair;
			}
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
		this.valid = false;		//  Not yet passed validation
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		int start = geneID * Parameters.geneSize;
		int end = (geneID+1) * Parameters.geneSize;
		String geneAlpha = this.chromo.substring(start, end);
		return (geneAlpha);
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
		char geneValue = chromo.charAt(geneID);
        return (int) geneValue - '0';
//		String geneAlpha = "";
//		int geneValue;
//		char geneSign;
//		char geneBit;
//		
//		geneValue = 0;
//		geneAlpha = getGeneAlpha(geneID);
//		for (int i=Parameters.geneSize-1; i>=1; i--){
//			geneBit = geneAlpha.charAt(i);
//			geneValue = (int) (geneValue + geneBit * Math.pow(35.0, Parameters.geneSize-i-1));
//		}
////		geneValue += (int)geneAlpha - '0';
//		return (geneValue);
//		geneValue = 0;
//		geneAlpha = getGeneAlpha(geneID);
//		for (int i=Parameters.geneSize-1; i>=1; i--){
//			geneBit = geneAlpha.charAt(i);
//			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
//		}
//		geneSign = geneAlpha.charAt(0);
//		if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
//		return (geneValue);
	}

	//  Get Integer Value of a Gene (Positive only) ****************************

	public int getPosIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=0; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		return (geneValue);
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		String mutChromo = "";
		//char x;
		int x;
		switch (Parameters.mutationType){

		case 1:     //  Replace with new random number

			int numNumbers = 35;
			int numSize = 35;
			for (int j=0; j<(numNumbers); j++){
				
				x = (int) this.chromo.charAt(j);
				
				randnum = Search.r.nextDouble();
				int baseSize = numSize;
				// If wins roll to mutate
				if (randnum < Parameters.mutationRate){
					
					// Randomize the number.
					// Distance is not an issue here, so allow any number.
					randnum = Search.r.nextDouble();
					int mut = (int) randnum * baseSize;
					x = (x-'0' + mut) % baseSize + '0';
				}
				mutChromo = mutChromo + (char)x;
			}
			this.chromo = mutChromo;
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}
	
	/**
	 * Reset internal fitness/valid variables
	 */
	public void reset(){
		
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
		this.valid = false;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     // Tournament Selection (pool size 2)
			double pk = 0.7;// Ratio for best to win
			int i;
			double r = Search.r.nextDouble();
			
			// Select two random indvs
			randnum = Search.r.nextDouble();
			j = i = (int) (randnum * Parameters.popSize);
			randnum = Search.r.nextDouble();
			while(i == j){ // Make sure they are different
				randnum = Search.r.nextDouble();
				j = (int) (randnum * Parameters.popSize);
			}
			double iFit = Search.member[i].proFitness;
			double jFit = Search.member[j].proFitness;
			
			// Select
			if(iFit > jFit){
				if(r > pk) return i;
				return j;
			}
			else{
				if(r > pk) return j;				
				return i;
			}

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			//  Create child chromosome from parental material
			child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
			child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.reset();
		child2.reset();
//		child1.rawFitness = -1;   //  Fitness not yet evaluated
//		child1.sclFitness = -1;   //  Fitness not yet scaled
//		child1.proFitness = -1;   //  Fitness not yet proportionalized
//		child2.rawFitness = -1;   //  Fitness not yet evaluated
//		child2.sclFitness = -1;   //  Fitness not yet scaled
//		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.reset();
//		child.rawFitness = -1;   //  Fitness not yet evaluated
//		child.sclFitness = -1;   //  Fitness not yet scaled
//		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		targetA.valid = sourceB.valid;
		return;
	}

}   // End of Chromo.java ******************************************************
