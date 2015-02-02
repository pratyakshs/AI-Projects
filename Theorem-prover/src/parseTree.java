
public class parseTree {
	parseTree ltree;
	parseTree rtree;
	String Nodeval;
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
		ltree = new parseTree(T.ltree);
		rtree = new parseTree(T.rtree);
	}
	public static void print_tree(parseTree t)
	{
		System.out.print("(");
		if(t.ltree!=null)
		{
			print_tree(t.ltree);
		}
		System.out.print(t.Nodeval);
		if(t.rtree!=null)
		{
			print_tree(t.rtree);
		}
		System.out.print(")");
	}
	public static void create_pt(parseTree t, String inp)
	{
		inp=TP.stripBrackets(inp);
		int loc=-1;
		for(int i=0;i<inp.length()-1;i++)
		{
			if(inp.charAt(i) == '-' && inp.charAt(i+1)=='>')
			{
				loc=i;
				break;
			}
		}
		if(loc==-1)
		{
			t.Nodeval=inp;
		}
		else
		{	t.Nodeval="->";
		
			if(loc>0)
			{
				t.ltree=new parseTree();
				create_pt(t.ltree,inp.substring(0,loc));
			}
			if(loc<inp.length()-2)
			{
				t.rtree=new parseTree();
				create_pt(t.rtree,inp.substring(loc+2));
			}
		}
	}
}
