import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BibCreator {
	
	public static int validCounter=0; //static so that the entire class can call it
	public static int invalidCounter=0; //static so that the entire class can call it

	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		Scanner sc = null;
		PrintWriter ieee = null; //for the PrintWriter to write the files
		PrintWriter acm = null;
		PrintWriter nj = null;
		int numAttempt=0;
		BufferedReader br = null; //for the BufferedReader to read the files created
		String readFile =""; //name of the file being read by BufferedReader
		Scanner search = new Scanner(System.in);


		
		

		System.out.println("Welcome to BibCreator!\n");
		
		for (int i = 1; i <= 10; i++) 
		{
			try
			{
				sc = new Scanner(new FileInputStream("Latex" + i + ".bib"));
				sc.close();
			}
			catch (FileNotFoundException e) //if file does not exist
			{
				sc.close(); //closes all opened files
				System.out.println("Could not open input file Latex" + i + ".bib for reading.\n\nPlease check if file exists! Program will terminate after closing any opened files.");
				System.exit(0);
			}
		}
		
		
		
		for (int i = 1; i <= 10; i++)
		{
			int flag = 1;
			try
			{
//				if (i == 5) //for testing
//					throw new FileNotFoundException();
				ieee = new PrintWriter(new FileOutputStream("IEEE" + i + ".json"));
				ieee.close(); //will get to this line if file creation was valid
				
				flag = 2;
				acm = new PrintWriter(new FileOutputStream("ACM" + i + ".json"));
				acm.close(); //will get to this line if file creation was valid
				
				flag = 3;
				nj = new PrintWriter(new FileOutputStream("NJ" + i + ".json"));
				nj.close(); //will get to this line if file creation was valid
			}
			catch (FileNotFoundException e)
			{
				if (flag == 1)
				{
					System.out.println("Could not create IEEE" + i + ".json for Latex" + i + ".bib.\n\nClearing directory of all other created output files.");
					ieee.close(); //must create another closes since it skips when the exception is thrown
				}
				if (flag == 2)
				{
					System.out.println("Could not create ACM" + i + ".json for Latex" + i + ".bib.\n\nClearing directory of all other created output files.");
					acm.close(); //must create another closes since it skips when the exception is thrown
				}
				if (flag == 3)
				{
					System.out.println("Could not create NJ" + i + ".json for Latex" + i + ".bib.\n\nClearing directory of all other created output files.");
					nj.close(); //must create another closes since it skips when the exception is thrown
				}		
				for (int j = 1; j <= 10; j++)
				{
					deleteFile(j);
				}
				System.exit(0);
			}

			

		}
//		System.exit(0);	
		for (int i = 1; i <= 10; i++) {
			try {
				sc = new Scanner(new FileInputStream("Latex" + i + ".bib"));
				ieee = new PrintWriter(new FileOutputStream("IEEE" + i + ".json"));
				acm = new PrintWriter(new FileOutputStream("ACM" + i + ".json"));
				nj = new PrintWriter(new FileOutputStream("NJ" + i + ".json"));
				try {
					processFilesForValidation(i,sc, ieee, acm, nj);
				} catch (FileInvalidException e) {
					// TODO Auto-generated catch block
					ieee.close();
					acm.close();
					nj.close();
					deleteFile(i);

				} 

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				sc.close(); //closes all opened files
				System.out.println("Could not open input file Latex" + i + ".bib for reading.\n\nPlease check if file exists! Program will terminate after closing any opened files.");
				System.exit(0);
			}

		
			
		}
		System.out.println("\n\nA total of " + invalidCounter + " files were invalid, and could not be processed. All other " + validCounter + " \"Valid\" files have been created.\n");

		
		while (numAttempt < 2) {//User will have 2 attempts maximum to enter the correct name of the .json file that they want to read the content
			System.out.println("Please enter the name of one of the files that you need to review:");
			readFile = search.next();
			
			try {
				br = new BufferedReader(new FileReader(readFile));
				System.out.println("Here are the contents of the successfully created Jason File: " + readFile);
				numAttempt =2; //if they successfully entered a correct file name, then the numAttempt=2 so that they exit of the while loop
			}
			catch (FileNotFoundException e) {
				if (numAttempt == 1) { //if they used more than 2 attempts
					System.out.println("Could not open input file again! Either file does not exist or could not be created.");
					System.out.println("Sorry! I am unable to display your desired files! Program will exit!");
					System.exit(0);
				}
				System.out.println("Could not open input file. File does not exist; possibly it could not be created!");
				System.out.println("\nHowever, you will be allowed another chance to enter another file name.");
				numAttempt++;
				
				
			}
		}//end of while
		try {
			displayFileContents(br);
			System.out.println("\n\nGoodbye! Hope you have enjoyed creating the needed files using BibCreator.");
		}
		catch(IOException e) {
			System.out.println("Error occured while reading the file: " + readFile);
			System.out.println("Program will terminate");
			System.exit(0);
		}
	}
		
	private static void displayFileContents(BufferedReader br) throws IOException {
		// TODO Auto-generated method stub
		int c;
		c = br.read();
		while (c != -1) {
			System.out.print((char)c);
			c = br.read();
		}
		
		br.close();
	}

	public static void deleteFile(int i)
	{
		File file = null;
		file = new File("IEEE"+ i +".json");
		file.delete();
		file = new File("ACM" + i + ".json");
		file.delete();
		file = new File("NJ" + i + ".json");
		file.delete();
	}

		
	
	private static void processFilesForValidation(int i, Scanner sc, PrintWriter ieee, PrintWriter acm, PrintWriter nj) throws FileInvalidException{
			// TODO Auto-generated method stub
		String s="";
		int numArticle =0;
		
		StringTokenizer stkn = null; 
		String[] token;
		
		String IEEE=""; //the final variable that will be output into the ieee.json file
		String ACM=""; //the final variable that will be output into the acm.json file
		String NJ=""; //the final variable that will be output into the nj.json file
		
		String storeAuthor =""; 
		String IEEEauthor="";
		String ACMauthor="";
		String NJauthor="";
		
		String storeTitle="";		
	
		String storeJournal="";
	
		String storeVolume ="";
		
		String storeNumber="";
				
		String storePage="";
	
		
		String storeMonth="";
		
		String storeYear="";
	
		String DOI ="";
		
		while (sc.hasNextLine()) { //read through the file until EOF
			s= sc.nextLine();
			
			if (s.contains("={}")){ //if there is an empty field which is {}
				invalidCounter++; //increment the invalidCounter
				System.out.println("Error : Detected Emply Field!");
				System.out.println("=============================");
				System.out.println("Problem detected with input file :"+"Latex" + i + ".bib");
				System.out.println("File Invalid. Field \"" + s.substring(0, s.indexOf("=")) + "\" is Empty. Processing stopped at this point. Other empty fields may be present as well!\n");
				throw new FileInvalidException();
			}
			
			else {
			if (s.contains("@ARTICLE{")) { //when there is this article string, it counts the amount of article
				numArticle++;
			}
			
			if (s.contains("author={")) { //when it reaches the field author, it copies the content of it and assign the its respected variables
				storeAuthor = s.substring(s.indexOf("{")+1, s.indexOf("}"));	
	
				IEEEauthor = storeAuthor.replaceAll(" and", ",");
			
				NJauthor = storeAuthor.replaceAll("and", "&");
				
				stkn = new StringTokenizer(storeAuthor, " "); //Tokenize the lengthy authors
				token = new String[stkn.countTokens()];
				for(int j=0; j<stkn.countTokens(); j++){
					token[j] = stkn.nextToken(); //Assign the separated words into token[]
				}
				
				ACMauthor = token[0] + " " + token[1] + " et al";		
			}
			
			if (s.contains("title={")) { //when it reaches the field title, it copies the content of it and assign the its respected variables
				storeTitle = s.substring(s.indexOf("{")+1, s.indexOf("}"));	
			}
			
			if (s.contains("journal={")) { //when it reaches the field journal, it copies the content of it and assign the its respected variables
				storeJournal = s.substring(s.indexOf("{")+1, s.indexOf("}")); 
			}
			
			if (s.contains("volume={")){ //when it reaches the field volume, it copies the content of it and assign the its respected variables
				storeVolume = s.substring(s.indexOf("{")+1, s.indexOf("}")); 
			
			}
			
			if (s.contains("number={")) { //when it reaches the field number, it copies the content of it and assign the its respected variables
				storeNumber = s.substring(s.indexOf("{")+1, s.indexOf("}")); 
			}
			
			if (s.contains("pages={")) { //when it reaches the field pages, it copies the content of it and assign the its respected variables
				storePage = s.substring(s.indexOf("{")+1, s.indexOf("}")); 
			}
			
			if (s.contains("month={")){ //when it reaches the field month, it copies the content of it and assign the its respected variables
				storeMonth = s.substring(s.indexOf("{")+1, s.indexOf("}")); 
			}
			
			if (s.contains("year={")){ //when it reaches the field year, it copies the content of it and assign the its respected variables
				storeYear = s.substring(s.indexOf("{")+1, s.indexOf("}")); 
			}			
			
			if (s.contains("doi={")){ //when it reaches the field doi, it copies the content of it and assign the its respected variables
				DOI = "DOI:https://doi.org/" + s.substring(s.indexOf("{")+1, s.indexOf("}"));
			}
			
			
			if (s.equals("}")){ //once it reaches the end of the article, it combines each field into one single output so that it can be written properly into the respective file and format
				IEEE = IEEEauthor +". " + "\""+storeTitle+"\"" +", " + storeJournal +", " + "vol. " + storeVolume +", " + "no. " +storeNumber +", " + "p. " + storePage +", " + storeMonth + " " + storeYear +".";
				ieee.println(IEEE);
				ieee.println();
				
				ACM = "["+numArticle+"] "+ ACMauthor + ". " + storeYear +". " + storeTitle + ". " + storeJournal + ". " + storeVolume + ", " + storeNumber + " (" + storeYear + "), " + storePage +". " + DOI +".";
				acm.println(ACM);
				acm.println();
				
				NJ = NJauthor + ". " + storeTitle + ". " + storeJournal + ". " + storeVolume + ", " + storePage + " (" + storeYear + ").";
				nj.println(NJ);
				nj.println();
			}
			
			}//end of else
			
			if (sc.hasNextLine() == false) {
				validCounter++; //when it reaches the EOF, increment the validCounter
			}
			
		}//end of while
		//close the opened files so that it will be perfectly fine
		ieee.close();
		acm.close();
		nj.close();
		sc.close();
		
	}





	


	
}
