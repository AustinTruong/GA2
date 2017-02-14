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
	public static int[][] preferences;

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
			scanner.nextInt();
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

		double difference = 0;
		for (int j=0; j<Parameters.numGenes; j++){
			difference = (double) Math.abs(X.getIntGeneValue(j) - testValue[j]);
			X.rawFitness = X.rawFitness + difference;
		}
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

