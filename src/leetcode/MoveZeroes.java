package leetcode;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;

/**
 * DESCRIPTION :
 * Given an array nums, write a function to move all 0's to the end of it while maintaining the relative order of the non-zero elements.
 * <p>
 * Example:
 * <p>
 * Input: [0,1,0,3,12]
 * Output: [1,3,12,0,0]
 * Note:
 * <p>
 * You must do this in-place without making a copy of the array.
 * Minimize the total number of operations.
 *
 * @author tym
 * @ceeate 2020/1/17
 **/
public class MoveZeroes {

    public void moveZeroes(int[] nums) {

        int index=0;

        for (int i = 0; i < nums.length; i++) {
            if( nums[i] != 0) {
                nums[index] = nums[i];
                index++;
            }
        }
       while (index < nums.length) {
           nums[index] = 0;
           index++;
       }

    }

    public static void main(String[] args) {
        int[] nums = new int[]{0, 1, 0, 3, 12};
        new MoveZeroes().moveZeroes(nums);
        System.out.println(Arrays.toString(nums));
    }
}
