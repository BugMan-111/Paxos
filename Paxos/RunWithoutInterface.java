public class RunWithoutInterface 
{
    public static void testCase()
    {
        // exmaple code
        // simulate paxos algorithm without the interface
        // create 3(1 ~ 3) Nodes contain both proposers and acceptors
        // create 6(4 ~ 9) Nodes contain acceptors
        // all of the acceptors have random delays in this setting except Node 1
            // Node 2 will sometimes delay
        // the proposer N3 will be selected due to it has the largest proposerID among all the proposers
        int[] portNums = {3000, 3001, 3002, 3003, 3004, 3005, 3006, 3007, 3008};

        // create 9 objects
        Node N1 = new Node(-1, 3000, portNums, 1, "N1");
        Node N2 = new Node(-2, 3001, portNums, 2, "N2");
        Node N3 = new Node(8,3002, portNums, 3, "N3");
        Node N4 = new Node(5, 3003, null, -1, "N4");
        Node N5 = new Node(6, 3004, null, -1, "N5");
        Node N6 = new Node(7, 3005, null, -1, "N6");
        Node N7 = new Node(4, 3006, null, -1, "N7");
        Node N8 = new Node(8, 3007, null, -1, "N8");
        Node N9 = new Node(5, 3008, null, -1, "N9");

        // create 9 thread objects
        Thread T1 = new Thread(N1);
        Thread T2 = new Thread(N2);
        Thread T3 = new Thread(N3);
        Thread T4 = new Thread(N4);
        Thread T5 = new Thread(N5);
        Thread T6 = new Thread(N6);
        Thread T7 = new Thread(N7);
        Thread T8 = new Thread(N8);
        Thread T9 = new Thread(N9);

        // start the nodes only have acceptors
        T4.start();
        T5.start();
        T6.start();
        T7.start();
        T8.start();
        T9.start();

        // give a little pause
        try{Thread.sleep(100);}
        catch(Exception e){}

        // start the nodes with proposers simultaneously
        T1.start();
        T2.start();
        T3.start();
    }

    public static void main(String[] args)
    {
        testCase();    
    }
}
