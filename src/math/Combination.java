package math;

import java.util.Arrays;

/**
 * ${DESCRIPTION}
 *
 * @author tym
 * @ceeate 2019/6/14
 **/
public class Combination {
    /**
     * 求组合数
     *
     * @param n
     * @param r
     * @return
     */
    static int c(int n, int r) {
        if (r > n) {
            return 0;
        }
        int R = n - r;
        int ret = 1;
        while (n > R) {
            ret *= n--;
        }
        while (r > 1) {
            ret /= r--;
        }
        return ret;
    }

    /**
     * 求组合情况
     *
     * @param es
     * @param r
     * @param I  数组es开始取数位置
     * @return
     */
    static int[][] C(int[] es, int r, int I) {
        int[][] rst = new int[c(es.length - I, r)][];
        if (r == 1) {
            for (int rsti = 0; rsti < rst.length; rsti++, I++) {
                rst[rsti] = new int[]{es[I]};
            }
        } else {
            for (int rsti = 0; I < es.length; I++) {
                int[][] srst = C(es, r - 1, I + 1);
                for (int[] sc : srst) {
                    int[] t = rst[rsti] = new int[sc.length + 1];
                    t[0] = es[I];
                    System.arraycopy(sc, 0, t, 1, sc.length);
                    rsti++;
                }
            }
        }
        return rst;
    }

    public static void main(String[] args) {
        int[][] c = C(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 3, 0);
        for (int[] cc : c) {
            System.out.println(Arrays.toString(cc));
        }
    }
}