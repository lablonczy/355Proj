public class Driver {
	public static void main(String[] args) {
		String regex = "/*/+/+a+";

		System.out.println(noOp(regex));
	}

	private static String noOp(String regex){
		return new ParseTree(regex).readPrefix();
	}


}


