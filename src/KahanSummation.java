public class KahanSummation {
    public static void main(String[] args) {
        float sum = 0.0f;
        float c = 0.0f;
        for (int i = 0; i < 20000000; i++) {
            float x = 1.0f;
            float y = x - c;
            float t = sum + y;
            c = (t-sum)-y;
            sum = t;
        }
        System.out.println("sum is " + sum);
    }
}