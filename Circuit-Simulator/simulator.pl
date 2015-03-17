%Xor[A,B] :- verify([or,[and,a,[not,b]],[and,[not,a],b]],0) verify(a,1).

% base case for or
verify(or,[1,1],1).
verify(or,[0,1],1).
verify(or,[1,0],1).
verify(or,[0,0],0).

% base case for and
verify(and,[1,1],1).
verify(and,[0,1],0).
verify(and,[1,0],0).
verify(and,[0,0],0).

% base case for not
verify(not,[0],1).
verify(not,[1],0).

% if the node has two inputs
verify([A,D,E],B,C) :- verify(D,B,X),verify(E,B,Y),verify(A,[X,Y],C),!.

% if the node has one input
verify([A,D],B,C) :- verify(D,B,X),verify(A,[X],C),!.

% if X is a line
verify(X,B,C) :- member([X,C],B).

% to convert circuit spec into tree form
convert(X,[and,W,Y]) :- andgate(X),!,connected(A,B,X),convert(A,W),convert(B,Y).
convert(X,[or,W,Y]) :- orgate(X),!,connected(A,B,X),convert(A,W),convert(B,Y).
convert(X,[not,W]) :- notgate(X),!,connected(A,X),convert(A,W).
convert(X,X).

% goal to verify given spec
verify2(X,B,C) :- convert(X,Z),verify(Z,B,C),!.

% checks if gate X is disconnected (no inputs)
is_disconnected(X) :- orgate(X), connected(A,B,X), !, false.
is_disconnected(X) :- andgate(X), connected(A,B,X), !, false.
is_disconnected(X) :- notgate(X), connected(A,X), !, false.
is_disconnected(X).

