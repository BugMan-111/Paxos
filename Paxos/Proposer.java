import java.util.*;
import java.io.*;  
import java.net.*;  


public class Proposer implements Runnable
{

    protected int proposerID;
    protected int portNums[];
    protected String name;


    /**
    * Create a proposer object
    * @param  portNumbers   Port number of all the acceptors
    * @param  proposerID    the proposer ID specified in the algorithm
    * @param  name          the name of the current server
    * @return   void
    */
    public Proposer(int[] portNumbers,int proposerID, String name)
    {
        this.portNums = portNumbers;
        this.proposerID = proposerID;
        this.name = name;
    }

    public void setPortNums(int[] portNumbers)
    {
        this.portNums = portNumbers;
    }


    /**
    * The implementation of phase1 of the proposer segment of the algorithms
    * @return   defaultVal      the value that this proposer wants to propose
    */
    private String phase1()
    {
        String defaultVal = "";
        try
        {
            // prepare phase
            ArrayList<Socket> acceptors = new ArrayList<Socket>();
            for(int i = 0; i < this.portNums.length; i++)
                acceptors.add(new Socket("localhost", this.portNums[i]));

            // send prepare message to all the acceptors
            for(Socket s : acceptors)
            {
                ObjectOutputStream currentOutput = new ObjectOutputStream(s.getOutputStream());
                currentOutput.writeObject("Prep@" + Integer.toString(this.proposerID));
            }

            // set default value
            ArrayList<Integer> checked = new ArrayList<Integer>();
            int currentID = -1;

            // wait for the acceptors to reply(note: a majority(int(9/2) + 1 == 5) of proposers are needed)
            while(true)
            {
                // check all the received messages
                for(int i = 0; i < acceptors.size(); i++)
                {
                    // get one connection from the list
                    Socket s = acceptors.get(i);

                    // get the information from the socket
                    String message = "";
                    try
                    {
                        ObjectInputStream acceptorMSG = new ObjectInputStream(s.getInputStream());
                        message = (String) acceptorMSG.readObject();
                    }
                    catch(Exception e){};

                    // if the message has been delivered and has not been recorded by checked array
                    if((!message.equals("")) && (!checked.contains(i)))
                    {
                        String[] information = message.split("@");
                        if(information[0].equals("promVal"))
                        {
                            // do any responses contain accepted values (from other proposals)?
                            // value from PROMISE message with the highest accepted ID
                            // find largest accepted id
                            int acceptedId = Integer.parseInt(information[1]);
                            if(currentID < acceptedId)
                            {
                                currentID = acceptedId;
                                defaultVal = information[2];
                            }
                        }
                        checked.add(i);
                    }
                }

                // if majority of acceptors are reached
                if(checked.size() >= 5)
                    break;
            }

            // close all the previous sockets
            for(Socket s : acceptors)
                s.close();

            // if there is no other values that are prefered, choose the Node ID
            if(defaultVal == "")
                defaultVal = this.name;

            return defaultVal;
        }
        catch(Exception e){System.out.println(e);}
        return defaultVal;
    }

    
    /**
    * The implementation of phase2 of the proposer segment of the algorithms
    * @param    defaultVal      the value which the proposer will propose to the acceptors
    * @return   result          Whether majority of acceptors accepts the proposed value
    */
    public int phase2(String defaultVal)
    {
        int result = -1;
        try
        {
            // create new sockets for phase 2
            ArrayList<Socket> acceptors = new ArrayList<Socket>();
            for(int i = 0; i < this.portNums.length; i++)
                acceptors.add(new Socket("localhost", this.portNums[i]));

            // create new checked array for phase 2
            ArrayList<Integer> checked = new ArrayList<Integer>();
            
            // send propose message to all the acceptors
            // PROPOSE(ID, val) to at least a majority of acceptors
            for(Socket s : acceptors)
            {
                ObjectOutputStream currentOutput = new ObjectOutputStream(s.getOutputStream());
                currentOutput.writeObject("Props@" + Integer.toString(this.proposerID) + "@" + defaultVal);
            }

            // wait for the acceptors to reply(note: a majority(int(9/2) + 1 == 5) of proposers are needed)
            checked.clear();
            int counterAccept = 0;
            int counterRufuse = 0;
            while(true)
            {
                // check all the received messages
                for(int i = 0; i < acceptors.size(); i++)
                {
                    // get the current socket
                    Socket s = acceptors.get(i);

                    // get the information from the socket
                    String message = "";
                    try
                    {
                        ObjectInputStream acceptorMSG = new ObjectInputStream(s.getInputStream());
                        message = (String) acceptorMSG.readObject();
                    }
                    catch(Exception e){};

                    // if the message has been delivered and has not been recorded by checked array
                    if((!message.equals("")) && (!checked.contains(i)))
                    {
                        
                        if(message.equals("Accept"))
                        {
                            counterAccept += 1;
                            checked.add(i);
                        }
                        else if(message.equals("Fail"))
                        {
                            counterRufuse += 1;
                            checked.add(i);
                        }
                    }
                }

                // if majority of acceptors are reached
                if(counterAccept >= 5)
                {
                    result = 1;
                    break;
                }
                if(counterRufuse >= 5)
                {
                    result = 0;
                    break;
                }
            }

            return result;
        }
        catch(Exception e){System.out.println(e);}
        return result;
    }

    /**
    * override the run method in the runnable interface
    * start the execution(all three phases) of this particular proposer
    * @return   void
    */
    public void run()
    {
        try
        {
            while(true)
            {
                
                String defaultVal = phase1();

                int result = phase2(defaultVal);
                
                // phase 3: broadcast result to all the acceptors
                if(result == 1)
                {
                    // create acceptor array
                    ArrayList<Socket> acceptors = new ArrayList<Socket>();

                    // create new connections
                    for(int i = 0; i < this.portNums.length; i++)
                        acceptors.add(new Socket("localhost", this.portNums[i]));

                    // send end message to acceptors
                    for(Socket s : acceptors)
                    {
                        ObjectOutputStream currentOutput = new ObjectOutputStream(s.getOutputStream());
                        currentOutput.writeObject("Final: " + defaultVal + " is selected");
                    }
                    return;
                }
                else
                    return;
            }   
        }
        catch(Exception e){System.out.println(e); return;}
    }
}
