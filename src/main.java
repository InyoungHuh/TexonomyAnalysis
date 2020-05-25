import sun.misc.IOUtils;

import java.io.*;
import java.util.*;

/**
 * The texonomy program implements an application that
 * show lowest common ancestor for two given nodes name.
 *
 * @author  InyoungHuh
 * @since   2020-05-24
 */

public class main {

    private static HashMap<Integer, List<Integer>> tree = new HashMap<>();
    private static HashMap<String, Integer> nameToId = new HashMap<>();
    private static HashMap<Integer,List<String>> idToName = new HashMap<>();

    private static final String nodePath = "resource/nodes.dmp";
    private static final String namePath = "resource/names.dmp";

    public static int lowestCommonAncestor(int parent, HashMap<Integer, List<Integer>> tree, int node1, int node2) {
        if(parent == node1 || parent == node2) return parent;
        //if node does not have any child node, then it is leaf -> return -1
        if( tree.get(parent) == null ) return -1;

        List<Integer> nodelist = new ArrayList<>();
        for(int child : tree.get(parent)) {
            if(child == parent) continue;
            int nodeId = lowestCommonAncestor(child, tree, node1, node2);
            if(nodeId != -1) nodelist.add(nodeId);
        }
        if(nodelist.size()==1)
            return nodelist.get(0);
        else if(nodelist.size()==2)
            return parent;  // When both node1 and node2 are children nodes under parent
        else
            return -1;  //Any children nodes under parent do not have node1 and node2
    }
    public static void loadData(){

        InputStream stream = (new main()).getClass().getResourceAsStream(nodePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            System.out.println("Loading node data...");
            while (reader.ready()) {
                String line = reader.readLine();
                String[] words = line.split("\t");
                int parent = Integer.parseInt(words[2]);
                int child = Integer.parseInt(words[0]);
                if(!tree.containsKey(parent))
                    tree.put(parent, new ArrayList<>());
                tree.get(parent).add(child);
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

        String node1, node2;
        loadData();
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
                if(!nameToId.containsKey(node1))
                    System.out.println("Cannot find the node name... Please try again");
                else break;
            }
        }
        int nodeId1 = nameToId.get(node1);
        int nodeId2 = nameToId.get(node2);

        int ancestor = lowestCommonAncestor(1, tree, nodeId1, nodeId2);
        String ancestorName = ancestor == 1 ? "Root" : idToName.get(ancestor).get(0);
        System.out.println("Lowest Common Ancestor : " + ancestorName);
    }
}
