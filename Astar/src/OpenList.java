import java.util.HashMap;
import java.util.PriorityQueue;


public class OpenList {
	HashMap<Long,Nodes> ol;
	PriorityQueue<Long> pq;
	OpenList() {
		ol=new HashMap<Long, Nodes>();
		pq=new PriorityQueue<Long>();
	}
	Nodes front()					///gets the Node with least f value also deletes corresponding values from pq and ol
	{
		if(ol.size()==0) return null;
		long temp = (long)(pq.peek() % 1000000000);
		Nodes ans = ol.get(temp);
//		ol.remove(ans.id);
		if (ans == null){
			System.out.println("pain: " + temp);
		}
		return ans;
	}
	void Delete(Integer id)			///removes from ol and pq
	{
		pq.remove((long) (ol.get((long)id).f*((long) 1000000000)+(long) id));
		ol.remove((long)id);
	}
	void DeleteOL(Integer id)			///removes from ol
	{
//		pq.remove((long) (ol.get((long)id).f*((long) 1000000000)+id));
		ol.remove((long)id);
	}
	
	void Insert(Nodes n)
	{
		ol.put((long) n.id, n);
		pq.add((long) ((long) n.f*((long) 1000000000)+(long)n.id));
	}
	Nodes Present(Integer id)    ///return null if not present else returns the node
	{
		return ol.get((long)id);
	}
}