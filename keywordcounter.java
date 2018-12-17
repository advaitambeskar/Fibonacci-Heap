import java.util.*;
import java.io.*;

public class keywordcounter{
    public static boolean sentenceIsInsertable(String line){
        /*
         * Function Name:       sentenceIsInsertable
         * Return Type:         boolean
         * Formal Parameters:   line - string
         * Description:         Accepts the variable 'line' and checks if it follows the pattern "$keyword value".
         *                      Returns true if pattern followed, else returns false.
         * 
         */
        if(line.charAt(0) == '$'){
            return true;
        }
        else return false;
    }
    public static boolean sentenceIsStop(String line){
        /*
         * Function Name:       sentenceIsStop
         * Return Type:         boolean
         * Formal Parameters:   line - string
         * Description:         Accepts the variable 'line' and checks if it follows the pattern "stop".
         *                      Returns true if pattern followed, else returns false.
         * 
         */
        if(line.equalsIgnoreCase("stop")){
            return true;
        }
        else return false;
    }

    public static void write(String outputString){
        /*
         * Function Name:       write
         * Return Type:         void
         * Formal Parameters:   String outputString
         * Description:         Accepts the 'outputString' that needs to be written to the output_file and then writes it.
         *                      File is created if not originally present
         * 
         */
        try{
        File f = new File("output_file.txt");
        if(!f.exists()){
            f.createNewFile();  //create new file if not present already
        }
        FileWriter file = new FileWriter(f.getName(), true);
        BufferedWriter out = new BufferedWriter(file);
        out.write(outputString);
        out.write("\n");
        out.close();
        }
        catch(Exception e){
            System.out.println("Error Occured");
        }
    }

    public static void main(String[] args){
        System.out.println("Program has begun");
        String fileName = args[0]; //accept the filename passed as the argument
        String line;
        String keyword;
        int node_value;
        int whiteSpaceIndex;

        /* 
         * We need to use hashmap instead of hashtable because hashmap allows for null values 
         * and we need support for null values
         */

        // create object for hashmap
        HashMap<String, Node> table = new HashMap<String, Node>();
        // create object of Heap so that we can start working on it
        Heap tree = new Heap(); 
        
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // use br for file-read operations

            while((line = br.readLine()) != null){
                // System.out.println(line);

                // Check the structure of the line(?)
                line = line.trim();

                if(sentenceIsInsertable(line)){ //if it matches pattern 1
                    // Extract the keyword and value and then add it to the hashtable. Pattern 1 is "$keyword value"
                    line = line.replace("$", "");
                    whiteSpaceIndex = line.indexOf(" ");
                    node_value = Integer.parseInt(line.substring(whiteSpaceIndex).trim());
                    keyword = line.substring(0, whiteSpaceIndex).trim();
                    keyword = keyword.toLowerCase();

                    // Check if the extracted keyword is already present in the hashtable
                    if(table.containsKey(keyword)){
                        // update the node value instead of increasing it
                        tree.increaseNodeValue(table.get(keyword), node_value);
                    }
                    else{
                        // insert the new node into the heap
                        Node node = new Node(node_value, keyword);
                        table.put(keyword, node);
                        tree.insertToHeap(node);
                        //System.out.println("Inserted Node is "+ node.keyword + " "+ node.value);                        
                    }
                    //System.out.println("Size is " +table.size());

                }
                else{
                    if(sentenceIsStop(line)){
                        // it is stop and end function. it follows the pattern "Stop"
                        //System.out.println("Third");
                        System.out.println("Program has ended!");
                        System.exit(0);
                    }
                    else{
                        String outputString = "";
                        // it is for printing n number of keywords
                        //System.out.println("second");
                        //System.out.println(line);
                        int noOfOutputNeeded = Integer.parseInt(line);
                        List<Node> removedNodes = new ArrayList<Node>(noOfOutputNeeded);
                        //System.out.println("Total number of nodes before display " + tree.totalNode);
                        if(noOfOutputNeeded == 1){
                            outputString = outputString + " " + tree.maxNodeReturn();
                        }
                        else{
                            for (int iteration = 0; iteration < noOfOutputNeeded; iteration++){
                                Node x = tree.removeMaxNode(); //x stores the removed maxNode
                                table.remove(x.keyword);
                                outputString = outputString + " " + x.keyword;
                                tree.updateMaxNode();
                                //System.out.println("The Current output String is "+ outputString);
                                Node y = new Node(x.value, x.keyword);
                                removedNodes.add(y);
                            }
                            for(Node e: removedNodes){
                                table.put(e.keyword,e);
                                tree.insertToHeap(e);
                                //System.out.println("Inserted Node " + e.keyword + " with value "+ e.value);
                            }
                        }
                        outputString = outputString.trim().replace(" ", ",");
                        //System.out.println("Total number of nodes after display "+ tree.totalNode);
                        write(outputString);
                    }
                }


            }
            //out.close();
            br.close();
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException e){
            System.out.println("Error reading file '"+ fileName + "'");
        }

        
    }
}