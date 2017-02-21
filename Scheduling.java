/** 
 * Scheduling.java
 * by Austin Truong
 * 
 * 2017-1-10
 * 
 * Based on NumberMatch.java by Hal Stringer & Annie Wu, UCF, 2014-1-18
 * 
 */


/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Scheduling extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/

	//  Assumes no more than 100 values in the data file
	public static int[] testValue = new int[100];
	public static final int[] preferenceweights = {-500,200,100,50,10}; // Change these to affect preference fitness
	public static final double contigWeight = 10;
	public static final double missWeight = -1000000000;
	public static int[][] preferences;
	public static String[] names = new String[7];

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Scheduling () throws java.io.IOException {

		name = "Lab Scheduling Problem";

		//	Create Table of X values from input file
		//BufferedReader input = new BufferedReader(new FileReader (Parameters.dataInputFileName));
		//		for (int i=0; i<Parameters.numGenes; i++){
		//			testValue[i] = Integer.parseInt(input.readLine().trim());
		//		}
		
		// Read in preferences for evaluation
		Scanner scanner = new Scanner(new File(Parameters.dataInputFileName));
		preferences = new int[7][35];
		for( int i = 0; i < 7; i++){
			scanner.next();
			for(int j = 0; j < 35; j++){
				preferences[i][j] = scanner.nextInt();
			}
		}
		scanner.close();
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){

		X.valid = true; // Assume valid
		double prefFitness = 0;
		double contigFitness = 0;
		double missPenalty = 0;
		int[] sorted;
		int[] hitArray = new int[35];
		
		double difference = 0;
		for (int j=0; j<7; j++){
			sorted = new int[5];
			for (int k=0; k<5; k++){
				// Preference weighting
				//System.out.println(X.chromo);
				int index = j*5+k;
				if(index>=35) System.out.println("ERRORERRORERROR");
				int val = (int) Math.abs(X.getIntGeneValue(index));
				
				int pref = preferences[j][val];
				
				if(pref == 0){ // Preference validity check
					X.valid = false; // Solution not valid
				}
				
				
				int bonus = preferenceweights[pref];
				prefFitness += bonus;
				//System.out.println(bonus);
				// Validation/penalty check array
				hitArray[val]+=1;
				
				// Maintain sortedness for contiguity checks
				int tmp = val;
				for (int l = 0; l < sorted.length; l++){
					if(sorted[l] == 0){
						sorted[l] = tmp;
						break;
					}
					else if( sorted[l] < tmp){
						continue;
					}
					else{ // Value is lower. Swap and move up.
						int swp = sorted[l];
						sorted[l] = tmp;
						tmp = swp;
					}
					
				}
			}
			
			// Contiguity checks
			// messing with sorted here
			for( int k = 0; k < sorted.length-1; k++){
				int x1 = sorted[k], x2 = sorted[k+1];
				if( x1 == x2 && // Two elements in sequence
					x1 / 7 == x2 / 7){ // Check that they are in the same day. Integer division.
					// Award contiguity bonus
					contigFitness += 1;
				}
			}
			
		}
		// Check how many shifts have been filled
		for (int j=0; j<hitArray.length; j++){
			if( hitArray[j] == 0 ){
				X.valid = false;
				missPenalty += 1;
			}
		}
			
			
		X.rawFitness = 1*prefFitness+contigWeight*contigFitness+missWeight*missPenalty;
		if(X.valid) X.rawFitness += 10000;
		return;
		
		//System.out.println("Fitness "+ Double.toString(X.rawFitness));
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getIntGeneValue(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of Scheduling.java *************************************************

