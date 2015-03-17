notgate(notA).
notgate(notB).
notgate(notC).
notgate(notD).

andgate(andA).
andgate(andB).
andgate(andC).
andgate(andD).
andgate(andE).
andgate(andF).

orgate(orA).
orgate(sum).
orgate(carry).

connected(a,notA).
connected(b,notB).
connected(c,notC).
connected(orA,notD).

connected(notA,b,andA). 
connected(notB,a,andB).
connected(andA,andB,orA).
connected(orA,notC,andC).
connected(notD,c,andD).
connected(andC,andD,sum).
connected(orA,c,andE).
connected(a,b,andF).
connected(andE,andF,carry).