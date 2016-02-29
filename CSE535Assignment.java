import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;


public class CSE535Assignment {
	public static int counter;
	public static int counter2;
	public static String indexfile;	//indexfile
	public static int k;
	public static String logfile;
	public static String queryfile;
	public static boolean found;
	public static PrintWriter writer;
	public static HashMap<String, Posting> map = new HashMap<>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		indexfile = args[0];  
		logfile = args[1];
		k = Integer.parseInt(args[2]);
		queryfile = args[3];
		writer = new PrintWriter(logfile, "UTF-8");
		
		new Parser(); 
		
		getTopK();												// run only once
		
		try (BufferedReader br = new BufferedReader(new FileReader(CSE535Assignment.queryfile))) {		//fetches input file
			String line;
			
			while ((line = br.readLine()) != null) {			// the following functions are run for each line in input file
				Parser.lineread(line);							// Parses all queries
				getPostings();
				termAtATimeQueryAnd();
				termAtATimeQueryOr();
				DocAtATimeAnd();
				DocAtATimeOr();
			}
		}writer.close();
	}
/**------------------------------------------------------------------------------------**/
static void getPostings(){
		
		for (String s: Parser.queries){
			List<Integer> resultset1 = new ArrayList<>();
			List<Integer> resultset2 = new ArrayList<>();
			if(map.containsKey(s)){
				for (Node n: map.get(s).postingList1){					//postingList1 has sorting by docIds
					resultset1.add(n.docID);							
				}
				
				for (Node n: map.get(s).postingList2){					//postingList2 has sorting by TF
					resultset2.add(n.docID);
				}
				new Printer("FUNCTION: getPostings ", s, resultset1, resultset2);		//Sent for printing to class Printer
			}else{
				writer.print("Query not found");
			}
		}
		
	}
	
/**------------------------------------------------------------------------------------**/	
static void getTopK () {
		List<String> resultlist = new ArrayList<String>();
		
		List<Map.Entry<String, Posting>> toplist = new LinkedList<Map.Entry<String, Posting>>(map.entrySet());
		Collections.sort(toplist, new comparingtk());
		toplist = toplist.subList(0, k);
		
		for (Map.Entry<String, Posting> p: toplist){
			resultlist.add(p.getKey());
		
		}
		new Printer("FUNCTION: getTopK ", k, resultlist);
		
}
	
/**------------------------------------------------------------------------------------**/	
static void termAtATimeQueryAnd() {
									//timer start
		boolean invalidquery=false;											//flag for queries not found
		
		List<Posting> querylist2 = new LinkedList<Posting>();
		for (String s: Parser.queries){										//created linked list of queries
			if(map.containsKey(s)){
				querylist2.add(map.get(s));
			}else{
				invalidquery = true;										//flag set if any of the query is not found
				new Printer("FUNCTION: termAtATimeQueryAnd", invalidquery);
				break;
			}
				
		}
		if (querylist2.isEmpty()||invalidquery){
			
		}else{																//runs only when all queries are found
			taatand(querylist2);											//taatand() holds the algorithm for TAATand. Here it is run for unsorted queries
			counter2 = counter;												//no of comparisons stored
			
			Collections.sort(querylist2, Collections.reverseOrder());		//sorted linked list of queries
			
			Long timeS = System.currentTimeMillis();						// timer start. Time taken is calculated only for optimized case
			
			List<Integer> intset = taatand(querylist2);						//algorithm run with sorted queries (Bonus part). Result is stored
			Collections.sort(intset);
			Long timeF = System.currentTimeMillis();						//timer finish
			Long dtime = (timeF - timeS);								//delta time in seconds
			new Printer("FUNCTION: termAtATimeQueryAnd", intset.size(), counter, counter2, intset, dtime);

		}
}	
/**------------------------------------------------------------------------------------**/	
public static List<Integer> taatand(List<Posting> querylist2){			//Algorithm for TAAT AND.
	counter = 0;											//counter holds no of comparisons
	found = false;
	

	List<Integer> tempresult = new ArrayList<>();
	for( Node n: querylist2.get(0).postingList2){
		tempresult.add(n.docID);									//postinglist of 1st query is added to temporary result
	}
	
	for (int i = 1; i<querylist2.size();i++){						//for all queries except 1st one
		List<Integer> deletions = new ArrayList<Integer>();					
		for(Integer doc:tempresult){								//for each docID in tempresult
			for(Node n: querylist2.get(i).postingList2){			//for each node in postinglist of selected query
				counter++;
				if(doc.equals(n.docID)){							//docID in tempresult is compared to docIDs in postinglist
					found=true;										//breaks if found. flag set
					break;
				}else{
					found=false;								 //flag remains false till docID is found
				}
			}
			if(found==false){													
				deletions.add(doc);								//if docID is never found, it is added to a list of deletions
			}
		}

		tempresult.removeAll(deletions);						//deletions are made in temporary result
	}
	return tempresult;
}
	

/**------------------------------------------------------------------------------------**/
public static void termAtATimeQueryOr(){							//TAAT OR
	
	
	List<Posting> querylist2 = new LinkedList<Posting>();
	for (String s: Parser.queries){									//created linked list of queries(querylist2)
		if(map.containsKey(s))
			querylist2.add(map.get(s));											
	}
	if(querylist2.isEmpty()){
		
	}else{
		taator(querylist2);											//algorithm for TAAT OR
		counter2 = counter;											//counter saved for unoptimized case
		
		Long timeS = System.currentTimeMillis();					//timer start
	
		Collections.sort(querylist2, Collections.reverseOrder());		//queries are sorted (optimized case)
		
		List<Integer> intset = taator(querylist2);						//algorithm is called for optimized case
		Collections.sort(intset);
		Long timeF = System.currentTimeMillis();						//timer finish
		Long dtime = (timeF - timeS);
		
		new Printer("FUNCTION: termAtATimeQueryOr", intset.size(), counter, counter2, intset, dtime);

	}
		
}
/**------------------------------------------------------------------------------------**/
public static List<Integer> taator(List<Posting> querylist2){				//algorithm for TAAT OR
	counter = 0;															//counter reset
	List<Integer> tempresult = new ArrayList<>();									
	for( Node n: querylist2.get(0).postingList2){
		tempresult.add(n.docID);											//docIDs in postinglist of 1st query added to temp result
	}
	for (int i = 1; i<querylist2.size();i++){								//for all other queries
		List<Integer> additions = new ArrayList<Integer>();
		for(Node n: querylist2.get(i).postingList2){						//for each node in the postinglist of selected query
			for(Integer doc:tempresult){									//for each docId in tempresult
				counter++;
				if(doc.equals(n.docID)){									//comparison
					found=true;												//breaks if found
					break;
				}else{
					found=false;											//flag set false docID if not found
				}
			}
			if(found==false){	
				additions.add(n.docID);										//if docID not found, it is stored in list of additions to be made
			}
		}
		tempresult.addAll(additions);										//additions are made
	}
	return tempresult;
	
}
/**------------------------------------------------------------------------------------**/
public static void DocAtATimeAnd(){														//DAAT AND
	Long timeS = System.currentTimeMillis();
	counter = 0;
//	boolean finish = false;
	boolean invalidquery = false;
	List<ListIterator<Node>> pointers = new LinkedList<ListIterator<Node>>();			//pointers = linked list for pointers
	List<Posting> querylist2 = new LinkedList<Posting>();								//querylist2 = linkedlist for Posting objects
	for (String s: Parser.queries){				//created linked list of queries
		if(map.containsKey(s)){
			querylist2.add(map.get(s));													// add Posting objects of queries in querylist2
		}else{
			invalidquery = true;
			new Printer("FUNCTION: docAtATimeQueryAnd", invalidquery);					//case when any of the queries is not found
			break;
		}
			
	}
	if (querylist2.isEmpty()||invalidquery){
		
	}else{																//Algorithm for DocAtATimeAnd executes if all queries are valid
		List<Integer> resultset = new ArrayList<>();
		for(Posting p:querylist2){
			ListIterator<Node> it = p.postingList1.listIterator();		
			pointers.add(it);											//Adds pointers for postinglists of each query
		}
		while(finished(pointers)){									//executes till any postinglist finishes. finished() is defined below
			
			
			if(equality(pointers)){									//if docIds are equal
				resultset.add(pointers.get(0).next().docID);		//add docID to result
				
				pointers.get(0).previous();
				for(ListIterator<Node> pr: pointers){				
					pr.next();										//all pointers next()
				}
			}else{													//if docIds are not equal
				Integer max = findmax(pointers);					//find maximum docID
				for(ListIterator<Node> pr: pointers){
					if(pr.hasNext()){								
						counter++;
						if(pr.next().docID.compareTo(max) < 0){ 	 //next the pointers which are less than max.	
						}
						else{
							pr.previous();							//.next() increments the pointer, whenever it is used
						}											//so .previous() is used to bring it back in the false case.
					}
				}
			}			
		}
		Long timeF = System.currentTimeMillis();
		Long dtime = (timeF - timeS);
	new Printer("FUNCTION: docAtATimeQueryAnd", resultset.size(), counter, resultset, dtime);
	} 
}
/**------------------------------------------------------------------------------------**/
static boolean finished(List<ListIterator<Node>> pointers){			//checks if any of the postinglist has finished.
	for(ListIterator<Node> pr: pointers){
		counter++;
		if(pr.hasNext() == false){
			return false;
		}
	}
	return true;
}
/**------------------------------------------------------------------------------------**/
static boolean equality(List<ListIterator<Node>> pointers2){		//checks if all pointers show equal docID
	
	Integer value = pointers2.get(0).next().docID;
	pointers2.get(0).previous();
	for(int i=1; i<pointers2.size();  i++){
		Integer value2 = pointers2.get(i).next().docID;
		pointers2.get(i).previous();
		counter++;
		if (!value.equals(value2)){
			return false;
		}
	}
	return true;
}
/**------------------------------------------------------------------------------------**/
static Integer findmax(List<ListIterator<Node>> pointers){			//finds maximum docID of current pointers
	Integer max = pointers.get(0).next().docID;
	pointers.get(0).previous();
	for(int i=0; i < pointers.size()-1; i++){
		counter++;
		Integer a,b;
		a = pointers.get(i).next().docID;
		pointers.get(i).previous();
		b = pointers.get(i+1).next().docID;
		pointers.get(i+1).previous();
		if(b.compareTo(a) > 0){
			max = b;
		}
	}		
	return max;
}
/**------------------------------------------------------------------------------------**/
static void DocAtATimeOr(){
	Long timeS = System.currentTimeMillis();
	counter=0;
	List<ListIterator<Node>> pointers = new LinkedList<ListIterator<Node>>();			//pointers = linked list for pointers
	List<Posting> querylist2 = new LinkedList<Posting>();								//querylist2 = linkedlist for Posting objects
	
	for (String s: Parser.queries){														//created linked list of queries
		if(map.containsKey(s)){
			querylist2.add(map.get(s));													// add Posting objects of queries in querylist2
		}		
	}
	if(querylist2.isEmpty()){
		
	}else{
		List<Integer> resultset = new ArrayList<>();
		for(Posting p:querylist2){											//for each query object
			ListIterator<Node> it = p.postingList1.listIterator();			//Create pointer for postinglist1. (Sorted by docID)
			pointers.add(it);												//Adds pointers for postinglists of each query to list of pointers
		}
		
		while(!pointers.isEmpty()){											//runs till pointers are empty
			Integer min = findmin(pointers);								//finds minimum of docIDs currently pointed by pointers
			resultset.add(min);												//adds minimum docID to result
			for(ListIterator<Node> pr: pointers){							//for each pointer in list of pointers
				if(!pr.hasNext()){								//if the selected pointer has reached the end of its linkedlist.
					pointers.remove(pr);						//it is removed from the list of pointers.
				}else{											
					counter++;
					if(pr.next().docID.equals(min)){				//if docID of selected pointer is equal to minimum. Increment it
						if(!pr.hasNext()){
							pointers.remove(pr);						//delete if it has now reached end	
						}
					}else{
						pr.previous();								//if docID is unequal(read greater) to minimum, it is not incremented
					}
				}
			}
		}
		Long timeF = System.currentTimeMillis();				
		Long dtime = (timeF - timeS);
		new Printer("FUNCTION: docAtATimeQueryOr", resultset.size(), counter, resultset, dtime);
	}
	
}
static Integer findmin(List<ListIterator<Node>> pointers){					//finds minimum of docIds currently pointed at
	Integer min = pointers.get(0).next().docID;
	pointers.get(0).previous();
	for(ListIterator<Node> pr: pointers){
		Integer q = pr.next().docID;
		pr.previous();
		if(q.compareTo(min) < 0){
			min = q;
		}
	}
	return min;
}
}

/**------------------------------------------------------------------------------------**/
/**------------------------------------------------------------------------------------**/


class comparingtk implements Comparator<Map.Entry<String, Posting>>{

	@Override
	public int compare(Entry<String, Posting> o1, Entry<String, Posting> o2) {
		return o2.getValue().postinglength - o1.getValue().postinglength;
	}
}
