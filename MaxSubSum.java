import java.util.Random;

class MaxSubSum {
  private int[] x;
  public MaxSubSum(int nMax, long seed) {
    x = new int[nMax];
    Random rnd = new Random(seed);
    for (int i = 0; i < nMax; i++) {
      x[i] = -100 + rnd.nextInt(200);
    }
  }

  public int calc1(int n) {
    int maxS = 0;
    for (int l = 0; l < n; l++) {
      for (int r = l; r < n; r++) {
        int s = 0;
        for (int i = l; i <= r; i++) s+= x[i];
        if (s > maxS) maxS = s;
      }
    }
    return maxS;
  }

  public int calc2(int n) {
    int maxS = 0;
    for (int l = 0; l < n; l++) {
      int s = 0;
      for (int r = l; r < n; r++) {
        s += x[r];
        if (s > maxS) maxS = s;
      }
    }
    return maxS;
  }

  public int calc3(int n) {
    return calc3(0, n - 1);
  }

  public int calc4(int n) {
    int maxS = 0, maxI = 0;
    for (int i = 0; i < n; i++) {
      maxI = Math.max(maxI + x[i], 0);
      maxS = Math.max(maxS, maxI);
    }
    return maxS;
  }

  private int calc3(int l, int r) {
    if (l > r) return 0;
    if (l == r) return Math.max(x[l], 0);

    int m = (l + r) / 2;
    int maxS = Math.max(calc3(l, m), calc3(m + 1, r));

    int s, maxL, maxR;
    s = maxL = 0;
    for (int i = m; i >= l; i--) {
      s += x[i];
      if (s > maxL) maxL = s;
    }
    s = maxR = 0;
    for (int i = m + 1; i <= r; i++) {
      s += x[i];
      if (s > maxR) maxR = s;
    }
    s = maxL + maxR;
    if (s > maxS) maxS = s;
    return maxS;
  }

  public static void main(String[] args) {
    int nMax = 700000000;
    int nStep = 2000000;
    MaxSubSum m = new MaxSubSum(nMax, 666);
    long tStart, t1, t2, t3, t4;
    int n = nStep;
    t1=0;
    // while(t1<1){
      
    //   tStart = System.currentTimeMillis();
    //   m.calc1(n);
    //   t1 = (System.currentTimeMillis() - tStart)/1000;
    //   n+=nStep;
    // }
    // System.out.println(n+ " " + t1);
    // n = 0;
    // t2=0;
    // while(t2<1){
      
    //   tStart = System.currentTimeMillis();
    //   m.calc2(n);
    //   t2 = (System.currentTimeMillis() - tStart)/1000;
    //   n+=nStep;
    // }
    // System.out.println(n+ " " + t2);

    // n = 0;
    // t3=0;
    // while(t3<1){
      
    //   tStart = System.currentTimeMillis();
    //   m.calc3(n);
    //   t3 = (System.currentTimeMillis() - tStart)/1000;
    //   n+=nStep;
    // }
    // System.out.println(n+ " " + t3);

    n = 0;
    t4=0;
    while(t4<1){
      
      tStart = System.currentTimeMillis();
      m.calc4(n);
      t4 = (System.currentTimeMillis() - tStart)/1000;
      n+=nStep;
    }
    System.out.println(n+ " " + t4);

  }
}