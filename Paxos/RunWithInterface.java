public class RunWithInterface 
{
    // exmaple code
    // use Paxos interface to simulate paxos algorithm
    // use addProposer method to add 4 proposers into the pool
    // use addAcceptor method to add 5 acceptors into the pool
    // all of the acceptors have random delays in this setting
    // use run method to activate all the nodes in the pool
    // the proposer N4 will be selected due to it has the largest proposerID among all the proposers
    public static void main(String[] args) {
        // using the interface
        PaxosInterface i = new PaxosInterfaceImpl();

        // adding 4 proposers with delays
        i.addProposer(5, "N1");
        i.addProposer(6, "N2");
        i.addProposer(7, "N3");
        i.addProposer(8, "N4");

        // adding 5 acceptors with delays
        i.addAcceptor(1, "N5");
        i.addAcceptor(2, "N6");
        i.addAcceptor(3, "N7");
        i.addAcceptor(4, "N8");
        i.addAcceptor(5, "N9");

        // run the program
        i.run();
    }    
}
