#include<stdio.h>
#include<vector>
#include<assert.h>
#include<iostream>
#include "generator.cpp"
using namespace std;

inline double dot_product(vector<double> weights, vector<int> x) {
	assert(weights.size() == x.size());

	double ans = 0;
	for(int i = 0; i < weights.size(); i++) {
		ans += (weights[i] * x[i]);
	}
	return ans;
}

void add_to_weight(vector<double> &weights, vector<int> &x_fail) {
	assert(weights.size() == x_fail.size());

	for(int i = 0; i < weights.size(); i++) {
		weights[i] += x_fail[i];
	}
}

int PTA(vector<double> &weights, vector<vector<int> > &examples) {
	double threshold = weights[0];
	int n = weights.size() - 1, num_failed = n, num_iters = 0;

	while(num_failed > 0) {
		num_iters++;
		num_failed = 0;

		for(int i = 0; i < examples.size(); i++) {
			double res = dot_product(weights, examples[i]);
			
			if (res > 0) {
				continue;
			}
			else {
				num_failed++;
				add_to_weight(weights, examples[i]);
			}
		}
	}
	return num_iters;
}

int forward_pass(vector<vector<vector<double> > > network, vector<int> inputs) {
	int layers = network.size(); 

	for(int i = 0; i < layers; i++) {
		
	}
}

int main() {
	vector<vector<int> > ans(power(2,5), vector<int>(5+1, 0));
	palindrome_n(5,ans);
	vector<double> weights(5+1, 0);

	// vector<double> weights(2+1, 0);
	int iters = PTA(weights, ans);

	printf("weights computed in %i iterations\n", iters);
	for(int i = 0; i < weights.size(); i++) {
		printf("%lf ", weights[i]);
	}
	return 0;
}
