import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class main {
//static int N,T;
static float[][] A=new float[50][50];
static float[][] B=new float[50][50];
static float P(int j,int i,int k)
{
//	System.out.println(j+" "+k+" "+A[j][i]+" "+B[j][k]+"P");
	return A[j][i]*B[j][k];
}


static void viterbi(int N,int T,int[] oseq,int[] sseq)
{
float[][] seqscore=new float[N+1][T+1];
int[][] backptr=new int[N+1][T+1];
//	int[] oseq=new int[T+1];
seqscore[1][1]=(float) 1.0;
for(int i=2;i<N+1;i++)
{
	seqscore[i][1]=0;
}
backptr[1][1]=0;
for(int t=2;t<T+1;t++)
{
	for(int i=1;i<N+1;i++)
	{
		float maximum=0;
		int val=0;
		for(int j=1;j<N+1;j++)
		{
			if(maximum<seqscore[j][t-1]*P(j,i,oseq[t-1]))
			{
				val=j;
				maximum=seqscore[j][t-1]*P(j,i,oseq[t-1]);
			}
		}
	//	System.out.println(maximum);
		seqscore[i][t]=maximum;
		backptr[i][t]=val;
		
	}
}
/*
for(int i=1;i<N+1;i++)
{
	float maximum=0;
	if(maximum>seqscore[i][T])
	{
		maximum=seqscore[i][T];
		sseq[T]=i;
	}
}*/
sseq[T]=2;
for(int i=T-1;i>0;i--)
{
	sseq[i]=backptr[sseq[i+1]][i+1];
}

}


	public static void main(String[] args) throws IOException {
		boolean x=false;
		for(int i=0;i<50;i++)
		{
			for(int j=0;j<50;j++)
			{
				A[i][j]=0;
				B[i][j]=0;
			}
		}
	    BufferedReader br = new BufferedReader(new FileReader("prms"));
	    try {
	        
	        String line = br.readLine();

	        while (line != null) {
	        	if(line.equals("==========")) {x=true;line = br.readLine(); continue;}
	            StringTokenizer st=new StringTokenizer(line);
	            int a=Integer.parseInt(st.nextToken());
	            int b=Integer.parseInt(st.nextToken());
	            float c=Float.parseFloat(st.nextToken());
	            if(!x)
	            {
	            	A[a][b]=c;
	            
	            }
	            else
	            {
	            //	System.out.println(c+"dsf");
	            	B[a][b]=c;
	            
	            }
	            line = br.readLine();
	        }
	        
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	    //int m=1/0;
		x=false;
		int correct=0;
	    br=new BufferedReader(new FileReader("check"));
	    try {
	        
	        String line = br.readLine();

	        while (line != null) {
	        	//if(line.equals("==========")) {x=true;line = br.readLine(); continue;}
	            StringTokenizer st=new StringTokenizer(line);
	            int[] oseq=new int[50];
	            int[] sseq=new int[50];
	            int[] sseq2=new int[50];
	            x=false;
	            int i=1;
	            while(st.hasMoreTokens())
	            {
	           // 	System.out.println("fg");
	            	String s=st.nextToken();
	            	if(s.equals("|"))
	            	{
	            		i=1;
	            		x=true;continue;
	            	}
	            	int a=Integer.parseInt(s);
	            	if(!x) oseq[i]=a;
	            	else sseq[i]=a;
	            	i++;
	            	
	            }
	            viterbi(42,i-1,oseq,sseq2);
	            correct++;
	            for(int j=1;j<i;j++)
	            {
	            	System.out.println(sseq2[j]);
	            	
	            }
	            line = br.readLine();
	            //System.out.println("Another Iteration");
	        }
	        
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	//	System.out.println(correct);
		}
}
