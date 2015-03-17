notgate(notA).
notgate(notB).

orgate(sum).

andgate(andA).
andgate(andB).
andgate(carry).

connected(a,notA).
connected(b,notB).

connected(a,notB,andA).
connected(notA,b,andB).
connected(andA,andB,sum).
connected(a,b,carry).