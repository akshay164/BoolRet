
public class Node implements Comparable<Node> {				//Each Node in LinkedLists holds docID and frequency.
	Integer docID;
	Integer freq;
	
	public Node (String s) {
		String [] arr = new String[2];
		arr = (s.split("/"));						//splitting docId and frequency  eg: 100/5
		
		docID = Integer.parseInt(arr[0]);			//adding docID to node				100
		
		freq = Integer.parseInt(arr[1]);			//adding frequency to node			5
		
		
		
	}
	public int compareTo(Node o) {						//used for sorting of linkedlists
		if(Posting.flag == 0){
			int comparedSize = o.freq;
			if(this.freq > comparedSize){
				return 1;
			}else if (this.freq == comparedSize){
				return 0;
			}else{
				return -1;
			}
		}
		else{
			int comparedSize = o.docID;
			if(this.docID > comparedSize){
				return -1;
			}else if (this.docID == comparedSize){
				return 0;
			}else{
				return 1;
			}
		}
	}
	
	
}
