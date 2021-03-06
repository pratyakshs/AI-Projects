import java.util.ArrayList;


public class Nodes {
	int[][] v;
	int id;
	int x0, y0;
	int f, g, h;
	Nodes parent;
	Nodes() {
		v = new int[3][3];
		parent = null;
	}
	
	public static int parity(int x){
		int[] a = new int[9];
		for(int i=8;i>=0;i--){a[i]=x%10-1;x/=10;}
		
		boolean[] chk = new boolean[9];
		int par = 0;
		for(int i=0;i<9;i++)chk[i]=false;
		for(int i=0;i<9;i++){
			if(!chk[i]){
				int n=i;
				while(a[n]!=i){chk[n]=true;par=1-par;n=a[n];}
				chk[n]=true;
			}
		}
		return par;
	}
	public static boolean reachable(int a,int b){
		int p = parity(a)+parity(b);
		for(int i=0;i<9;i++){
			if(a%10==9)p+=(i/3+i%3)%2;
			if(b%10==9)p+=(i/3+i%3)%2;
			a/=10;
			b/=10;
		}
		return (p)%2==0;
	}
	
	public static int abs(int x){return x>0?x:-x;}
	
	public static  int manhattan(int a,int b){
		int dist = 0;
//		long ten = 10;
		int[] pxa = new int[9];
		int[] pxb = new int[9];
		int[] pya = new int[9];
		int[] pyb = new int[9];
		for(int i=8;i>-1;i--){
			int va = a%10-1,vb=b%10-1;
			a/=10;b/=10;
			pxa[va] = i/3;pxb[vb]=i/3;
			pya[va] = i%3;pyb[vb]=i%3;
		}
		for(int i=0;i<8;i++){
			dist+=abs(pxa[i]-pxb[i])+abs(pya[i]-pyb[i]);
		}
		return dist;
	}
	public int displacement(int a,int b){
		int dist = 0;
		for(int i=8;i>-1;i--){
			
			if(a%10!=b%10 && a%10 != 9 && b%10 != 9)dist++;
			a/=10;b/=10;
		}
		return dist;
	}
	
	public int conflicts(int a,int b){
		int val = 0;
		for(int i=0;i<3;i++){
		int[] u = new int[3];
		int[] v = new int[3];
		boolean conf = false;
		for(int j=0;j<3;j++){u[j]=a%10;a/=10;v[j]=b%10;b/=10;}
		for(int p=0;p<2;p++)for(int q=p+1;q<3;q++)
			for(int r=1;r<3;r++)for(int s=0;s<r;s++)
				if(u[p]==v[r]&&u[q]==v[s] && u[p] != 9 && u[q] != 9)conf=true;
		if(conf)val+=2;
		}
		
		return val;
	}
	
	int h(int goal){
//		return 0;
//		return manhattan(id, goal);//+ conflicts(id,goal);
//		return displacement(id, goal);
		if (id % 2 == 0) {
			return manhattan(id, goal);
		}
		else
			return 0;
		
	}
	
	Nodes(int[][] v1, int goal) {
		
		v = new int[3][3];
		parent = null;
		
		// copy the state
		for(int i = 0; i < 3; i++) 
			v[i] = v1[i].clone();
		
		// find the blank tile
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if (v[i][j] == 9){ 
					x0 = i;
					y0 = j;
					break;
				}	
			}
		}
		
		// initialize id
		id = convert(v1);
		h = h(goal);
	}

	Nodes(int id, int goal) {
		
		v = new int[3][3];
		parent = null;
		
		// initialize id
		this.id = id; 

		// initialize 2d array
		int i = 2, j = 2;
		while(id > 0) {
			int a = id % 10;
			id = id / 10;
			v[i][j] = a;
			if (j > 0)
				j--;
			else {
				j = 2;
				i--;
			}
		}
		
		// find the blank tile
		for(i = 0; i < 3; i++) {
			for(j = 0; j < 3; j++) {
				if (v[i][j] == 9){ 
					x0 = i;
					y0 = j;
					break;
				}	
			}
		}
		h = h(goal);
	}

	static int convert(int[][] v1) {
		int val = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				val += v1[i][j] * Math.pow(10, 2-j + (2-i) * 3);
			}
		}
		return val;
	}


	ArrayList<Integer> Next(){
		ArrayList<Integer> ans = new ArrayList<Integer>();
		int[][] v1 = new int[3][3];
		if (y0 > 0) {
			for(int i = 0; i < 3; i++) 
				v1[i] = v[i].clone();
			int t = v1[x0][y0-1];
			v1[x0][y0-1] = v1[x0][y0];
			v1[x0][y0] = t;
			ans.add(convert(v1));
		}
		if (y0 < 2) {
			for(int i = 0; i < 3; i++) 
				v1[i] = v[i].clone();
			int t = v1[x0][y0+1];
			v1[x0][y0+1] = v1[x0][y0];
			v1[x0][y0] = t;		
			ans.add(convert(v1));
		}
		if (x0 > 0) {
			for(int i = 0; i < 3; i++) 
				v1[i] = v[i].clone();
			int t = v1[x0-1][y0];
			v1[x0-1][y0] = v1[x0][y0];
			v1[x0][y0] = t;		
			ans.add(convert(v1));
		}
		if (x0 < 2) {
			for(int i = 0; i < 3; i++) 
				v1[i] = v[i].clone();
			int t = v1[x0+1][y0];
			v1[x0+1][y0] = v1[x0][y0];
			v1[x0][y0] = t;
			ans.add(convert(v1));
		}

		return ans;
	}
}