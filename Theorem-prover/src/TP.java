import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class TP {
	static List<String> lhs = new ArrayList<String>();
	
	static List<parseTree> lhs_tree = new ArrayList<parseTree>();
	public static String stripBrackets(String line) {
		if (line.charAt(0) != '(')
			return line;
		if (line.charAt(line.length()-1) != ')')
			return line;
		int ob = 0, cb = 0;
		for(int i = 1; i < line.length()-1; i++) {
			if (line.charAt(i) == '(')
				ob++;
			else if (line.charAt(i) == ')')
				cb++;
			if (cb > ob)
				return line;
		}
		if (ob == cb) {
			return stripBrackets(line.substring(1, line.length()-1));
		}
		else
			return line;
	}
	
	static void moveLeft(String rhs) {
		rhs = stripBrackets(rhs);
		if (rhs.length() == 0) 
			return; 
		int ob = 0, cb = 0, imp = 0;
		for(int i = 0; i < rhs.length()-1; i++) {
			if (rhs.charAt(i) == '(') 
				ob++;
			else if (rhs.charAt(i) == ')')
				cb++;
			else if (rhs.charAt(i) == '-' && rhs.charAt(i+1) == '>') {
				imp++;
				if (ob == cb) {
					lhs.add(stripBrackets(rhs.substring(0, i)));
					parseTree t=new parseTree();
					parseTree.create_pt(t,stripBrackets(rhs.substring(0, i)));
					lhs_tree.add(t);
					moveLeft(rhs.substring(i+2, rhs.length()));
				}
			}
		}
		if (imp == 0) {
			if (rhs.charAt(0) == '~') {
				parseTree t=new parseTree();
				parseTree.create_pt(t,stripBrackets(rhs.substring(1)));
				lhs_tree.add(t);
				lhs.add(rhs.substring(1));
				lhs.add("F");
				return;
			}
			else {
				parseTree t=new parseTree();
				parseTree.create_pt(t,stripBrackets("~"+rhs.substring(0,1)));
				lhs.add("~"+rhs.substring(0,1));
				lhs_tree.add(t);
				lhs.add("F");
				return;
			}
		}
	}
	
	public static String reduceOR(String line) {
		String ans = line;
		int index, ob, cb, i;
		index = ans.indexOf('|');
		while(index != -1){
			ob = cb = 0;
			for(i = index+1; i < ans.length(); i++) {
				if (ans.charAt(i) == '(') 
					ob++;
				else if (ans.charAt(i) == ')') 
					cb++;
				if (ob == cb) 
					break;
			}
			String r1 = ans.substring(index+1, i+1), r2 = ans.substring(i+1);
			ob = cb = 0;
			for(i = index-1; i >= 0; i--) {
				if (ans.charAt(i) == '(') 
					ob++;
				else if (ans.charAt(i) == ')') 
					cb++;
				if (ob == cb) 
					break;
			}			
			
			String l1 = ans.substring(i, index), l2;
			if (i != 0)
				l2 = ans.substring(0, i-1);
			else 
				l2 = "";
			
			ans = l2 + "((" + l1 + "->F)->" + r1 + ")" + r2;
			index = ans.indexOf('|');
		} 
		return ans;
	}
	
	public static String reduceAND(String line) {
		String ans = line;
		int index, ob, cb, i;
		index = ans.indexOf('&');
		while(index != -1){			
			ob = cb = 0;
			for(i = index+1; i < ans.length(); i++) {
				if (ans.charAt(i) == '(') 
					ob++;
				else if (ans.charAt(i) == ')') 
					cb++;
				if (ob == cb) 
					break;
			}			
			String r1 = ans.substring(index+1, i+1), r2 = ans.substring(i+1);
			ob = cb = 0;
			for(i = index-1; i >= 0; i--) {
				if (ans.charAt(i) == '(') 
					ob++;
				else if (ans.charAt(i) == ')') 
					cb++;
				if (ob == cb) 
					break;
			}			
			String l1 = ans.substring(i, index), l2;
			if (i != 0)
				l2 = ans.substring(0, i-1);
			else 
				l2 = "";
			
			ans = l2 + "(" + l1 + "->(" + r1 + "->F))->F" + r2;
			index = ans.indexOf('&');
		} 
		return ans;
	}
	
	public static String reduceNOT(String line) {
		String ans = line;
		int index, ob, cb, i;
		index = ans.indexOf('~');
		while(index != -1){
			ob = cb = 0;
			for(i = index+1; i < ans.length(); i++) {
				if (ans.charAt(i) == '(') 
					ob++;
				else if (ans.charAt(i) == ')') 
					cb++;
				if (ob == cb) 
					break;
			}
			String r1 = ans.substring(index+1, i+1), r2 = ans.substring(i+1);
			String l1 = ans.substring(0, index);
			
			ans = l1 + "(" + r1 + "->F)" + r2;
			index = ans.indexOf('~');
		} 
		return ans;
	}
	
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
	    String line = input.nextLine();
//	    System.out.println(reduceOR(reduceAND(line)));
	    System.out.println(reduceNOT(line));
//	    moveLeft(line);
//	    for(int i = 0; i < lhs.size()-1; i++) {
//	    	System.out.println(lhs.get(i));
//	    }
//	    System.out.println("----");
//	    System.out.println(lhs.get(lhs.size()-1)+"End it");
//	    
//	    
//	    for(int i = 0; i < lhs_tree.size(); i++) {
//	    	parseTree.print_tree(lhs_tree.get(i));
//	    	System.out.println();
//	    }
	   // System.out.println("----");
	   // parseTree.print_tree(lhs_tree.get(lhs_tree.size()-1));
	}
}
//	(((p->(((a->r)->F)->(v->c)))->((d->F)->e)))&(f->(w&y))