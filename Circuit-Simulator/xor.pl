% sample spec of xor circuit
orgate(xor).
andgate(m).
andgate(n).
notgate(o).
notgate(p).
connected(a,o).
connected(b,p).
connected(o,b,m).
connected(a,p,n).
connected(m,n,xor).

% notgate(ppp).
% orgate(h).
% orgate(j).
% andgate(k).
% andgate(i).
% connected(x,y,i).
% connected(x,y,j).
% connected(j,z,k).
% connected(i,k,h).