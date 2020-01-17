package leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * DESCRIPTION :
 *
 * @author tym
 * @ceeate 2020/1/16
 **/
public class MajorityElement {

    public int majorityElement(int[] nums) {

        Map<Integer,Integer> map = new HashMap<>();

        for(int i : nums) {
            int num = map.get(nums[i]) == null ?  0 : map.get(nums[i]);
            map.put(nums[i],num+1);
        }
        int max = 0;
        int returnkey = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int num = entry.getValue();
            if(num > max) {
                max = num;
                returnkey = entry.getKey();
            }
        }


        return returnkey;
    }

    public int majorityElement2(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length/2];
    }

    public int majorityElement3(int[] nums) {
        int count = 0;
        Integer candidate = null;

        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            count += (num == candidate) ? 1 : -1;
        }

        return candidate;
    }


}
