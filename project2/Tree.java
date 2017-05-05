
import java.util.Vector;

class Tree<T> {
    // from http://stackoverflow.com/questions/3522454/java-tree-data-structure
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new Vector<Node<T>>(); // changed from root.children = new ArrayList<Node<T>>();
        root.isRoot = true;
    }
    
    public void insert(Node<T> node){
        // inserts a node into the 
        Node<T> parent = node.parent;
        boolean nodeInserted = false;
        
        if(parent.isRoot){
            root.children.add(node);
        }
        else{
            for(int i = 0; i < root.children.size(); i++){
                if(root.children.get(i) == parent){
                    root.children.get(i).children.add(node);
                    nodeInserted = true;
                    break;
                }
            }
        }  
    }
    
    public Node<T> getRoot(){
        return root;
    }
    
    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private Vector<Node<T>> children;// changed from private List<Node<T>> children;
        private boolean isRoot;
        
        public Node(){
            data = null;
            parent = null;
            isRoot = false;
        }
        
        public Node(T nodeData, Node<T> nodeParent){
            data = nodeData;
            parent = nodeParent;
            isRoot = false;
        }
        
        public void setNodeData(T nodeData){
            this.data = nodeData;
        }
        
        public T getNodeData(){
            return data;
        }
        
        public void setNodeParent(Node<T> newParent){
            parent = newParent;
        }
        
        public Node<T> getNodeParent(){
            return parent;
        }
        
        public void addChild(Node<T> child){
            children.add(child);
        }
    }
    
   
}