public interface PaxosInterface 
{
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
    public void addProposer(int delayTime, String name);

    /**
     * Adding a acceptor node to the execution pool
     * @param  delayTime     Whether this member will reply the proposer late
     *                       the higher the weight, the lower the delayTime
     *                       if the delay time is set to be -2, the node will sometimes be delaying
     *                       sometimes not
     * @param  name          the name of the acceptor
     * return  void         
     */
    public void addAcceptor(int delayTime, String name);

    /**
     * running all the nodes
     * return  void    
     */
    public void run();
    
}
