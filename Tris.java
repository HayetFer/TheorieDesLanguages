import java.util.Random;

public class Tris {
    public int[] x;

    public Tris(int nMax, long seed) {
        x = new int[nMax];
        Random rnd = new Random(seed);
        for (int i = 0; i < nMax; i++) {
            x[i] = -100 + rnd.nextInt(200);
        }
    }

    public void triInsertion() {
        int n = x.length;
        for (int i = 1; i < n; i++) {
            int key = x[i];
            int j = i - 1;

            while (j >= 0 && x[j] > key) {
                x[j + 1] = x[j];
                j = j - 1;
            }
            x[j + 1] = key;
        }
    }
    public void triFusion() {
        triFusion(x, 0, x.length - 1);
    }
    private void triFusion(int[] arr, int gauche, int droite) {
        if (gauche < droite) {
            int milieu = (gauche + droite) / 2;

            triFusion(arr, gauche, milieu);
            triFusion(arr, milieu + 1, droite);

            fusionner(arr, gauche, milieu, droite);
        }
    }

    private void fusionner(int[] arr, int gauche, int milieu, int droite) {
        int n1 = milieu - gauche + 1;
        int n2 = droite - milieu;

        int[] tableauGauche = new int[n1];
        int[] tableauDroite = new int[n2];

        for (int i = 0; i < n1; i++) {
            tableauGauche[i] = arr[gauche + i];
        }
        for (int j = 0; j < n2; j++) {
            tableauDroite[j] = arr[milieu + 1 + j];
        }

        int i = 0, j = 0;
        int k = gauche;
        
        while (i < n1 && j < n2) {
            if (tableauGauche[i] <= tableauDroite[j]) {
                arr[k] = tableauGauche[i];
                i++;
            } else {
                arr[k] = tableauDroite[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = tableauGauche[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = tableauDroite[j];
            j++;
            k++;
        }
    }


    public static void main(String[] args) {
        int nMax = 10;
        int nStep = 2000;
        Tris t = new Tris(nMax, 666);

        System.out.println("Avant le tri :");
        for (int i = 0; i < nMax; i++) {
            System.out.print(t.x[i] + " ");
        }

        t.triInsertion();

        System.out.println("\nAprès le tri :");
        for (int i = 0; i < nMax; i++) {
            System.out.print(t.x[i] + " ");
        }
        Tris t1 = new Tris(nMax, 666);

        System.out.println("Avant le tri :");
        for (int i = 0; i < nMax; i++) {
            System.out.print(t1.x[i] + " ");
        }

        t1.triFusion();

        System.out.println("\nAprès le tri :");
        for (int i = 0; i < nMax; i++) {
            System.out.print(t1.x[i] + " ");
        }
    }
}

