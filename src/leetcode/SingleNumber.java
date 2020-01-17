package leetcode;

import java.util.*;

/**
 * SingleNumber :
 *
 * @author tym
 * @ceeate 2020/1/15
 **/
public class SingleNumber {

    public int singleNumber(int[] nums) {
        Map<Integer,Integer> map = new HashMap<>();

        for(int i=0; i<nums.length; i++) {
               if( map.containsKey(nums[i])) {
                   map.remove(nums[i]);
               }else {
                   map.put(nums[i],i);
               }

        }
        return (int)map.keySet().toArray()[0];
    }
}
