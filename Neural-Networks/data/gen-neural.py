import sys

def encode(n,l):
	s = bin(n)[2:]
	return '0'*(l-len(s))+s
mp={}
mp2={}
f=open('SphinxPhones_40','r')
c=1
for l in f.readlines():
	mp[l.strip()]=c;c+=1

for i in range(26):mp2[chr(i+ord('A'))]=i+1

# fr = open('data','r')
# fw = [open('ndata'+str(i),'w') for i in range(50)]
# fw2 = [open('ndatapadded'+str(i),'w') for i in range(5)]
# for line in fr.readlines():
# 	l = line.strip().split()
# 	s = "".join([encode(mp2[k],5) for k in l[0]])+" "+"".join([encode(mp[k],6) for k in l[1:]])+"\n"
# 	sp = "".join([encode(mp2[k],5) for k in l[0]])+"0"*5*((-len(l[0]))%8)+" "+"".join([encode(mp[k],6) for k in l[1:]])+"0"*6*((-len(l[1:]))%8)+"\n"
# 	fw[len(l[0])].write(s)
# 	fw2[(len(l[0])-1)//8].write(sp)
	