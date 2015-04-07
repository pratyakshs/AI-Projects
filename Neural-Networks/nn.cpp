#include<stdio.h>
#include<vector>
#include<assert.h>
#include<map>
#include<iostream>
#include<math.h>
#include "generator.cpp"
using namespace std;

template <typename T> 
inline double dot_product(vector<double> weights, vector<T> x) {
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

int PTA(vector<double> &weights, vector<vector<int>> &examples) {
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

inline double sigmoid(double arg) {
	return 1.0 / (1.0 + exp(-arg));
}

template <typename T> 
void forward_pass(vector<vector<vector<double>>> &network, vector<T> inputs, vector<vector<T>> l_outputs) {
	int layers = network.size();
	// layer -1 = inputs
	vector<double> prev_outputs = inputs;
	prev_outputs.push_back(-1);
	l_outputs.push_back(inputs);

	for(int i = 1; i < layers; i++) {
		vector<double> outputs;
		for(int j = 0; j < network[i].size(); j++) {
			double net = dot_product(network[i][j], prev_outputs);
			outputs.push_back(sigmoid(net));
		}
		outputs.push_back(-1);
		l_outputs.push_back(outputs);
		prev_outputs.clear();
		prev_outputs = outputs;
	}
}

template <typename T>
void backpropagation(vector<vector<vector<double>>> &network, vector<vector<T>> &examples_i, vector<vector<T>> &examples_o, double N) {
	// dw[j,i] = n*del[j]*o[i]
	// for outputs: del[j] = (t[j] - o[j]) o[j] (1 - o[j])
	// for hidden: del[j] = sum{k in NL}(w[k,j]*del[k])o[j](1-o[j])

	assert(examples_i.size() == examples_o.size());
	int iters = examples_i.size(), layers = network.size(); 

	while(true) {
		int zero_delW = 0;
		for(int i = 0; i < iters; i++) {
			vector<T> inputs = examples_i[i];
			vector<vector<T>> outputs; 
			forward_pass(network, inputs, outputs);
			vector<double> del[layers];
			vector<vector<double>> delW[layers];

			//for o/p layer
			int layer_size = network[layers-1].size();
			for(int j = 0; j < layer_size; j++) {
				del[layers-1].push_back(outputs[layers-1][j] * (1 - outputs[layers-1][j])
						* (examples_o[i][j] - outputs[layers-1][j]));
				vector<double> delW_;
				for(int k = 0; k < network[layers-1][j].size(); k++) {
					delW_.push_back(N * del[layers-1][j] * outputs[layers-2][k]);
				}
				delW[layers-1].push_back(delW_);
			}

			//for hidden layers
			for(int j = layers-2; j >= 1; j--) {
				layer_size = network[j].size();
				for(int k = 0; k < layer_size; k++) {
					double sum = 0;
					for(int l = 0; l < network[j+1].size(); l++) {
						sum += network[j+1][l][k] * del[j+1][l] * outputs[j][k] * (1 - outputs[j][k]); 
					}
					del[j].push_back(sum);

					vector<double> delW_;
					for(int l = 0; l < network[j][k].size(); l++) {
						delW_.push_back(N * del[j][k] * outputs[j-1][l]);
					}
					delW[j].push_back(delW_);
				}
			}

			for(int j = 1; j < layers; j++) {
				for(int k = 0; k < network[j].size(); k++) {
					for(int l = 0; l < network[j][k].size(); l++) {
						network[j][k][l] += delW[j][k][l];
					}
				}
			}
			bool flag = true;
			for(int i = 1; i < layers; i++) {
				for(int j = 0; j < delW[i].size(); j++) {
					for(int k = 0; k < delW[i][j].size(); k++) {
						if (delW[i][j][k] != 0)
							flag = false;
					}
				}
			}
			if (flag)
				zero_delW++;
		}
		// stopping condition
		if (zero_delW == iters) 
			break;
	}
}

int main() {
	vector<vector<int> > ans(power(2,5), vector<int>(5+1, 0));
	majority_n(5,ans);
	vector<double> weights(5+1, 0);

	// vector<double> weights(2+1, 0);
	int iters = PTA(weights, ans);

	printf("weights computed in %i iterations\n", iters);
	for(int i = 0; i < weights.size(); i++) {
		printf("%lf ", weights[i]);
	}


	return 0;
}
