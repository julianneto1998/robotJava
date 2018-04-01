import java.util.HashMap;

public class DCMotor{

    // Object to run shell commands.
    private static Runtime runtime = Runtime.getRuntime();
    
    private int pinAry[] = new int[2];  // index 0: forward pin
                                       // index 1: reverse pin

    // Unique identification number fo motor.
    private int motorNum;

    // Direction of DCMotor
    //   0: motor off
    //   1: motor running forward
    //  -1: motor running backwards
    private int direction = 0;  

    private static int totalMotorNum = 0;

    // Hashmap to translate pin numbers from Broadcom numbering scheme
    // to WiringPi numbering scheme.
    private static final HashMap<Integer, Integer> bcmToWirMap = new
	HashMap<Integer, Integer>();

    // Initialization of hasmap.
    static {
	int bcmAry[] = {17, 18, 27, 22, 23, 24, 25, 4, 2, 3, 8,
			7, 10, 9, 11, 14, 15, 5, 6, 13, 19, 26,
			12, 16, 20, 21};
	
	int wirAry[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 21, 22, 23, 24, 25, 26,
			27, 28, 29};
	
	for (int i = 0; i < bcmAry.length; i++) {
	    bcmToWirMap.put(bcmAry[i], wirAry[i]);
	}
    }

    public DCMotor(int bcmForward, int bcmReverse) {
	// Note: All GPIO pin operations assume WiringPi numbering.
	pinAry[0] = bcmToWirMap.get(bcmForward);
	pinAry[1] = bcmToWirMap.get(bcmReverse);
	motorNum = totalMotorNum++;
	setMotor();
    }

    public int getMotorNum() {
	return motorNum;
    }

    public int getTotalMotorNum() {
	return totalMotorNum;
    }

    // Sets mode of GPIO pins to output.
    private void setMotor() {
	try {
	    for (int i = 0; i < pinAry.length; i++)
		runtime.exec(String.format("gpio mode %d out",
			pinAry[i]));
	} catch (Exception e) {
	    System.out.println("Exception occured: " + e.getMessage());
	}
    }

    // Sets the output state of the pins in the pin Array
    private void setPins(int state0, int state1) {
	try {
	    runtime.exec(String.format("gpio write %d %d", pinAry[0], state0));
	    runtime.exec(String.format("gpio write %d %d", pinAry[1], state1));
	} catch (Exception e) {
	    System.out.println("Exception occured: " + e.getMessage());
	}
    }

    public void setDirection(int direction) {
	switch (direction) {
	case 0:  // stop motor
	    setPins(0, 0);
	    break;
	case 1:  // spin forwards
	    setPins(1, 0);
	    break;
	case -1:  // spin backwards
	    setPins(0, 1);
	    break;
	default:
	    throw new RuntimeException("Invalid direction specified");
	}
	this.direction = direction;
    }

    // setDirection() but being able to specifiy how long to run the motor.
    public void setDirection(int direction, double seconds) {
	setDirection(direction);
	waitSeconds(seconds);
	setDirection(0);
    }

    // Delays exectuion
    private static void waitSeconds(double secondsDouble) {
	long milliSecondsLong =  (long) (secondsDouble * 1000);
	try {
	    Thread.sleep(milliSecondsLong);
	} catch (Exception e) {
	    System.out.println("Exception occured: " + e.getMessage());
	}
    }
}
