import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
	
	public static String [] arr1 = new String[4];
	public static List<String> queries = new ArrayList<>();
	public static String querytxt;
	public static List<String> postlist = new ArrayList<>();
	
	
	public Parser() throws FileNotFoundException, IOException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(CSE535Assignment.indexfile))) {	
			String line;
			
			while ((line = br.readLine()) != null) {			//Fetching index file line by line
				arr1 = splitter(line, Pattern.quote("\\"));		//splitting line into 3 parts. term\frequency\postingslist
																// arr1[0]-> term, arr1[1]-> length of postings, arr[2]-> postinglist
				
				arr1 = cleaner(arr1);							//removal of unwanted characters
				
				postlist = Arrays.asList(arr1[2].split(","));	// array of postings

				CSE535Assignment.map.put(arr1[0], new Posting());	//Inserting term (key) and object of type Posting (value) into hashmap
																	// Object of class Posting holds 2 linked lists and length of list.
				
			}
		}


	}
	public static void lineread(String line){
		queries.clear();
		querytxt = "";
		queries.addAll(Arrays.asList(line.split(" ")));		//queries stored in array
		querytxt = queries.toString();
		querytxt = querytxt.replace("]", "");
		querytxt = querytxt.replace("[", "");				//queries stored in string. This is just for outputs.
	}
	public String[] splitter(String line, String reg){		
		String[] splitstr = new String[3];
		
		
		splitstr = line.split(reg);
		return splitstr;
	}
	
	public String[] cleaner(String[] cleanstr){

		cleanstr[1] = cleanstr[1].substring(1);
		cleanstr[2] = cleanstr[2].replace("[", "");
		cleanstr[2] = cleanstr[2].replace("]", "");
		cleanstr[2] = cleanstr[2].replace("m", "");
		cleanstr[2] = cleanstr[2].replace(" ", "");
		return cleanstr;
	}
}
