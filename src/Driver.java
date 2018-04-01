import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

	DCMotor dcmotor = new DCMotor(2, 3);

	dcmotor.setDirection(1, 1);  // run the motor forward for 1 second

	dcmotor.setDirection(-1, 3);  // run the motor backwards for 3 seconds

    }
}
