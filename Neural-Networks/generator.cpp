#include<iostream>
#include<vector>

using namespace std;
int power(int a,int b)
{
	if (b==0) return 1;
	return a*power(a,b-1);
}


void majority_n(int n, vector<vector<int> > &ans)
{
	for(int i=0;i<power(2,n);i++)
	{
		int val=i;
		for(int j=n-1;j>=0;j--)
		{

			if(val>=power(2,j))
			{
				ans[i][n-j]=1;
				val=val-power(2,j);
			}
			else ans[i][n-j]=0;
		}
		int pos=0;
		int neg=0;
		for(int j=1;j<=n;j++)
		{
			if(ans[i][j]==1) pos++;
			else neg++;
		}
		if(pos>neg)
		{
			ans[i][0]=-1;
		}
		else
		{
			ans[i][0]=1;
			for(int j=1;j<=n;j++)
			{
				ans[i][j]=-ans[i][j];
			}
		}
		// for(int j=0;j<=n;j++)
		// {
		// 	cout<<ans[i][j]<<" ";
		// }
		// cout<<endl;
	}
}

void palindrome_n(int n, vector<vector<int> > &ans)
{
	for(int i=0;i<power(2,n);i++)
	{
		int val=i;
		for(int j=n-1;j>=0;j--)
		{

			if(val>=power(2,j))
			{
				ans[i][n-j]=1;
				val=val-power(2,j);
			}
			else ans[i][n-j]=0;
		}
		bool palindrome=true;
		for(int j=1;j<=n/2;j++)
		{
			if(ans[i][j]!=ans[i][n-j+1]) {
				palindrome=false;
				break;
			}

		}
		if(palindrome)
		{
			ans[i][0]=-1;
		}
		else
		{
			ans[i][0]=1;
			for(int j=1;j<=n;j++)
			{
				ans[i][j]=-ans[i][j];
			}
		}
	}
}


vector<vector<int> > training_nand = {{-1,0,0},{-1,0,1},{-1,1,0},{1,-1,-1}};
vector<vector<int> > training_or = {{1,0,0},{-1,0,1},{-1,1,0},{-1,1,1}};
vector<vector<int> > training_and = {{1,0,0},{1,0,-1},{1,-1,0},{-1,1,1}};
vector<vector<int> > training_xor = {{1,0,0},{-1,0,1},{-1,1,0},{1,-1,-1}};

