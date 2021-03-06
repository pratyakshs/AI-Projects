import java.util.ArrayList;
import java.util.Scanner;


public class Astar {
	private static Scanner inp;
	
	public static void main(String[] Args){
		int[][] starting_board, final_board;
		starting_board = new int[3][3];
		final_board = new int[3][3];
		
		// take input
		inp = new Scanner(System.in);
		for(int i = 0; i < 9; i++) {
			starting_board[i/3][i%3] = inp.nextInt();
		}
		for(int i = 0; i < 9; i++) {
			final_board[i/3][i%3] = inp.nextInt();
		}
		int goal = Nodes.convert(final_board);
		
		OpenList ol = new OpenList();
		ClosedList cl = new ClosedList();
		
		Nodes frontNode = new Nodes(starting_board, goal);
		frontNode.f = frontNode.g = 0;
		System.out.println(frontNode.id);
		ol.Insert(frontNode);
		
		int expanded = 0, maxOpen = 0;
		int id = ol.front().id;
		if (!Nodes.reachable(id, 123456789))
			id = -1;
		int redirections=0; 
		
		while(id != goal && id != -1){
			
			Nodes nn=ol.Present(id);
			ArrayList<Integer> nxt = nn.Next();
			expanded++;
			for(int i = 0; i < nxt.size(); i++){
				int ng;
				ng=ol.Present(id).g+1;
				if(ol.Present(nxt.get(i)) != null) {
					if(ng < ol.Present(nxt.get(i)).g){
				
						Nodes n=ol.Present(nxt.get(i));
						ol.Delete(nxt.get(i));
						n.parent=ol.Present(id);
						n.g=ng;
						n.f=ng+n.h;
						
						ol.Insert(n);
					}
				}
				else if(cl.Present(nxt.get(i)) != null){

					if(ng < cl.Present(nxt.get(i)).g){
						redirections++;
						Nodes n=cl.Present(nxt.get(i));
						cl.Delete(nxt.get(i));
						n.parent=ol.Present(id);
						n.g=ng;
						n.f=ng+n.h;
						
						ol.Insert(n);
					}
				}
				else
				{
					Nodes n=new Nodes(nxt.get(i), goal);
					n.g=ng;
					n.parent=ol.Present(id);
					n.f=n.g+n.h;
					ol.Insert(n);
				}
			}
			cl.Insert(ol.Present(id));
			ol.Delete(id);
			if(ol.ol.size() == 0){id=-1; break;}
			id = ol.front().id;
			if (ol.ol.size() > maxOpen) 
				maxOpen = ol.ol.size();
			
		}
		if(id==-1) 
			System.out.println("Goal not reachable");
		else
			System.out.println("Cost of path found: " + ol.Present(id).g);
		System.out.println("Nodes expanded: " + cl.cl.size());
		System.out.println("Max size of OL: " + maxOpen);
		System.out.println("No of Redirections " + redirections);
		return;
	}
}
