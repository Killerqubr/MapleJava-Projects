package utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;

public class _3n_plus_1_method {

	//f1() 3n+1 basic
	public static void f1(int num) {
		int times = 0;
		while (num != 1) {
			if (num%2 == 0) {
				num = num >> 1;
			} else {
				num *= 3;num++;
			}
			times++;
		}
		System.out.println("times = " + times);
	}
    public static void Basic() {
        while (true) { 
            try {
            	f1(method.intInput("num = "));
	        	break;
            } catch (InputMismatchException e) {
	        	System.out.println("Errow!");
            }
        }
    }

	//f2() 3n+1 pro
	public static void f2(int num) {
		int times = 0;
		double average = 0;
		ArrayList<Integer> path = new ArrayList<>();
		path.add(num);
		while (num != 1) {
			if (num%2 == 0) {
				num = num >> 1;
			} else {
				num *= 3;num++;
			}
			path.add(num);
			times++;
		}
		for (int useNum : path) {
            average += useNum;
		}
		average = average / path.size();
		int max = Collections.max(path);
		System.out.println("times = " + times);
		System.out.println("path = " + path);
		System.out.println("average = " + average);
		System.out.println("max = " + max);
	}
    public static void Pro() {
        while (true) { 
            try {
            	f2(method.intInput("num = "));
	        	break;
            } catch (InputMismatchException e) {
	        	System.out.println("Errow!");
            }
        }
    }

	//f3() 3n+1 max
	public static void f3(int num1,int num2) {
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<Integer> timespath = new ArrayList<>();
		ArrayList<Integer> maxpath = new ArrayList<>();
		ArrayList<Double> averagepath = new ArrayList<>();
        while (num1 <= num2) {
			path.clear();
			int times = 0;
			double average = 0;
			int num = num1;
		    while (num != 1) {
				path.add(num);
		    	if (num%2 == 0) {
		    		num = num >> 1;
		    	} else {
		    		num *= 3;num++;
		    	}
		    	times++;
	    	}
			path.add(1);
			num1++;
	    	for (int useNum : path) {
                average += useNum;
		    }
	    	average = average / path.size();
		    maxpath.add(Collections.max(path));
	    	timespath.add(times);
		    averagepath.add(average);
		}
		if (timespath.size() <= 80){
		System.out.println("-----------------------------------------------------");
		System.out.println("timespath = " + timespath);
		System.out.println("And the max times in it is " + Collections.max(timespath));
		method.msSleep(2000);
		System.out.println("-----------------------------------------------------");
		System.out.println("maxpath = " + maxpath);
		System.out.println("And the max number in it is " + Collections.max(maxpath));
		method.msSleep(2000);
		System.out.println("-----------------------------------------------------");
		System.out.println("averagepath = " + averagepath);
		System.out.println("And the max average in it is " + Collections.max(averagepath));
		System.out.println("And the min average in it is " + Collections.min(averagepath));
		}else{
		System.out.println("-----------------------------------------------------");
		System.out.println("The timespath is way too long");
		System.out.println("And the max times in it is " + Collections.max(timespath));
		method.msSleep(2000);
		System.out.println("-----------------------------------------------------");
		System.out.println("The maxpath is way too long");
		System.out.println("And the max number in it is " + Collections.max(maxpath));
		method.msSleep(2000);
		System.out.println("-----------------------------------------------------");
		System.out.println("The averagepath is way too long");
		System.out.println("And the max average in it is " + Collections.max(averagepath));
		System.out.println("And the min average in it is " + Collections.min(averagepath));
		}
	}
	public static void Max(){
        while (true) { 
            try {
                System.out.println("It will compute from num1 to num2");
				int num1 = method.intInput("num1 = ");
				int num2 = method.intInput("num2 = ");
				if (num1 >= num2){
					System.out.println("Errow!");
					continue;
				}
            	f3(num1,num2);
	        	break;
            } catch (InputMismatchException e) {
	        	System.out.println("Errow!");
            }
        }
	}
}
