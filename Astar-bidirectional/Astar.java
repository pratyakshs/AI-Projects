import java.util.ArrayList;
import java.util.Scanner;


public class Astar {
	private static Scanner inp;

	public static int max(int a,int b)
	{
		if(a>b) return a;
		else return b;
	}

	public static void main(String[] Args){
		
		int[][] starting_board, final_board;

		starting_board = new int[3][3];
		final_board = new int[3][3];

		// take input
		inp = new Scanner(System.in);
		for(int i = 0; i < 9; i++) {
			starting_board[i / 3][i % 3] = inp.nextInt();
		}
		for(int i = 0; i < 9; i++) {
			final_board[i / 3][i % 3] = inp.nextInt();
		}

		int goal = Nodes.convert(final_board);
		int goal2 = Nodes.convert(starting_board);
		
		OpenList ol = new OpenList();
		ClosedList cl = new ClosedList();

		OpenList ol2 = new OpenList();
		ClosedList cl2 = new ClosedList();

		Nodes frontNode = new Nodes(starting_board, goal);
		frontNode.f = frontNode.g = 0;

		System.out.println(frontNode.id);

		ol.Insert(frontNode);

		Nodes frontNode2 = new Nodes(final_board, goal2);
		frontNode2.f = frontNode2.g = 0;

		ol2.Insert(frontNode2);

		int expanded = 0, maxOpen = 0, maxOpen2 = 0;

		int id2 = ol2.front().id;
		int id = ol.front().id;

		if (!Nodes.reachable(id, goal))
			id = -1;

		int redirections = 0; 
		boolean change = true;
		int ans = 0;

		while(id != -1 )
		{
			if(change){

				Nodes nn = ol.Present(id);
				Nodes x = cl2.Present(id);
				if(x != null)
				{
					ans = nn.g + x.g;
					break;
				}
				ArrayList<Integer> nxt = nn.Next();
				expanded++;
				for(int i = 0; i < nxt.size(); i++) {
					int ng;
					ng = ol.Present(id).g+1;
					if(ol.Present(nxt.get(i)) != null) {
						if(ng < ol.Present(nxt.get(i)).g){

							Nodes n = ol.Present(nxt.get(i));
							ol.Delete(nxt.get(i));
							n.parent = ol.Present(id);
							n.g = ng;
							n.f = ng + n.h;
							ol.Insert(n);
						}
					}
					else if(cl.Present(nxt.get(i)) != null){

						if(ng < cl.Present(nxt.get(i)).g){
							redirections++;
							Nodes n=cl.Present(nxt.get(i));
							cl.Delete(nxt.get(i));
							n.parent=ol.Present(id);
							n.g = ng;
							n.f = ng + n.h;

							ol.Insert(n);
						}
					}
					else
					{
						Nodes n = new Nodes(nxt.get(i), goal);
						n.g = ng;
						n.parent = ol.Present(id);
						n.f = n.g + n.h;
						ol.Insert(n);
					}
				}
				cl.Insert(ol.Present(id));
				ol.Delete(id);
				if(ol.ol.size() == 0){id =- 1; break;}
				id = ol.front().id;
				if (ol.ol.size() > maxOpen) 
					maxOpen = ol.ol.size();
			}

			else
			{			
				Nodes nn = ol2.Present(id2);
				Nodes x = cl.Present(id2);
				if(x != null)
				{
					ans = nn.g + x.g;
					break;
				}

				ArrayList<Integer> nxt = nn.Next();
				expanded++;
				for(int i = 0; i < nxt.size(); i++){
					int ng;
					ng = ol2.Present(id2).g+1;
					if(ol2.Present(nxt.get(i)) != null) {
						if(ng < ol2.Present(nxt.get(i)).g) {

							Nodes n = ol2.Present(nxt.get(i));
							ol2.Delete(nxt.get(i));
							n.parent = ol2.Present(id2);
							n.g = ng;
							n.f = ng + n.h;

							ol2.Insert(n);
						}
					}
					else if(cl2.Present(nxt.get(i)) != null){

						if(ng < cl2.Present(nxt.get(i)).g){
							System.out.println("Chudh gaye");
							redirections++;
							Nodes n = cl2.Present(nxt.get(i));
							cl2.Delete(nxt.get(i));
							n.parent = ol2.Present(id2);
							n.g = ng;
							n.f = ng + n.h;

							ol2.Insert(n);
						}
					}
					else
					{
						Nodes n = new Nodes(nxt.get(i), goal2);
						n.g = ng;
						n.parent = ol2.Present(id2);
						n.f = n.g + n.h;
						ol2.Insert(n);
					}
				}
				cl2.Insert(ol2.Present(id2));
				ol2.Delete(id2);
				if(ol2.ol.size() == 0){id2 = -1; break;}
				id2 = ol2.front().id;
				if (ol2.ol.size() > maxOpen2) 
					maxOpen2 = ol2.ol.size();

			}
			change =! change;
		}
		if(id==-1 || id2 == -1) 
			System.out.println("Goal not reachable");
		else
			System.out.println("Cost of path found: " + ans);

		System.out.println("Nodes expanded: " + (cl.cl.size()+cl2.cl.size()));
		System.out.println("Max size of OL: " + max(maxOpen,maxOpen2));
		System.out.println("No of Redirections " + redirections);
		return;
	}
}