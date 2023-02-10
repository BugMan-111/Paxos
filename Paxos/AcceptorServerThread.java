import java.net.*;
import java.io.*;  
import java.util.concurrent.ThreadLocalRandom;


public class AcceptorServerThread extends Thread {

    protected Socket socket;
    protected String type;
    protected int proposerId;
    protected String proposedValue;
    protected int delayTime;
    protected MaxId maxId;
    protected AcceptedValue acceptValue;
    protected String name;

    /**
    * Create a Thread object to handle one request from the proposer
    * @param  socket                     The socket between aggregation server and the sender
    * @param  maxId                      The max_id of the acceptor 
    * @param  acceptedValue              The pointer(container) of Accepted proposer ID and accepted proposed value as well as a stop flag
    * @param  couldDrop                  Whether this member will sometimes ignore some messages
    * @param  delayTime                  Whether this member will reply the proposer late
    * @param  name                       The name of this acceptor
    * @return void
    */
    public AcceptorServerThread(Socket socket, MaxId maxId, AcceptedValue acceptedValue, int delayTime, String name)
    {
        this.socket = socket;
        this.maxId = maxId;
        this.acceptValue = acceptedValue;
        this.delayTime = delayTime;
        this.name = name;
    }


    /**
    * respond to one of the request made by proposer
    * @return   void
    */
    public void run()
    {
        // read the message from the proposer
        ObjectInputStream ois = null;
        String inputInformation = "";
        try
        {
            ois = new ObjectInputStream(this.socket.getInputStream());
            inputInformation = (String)ois.readObject();  
        }
        catch(Exception e){System.out.println(e);} 

        // get information from the proposer
        // msg from proposer are separeted by + sign
        String[] msgs = inputInformation.split("@");
        for(int i = 0; i < msgs.length; i++)
        {
            if(i == 0)
                this.type = msgs[i];
            if(i == 1)
                this.proposerId = Integer.parseInt(msgs[i]);
            if(i == 2)
                this.proposedValue = msgs[i];
        }

        // random sleep
        // adjusted by the delay time
        if(this.delayTime != -1)
        {
            // for some nodes, sometimes it will delay, sometimes it will not
            if(this.delayTime == -2)
            {
                try
                {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
                    if(randomNum >= 50)
                        Thread.sleep((long)(Math.random() * 1000 / (-1 * this.delayTime)));
                }
                catch(Exception e){System.out.println(e);return;}
            }  
            else
            {
                // For rest of the servers
                try
                {
                    Thread.sleep((long)(Math.random() * 1000 / this.delayTime));
                }
                catch(Exception e){System.out.println(e);return;}
            }
        }
        
        // reply messages
        if(this.type.equals("Prep"))
        {
            // if the proposer is not listening to this part
            // simply abort this connection(as proposer does not need the reply from this acceptor anymore)
            try
            {
                // ID should be >= max_id
                String respondMsg = "";
                if(this.proposerId >= this.maxId.getOrChange(-1))
                {
                    // max_id = ID ----> save highest ID we've seen so far
                    this.maxId.getOrChange(this.proposerId);

                    // proposal_accepted == true(a proposal has been accepted)
                    if(!this.acceptValue.getOrChangeValue("").equals(""))
                        respondMsg += "promVal@" + Integer.toString(acceptValue.getOrChangeID(-1)) + "@" + acceptValue.getOrChangeValue("");
                    else
                        respondMsg += "Prom";
                }
                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                oos.writeObject(respondMsg);
            }
            catch(Exception e){return;}
        }
        else if(this.type.equals("Props"))
        {
            // if the proposer is not listening to this part
            // simply abort this connection(as proposer does not need the reply from this acceptor anymore)
            try
            {
                String respondMsg = "";
                // if ID == max_id
                //proposal_accepted = true     // this will be noted by looking at the value of accepted proposer ID
                //accepted_ID = ID             // save the accepted proposal number
                //accepted_VALUE = VALUE       // save the accepted proposal data
                if(this.proposerId == this.maxId.getOrChange(-1))
                {
                    this.acceptValue.getOrChangeID(this.proposerId);
                    this.acceptValue.getOrChangeValue(this.proposedValue);
                    respondMsg += "Accept";
                }
                else
                {
                    // send the fail message
                    respondMsg += "Fail";
                }

                // write the outcome to that proposer
                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                oos.writeObject(respondMsg);
            }
            catch(Exception e){return;}
        }
        else if(inputInformation.substring(0, 5).equals("Final"))
        {
            System.out.println(this.name + ": " + inputInformation);
            this.acceptValue.getOrChangeFinished(2);
        }
        else
        {
            // should not ended up here
            // ilegal message type
            System.out.println("current type: " + this.type + " compare with Prep: " + this.type.equals("Prep"));
        }
    }
}
