import java.util.Scanner;

public class BinaryMultipleThree
{
	public static boolean scan(String s)
	{
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);

			switch (state) {
			case 0:
				if (ch == '0')
					state = 1;
				else if (ch == '1')
					state = 2;
				else
					state = -1;
				break;

			case 1:
				if (ch == '0')
					state = 1;
				else if (ch == '1')
					state = 3;
				else
					state = -1;
				break;

			case 2:
				if (ch == '1')
					state = 1;
				else if (ch == '0')
					state = 4;
				else
					state = -1;
				break;

			case 3:
				if (ch == '1')
					state = 1;
				else if (ch == '0')
					state = 4;
				else
					state = -1;
				break;

			case 4:
				if (ch == '1')
					state = 4;
				else if (ch == '0')
					state = 3;
				else
					state = -1;
				break;
			}
		}
		return state == 1;
	}

	public static void main(String[] args)
	{
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}