import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

	DCMotor dcmotor = new DCMotor(2, 3);

	dcmotor.setDirection(1, 1);

	dcmotor.setDirection(-1, 3);

    }
}
