import java.io.*;
import java.util.*;
import java.text.*;

public class InputGenerator{
	public static Random r = new Random();
	
	
	public static void main(String[] args) throws java.io.IOException{
		
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
					output.write(Integer.toString(randnum) + " ");
				}
				output.write('\n');
			}
			System.out.println(Integer.toString((int)(6.5)%2));
		}
		output.close();
	}
	
}