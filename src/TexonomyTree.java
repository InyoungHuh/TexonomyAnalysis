import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Tree {
    class Node {
        int id;
        String name;
        List<Node> children;

        Node(int id, String name) {
            this.id = id;
            this.name = name;
            children = new ArrayList<>();
        }
    }

    Node root;
    Tree(){
        root = new Node(1, "root");
    }
    void add(HashMap<Integer, List<Integer>> map, HashMap<Integer, List<String>> idToName){
        root = addRecursive(root, map, idToName,1);
    }
    Node addRecursive(Node node, HashMap<Integer, List<Integer>> map, HashMap<Integer, List<String>> idToName, int parent) {
        List<Integer> children = map.get(parent);
        if (children == null) return node;
        for (int child : children) {
            if (child == parent) continue;
            Node newNode = new Node(child, idToName.get(child).get(0));
            newNode = addRecursive(newNode, map, idToName, child);
            node.children.add(newNode);
        }
        return node;
    }
    String findLowestCommonAncestor(int node1, int node2){
        Node node = lowestCommonAncestor(root, node1, node2);
        return node.name;
    }
    public Node lowestCommonAncestor(Node root, int node1, int node2){
        if( root.id == node1 || root.id == node2 || root == null)
            return root;
        if(root.children == null) return null;

        List<Node> nodelist = new ArrayList<>();
        for(Node child : root.children) {
            if(child.id == root.id) continue;
            Node n = lowestCommonAncestor(child, node1, node2);
            if(n != null) nodelist.add(n);
        }
        if(nodelist.size()==1)
            return nodelist.get(0);
        else if(nodelist.size()==2)
            return root;  // When both node1 and node2 are children nodes under parent
        else
            return null;
    }
}
public class TexonomyTree {


    private static HashMap<Integer, List<Integer>> parentMap = new HashMap<>();
    private static HashMap<String, Integer> nameToId = new HashMap<>();
    private static HashMap<Integer,List<String>> idToName = new HashMap<>();

    private static final String nodePath = "resource/nodes.dmp";
    private static final String namePath = "resource/names.dmp";

    public static void loadData(){

        InputStream stream = (new main()).getClass().getResourceAsStream(nodePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            System.out.println("Loading node data...");
            while (reader.ready()) {
                String line = reader.readLine();
                String[] words = line.split("\t");
                int parent = Integer.parseInt(words[2]);
                int child = Integer.parseInt(words[0]);
                if(!parentMap.containsKey(parent))
                    parentMap.put(parent, new ArrayList<>());
                parentMap.get(parent).add(child);
            }
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Load success");

        stream = (new main()).getClass().getResourceAsStream(namePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            System.out.println("Loading name data...");
            while (reader.ready()) {
                String line = reader.readLine();
                String[] words = line.split("\t");
                String name = words[2].toLowerCase();
                int id = Integer.parseInt(words[0]);
                //System.out.println(line);
                nameToId.put(name, id);
                if(!idToName.containsKey(id))
                    idToName.put(id, new ArrayList<>());
                idToName.get(id).add(name);
            }
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Load success");
    }

    public static void main(String[] args){

        //Load data from dmp files
        loadData();
        //build tree based on data
        Tree tree = new Tree();
        tree.add(parentMap, idToName);

        String node1, node2;
        try(Scanner scanner = new Scanner(System.in)){
            while(true){
                System.out.print("Please enter first node name : ");
                node1 = scanner.nextLine().toLowerCase();
                if(!nameToId.containsKey(node1))
                    System.out.println("Cannot find the node name... Please try again");
                else break;
            }
            while(true){
                System.out.print("Please enter second node name : ");
                node2 = scanner.nextLine().toLowerCase();
                if(!nameToId.containsKey(node2))
                    System.out.println("Cannot find the node name... Please try again");
                else break;
            }
        }
        int nodeId1 = nameToId.get(node1);
        int nodeId2 = nameToId.get(node2);
        System.out.println("Lowest Common Ancestor : " + tree.findLowestCommonAncestor(nodeId1, nodeId2));
    }
}
