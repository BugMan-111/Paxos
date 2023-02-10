public class MaxId
{
    protected int maxId;

    /**
    * Create a container for max_id,
    * This constructor will initilise max_id to iniial value
    * @return void
    */
    public MaxId()
    {
        this.maxId = -1;
    }


    /**
    * Control the critical state of the max_id of a acceptor
    * this function will either change or get the value of max_id of a acceptor
    * @param  option          if the option is -1, this will be seen as get operation, if it is not -1, then it is set operation
    * @return int             the max_id for get operation, a placeholder for set operation  
    */
    public synchronized int getOrChange(int option)
    {
        if(option == -1)
            return this.maxId;
        else
            this.maxId = option;
        return -1;
    }
}
