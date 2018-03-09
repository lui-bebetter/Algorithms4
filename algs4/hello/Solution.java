package algs4.hello;
/**********************************************************************
 *FileName: Solution
 *Author:   luibebetter
 *Date:     2018/3/116:17
 *Description:leetcode template
 **********************************************************************/



import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/********************************
 *@author: luibebetter
 *@create: 2018/3/1
 *Description:
 ********************************/

public class Solution {
	public int NumberOf1(int n) {
		if(n<Integer.MIN_VALUE || n>Integer.MAX_VALUE) throw new IllegalArgumentException();
		final int MARK=1;
		final int BIT_FOR_INTEGER=32;
		int count=0;
		for (int i = 0; i < BIT_FOR_INTEGER; i++) {
			if((n>>i & MARK )==1)  count++;
		}
		return count;
	}


}

