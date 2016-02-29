import java.util.List;

public class Printer {
	public Printer(String c, int k, List<String> resultset){
		CSE535Assignment.writer.println(c+k);
		String resultset2 = resultset.toString();
		resultset2 = resultset2.replace("[", "");
		resultset2 = resultset2.replace("]", "");
		CSE535Assignment.writer.println("Result: "+resultset2);
	}
	public Printer(String c, int a, int b, int d, List<Integer> resultset, long dtime){ //taator taatand
		//CSE535Assignment.writer.println("printer se bolra hu");
		CSE535Assignment.writer.println(c + " " + Parser.querytxt);
		CSE535Assignment.writer.print(a+" documents are found\n" + d +" comparisons are made\n");
		CSE535Assignment.writer.println(dtime/1000 + " seconds are used");
		CSE535Assignment.writer.println(b + " comparisons are made with optimization (optional bonus part)");	
		String resultset2 = cleaner(resultset);
		CSE535Assignment.writer.print("Result: "+resultset2 +"\n");
	}
	public Printer(String c, boolean termfound){
		CSE535Assignment.writer.println(c + " " + Parser.querytxt);
		CSE535Assignment.writer.println("terms not found");
	}
	public Printer(String c, int a, int b, List<Integer> resultset, long dtime){	//daator daatand
		CSE535Assignment.writer.println(c + " " + Parser.querytxt);
		CSE535Assignment.writer.print(a+" documents are found\n" + b +" comparisons are made\n");
		CSE535Assignment.writer.println(dtime/1000 + " seconds are used");
		String intset2 = cleaner(resultset);
		CSE535Assignment.writer.print("Result: "+intset2 +"\n");
	}
	public Printer(String c, String s, List<Integer> resultset1, List<Integer> resultset2){
		String intset2 = cleaner(resultset1);
		String intset3 = cleaner(resultset2);
		CSE535Assignment.writer.println(c + s);
		CSE535Assignment.writer.println("Ordered by doc IDs: " + intset2);
		CSE535Assignment.writer.println("Ordered by TF: " + intset3);
		
		
	}
	private String cleaner(List<Integer> resultset){
		String intset2 = resultset.toString();
		intset2 = intset2.replace("[", "");
		intset2 = intset2.replace("]", "");
		return intset2;
	}
	
}
