public class ParseTree {
		private String data;
		private String type; // leaf or node. leaf = atom node = operator
		protected ParseTree left;
		protected ParseTree right;

		public ParseTree(){
			data = null;
			left = null;
			right = null;
			type = null;
		}

		public ParseTree(String input){
			initTree(input);
		}

		public ParseTree(char input){
			insert(input);
			left = null;
			right = null;
		}

		private void initTree(String input){
			char[] chars = backwardsChars(input);

			for(char c : chars)
				insert(c);
		}

		private void insert(char c){
			if(data == null) {
				data = String.valueOf(c);

				if(c == '*' || c == '.' || c == '+')
					type = "node";
				else
					type = "leaf";

				return;
			}

			if(right == null)
				right = new ParseTree(c);
			else if(!right.isFull())
				right.insert(c);
			else if(left == null)
				left = new ParseTree(c);
			else if(!left.isFull() && this.type.equals("node") && !this.data.equals("*"))
				left.insert(c);

		}

		private char[] backwardsChars(String input){
			return new StringBuilder(input).reverse().toString().toCharArray();
		}

		public boolean isFull(){
			if(data == null) return false;

			if(type.equals("leaf")) return true;

			if((data.equals("+") || data.equals(".")) && right != null && right.isFull() && left != null && left.isFull()) {
				return true;
			} else if(data.equals("*") && right != null && right.isFull())
				return true;

			return false;
		}

		public ParseTree stripA(String a){
			if(type.equals("leaf"))
				if(data.equals("/") || !data.equals(a))
					return this;
				else {
					return new ParseTree("/*");
				}

			if(type.equals("node")){
				if(data.equals("+")){
					right = right.reverse();
					left = left.reverse();

					return this;
				} else if(data.equals(".")) {
					if(right.q1()){
						ParseTree temp = new ParseTree("+");

						ParseTree tempLeft = new ParseTree(".");
						tempLeft.left = right.reverse();
						tempLeft.right = right;

						temp.left = tempLeft;
						temp.right = right.reverse();
					} else {
						ParseTree temp = new ParseTree(".");
						temp.left = right.reverse();
						temp.right = right;

						return temp;
					}
				} else if(data.equals("*")) {
					ParseTree temp = new ParseTree('.');
					temp.right = new ParseTree(right.data+"*");
					temp.left = right.reverse();

					return temp;
				}
			}

			return null;
		}

		public ParseTree dropOne(){
			if(type.equals("leaf"))
				if(data.equals("/"))
					return this;
				else {
					return new ParseTree("/*");
				}

			if(type.equals("node")){
				if(data.equals("+")){
					right = right.reverse();
					left = left.reverse();

					return this;
				} else if(data.equals(".")) {
					ParseTree temp = new ParseTree('+');
					ParseTree tempInnerLeft = new ParseTree('.');
					ParseTree tempInnerRight = new ParseTree('.');

					tempInnerLeft.left = left.reverse();
					tempInnerLeft.right = right;

					tempInnerRight.left = left;
					tempInnerRight.right = right.reverse();

					temp.right = tempInnerRight;
					temp.left = tempInnerLeft;

					return temp;
				} else if(data.equals("*")) {
					ParseTree temp = new ParseTree('.');
					temp.right = new ParseTree(right.data+"*");
					temp.left = new ParseTree('.');

					temp.left.insert(right.data.toCharArray()[0]);

					temp.left.right = new ParseTree(right.data+"*");

					return temp;
				}
			}

			return null;
		}

		public ParseTree Bbeforea(){
			return null;
		}

		public ParseTree suffixes(){
			return null;
		}

		public ParseTree prefixes(){
			if(type.equals("leaf"))
				if(data.equals("/"))
					return this;
				else {
					ParseTree temp = new ParseTree('+');
					temp.right = new ParseTree('*');
					temp.right.insert('/');

					temp.left = new ParseTree(this.data);

					return temp;
				}

			if(type.equals("node")){
				if(data.equals("+")){
					right = right.reverse();
					left = left.reverse();

					return this;
				} else if(data.equals(".")) {
					if(right.q0())
						return new ParseTree('/');
					else {
						ParseTree tempOuter = new ParseTree('+');
						tempOuter.left = left.reverse();

						ParseTree tempInner = new ParseTree('.');
						tempInner.left = left;
						tempInner.right = right.reverse();

						tempOuter.right = tempInner;

						return tempOuter;
					}
				} else if(data.equals("*")) {
					if(left.q0())
						return new ParseTree("/*");
					else {
						ParseTree tempOuter = new ParseTree('.');
						tempOuter.right = right.reverse();

						ParseTree tempInner = new ParseTree('*');
						tempInner.right = right;

						tempOuter.left = tempInner;
						return tempOuter;
					}
				}
			}

			return null;
		}

		public ParseTree reverse(){
			if(type.equals("node")){
				if(data.equals("+")) {
					left = left.reverse();
					right = right.reverse();
				} else if(data.equals(".")) {
					ParseTree tempLeft = left.reverse(), tempRight = right.reverse();
					left = tempRight;
					right = tempLeft;
				}
				else if(data.equals("*")){
					right = right.reverse();
				}

			}

			return null;
		}

		public boolean q5(String a){
			return q4(a);//todo check
		}

		public boolean q4(String a){
			if(type.equals("leaf"))
				if(data.equals("/"))
					return false;
				else
					return data.equals(a);

			if(type.equals("node")){
				if(data.equals("+"))
					return left.q4(a) || right.q4(a);
				else if(data.equals("."))
					return (left.q4(a) && !right.q0()) || (left.q1() && right.q4(a));
				else if(data.equals("*"))
					return q4(a);
			}

			return false;
		}

		public boolean q3(){
			if(type.equals("leaf"))
				return false;

			if(type.equals("node")){
				if(data.equals("+"))
					return left.q0() || right.q0();
				else if(data.equals("."))
					return (left.q3() && !right.q0()) || (right.q3() && !left.q0());
				else if(data.equals("*"))
					return q2();
			}

			return false;
		}

		public boolean q2(){
			return !q1();//todo check this
		}

		public boolean q1(){
			if(type.equals("leaf"))
				return false;

			if(type.equals("node")){
				if(data.equals("+"))
					return left.q1() || right.q1();
				else if(data.equals("."))
					return left.q1() && right.q1();
				else if(data.equals("*"))
					return true;
			}

			return false;
		}

		public boolean q0(){
			if(type.equals("leaf"))
				return data.equals("/");

			if(type.equals("node")){
				if(data.equals("+"))
					return left.q0() && right.q0();
				else if(data.equals("."))
					return left.q0() || right.q0();
				else if(data.equals("*"))
					return false;
			}

			return false;
		}

		public String readPrefix(){
			if(type.equals("leaf"))
				return this.data;

			if(data.equals("*"))
				return data + right.readPrefix();

			return this.data + left.readPrefix() + right.readPrefix();
		}
}
