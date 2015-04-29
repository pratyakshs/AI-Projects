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
	static List<parseTree> lhs_tree_orig = new ArrayList<parseTree>();
	static HashMap<String, parseTree> map_orig = new HashMap<String,parseTree>();


	static boolean mod_pon(parseTree T1, parseTree T2){
		boolean b = T1.compare(T2.ltree);
		if (b == true) {
			//			System.out.println(parseTree.print_tree(T1));
			//			System.out.println(parseTree.print_tree(T2));
		}
		return b;
	}

	public static void Add_pt(parseTree ins) {
		String k="";
		k = parseTree.print_tree(ins);
	//	System.out.println(k +" : inserted");
		 
		if(map.get(k)==null) 
		{
			//			System.out.println(k +" : inserted");
			map.put(k,ins);
			lhs_tree.add(ins);
		}
		if(ins.Nodeval.equals("F")) {found=true; System.out.println(parseTree.print_steps(ins));System.out.println("QED");}
	}
	public static void applymp()
	{
		List<Integer> toRemove = new ArrayList<Integer>();
		int count = 0, size_i = lhs_tree.size();
		for(int i=0;i<size_i;i++)
		{
			count = 0;
			for(int j=0;j<size_i;j++)
			{
		//		if((i==1 && j==10))
		//		{
		//			System.out.println(mod_pon(lhs_tree.get(i),lhs_tree.get(j))+"   "+mod_pon(lhs_tree.get(j),lhs_tree.get(i)));
		//			System.out.println(parseTree.print_tree(lhs_tree.get(i))+" "+parseTree.print_tree(lhs_tree.get(j).rtree));
		//		}	
				if(mod_pon(lhs_tree.get(i),lhs_tree.get(j)))
				{
				//	if(map_orig.get(parseTree.print_tree(lhs_tree.get(j)))!=null || map_orig.get(parseTree.print_tree(lhs_tree.get(i)))!=null) {
						count++;
						parseTree ins=new parseTree(lhs_tree.get(j).rtree);
						ins.type=0; ins.p1 = lhs_tree.get(i); ins.p2 = lhs_tree.get(j);
						Add_pt(ins);///
				//	}
				}
				if(mod_pon(lhs_tree.get(j),lhs_tree.get(i)))
				{
				//	if(map_orig.get(parseTree.print_tree(lhs_tree.get(j)))!=null || map_orig.get(parseTree.print_tree(lhs_tree.get(i)))!=null) {
						count++;
						parseTree ins=new parseTree(lhs_tree.get(i).rtree);
						ins.type=0; ins.p1 = lhs_tree.get(j); ins.p2 = lhs_tree.get(i);
						Add_pt(ins);///
				//	}
				}
			}
			if(count == 0)
				toRemove.add(i);
		}
		// for(int i = 0; i < toRemove.size(); i++){
		// 	parseTree t = lhs_tree.get(toRemove.get(i));
		// 	lhs_tree.remove(toRemove.get(i));
		// 	map.remove(parseTree.print_tree(t));
		// }
		//System.out.println("Removed "+toRemove.size()+" after MP.");
		lowval=lhs_tree.size();
	}


	static boolean mod_tol(parseTree T1, parseTree T2)
	{
		// T1~q
		// T2p->q
		if(T1.ltree==null) return false;
		boolean b=T1.ltree.compare(T2.rtree);
		boolean k=false;
		if(T1.rtree.Nodeval.equals("F") && T1.rtree.rtree==null && T1.rtree.ltree==null) k=true;
		return b&k;
	}

	public static void applymt()
	{
		int size_i = lhs_tree.size();
		for(int i=0;i<size_i;i++)
		{
			for(int j=0;j<size_i;j++)
			{
				if(mod_tol(lhs_tree.get(i),lhs_tree.get(j)))
				{
					parseTree ins = new parseTree(lhs_tree.get(j).rtree);
					Add_pt(ins);///
				}
				if(mod_tol(lhs_tree.get(j),lhs_tree.get(i)))
				{
					parseTree ins=new parseTree(lhs_tree.get(i).rtree);
					Add_pt(ins);///
				}
			}
		}
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
		System.out.println(rhs);
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
					System.out.println(parseTree.print_tree(t));
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
				lhs.add(stripBrackets(reduceNOT("~"+rhs.substring(0,1))));
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
			if (parseTree.print_tree(A).length() > 100)
				continue;
			parseTree T3 = parseTree.A3(A);
			new_map.put(parseTree.print_tree(T3), T3);

			//	System.out.println("A3 on " + parseTree.print_tree(A) + " gives " + parseTree.print_tree(T3));
			for (String key2 : subtrees.keySet()) 
			{
				parseTree B = subtrees.get(key2);
				if (parseTree.print_tree(B).length() > 100 )//|| A.compare(B))
					continue;
				parseTree T1 = parseTree.A1(A, B);
				new_map.put(parseTree.print_tree(T1), T1);

				//	System.out.println("A1 on " + parseTree.print_tree(A) + " and " + parseTree.print_tree(B) + " gives " + parseTree.print_tree(T1));
				for (String key3 : subtrees.keySet()) 
				{
					parseTree C = subtrees.get(key3);
					if (parseTree.print_tree(C).length() > 100 )//|| A.compare(C) || B.compare(C))
						continue;
					parseTree T2 = parseTree.A2(A, B, C);
					new_map.put(parseTree.print_tree(T2), T2);
					//	System.out.println("A2 on " + parseTree.print_tree(A) + " and " + parseTree.print_tree(B) + " and " + parseTree.print_tree(C) + " gives " + parseTree.print_tree(T2));
				}
			}
		}


		for (String key : new_map.keySet()) {
			if (stripBrackets(key).equals("F")) {
				found = true;
			}
			Add_pt(new_map.get(key));
		}
	}



	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		String line = input.nextLine();

		line = reduceAND(reduceOR(reduceNOT(line)));
		System.out.println("line = " + line);
		moveLeft(line);
		System.out.println("Initial set of hypothesis: ");
		for(int i = 0; i < lhs.size()-1; i++) {
			System.out.println(lhs.get(i));
		}
		System.out.println("----");
		System.out.println(lhs.get(lhs.size()-1));

		// store the initial propositions
		lhs_tree_orig = new ArrayList<parseTree>(lhs_tree);
		map_orig = new HashMap<String, parseTree>(map);
	//found=true;
			applyAxioms();
			
		int i = 0, lim = 1;
		while (!found) {
			i = 0;
			while(!found) {
				
//				System.out.println(lhs_tree.size());
				int before=lhs_tree.size();
				applymp();
				if(before==lhs_tree.size()) break;

//				applymt();
//				System.out.println(lhs_tree.size());
			}
	/*		for( i = 0; i < lhs_tree.size()-1; i++) {
			System.out.println("tr"+parseTree.print_tree(lhs_tree.get(i)));}
			applymp();

			for( i = 0; i < lhs_tree.size()-1; i++) {
			System.out.println("tr2"+parseTree.print_tree(lhs_tree.get(i)));
		}*/
		System.out.println(lhs_tree.size()+"   "+map.size());
			if (!found) {
				System.out.println("It seems that the ATP is stuck and need some human intervention. \nPress 1 to Apply Axiom 1, 2 to apply Axiom 2, 3 to apply axiom 3 and 4 to use a new theorem");
				String ans=input.nextLine();
				parseTree ins=new parseTree();
				if(ans.equals("1"))
				{
					System.out.println("Axiom 1 is (A->(B->A))");
					System.out.println("Input A: ");
					String a=input.nextLine();
					System.out.println("Input B: ");
					String b=input.nextLine();
					a = reduceAND(reduceOR(reduceNOT(a)));
					b = reduceAND(reduceOR(reduceNOT(b)));
					parseTree A=new parseTree();
					parseTree.create_pt(A,a);
					parseTree B=new parseTree();
					parseTree.create_pt(B,b);

					ins=parseTree.A1(A,B);
				}
				else if(ans.equals("2"))
				{
					System.out.println("Axiom 2 is (A->(B->C))->((A->B)->(A->C))");
					System.out.println("Input A: ");
					String a=input.nextLine();
					System.out.println("Input B: ");
					String b=input.nextLine();
					System.out.println("Input C: ");
					String c=input.nextLine();

					a = reduceAND(reduceOR(reduceNOT(a)));
					b = reduceAND(reduceOR(reduceNOT(b)));
					c = reduceAND(reduceOR(reduceNOT(c)));
					parseTree A=new parseTree();
					parseTree.create_pt(A,a);
					parseTree B=new parseTree();
					parseTree.create_pt(B,b);
					parseTree C=new parseTree();
					parseTree.create_pt(C,c);
					ins=parseTree.A2(A,B,C);
				}
				else if(ans.equals("3"))
				{
					System.out.println("Axiom 3 is ((A->F)->F)->A");
					System.out.println("Input A: ");
					String a=input.nextLine();
					parseTree A=new parseTree();
					parseTree.create_pt(A,a);
					ins=parseTree.A3(A);
				}
				else if(ans.equals("4"))
				{
					String a=input.nextLine();
					parseTree.create_pt(ins,a);
				}

				String k = parseTree.print_tree(ins);
				if(map.get(k)==null) 
				{
					//			System.out.println(k +" : inserted");
					map.put(k,ins);
					lhs_tree.add(ins);
				}
				//lhs_tree = new ArrayList<parseTree>(lhs_tree_orig);
				//map = new HashMap<String, parseTree>(map_orig);
			}
		}
	}	
}
/* Demorgan's laws
 * ((~p)|(~q))->(~(p&q))
 */


