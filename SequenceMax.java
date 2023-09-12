import java.util.Random;

public class SequenceMax {
    
    public static int somme (int tab[]){
        /*int max = 0;
        for(int i = 0; i<tab.length ; i++ ){
            int s = 0;
            for(int j = 0; j<tab.length; j++){
                s+=tab[j];
                if(s>max){
                    max=s;
                }
            }
            
        }*/
         
        /* ----------------TRI N2
        int max = 0; int somme = 0; 
        for(int i = 0 ; i<tab.length ; i++){
            somme+= tab[i];
                    if(somme<0){
            somme = 0;
        }
        if(somme>max){
            max=somme;
        } 
        }*/


        return max;
    }
    public static void main(String[] args){
        //int[] tab= new int[4];
        System.out.println("*TAB*");
        /*for(int i = 0 ; i<4; i++){
            Random rand = new Random();
            tab[i] = rand.nextInt()%30;
        }*/
        int[] tab= new int[]{31,-41,59,26,-53,58,97,-93,-23,84};
         for(int i = 0 ; i<4; i++){
            System.out.println(tab[i] + " ");

        }
        System.out.println("\n");
        System.out.println("Plus grande somme " + somme(tab));
	}
}
