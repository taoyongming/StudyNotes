package leetcode;

/**
 * HammingDistance
 *
 * @author tym
 * @ceeate 2020/1/9
 **/
public class HammingDistance {

        public int hammingDistance(int x, int y) {
            int i = x^y;
            int count = 0;
            while (i != 0 ) {

                if((i & 1) == 1) {
                    count++;
                }
                i= i>>1;
            }
            return count;
    }

    public int hammingDistance2(int x, int y) {
        return Integer.bitCount(x ^ y);
    }



    public static void main(String[] args) {
        new HammingDistance().hammingDistance(2,5);
    }
}
