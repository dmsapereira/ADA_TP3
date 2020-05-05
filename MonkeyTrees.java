import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MonkeyTrees {

    private static final int MAX_POS = 30001;

    private float maxDistance;
    private int nodesCounter, edgesCounter;
    private Map<Integer, Integer> nodes;
    private int[][] positions;
    private List<Integer>[] edges; //JUMPIES
    private List<Integer>[] nodeEdges; //TREES

    public MonkeyTrees(float maxDistance, int nTrees) {
        this.maxDistance = maxDistance;
        this.nodesCounter = 1;
        this.edgesCounter = 0;
        this.nodes = new HashMap<>();
        this.nodes.put(-1, 0);
        this.edges = this.buildArray(nTrees * nTrees);
        this.positions = new int[nTrees][2];
        this.nodeEdges = this.buildArray(2 * nTrees);

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

    private void creatOppositeArc(int edgeIndex){
        this.edgesCounter++;
        List<Integer> arc = this.edges[edgeIndex];
        List<Integer> reverseArc = this.edges[edgeIndex + 1];

        //TIME LIMIT EXCEEDED ALERT
        reverseArc.add(arc.get(1));
        reverseArc.add(arc.get(0));
        reverseArc.add(0);

        this.nodeEdges[arc.get(1)].add(edgeIndex + 1);
    }

    private void newSource(int treeIndex, int nMonkeys){
        int edgeIndex = this.edgesCounter++;
        List<Integer> list = this.edges[edgeIndex];

        list.add(0);
        list.add(treeIndex);
        list.add(nMonkeys);

        this.nodeEdges[0].add(edgeIndex);
        
        this.creatOppositeArc(edgeIndex);
    }

    private void splitTree(int treeIndex, int durability){
        int edgeIndex = this.edgesCounter++;//arco que liga entrada a saida

        List<Integer> list = this.edges[edgeIndex]; //lista de sucessores da entrada

        //info do arco
        list.add(treeIndex);
        list.add(treeIndex + 1);
        list.add(durability);

        this.nodeEdges[treeIndex].add(edgeIndex);

        this.creatOppositeArc(edgeIndex);
    }

    private void connectTrees(int treeIndex1, int treeIndex2){
        //LIGAR 1 A 2
        int edgeIndex = this.edgesCounter++;
        List<Integer> list = this.edges[edgeIndex];

        list.add(treeIndex1 + 1);
        list.add(treeIndex2);
        list.add(Integer.MAX_VALUE);

        this.nodeEdges[treeIndex1 + 1].add(edgeIndex);

        this.creatOppositeArc(edgeIndex);

        //LIGAR 2 A 1
        edgeIndex = this.edgesCounter++;
        list = this.edges[edgeIndex];

        list.add(treeIndex2 + 1);
        list.add(treeIndex1);
        list.add(Integer.MAX_VALUE);

        this.nodeEdges[treeIndex2 + 1].add(edgeIndex);
        
        this.creatOppositeArc(edgeIndex);
    }

    public void addTree(int x, int y, int nMonkeys, int durability){
        int treeIndex = this.nodesCounter;

        //inc por 2 porque numeros pares sao nós de saida
        this.nodesCounter+=2;

        //Adicionar nó e adicionar posição de nó 
        this.nodes.put(MAX_POS * x + y, treeIndex);
        this.positions[treeIndex][0] = x;
        this.positions[treeIndex][1] = y;

        this.splitTree(treeIndex, durability);

        //Nova ligação a fonte
        //Macacoides
        if(nMonkeys > 0)
            this.newSource(treeIndex, nMonkeys);
        
        for(int dst = 0; dst < this.nodesCounter - 1; dst++){
            if(this.reachable(treeIndex, dst))
                this.connectTrees(treeIndex, dst);
        }
    }
}