import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class TrisComparison {
    public static void main(String[] args) {
        int nMin = 500;
        int nMax = 100000;
        int nStep = 2000;
        long seed = 666;

        try {
            FileWriter csvWriter = new FileWriter("sorting_times.csv");
            csvWriter.append("ArraySize,InsertionSortTime,FusionSortTime,JavaSortTime\n");

            for (int n = nMin; n <= nMax; n += nStep) {
                Tris t = new Tris(n, seed);
                Tris t1 = new Tris(n, seed);
                int[] copy = Arrays.copyOf(t.x, n);

                long startTime = System.currentTimeMillis();
                t.triInsertion();
                long endTime = System.currentTimeMillis();
                long insertionTime = endTime - startTime;

                startTime = System.currentTimeMillis();
                t1.triFusion();
                endTime = System.currentTimeMillis();
                long fusionTime = endTime - startTime;

                startTime = System.currentTimeMillis();
                Arrays.sort(copy);
                endTime = System.currentTimeMillis();
                long javaSortTime = endTime - startTime;

                csvWriter.append(n + "," + insertionTime + "," + fusionTime + "," + javaSortTime + "\n");
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
