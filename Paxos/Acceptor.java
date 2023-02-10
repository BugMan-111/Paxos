import java.net.*;  


public class Acceptor implements Runnable
{
    
    protected ServerSocket server;
    protected MaxId maxId;
    protected AcceptedValue acceptedValue;
    protected int delayTime;
    protected String name;


    /**
    * Create a Thread object to handle one request from the proposer
    * @param  portNumber                 The port number this particular acceptor will listen on 
    * @param  delayTime                  Whether this member will reply the proposer late
    * @param  acceptedValue              The pointer(container) of Accepted proposer ID and accepted proposed value as well as a stop flag
    * @param  name                       The name of this acceptor
    * @return void
    */
    public Acceptor(int portNumber, int delayTime, String name)
    {
        try{this.server = new ServerSocket(portNumber);}
        catch(Exception e){return;}
        this.maxId = new MaxId();
        this.acceptedValue = new AcceptedValue();
        this.delayTime = delayTime;
        this.name = name;
    }


    /**
    * Create a multi-threaded server which listens to the proposers
    * @return void
    */
    public void run()
    {
        try
        {
            // keep listening to the port until the stop flagged is raised
            // or a council memeber is selected
            while(true)
            {
                if(this.acceptedValue.getOrChangeFinished(1) == true)
                    break;
                Socket s = this.server.accept();
                new AcceptorServerThread(s, maxId, acceptedValue, delayTime, this.name).start();
            }  
           
        }
        catch(Exception e){System.out.println(e); return;}
    }
}