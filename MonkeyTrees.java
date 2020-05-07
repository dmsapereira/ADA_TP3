import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MonkeyTrees {

    private static final int MAX_POS = 30001;

    private float maxDistance;
    private int nodesCounter, edgesCounter, nMonkeys;
    private Map<Integer, Integer> nodes;
    private int[][] positions;
    private List<Integer>[] edges; //JUMPIES
    private List<Integer>[] nodeEdges; //TREES

    public MonkeyTrees(float maxDistance, int nTrees) {
        this.maxDistance = maxDistance;
        this.nodesCounter = 1;
        this.edgesCounter = 0;
        this.nMonkeys = 0;
        this.nodes = new HashMap<>();
        this.nodes.put(-1, 0);
        this.edges = this.buildArray(4 * (int) Math.pow(nTrees,2));
        this.positions = new int[nTrees][2];
        this.nodeEdges = this.buildArray((2 * nTrees) + 1);

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

    private void newSource(int nodeIndex, int nMonkeys){
        int edgeIndex = this.edgesCounter++;
        List<Integer> list = this.edges[edgeIndex];

        list.add(0);
        list.add(nodeIndex);
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

    private void connectTrees(int nodeIndex1, int nodeIndex2){
        //LIGAR 1 A 2
        int edgeIndex = this.edgesCounter++;
        List<Integer> list = this.edges[edgeIndex];

        list.add(nodeIndex1 + 1);
        list.add(nodeIndex2);
        list.add(Integer.MAX_VALUE);

        this.nodeEdges[nodeIndex1 + 1].add(edgeIndex);

        this.creatOppositeArc(edgeIndex);

        //LIGAR 2 A 1
        edgeIndex = this.edgesCounter++;
        list = this.edges[edgeIndex];

        list.add(nodeIndex2 + 1);
        list.add(nodeIndex1);
        list.add(Integer.MAX_VALUE);

        this.nodeEdges[nodeIndex2 + 1].add(edgeIndex);
        
        this.creatOppositeArc(edgeIndex);
    }

    public void addTree(int x, int y, int nMonkeys, int durability){
        int treeIndex = (this.nodesCounter - 1) / 2;
        int nodeIndex = this. nodesCounter;

        //inc por 2 porque numeros pares sao nós de saida
        this.nodesCounter+=2;
        this.nMonkeys += nMonkeys;

        //Adicionar nó e adicionar posição de nó 
        this.nodes.put(MAX_POS * x + y, treeIndex);
        this.positions[treeIndex][0] = x;
        this.positions[treeIndex][1] = y;

        this.splitTree(nodeIndex, durability);

        //Nova ligação a fonte
        //Macacoides
        if(nMonkeys > 0)
            this.newSource(nodeIndex, nMonkeys);
        
        for(int dst = 0; dst < treeIndex; dst++){
            if(this.reachable(treeIndex, dst))
                this.connectTrees(nodeIndex, (dst * 2) + 1);
        }
    }

    private int edmondsKarp(int sinkIndex){
        //numNodes = nodesCounter
        int increment;
        
        int[][] flow = new int[this.nodesCounter][this.nodesCounter];

        int[] via = new int[this.nodesCounter];

        int flowValue = 0;

        while((increment = this.findPath(flow, 0, sinkIndex, via)) != 0){
            int node = sinkIndex;
            flowValue += increment;

            while(node != 0){
                int origin = via[node];
                flow[origin][node] += increment;
                flow[node][origin] -= increment;
                node = origin;
            }
        }

        return flowValue;

    }

    private int findPath(int[][] flow, int sourceIndex, int sinkIndex, int[] via){
        //numNodes = this.nodesCounter

        List<Integer> waiting = new LinkedList<>();
        
        boolean[] found = new boolean[this.nodesCounter];

        int[] pathIncr = new int[this.nodesCounter];

        //source is always 0
        waiting.add(sourceIndex);
        found[sourceIndex] = true;
        via[sourceIndex] = sourceIndex;
        pathIncr[sourceIndex] = Integer.MAX_VALUE;

        do{
            int origin = waiting.get(0);
            waiting.remove(0);

            for(int edgeIndex : this.nodeEdges[origin]){
                int dst = this.edges[edgeIndex].get(1);

                int residue = this.edges[edgeIndex].get(2) - flow[origin][dst];

                if(!found[dst] && residue > 0){
                    via[dst] = origin;
                    pathIncr[dst] = Math.min(pathIncr[origin], residue);

                    if(dst == sinkIndex)
                        return pathIncr[dst];

                    waiting.add(dst);
                    found[dst] = true;
                }
            }

        }while(!waiting.isEmpty());

        return 0;
    }

    public List<Integer> solve(){

        List<Integer> treeList = new LinkedList<>();

        //node exits are always on the even index
        for(int sinkIndex = 1 ; sinkIndex < this.nodesCounter; sinkIndex += 2){
            if(this.edmondsKarp(sinkIndex) == this.nMonkeys){
                treeList.add(sinkIndex / 2);
            }
        }

        return treeList;
    }
}