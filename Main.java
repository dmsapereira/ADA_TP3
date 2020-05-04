import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static MonkeyTrees readInput(BufferedReader in) throws IOException {
        MonkeyTrees mt;

        String[] line = in.readLine().split(" ");

        float maxDistance = Float.parseFloat(line[0]);

        int nTrees = Integer.parseInt(line[1]);

        mt = new MonkeyTrees(maxDistance, nTrees);

        for(int i = 0; i < nTrees; i++){
            line = in.readLine().split(" ");
            
        }

        return mt;
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int nProblems = Integer.parseInt(in.readLine());

        for(int i = 0; i < nProblems; i++){
            MonkeyTrees mt = readInput(in);
        }
    }

}