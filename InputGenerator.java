/**
 * InputGenerator.java
 * 
 * By: Austin Truong
 * 
 * Generates a test input for HW2
 * 
 * Does not check to make sure input case can have a valid solution. 
 */

import java.io.*;
import java.util.*;
import java.text.*;

public class InputGenerator{
	public static Random r = new Random();
	
	
	public static void main(String[] args) throws java.io.IOException{
		
		// Testing integer -> char conversion
		String str = "";
		for( int i = 0; i < 35; i++){
			str = str+(char)(i+'0');
		}
		System.out.println(str);
		for( int i = 0; i < 35; i++){
			System.out.print((int)str.charAt(i)-'0');
		}
		
		int randSeed = 74515; // Default random seed.
		String outFileName = "inputfile.txt";
		FileWriter output = new FileWriter(outFileName);
		
		r.setSeed(randSeed);
		
		int numPeople = 7;
		
		for(int i = 0; i < numPeople; i++){
			// Person ID string:
			output.write(Integer.toString(i)+'\n');
			
			// 5 rows
			for(int row = 0; row < 5; row++){
				// 7 columns
				for(int column = 0; column < 7; column ++){
					int randnum = r.nextInt(5);
					//int randnum = 1 + r.nextInt(4); // for trivially valid case
					output.write(Integer.toString(randnum) + " ");
				}
				output.write('\n');
			}
			System.out.println(Integer.toString((int)(6.5)%2));
		}
		output.close();
	}
	
}