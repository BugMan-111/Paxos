import java.util.ArrayList;

public class PaxosInterfaceImpl implements PaxosInterface
{
    int acceptorPort = 3000;
    int proposerId = 0;

    ArrayList<Integer> portNums = new ArrayList<Integer>(); 
    ArrayList<Node> nodes = new ArrayList<Node>();
    ArrayList<Integer> proposers = new ArrayList<Integer>(); 
    ArrayList<Integer> acceptors = new ArrayList<Integer>();  
    
    
    /**
     * Adding a proposer node to the execution pool
     * A proposer will also be an acceptor
     * @param  delayTime     Whether this member will reply the proposer late
     *                       the higher the weight, the lower the delayTime
     *                       if the delay time is set to be -2, the node will sometimes be delaying
     *                       sometimes not
     * @param  name          the name of the proposer
     * return  void         
     */
    public void addProposer(int delayTime, String name)
    {
        // create a new node
        Node n = new Node(delayTime, acceptorPort, null, proposerId, name);
        nodes.add(n);
        portNums.add(acceptorPort);

        // mark the proposer index
        proposers.add(nodes.size() - 1);

        // increment the proposerId for the next proposer
        // increment the acceptorPort for the next Node(both proposer and acceptor will need a acceptor port)
        proposerId += 1;
        acceptorPort += 1;
    }


    /**
     * Adding a acceptor node to the execution pool
     * @param  delayTime     Whether this member will reply the proposer late
     *                       the higher the weight, the lower the delayTime
     *                       if the delay time is set to be -2, the node will sometimes be delaying
     *                       sometimes not
     * @param  name          the name of the acceptor
     * return  void         
     */
    public void addAcceptor(int delayTime, String name)
    {
        // create a new node
        Node n = new Node(delayTime, acceptorPort, null, -1, name);
        nodes.add(n);
        acceptors.add(nodes.size() - 1);

        // assign new port
        portNums.add(acceptorPort);
        acceptorPort += 1;
    }


    /**
     * running all the nodes
     * return  void    
     */
    public void run()
    {
        // set ports to all the proposers
        int[] ports = ArrayListToArray(portNums);
        for(Integer i : proposers)
        {
            // get one proposer
            Node node = nodes.get(i.intValue());
            
            // set portnums to this proposer
            node.setPortNums(ports);
        }

        // create threads
        Thread[] threads = new Thread[nodes.size()];
        for(int i = 0; i < nodes.size(); i++)
        {
            // get current node
            Node current = nodes.get(i);
            threads[i] = new Thread(current);
        }

        // run nodes with only acceptors
        for(Integer i : acceptors)
        {
            threads[i.intValue()].run();
        }

        // give a little pause
        try{Thread.sleep(150);}
        catch(Exception e){}

        // run nodes with proposers
        for(Integer i : proposers)
        {
            threads[i.intValue()].run();
        }
        
    }


    /**
     * convert a Integer arrayList to int array
     * @param  portNums the Integer arrayList contains all the ports
     * return  int[]         
     */
    protected int[] ArrayListToArray(ArrayList<Integer> portNums)
    {
        int[] result = new int[portNums.size()];
        for (int i = 0 ; i < portNums.size(); i++)
        {
            result[i] = portNums.get(i).intValue();
        }
        return result;
    }
}
