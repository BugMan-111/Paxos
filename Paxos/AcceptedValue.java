public class AcceptedValue 
{
    protected String acceptedValue;
    protected int acceptedId;
    protected boolean finished;


    /**
    * Create a container for accepted proposer ID, accepted proposed valuem and a stop flag
    * This constructor will initilise all three varaible to a default value
    * @return void
    */
    public AcceptedValue()
    {
        this.acceptedValue = "";
        this.acceptedId = -1;
        this.finished = false;
    }


    /**
    * Control the critical state of the accepted proposed value
    * this function will either change or get the value of accepted proposed value
    * @param  option          if the option is an empty string, this will be seen as get operation, if it is not an empty string, then it is set operation
    * @return String          the acceptedValue for get operation, a placeholder for set operation  
    */
    public synchronized String getOrChangeValue(String option)
    {
        if(option.equals(""))
            return this.acceptedValue;
        else
            this.acceptedValue = option;
        return "";
    }


    /**
    * Control the critical state of the accepted proposer ID
    * this function will either change or get the value of accepted proposer ID
    * @param  option          if the option is -1, this will be seen as get operation, if it is not -1, then it is set operation
    * @return int             the proposer ID for get operation, a placeholder for set operation  
    */
    public synchronized int getOrChangeID(int option)
    {
        if(option == -1)
            return this.acceptedId;
        else
            this.acceptedId = option;
        return -1;
    }


    /**
    * Control the critical state of the stop flag
    * this function will either change or get the value of stop flag
    * @param  option          if the option is 1, this will be seen as get operation, if it is not 1, then it is set operation
    * @return boolean         the stop flag for get operation, a placeholder for set operation  
    */
    public synchronized boolean getOrChangeFinished(int option)
    {
        if(option == 1)
            return this.finished;
        else
        {
            this.finished = true;
        }
        return false;
    }
}
