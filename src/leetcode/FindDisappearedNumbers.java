package leetcode;

import java.lang.reflect.Array;
import java.util.*;

/**
 * DESCRIPTION :
 *Given an array of integers where 1 ≤ a[i] ≤ n (n = size of array), some elements appear twice and others appear once.
 *
 * Find all the elements of [1, n] inclusive that do not appear in this array.
 *
 * Could you do it without extra space and in O(n) runtime? You may assume the returned list does not count as extra space.
 *
 * Example:
 *
 * Input:
 * [4,3,2,7,8,2,3,1]
 *
 * Output:
 * [5,6]
 *
 * @author tym
 * @ceeate 2020/1/20
 **/
public class FindDisappearedNumbers {

    public List<Integer> findDisappearedNumbers(int[] nums) {

        Set<Integer> set = new HashSet();
        if(nums.length > 0) {

            for (int i = 1; i <= nums.length; i++) {
                set.add(i);
            }
            for (int num : nums) {
                set.remove(num);
            }

            return new ArrayList<>(set);
        }else {
            return  new ArrayList<>();
        }
    }

    public void swap(int[] nums, int index1, int index2){
        if (index1 != index2){
            nums[index1] = nums[index1] ^ nums[index2];
            nums[index2] = nums[index1] ^ nums[index2];
            nums[index1] = nums[index1] ^ nums[index2];
        }
    }
    public List<Integer> findDisappearedNumbers2(int[] nums) {
        int l = nums.length;
        List<Integer> ret = new ArrayList<>();

        for (int i = 0; i < l; i++){
            while (nums[i] != nums[nums[i]-1]){
                swap(nums, i, nums[i]-1);
            }
        }

        for (int i = 0; i < l; i++){
            if (nums[i] != i+1){    // 如果鸽子不在正确的巢里
                ret.add(i+1);       // 这个巢本该有的鸽子就是缺失的数字
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{4,3,2,7,8,2,3,1};
        new FindDisappearedNumbers().findDisappearedNumbers2(nums);
    }
}
