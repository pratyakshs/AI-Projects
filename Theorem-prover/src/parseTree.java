import java.util.HashMap;


public class parseTree {
	parseTree ltree;
	parseTree rtree;
	String Nodeval;
	parseTree p1;
	parseTree p2;
	parseTree p3;
	int type;	//0 - mp , 1,2,3 for axioms 1,2,3. 4 - from the theorem.
	public void clean() {
		if (ltree != null) 
			ltree.clean();
		if (rtree != null)
			rtree.clean();
		ltree = null;
		rtree = null;
		Nodeval = null;
	}



	public parseTree()
	{
		Nodeval="";
		ltree=null;
		rtree=null;
	}
	public parseTree(parseTree T){
		if(T==null)
		{
			return;}
		Nodeval = T.Nodeval;
		if(T.ltree!=null)ltree = new parseTree(T.ltree);
		if(T.rtree!=null)rtree = new parseTree(T.rtree);
		p1 = T.p1; p2 = T.p2; p3 = T.p3;
		type = T.type;
	}
	public boolean compare(parseTree T){
		if(T==null){return false;}
		try
		{
			if(ltree==null && T.ltree!=null)return false;
			if(rtree==null && T.rtree!=null)return false;
			return Nodeval.equals(T.Nodeval) && ((ltree==null && T.ltree==null) || ltree.compare(T.ltree)) && ((rtree==null && T.rtree==null) || rtree.compare(T.rtree));
		}
		catch(Exception e) {
			return false;
		}

	}
	public static String print_tree(parseTree t)
	{
		String k="";
		k=k+"(";
		if(t.ltree!=null)
		{
			k=k+print_tree(t.ltree);
		}
		k=k+t.Nodeval;
		if(t.rtree!=null)
		{
			k=k+print_tree(t.rtree);
		}
		k=k+")";
		return k;
	}

	public static String print_steps(parseTree T){
		String s="";
		if(T.p1!=null){
			if(T.type == 0){
				s = print_steps(T.p1)+print_steps(T.p2);
				s += "Modus Ponens: "+print_tree(T.p1)+" "+print_tree(T.p2);}
			if(T.type==1){
				if(T.p1!=null)s = "Applying Axiom 1: "+print_tree(T.p1)+" "+print_tree(T.p2);
			}
			if(T.type==2){
				if(T.p1!=null)s = "Applying Axiom 2: "+print_tree(T.p1)+" "+print_tree(T.p2)+" "+print_tree(T.p3);
			}
			if(T.type==3){
				if(T.p1!=null)s = "Applying Axiom 3: "+print_tree(T.p1);
			}
		}
		if(T.type==4){
			s = "From Hypothesis.";
		}
		return s +"\n"+ print_tree(T)+"\n";
		
	}

	public static void create_pt(parseTree t, String inp)
	{
		inp = TP.stripBrackets(inp);
		int loc = -1, ob = 0, cb = 0;
		for(int i = 0; i < inp.length()-1; i++) {
			if (inp.charAt(i) == '(')
				ob++;
			else if (inp.charAt(i) == ')')
				cb++;
			if(inp.charAt(i) == '-' && inp.charAt(i+1)=='>' && ob == cb) {
				loc=i;
				break;
			}
		}
		if(loc==-1)
			t.Nodeval=inp;
		
		else {	
			t.Nodeval="->";
			if(loc > 0) {
				t.ltree = new parseTree();
				create_pt(t.ltree, inp.substring(0,loc));
			}
			if(loc < inp.length()-2) {
				t.rtree = new parseTree();
				create_pt(t.rtree, inp.substring(loc+2));
			}
		}
	}

	public static parseTree A1(parseTree T1, parseTree T2){
		// (T1->(T2->T1))
		parseTree T3 = new parseTree();
		T3.Nodeval = "->";
		T3.ltree = new parseTree(T1);
		T3.rtree = new parseTree();
		T3.rtree.Nodeval = "->";
		T3.rtree.ltree = T2;
		T3.rtree.rtree = T1;
		T3.p1 = T1;
		T3.p2 = T2;
		T3.type = 1;
		return T3;
	}

	public static parseTree A2(parseTree p, parseTree q, parseTree r) {
		//	(p->(q->r))->((p->q)->(p->r))
		parseTree T = new parseTree();
		T.Nodeval = "->";

		T.ltree = new parseTree();
		T.ltree.Nodeval = "->";
		T.ltree.ltree = new parseTree(p);
		T.ltree.rtree = new parseTree();
		T.ltree.rtree.Nodeval = "->";
		T.ltree.rtree.ltree = new parseTree(q);
		T.ltree.rtree.rtree = new parseTree(r);

		T.rtree = new parseTree();
		T.rtree.Nodeval = "->";
		T.rtree.ltree = new parseTree();
		T.rtree.ltree.Nodeval = "->";
		T.rtree.ltree.ltree = new parseTree(p);
		T.rtree.ltree.rtree = new parseTree(q);
		T.rtree.rtree = new parseTree(); 
		T.rtree.rtree.Nodeval = "->";
		T.rtree.rtree.ltree = new parseTree(p);
		T.rtree.rtree.rtree = new parseTree(r);
		T.p1 = p;
		T.p2 = q;
		T.p3 = r;
		T.type = 2;
		return T;
	}

	public parseTree neg(){
		parseTree T1 = new parseTree();
		T1.Nodeval = "->";
		T1.rtree = new parseTree();
		T1.rtree.Nodeval = "F";
		T1.ltree = new parseTree(this);
		return T1;
	}

	public static parseTree A3(parseTree T){
		//	((T->F)->F)->T
		parseTree T3 = new parseTree();
		T3.Nodeval = "->";
		parseTree T4 = T.neg();
		T3.ltree = T4.neg();
		T4.clean();
		T4 = null;
		T3.rtree = new parseTree(T);
		T3.p1 = T;
		T3.type = 3;
		return T3;
	}

	public static void subtree(HashMap<String,parseTree> map,parseTree t)
	{
		if(t==null) return;
	/*	if(t!=null)
		{*/
			String k="";
			k=parseTree.print_tree(t);
		/*	if(map.get(k)==null) 
			{
				map.put(k,t);
			}
		}
		if(t.ltree!=null)
		{
			String k="";
			k=parseTree.print_tree(t.ltree);
			if(map.get(k)==null) 
			{
				map.put(k,t.ltree);
			}
		}
		if(t.rtree!=null)
		{
			String k="";
			k=parseTree.print_tree(t.rtree);
			if(map.get(k)==null) 
			{
				map.put(k,t.rtree);
			}
		}*/		
		if(map.get(k)==null) 
		{
			map.put(k,t);
			subtree(map,t.ltree);
			subtree(map,t.rtree);
		}
	}
}

