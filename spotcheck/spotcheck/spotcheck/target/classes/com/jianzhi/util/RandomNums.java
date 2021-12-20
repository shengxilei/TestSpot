package com.jianzhi.util;

import java.util.Random;

public class RandomNums {
	
	public static int[] getNnms(int size,int count) {
		Random random=new Random();
		int[ ]  redBall = new int[size];//几个球

		for(int i = 0;i<redBall.length;i++){

		  redBall[i] = i+1;//给球标上序号

		}

		int[ ]  redNumber = new int[count];     //存储六个随机数的实际数组，取几个球
		//System.out.println("count:"+count);
		int index = -1;                             //通过随机数字数组下标获取随机数
		//System.out.println("redNumber:"+redNumber.length);
		for(int i = 0;i<redNumber.length;i++){
			//System.out.println("i:"+i);
			System.out.println("长度："+(redBall.length-i));
		   index = random.nextInt(redBall.length-i);   //每次获取数字数组长度-i的随机数，比如第一次循环为33第二次为32，
		   System.out.println("抽中index:"+index);
			redNumber[i] = redBall[index];                   //把数字数组随机下标的值赋给实际数组

			int temp = redBall[index];                           //定义一个变量暂存下标为index时的值

			redBall[index] = redBall[redBall.length-1-i];  //把下标为index的值与数组下标最后的值交换

			redBall[redBall.length-1-i] = temp;             //交换后，下次循环把数字数组最后的值去掉，从而实现不重复

		}
		return redNumber;
	}
	
	

}
