import java.util.*;



public class Posting implements Comparable<Posting> {
	public static int flag;
	int postinglength;
	String term;
	public List<Node> postingList1 = new LinkedList<>();
	public List<Node> postingList2 = new LinkedList<>();
	
	public Posting(){
		term = Parser.arr1[0];								
		postinglength = Integer.parseInt(Parser.arr1[1]);	//storing length of postings list in Posting Object
		
		for (String s: Parser.postlist){			//for every doc in postinglist
			Node n = new Node(s);					// creating a Node
			postingList1.add(n);					// Adding into LinkedList1
			postingList2.add(n);					// Adding into LinkedList2
			
			
		}
		flag = 0;
		Collections.sort(postingList2, Collections.reverseOrder());		//sorting linkedlist 1 in increasing doc id
		flag = 1;
		Collections.sort(postingList1, Collections.reverseOrder());		//sorting linkedlist 2 in decreasing term frequency
	}

	@Override
	public int compareTo(Posting o) {
		int comparedsize = o.postinglength;
		if(this.postinglength>comparedsize){
			return -1;
		}
		else if(this.postinglength == comparedsize){
			return 0;
		}
		else
			return 1;
	}
}
