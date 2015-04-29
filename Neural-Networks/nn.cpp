#include <stdio.h> 
#include <vector> 
#include <assert.h> 
#include <map> 
#include <iostream> 
#include <math.h> 
#include <stdlib.h>
#include <fstream> 
#include <bitset>
#include "generator.cpp"
using namespace std;

#define G_LEN 5 
#define P_LEN 6

int len = 8;
bool reverse_run = false;

template <typename T> 
inline double dot_product(vector<double> weights, vector<T> x) {
//    cout << weights.size() << " " << x.size() << endl;
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
void forward_pass(vector<vector<vector<double>>> &network, vector<T> inputs, vector<vector<T>> &l_outputs) {
    // l_outputs is empty initially

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
    int loop_iters = 2;
	while(loop_iters--) {
		cout << loop_iters << endl;

		int zero_delW = 0;
		for(int i = 0; i < iters; i++) {
			if (i % 1000 == 0) cout << i << endl;
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
						// cout << delW[j][k][l] << endl;
					}
				}
			}

		}
	}
}

void read_data(string filename, vector<vector<double>> &inputs, vector<vector<double>> &outputs) {
	ifstream infile(filename);
	string in, out;

	while(infile >> in >> out) {
		vector<double> inp, outp;
		for(int i = 0; i < in.length(); i++) {
			inp.push_back(in[i]-'0');
		}
		for(int i = 0; i < out.length(); i++) {
			outp.push_back(out[i]-'0');
		}
		if (reverse_run) {
			inputs.push_back(outp);
			outputs.push_back(inp);
		}
		else{
			inputs.push_back(inp);
			outputs.push_back(outp);
		}
	}
}

void test_palindrome() {
	vector<vector<int> > ans(power(2,3), vector<int>(3+1, 0));
	palindrome_n(3,ans);
    
    vector<vector<double>> examples_i, examples_o;    
    for(int i = 0; i < ans.size(); i++) {

        if (ans[i][0] > 0) {
            examples_o.push_back(vector<double>(1,0));
            vector<double> v;
            for(int j = 1; j < ans[i].size(); j++) {
                v.push_back(-ans[i][j]);
            }
            examples_i.push_back(v);
        }
        else {
            examples_o.push_back(vector<double>(1,1));
            vector<double> v;
            for(int j = 1; j < ans[i].size(); j++)
                v.push_back(ans[i][j]);
            examples_i.push_back(v);
        }
    }

    vector<vector<vector<double>>> network = {{{0.01,0.01,0.01,0.01},{0.01,0.01,0.01,0.01},{0.01,0.01,0.01,0.01}},
                                               {{0.1,1,1,1},{0.1,0.1,0.1,0.1},{0.1,0.1,0.1,0.1}},
                                               //{{1,1,1,1},{1,1,1,1},{1,1,1,1}},
                                               //{{1,1,1,1,1,1},{1,1,1,1,1,1},{1,1,1,1,1,1},{1,1,1,1,1,1},{1,1,1,1,1,1}},
                                               {{0.1,0.1,0.1,0.1}}};
    vector<vector<double>> l_out;

    backpropagation(network, examples_i, examples_o, 0.1);
    cout << "learning done!" << endl;
    // while (true) {
    	// cout << "Enter binary string: ";
    	vector<double> in(3);
    	// for(int i = 0; i < 3; i++)
    	// 	cin >> in[i];
    	for(int i = 0; i < 2; i++) {
    		for(int j = 0; j < 2; j++) {
    			for(int k = 0; k < 2; k++) {
    				in = {i, j, k};
    				vector<vector<double>> cur_out;

    				forward_pass(network, in, cur_out);
    				cout << i << " " << j << " " << k << ": " << cur_out[2][0] << endl;

    			}
    		}
    	}
}


void test_PTA() {
	/*	vector<double> weights(5+1, 0);

    int iters = PTA(weights, ans);
    vector<vector<vector<double>>> weights(
    
    printf("weights computed in %i iterations\n", iters);
	for(int i = 0; i < weights.size(); i++) {
		printf("%lf ", weights[i]);
	}
*/
}

inline vector<double> rand_vec(int l) {
	vector<double> ans;
	for(int i = 0; i < l; i++) {
		int sign = rand()%2?-1:1;
		ans.push_back(sign * (double) rand() * 3.0 / (RAND_MAX ));
		// ans.push_back(0);
	}
	return ans;
}

void init_network(vector<vector<vector<double>>> &network) {
	network.clear();
	// // i/p layer
	int input_neurons = (reverse_run?P_LEN:G_LEN) * len;
	vector<vector<double>> layer1;
	for(int i = 0; i < input_neurons; i++) {
		layer1.push_back(rand_vec((reverse_run?P_LEN:G_LEN) * len + 1));
	}
	network.push_back(layer1);

	//hidden layer
	int hidden_neurons = 175;
	vector<vector<double>> layer2;
	for(int i = 0; i < hidden_neurons; i++) {
		layer2.push_back(rand_vec(input_neurons + 1));
	}
	network.push_back(layer2);

	//output layer
	int output_neurons = (reverse_run?G_LEN:P_LEN) * len;
	vector<vector<double>> layer3;
	for(int i = 0; i < output_neurons; i++) {
		layer3.push_back(rand_vec(hidden_neurons + 1));
	}
	network.push_back(layer3);
}

void k_fold(int k, vector<vector<double>> &examples_i, vector<vector<double>> &examples_o) {
	
	int total_size = examples_i.size();
	double accuracy_b = 0, accuracy_w = 0;
	for(int iter = 0; iter < k; iter++) {
		int val_start = iter * (total_size) / k, val_end = min(val_start + (total_size/k), total_size);
		vector<vector<double>> training_inp, training_out, validate_inp, validate_out;
		for(int i = 0; i < examples_i.size(); i++) {
			if (i >= val_start && i < val_end) {
				validate_inp.push_back(examples_i[i]);
				validate_out.push_back(examples_o[i]);
			}
			else {
				training_inp.push_back(examples_i[i]);
				training_out.push_back(examples_o[i]);
			}
		}

		// cout << "hello: " << training_out.size() << " " << training_inp.size() << endl;
		// cout << "bye: " << validate_out.size() << " " << validate_inp.size() << endl;
		vector<vector<vector<double>>> network;
		init_network(network);
		backpropagation(network, training_inp, training_out, 0.25);

		int correct_b = 0, total_b = 0, correct_w = 0, total_w = 0;
		for(int i = 0; i < validate_inp.size(); i++) {
			vector<vector<double>> l_outputs;
			forward_pass(network, validate_inp[i], l_outputs);
			int bits_matched = 0;
			for(int j = 0; j < validate_out[i].size(); j++) {
				int bit = 1?l_outputs[2][j]>0.5:0;
				if (validate_out[i][j] == bit) {
					correct_b++;
					bits_matched++;
				}
				total_b++;

				if (j % 6 == 5) {
					if (bits_matched == 6) 
						correct_w++;
					total_w++;
					bits_matched = 0;
				}
			}
		}
		accuracy_b += 100*double(correct_b)/double(total_b);
		accuracy_w += 100*double(correct_w)/double(total_w);


	}	
	cout << "word level accuracy: " << accuracy_w/k << endl; 
	cout << "bit level accuracy: " << accuracy_b/k << endl;
}

void stringToUpper(string &s)
{
	for(unsigned int l = 0; l < s.length(); l++)
		s[l] = toupper(s[l]);
}

vector<double> grapheme_to_bits(string grapheme) {
	stringToUpper(grapheme);
	vector<double> ans;
	for(int i = 0; i < grapheme.length(); i++) {
		bitset<G_LEN> g(grapheme[i] - 'A' + 1);
		for(int j = G_LEN - 1; j >= 0; j--)
			ans.push_back(g[j]);
	}
	int x = ans.size(); 
	while(x < G_LEN * len) {
		ans.push_back(0);
		x = ans.size();
	}
	return ans;
}

map<int, string> ph_map;
map<string, int> inv_ph_map;

void init_ph_map() {
	ifstream infile("data/SphinxPhones_40");
	int i = 0;
	string ph;
	while(infile >> ph) {
		i++;
		ph_map[i] = ph;
		inv_ph_map[ph] = i;
	}
}

string bits_to_phoneme(vector<double> &output) {
	string phoneme = "";
	bitset<P_LEN> curr;
	for(int i = 0; i < output.size(); i++) {
		curr[P_LEN - 1 - (i % P_LEN)] = (int)output[i];
		if (i % P_LEN == (P_LEN-1)) {
			int val = curr.to_ulong();
			curr.reset();
			if (val != 0)
				phoneme += ph_map[val];
		}
	}
	return phoneme;
}

void apply_threshold(vector<double> &outputs) {
	for(int i = 0; i < outputs.size(); i++) {
		outputs[i] = outputs[i]<0.5?0:1;
	}
}

void run_G_P(vector<vector<vector<double>>> &network) {
	string inp;
	while(true) {
		cout << "Enter Grapheme sequence" << ": ";
		cin >> inp;
		if (inp[0] == '0') break;
		vector<double> input = grapheme_to_bits(inp);
		vector<vector<double>> l_outputs;
		forward_pass(network, input, l_outputs);

		apply_threshold(l_outputs[2]);
		string phoneme = bits_to_phoneme(l_outputs[2]);

		for(int i = 0; i < l_outputs[2].size(); i++) {
			cout << l_outputs[2][i] << " ";
		}

		cout << "Phoneme sequence: " << phoneme << endl;
	}
}

vector<double> phoneme_to_bits(vector<string> &inp) {
	vector<double> ans;

	for(int i = 0; i < inp.size(); i++) {
		int index = inv_ph_map[inp[i]];
		bitset<P_LEN> p(index);
		for(int j = P_LEN-1; j >= 0; j--) 
			ans.push_back(p[j]);
	}
	int x = ans.size();
	while (x < P_LEN * len) {
		ans.push_back(0);
		x = ans.size();
	}
	return ans;
}

string bits_to_grapheme(vector<double> &output) {
	string grapheme = "";
	bitset<G_LEN> curr;
	for(int i = 0; i < output.size(); i++) {
		curr[G_LEN - 1 - (i % G_LEN)] = (int)output[i];
		if (i % G_LEN == (G_LEN-1)) {
			int val = curr.to_ulong();
			curr.reset();
			if (val != 0)
				grapheme.push_back('A' + (val - 1));
		}
	}
	return grapheme;
}

void run_P_G(vector<vector<vector<double>>> &network) {
	string inp;
	while(true) {
		cout << "Enter Phoneme sequence" << ": ";
		vector<string> ph_seq;
		while(cin >> inp) {
			stringToUpper(inp);
			if (inp == "X" || inp[0] == '0')
				break;
			ph_seq.push_back(inp);
		}
		
		if (inp[0] == '0') break;
		vector<double> input = phoneme_to_bits(ph_seq);
		vector<vector<double>> l_outputs;
		forward_pass(network, input, l_outputs);

		apply_threshold(l_outputs[2]);
		string phoneme = bits_to_grapheme(l_outputs[2]);

		for(int i = 0; i < l_outputs[2].size(); i++) {
			cout << l_outputs[2][i] << " ";
		}

		cout << "Grapheme sequence: " << phoneme << endl;
	}
}

void test_G_P() {


	vector<vector<double>> examples_i, examples_o;
	// read_data("data/ndata"+to_string(len), examples_i, examples_o);
	read_data("data/ndatapadded0", examples_i, examples_o);
	
	cout << "read " << examples_i.size() << " examples\n";

	vector<vector<vector<double>>> network;
	init_network(network);

	// print the initial NN
	// for(int i =0; i < network.size(); i++) {
	// 	for(int j = 0; j < network[i].size(); j++) {
	// 		for(int k = 0; k < network[i][j].size(); k++) 
	// 			cout << network[i][j][k] << " ";
	// 	}
	// }

	backpropagation(network, examples_i, examples_o, 0.4);
	cout << "learning done! \n";
	return;

	// vector<vector<double>> l_outputs;
	// forward_pass(network, examples_i[0], l_outputs);
	// cout << "input: \n";
	// for(int i = 0; i < examples_i[0].size(); i++) {
	// 	cout << examples_i[0][i] << " ";
	// }
	// cout << endl << "ouput: \n";
	// for(int i = 0; i < l_outputs[2].size(); i++) {
	// 	cout << l_outputs[2][i] << " ";
	// }
	// cout << "\n correct: \n"; 
	// for(int i = 0; i < examples_o[0].size(); i++) {
	// 	cout << examples_o[0][i] << " ";
	// }

	if (reverse_run) 
		run_P_G(network);
	else
		run_G_P(network);
	
	k_fold(5, examples_i, examples_o);
}

int main() {
	init_ph_map();
	srand(time(NULL));

	test_G_P();
	return 0;
}
