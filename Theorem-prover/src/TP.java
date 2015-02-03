import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;



public class TP {
	static List<String> lhs = new ArrayList<String>();
	static int lowval=0;
	static List<parseTree> lhs_tree = new ArrayList<parseTree>();
	static HashMap<String, parseTree> map = new HashMap<String,parseTree>();
	static HashMap<String, parseTree> map2 = new HashMap<String,parseTree>();
	static boolean found=false;
	
	
	static boolean mod_pon(parseTree T1, parseTree T2){
		return T1.compare(T2.ltree);
	}

	public static void Add_pt(parseTree ins)
	{
		String k="";
		k = parseTree.print_tree(ins);
		if(map.get(k)==null) 
		{
			System.out.println(k+" : inserted");
			map.put(k,ins);
			lhs_tree.add(ins);
		}
		if(ins.Nodeval.equals("F")) {found=true; System.out.println("Mil Gaya");}
	}
	public static void applymp()
	{
		for(int i=0;i<lhs_tree.size();i++)
		{
			for(int j=0;j<lhs_tree.size();j++)
			{
				if(mod_pon(lhs_tree.get(i),lhs_tree.get(j)))
				{
					parseTree ins=new parseTree(lhs_tree.get(j).rtree);
					String k=parseTree.print_tree(ins);
					map2.put(k,ins);
					k=parseTree.print_tree(lhs_tree.get(j));
					map2.put(k,lhs_tree.get(j));
				}
				if(mod_pon(lhs_tree.get(j),lhs_tree.get(i)))
				{
					parseTree ins=new parseTree(lhs_tree.get(i).rtree);
					String k=parseTree.print_tree(ins);
					map2.put(k,ins);
					k=parseTree.print_tree(lhs_tree.get(j));
					map2.put(k,lhs_tree.get(j));
					k=parseTree.print_tree(lhs_tree.get(i));
					map2.put(k,lhs_tree.get(i));
				}
			}
		}
		lowval=lhs_tree.size();
	}


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
					Add_pt(t);
					moveLeft(rhs.substring(i+2, rhs.length()));
				}
			}
		}
		if (imp == 0) {
			if (rhs.charAt(0) == '~') {
				parseTree t=new parseTree();
				parseTree.create_pt(t,stripBrackets(rhs.substring(1)));
				Add_pt(t);
				lhs.add(rhs.substring(1));
				lhs.add("F");
				return;
			}
			else {
				parseTree t=new parseTree();
				parseTree.create_pt(t,stripBrackets(reduceNOT("~"+rhs.substring(0,1))));
				lhs.add(reduceNOT("~"+rhs.substring(0,1)));
				Add_pt(t);
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
				l2 = ans.substring(0, i);
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
				l2 = ans.substring(0, i);
			else 
				l2 = "";
			
			
			ans = l2 + "((" + l1 + "->(" + r1 + "->F))->F)" + r2;
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
	
	public static void build_subtree(HashMap<String,parseTree> map)
	{
		for(int i=0;i<lhs_tree.size();i++)
		{
			parseTree.subtree(map,lhs_tree.get(i));
		}
	}
	
	public static void applyAxioms() {
		HashMap<String, parseTree> new_map = new HashMap<String,parseTree>();
		
		HashMap<String, parseTree> subtrees = new HashMap<String, parseTree>();
		build_subtree(subtrees);
		
		for (String key : subtrees.keySet()) 
	    {
	    	parseTree A = subtrees.get(key);
			parseTree T3 = parseTree.A3(A);
			new_map.put(parseTree.print_tree(T3), T3);
	    	for (String key2 : subtrees.keySet()) 
	    	{
	    		parseTree B = subtrees.get(key2);
				parseTree T1 = parseTree.A1(A, B);
				new_map.put(parseTree.print_tree(T1), T1);

	    		for (String key3 : subtrees.keySet()) 
	    		{
	    			parseTree C = subtrees.get(key3);
	    			parseTree T2 = parseTree.A2(A, B, C);
	    			new_map.put(parseTree.print_tree(T2), T2);
	    		}
	    	}
	    }
		
		for (String key : new_map.keySet()) {
			if (key == "F") {
				found = true;
			}
			Add_pt(new_map.get(key));
		}
	}
	
	
	
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		String line = input.nextLine();
		line = reduceAND(reduceOR(reduceNOT(line)));
		
		moveLeft(line);
		for(int i = 0; i < lhs.size()-1; i++) {
			System.out.println(lhs.get(i));
		}
		System.out.println("----");
		System.out.println(lhs.get(lhs.size()-1)+"End it");
		int i=0;
		while(!found) {
			map2.clear();
			for (String key : map.keySet()) {
				map2.put(key, map.get(key));
			}
			applyAxioms();
			applymp();
			map.clear();
			lhs_tree.clear();
			for(String key : map2.keySet())
			{
				Add_pt(map2.get(key));
			}
		}
		if (found) {
			System.out.println("Mil gaya!!");
		}
	}
	
}