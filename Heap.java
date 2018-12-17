import java.util.*;
import java.io.*;
import java.io.StringWriter;

public class Heap{
    Node maxNode;
    int totalNode;


    public void insertToHeap(Node node_to_insert){
        /*
         * Function Name:       insertToHeap  
         * Return Type:         void
         * Formal Parameters:   Node node_to_insert
         * Description:         inserts the node 'node_to_insert' into the preexisting heap
         * 
         * 
         */
        if(maxNode == null){
            //node_to_insert is the first insertion into the heap
            maxNode = node_to_insert;
        }
        else{
            //node_to_insert is not the first node in the heap
            // insert such that maxNode is the left sibling and the right sibling of the maxNode is now the right sibling of the inserted node
            node_to_insert.leftSibling = maxNode;
            node_to_insert.rightSibling = maxNode.rightSibling;
            maxNode.rightSibling = node_to_insert;
            // make the top-level-list cyclic
            if(node_to_insert.rightSibling != null){
                node_to_insert.rightSibling.leftSibling = node_to_insert;
            }
            else{
                node_to_insert.rightSibling = maxNode;
                maxNode.leftSibling = node_to_insert;
            }
            // update the value of the node if necessary
            if(node_to_insert.value > maxNode.value){
                maxNode = node_to_insert;
            }
        }
        //increase the count of totalNodes
        totalNode = totalNode + 1;
    }

    public void increaseNodeValue(Node currentNode, int addValue){
        /*
         * Function Name:       increaseNodeValue  
         * Return Type:         void
         * Formal Parameters:   Node currentNode, int addValue
         * Description:         increases the value of the node 'currentNode' by addValue
         * 
         * 
         */
        Node parentNode = currentNode.parent;
        currentNode.value = currentNode.value + addValue;

        if(parentNode!= null){
            // checks if the value of currentNode after increase is more than value of parentNode
            if(currentNode.value > parentNode.value){
                /* rectifies the discrepency in the tree due to child node being greater than parent node
                 * by cutting the child and adding it to the top-level
                 */
                cutChild(currentNode, parentNode);
                // performs cascadingCut to allow the node childCut values to be rectified
                cascadingCut(parentNode);
            }
        }
        //updates the value of the maxNode
        if(currentNode.value > maxNode.value){
            maxNode = currentNode;
        }
    }

    public void cutChild(Node currentNode, Node parentNode){
        /*
         * Function Name:       cutChild  
         * Return Type:         void
         * Formal Parameters:   Node currentNode, Node parentNode
         * Description:         removes the currentNode from the parentNode's child and adds it to the top-level
         * 
         * 
         */
        // reduce the degree of the parent and adjust the siblings of the currentNode to make sure sibling cyclic list is maintained after removal
        parentNode.degree --;
        currentNode.leftSibling.rightSibling = currentNode.rightSibling;
        currentNode.rightSibling.leftSibling = currentNode.leftSibling;
        // add the currentNode as the right sibling of maxNode
        currentNode.leftSibling = maxNode;
        currentNode.rightSibling = maxNode.rightSibling;
        maxNode.rightSibling = currentNode;
        currentNode.rightSibling.leftSibling = currentNode;
        // parent will have null in childNode if degree is zero after removing currentNode
        if(parentNode.degree == 0){
            parentNode.childNode = null;
        }
        //update the parentNode's childNode field if the currentNode had a right sibling
        if(parentNode.childNode == currentNode){
            parentNode.childNode = currentNode.rightSibling;
        }
        //make currentNode a top-level node and make its childCut to be false;
        currentNode.parent = null;
        currentNode.childCut = false;
    }

    public void cascadingCut(Node currentNode){
        /*
         * Function Name:       cascadingCut  
         * Return Type:         void
         * Formal Parameters:   Node currentNode
         * Description:         performs cascading cut on currentNode and subsequent parentnodes till required
         * 
         * 
         */
        Node parentNode = currentNode.parent;
        //currentNode needs to have childCut to be true on removal
        if(parentNode != null && (!currentNode.childCut)){
            currentNode.childCut = true;
        }
        else if(parentNode!=null && (currentNode.childCut)){
            // essentialy causes a recursive childCut and cascadingCut action so that the heap rules are maintained
            // (no parent can lose two children without being sent to the top-level)
            cutChild(currentNode, parentNode);
            cascadingCut(parentNode);
        }
        else{}
    }

    public Node removeMaxNode(){
        /*
         * Function Name:       removeMaxnNode  
         * Return Type:         void
         * Formal Parameters:   -
         * Description:         removes the maxNode, updates the maxNode and merges the trees with equal degrees
         * 
         * 
         */
        //System.out.println("Node to be removed is " + maxNode.keyword + maxNode.value);
        Node temp = maxNode;
        int i;
        if(maxNode == null){
            //no element in the tree
            return null;
        }else{
            int numberOfChildren = temp.degree;
            Node children = temp.childNode;
            Node secondTemp;

            for(i = numberOfChildren; i > 0; i--){
                //adjusts the sibling pointers of the top-level list to reflect the cyclic list after maxNode is removed
                secondTemp = children.rightSibling;

                children.rightSibling.leftSibling = children.leftSibling;
                children.leftSibling.rightSibling = children.rightSibling;

                children.rightSibling = maxNode.rightSibling;
                children.leftSibling = maxNode;
                maxNode.rightSibling = children;
                children.rightSibling.leftSibling = children;

                children.parent = null;
                children = secondTemp;
            }
            temp.rightSibling.leftSibling = temp.leftSibling;
            temp.leftSibling.rightSibling = temp.rightSibling;
            // perform pair wise combine
            if(temp != temp.rightSibling){
                maxNode = temp.rightSibling;
                //pairwiseCombine(); 
            }
            else{
                maxNode = null;
            }
            totalNode = totalNode -1;
            updateMaxNode();
            //System.out.println("Max node is "+ maxNode.keyword);
            //returned the removed node for safekeeping
            //System.out.println("Removed node is " + temp.keyword);
            return temp;
        }
    }

    public void updateMaxNode(){
        /*
         * Function Name:       updateMaxNode  
         * Return Type:         void
         * Formal Parameters:   -
         * Description:         updates the maxNode pointer to hold the maximum of all the siblings in the top-level
         * 
         * 
         */
        Node temp = maxNode;
        Node temp2 = maxNode;
        if((temp != null)){
            temp = temp.rightSibling;
            //System.out.println(totalNode);
            while(temp != maxNode){
                // essentially goes through the sibling list for top level, 
                // checks if temp2.value is greater than all the other elements in the top-level
                if(temp2.value > temp.value){
                    temp = temp.rightSibling;
                }
                else{
                    // updates the value of temp2 to the value corresponding to the highest value in the sibling list
                    // of the top level
                    temp2 = temp;
                    temp = temp.rightSibling;
                }
            }
        }
        //maxNode is temp2 as it has the highest value in the sibling list of the top-level
        maxNode = temp2;
    }

    public void pairwiseCombine(){
        
        int sizeofDegreeTable = 15; // select the arbit size 


        List<Node> mergeTable = new ArrayList<Node>(sizeofDegreeTable);

        // Initialize the table which will hold the degrees of the trees to be merged
        for (int i = 0; i < sizeofDegreeTable; i++) {
            mergeTable.add(null);
        }
                       


        // Find the number of top-level nodes.
        int numRoots = 0;
        Node x = maxNode;


        if (x != null) {
            numRoots++;
            x = x.rightSibling;                     

            while (x != maxNode) {
                numRoots++;
                x = x.rightSibling;
            }
        }

        // For node in top-level list 
        while (numRoots > 0) {

            int d = x.degree;
            Node next = x.rightSibling;

            // check if the degree is there in degree table, if not add,if yes then combine and merge
            for (;;){ // essentially continue till break
                Node y = mergeTable.get(d);
                if (y == null) {
                    break;
                }

                //Check which node.value is greater, that becomes the root node
                if (x.value < y.value) {
                    Node temp = y;
                    y = x;
                    x = temp;
                }

                //make y the child of x as x key value is greater
                //makeChild(y, x);
                y.leftSibling.rightSibling = y.rightSibling;
                y.rightSibling.leftSibling = y.leftSibling;

                // make y a child of x
                y.parent = x;

                if (x.childNode == null) {
                    x.childNode = y;
                    y.rightSibling = y;
                    y.leftSibling = y;
                } else {
                    y.leftSibling = x.childNode;
                    y.rightSibling = x.childNode.rightSibling;
                    x.childNode.rightSibling = y;
                    y.rightSibling.leftSibling = y;
                }

                // increase degree of x by 1
                x.degree++;

                // make mark of y as false
                y.childCut = false;

                //setthe degree to null as x and y are combined now
                mergeTable.set(d, null);
                d++;
            }

            //store the new x(x+y) in the respective degree table postion
            mergeTable.set(d, x);

            // Move forward through list of degree tables.
            x = next;
            numRoots--;
        }



        
        //delete the max node
        maxNode = null;

        // combine entries of the degree table
        for (int i = 0; i < sizeofDegreeTable; i++) {
            Node y = mergeTable.get(i);
            if (y == null) {
                continue;
            }

            //till max node is not null
            if (maxNode != null) {

                // First remove node from root list.
                y.leftSibling.rightSibling = y.rightSibling;
                y.rightSibling.leftSibling = y.leftSibling;

                // Now add to root list, again.
                y.leftSibling = maxNode;
                y.rightSibling = maxNode.rightSibling;
                maxNode.rightSibling = y;
                y.rightSibling.leftSibling = y;

                // Check if this is a new maximum
                if (y.value > maxNode.value) {
                    maxNode = y;
                }
            } else {
                maxNode = y;
            }
        }
    }
    public String maxNodeReturn(){
        return maxNode.keyword;
    }
}