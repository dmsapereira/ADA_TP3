import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MonkeyTrees {

    private static final int MAX_POS = 30001;

    private float maxDistance;
    private int nTrees, nodesCounter, edgesCounter;
    private Map<Integer, Integer> nodes;
    private int[][] positions;
    private List<Integer>[] edges; //JUMPIES
    private List<Integer>[] nodeEdges; //TREES

    public MonkeyTrees(float maxDistance, int nTrees) {
        this.maxDistance = maxDistance;
        this.nTrees = nTrees;
        this.nodesCounter = 0;
        this.edgesCounter = 0;
        this.nodes = new HashMap<>();
        this.edges = this.buildArray((nTrees - 1) * 2);
        this.positions = new int[nTrees][2];
        this.nodeEdges = this.buildArray(2); //TODO

    }

    @SuppressWarnings("unchecked")
    private List<Integer>[] buildArray(int nLines){
        List<Integer>[] list = new List[nLines];

        for(int i = 0; i < list.length; i++)
            list[i] = new LinkedList<>();

        return list;
    }

    private boolean reachable(int index1, int index2){
        int x1 = this.positions[index1][0];
        int y1 = this.positions[index1][1];
        int x2 = this.positions[index2][0];
        int y2 = this.positions[index2][1];

        float distance = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        return distance < this.maxDistance;
    }

    public void addTree(int x, int y, int nMonkeys, int durability){
        int treeIndex = this.nodesCounter++;

        this.nodes.put(MAX_POS * x + y, treeIndex);
        this.positions[treeIndex][0] = x;
        this.positions[treeIndex][1] = y;


        for(int dst = 0; dst < this.nodesCounter - 1; dst++){
            if(this.reachable(treeIndex, dst)){
                List<Integer> list = this.edges[this.edgesCounter++];
                list.add(treeIndex);
                list.add(dst);
                list.add(nMonkeys);
                list.add(durability);

                //ENTAO E A BIFURCAÇÃO DOS NÓS? FODEU


            }
        }
    }
}