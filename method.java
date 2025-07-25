/**Some useful simple code
 * @author flizc
 * @version 1.0 forever
 * Hope it would help you */ 

package method;

import java.util.Scanner;

public class method{
	//input()    //str   //I miss the input() in python
	public static String strInput(String str) {
	System.out.print(str);
	Scanner scanner = new Scanner(System.in);
	String input = scanner.nextLine();
	return input;
	}

	//input()    //int   //I miss the input() in python
	public static int intInput(String str) {
	System.out.print(str);
	Scanner scanner = new Scanner(System.in);
	int input = scanner.nextInt();
	return input;
	}

	//time.sleep()   //I miss the sleep() in python
	public static void msSleep(int time) {
        try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	//random()   //int    //I miss the random() in python
	//make sure the end is greater than the start or the start will appear again and again
	public static int intRandom(int start,int end){
		int result = (int)((end - start + 1)*Math.random())+start;
        return result;
	}
}

/*#list   
 * import java.util.ArrayList; 
 * ArraryList<#包装类> lit = new ArraryList<>();
 * #基本型与包装类
 * int   Integer
 * doubt   Doubt
 * char   Character
 * byte   Byte
 * boolean   Boolean
*/

/*#访问修饰符
 * private    同类
 * 默认       同类 同包
 * protected  同类 同包 子类
 * public     同类 同包 子类 不同包
 */

/*#max() min()
 * import java.util.Collections;
 * int num = Collections.max(ArraryList<Integer>)
 * int num = Collections.min(ArraryList<Integer>)
 */

