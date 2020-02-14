package geekTime.algorithm;

/**
 * DESCRIPTION :
 *
 * @author tym
 * @ceeate 2020/1/20
 **/
public class MergeSort {

    public void mergeSort(int[] nums, int n) {
        mergeSortC(nums,0,n-1);
    }

    private void mergeSortC(int[] nums,int p,int r) {
        // 递归终止条件
        if(p >= r) {
            return;
        }
        // 取 p 到 r 之间的中间位置 q
        int q = (p+r) / 2;

        // 分治递归
        mergeSortC(nums, p, q);
        mergeSortC(nums, q+1, r);
        // 将 A[p...q] 和 A[q+1...r] 合并为 A[p...r]
        merge(nums, p, q, r);
    }

    private void merge(int[] a,int p, int q, int r) {
        int i =p;
        int j = q+1;
        int k =0;
        int[] tmp = new int[r-p+1];
        while (i<=q && j <=r) {
            if(a[i] <= a[j]) {
                tmp[k++] = a[i++];
            }else {
                tmp[k++] = a[j++];
            }
        }
        // 判断哪个子数组中有剩余的数据
        int start = i;
        int end = q;
        if(j <= r) {
            start = j;
            end = r;
        }

        if(start <= end) {
            tmp[k++] = a[start ++];
        }

        for(i=0; i< r-p; i++) {
            a[p+i] = tmp[i];
        }
    }


    public static void main(String[] args) {

    }
}
