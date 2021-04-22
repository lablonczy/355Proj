public class Driver {
	public static void main(String[] args) {
		String regex = "/";
		String cmd = args[0];
		cmdHandler(cmd,regex);
	}

	private static void cmdHandler(String cmd, String... inputs){
		String result  = "";
		ParseTree tree = new ParseTree(inputs[0]);

		switch (cmd){
			case "--no-op": result = tree.readPrefix(); break;
			case "--empty": result = String.valueOf(tree.q0()); break;
			case "--has-epsilon": result = String.valueOf(tree.q1()); break;
			case "--has-nonepsilon": result = String.valueOf(tree.q2()); break;
			case "--infinite": result = String.valueOf(tree.q3()); break;
			case "--starts-with": result = String.valueOf(tree.q4(inputs[1])); break;
			case "--ends-with": result = String.valueOf(tree.q5(inputs[1])); break;
			case "--reverse": result = tree.reverse().readPrefix(); break;
			case "--prefixes": result = tree.prefixes().readPrefix(); break;
			case "--suffixes": result = tree.suffixes().readPrefix(); break;
			case "--b-before-a": result = tree.Bbeforea().readPrefix(); break;
			case "--drop-one": result = tree.dropOne().readPrefix(); break;
			case "--strip": result = tree.stripA(inputs[1]).readPrefix(); break;
			default:
				System.out.println("bad");
		}

		System.out.println(result);
	}

}


