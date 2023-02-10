public class Node implements Runnable
{
    private Acceptor acceptor;
    private Proposer proposer;
    private Thread tAcceptor; 
    private Thread tProposer; 


    /**
    * Create a node object to represent one of the council member
    * @param  delayTime     Whether this member will reply the proposer late
    * @param  acceptorPort  the port reserved for the acceptor
    * @param  portNums      Port number of all the acceptors
    * @param  proposerID   the proposer ID specified in the algorithm
    * @return   void
    */
    public Node(int delayTime, int acceptorPort, int[] portNums, int proposerID, String name)
    {
        // create a new acceptor
        if(acceptorPort <= 1024)
            System.out.println("Error: each node should at least have an acceptor, the port number of the acceptor should be greater than 1024");
        this.acceptor = new Acceptor(acceptorPort, delayTime, name);

        // create a new propser(if a proposer is needed for this particular node)
        if(proposerID == -1)
            this.proposer = null;
        else
            this.proposer = new Proposer(portNums, proposerID, name);

        if(this.proposer != null)
            this.tProposer = new Thread(this.proposer);
        this.tAcceptor = new Thread(this.acceptor);
    }


    /**
    * execute the current node(run its acceptor and its proposer)
    * @return   void
    */
    public void run()
    {
        this.tAcceptor.start();;
        try{Thread.sleep(100);}
        catch(Exception e){};
        if(proposer != null)
            this.tProposer.start();;
    }

    
    public void setPortNums(int[] portNums)
    {
        proposer.setPortNums(portNums);
    }
}
