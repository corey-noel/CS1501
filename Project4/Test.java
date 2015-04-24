import java.util.Arrays;

public class Test {
	
	public static void main(String[] args) {
		SymCipher c = new Substitute();

		String s = "ABCD";

		System.out.println(c.decode(c.encode(s)));	
	}

}