# Paxos
This project implements Leslie Lamport's Paxos Algorithm. 
<br />
It uses a 3-phase version of the algorithm, which differs slightly from the original described in "Paxos Made Simple" by Leslie Lamport. 
<br />
In this implementation, listeners are omitted and their functionality is transferred to the acceptors. 
<br />
This algorithm allows multiple computers to reach consensus when there are multiple proposers. 
<br />
The consensus is determined by the proposerID of the proposers. 
<br />
The design also tolerates certain levels of delay from the acceptors.
<br />
<br />
Reference:
<br />
https://people.cs.rutgers.edu/~pxk/417/notes/paxos.html
<br />
https://www.youtube.com/watch?v=SRsK-ZXTeZ0
<br />
https://www.youtube.com/watch?v=JEpsBg0AO6o&t=831s
<br />
https://www.youtube.com/watch?v=UCmAzWvrFmo&t=267s
<br />
https://lamport.azurewebsites.net/pubs/paxos-simple.pdf
<br />
<br />
To try out this Paxos Implementation
<br />
1.To compile the code: javac *.java
<br />
2.1For PaxosInterface: java RunWithInterface.java
<br />
2.2For not using the interface: java RunWithoutInterface.java
<br />
If there is any Execptions(this should not be the case)
please termiante the program and run again(could be the OS is trying to use ports that we are using).
