import static utils._3n_plus_1_method.*;
import static utils.method.*;
public class _3n_plus_1 {
    @SuppressWarnings("ConvertToStringSwitch")

	public static void main(String[] args) {
        do {
            System.out.println("------------------------------------------------------");
            String parten = strInput("Please choose basic, pro, max or ultra parten:");
            if (parten.equals("basic")){
                Basic();
		msSleep(1000);
            }else if (parten.equals("pro")){
                Pro();
		msSleep(1000);
            }else if (parten.equals("max")){
                Max();
		msSleep(1000);
            }else if (parten.equals("ultra")){
                System.out.println("Oh my pccccccccccc!");
                msSleep(2000);
                System.out.println("What are you talking about?!");
                System.out.println("There is no ultra!!!!!");
		msSleep(3000)
		System.out.println("Hello...I'm  ...ultra");
		for(int i = 0;i <= 300;i++){
		    System.out.println("GO BACK")
		    msSleep(50)
		}
		break;
            }else{
                System.out.println("The system is loading. Please wait a minute.");
                msSleep(10000);
                System.out.println("System: What are you talking about??!!! Retry!!!");
            }
		msSleep(1000);
            String re = strInput("Retry?(yes/no)");
            if (re.equals("yes")){
		}else if (re.equals("no")){
		break;
		}
		System.out.println("You must want to retry!");
		msSleep(1000);
        } while (true);
        System.out.println("quit");
        msSleep(2000);
    }
}
