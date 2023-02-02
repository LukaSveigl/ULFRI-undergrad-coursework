public class DN02 {
	public static void main (String [] args) {
		if (args.length > 0) { // If arguments not empty

			if (args[0].equals("1")) { // Inline output, comma separated

				for (int argumentIndex = 1; argumentIndex < args.length; argumentIndex++) {

					System.out.print(args[argumentIndex]);

					if (argumentIndex != args.length - 1) { // If not last argument print ', '
						System.out.print(", ");
					}
				}
			}

			else if (args[0].equals("2")) { // Framed output

				String statement = "* "; // Statement is middle line of output (what has to be bordered)

				for (int i = 1; i < args.length; i++) {
					statement += args[i] + " ";
				}

				statement += "*";

				for (int j = 0; j < statement.length(); j++) { // Print upper border
					System.out.print("*");
				}
				System.out.print("\n");

				System.out.println(statement); // Print middle line

				for (int j = 0; j < statement.length(); j++) { // Print lower border
					System.out.print("*");
				}
				System.out.print("\n");
			}
		
		}	

	}
}