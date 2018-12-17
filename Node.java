public class Node{
    /*
     *
     * Class Node --> nodes for each 'element' of the fibonacci series
     * A node in the Fibonacci Heap contains following fields
     *      degree: count of children
     *      leftSibling : the pointer to the sibling on the left of the node
     *      rightSibling: the pointer to the sibling on the right of the node
     *      parent      : the pointer to the parent of the node
     *      childNode   : the pointer to the left-most child of the node
     *      value       : the integer value (frequency of occurance)
     *      childCut    : the boolean value suggesting if the node has lost a child
     *      keyword     : copy of the keyword associated with the node
     * 
     */
    int degree;
    Node leftSibling, parent, rightSibling, childNode;
    String keyword;
    int value;
    boolean childCut;
    Node(int value, String keyword){
        this.leftSibling = this;
        this.parent = null;
        this.rightSibling = this;
        this.degree = 0;
        this.keyword = keyword;
        this.value = value;
        this.childCut = false;
    }

}