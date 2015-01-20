import java.util.HashMap;


public class ClosedList {
	HashMap<Long,Nodes> cl;
	ClosedList()
	{
		cl=new HashMap<Long, Nodes>();
	}
	void Delete(Integer id)
	{
		cl.remove((long)id);
	}
	Nodes Present(Integer id)    ///return null if not present
	{
		return cl.get((long)id);
	}
	void Insert(Nodes n)
	{
		cl.put((long)n.id,n);
	}
}