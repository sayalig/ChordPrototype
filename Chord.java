import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Chord {

	class Node {
		int id;
		int pre, succ;
		int fingertable[];

		Node(int id) {
			this.id = id;
			this.succ = id;
			this.pre = -1;
			this.fingertable = new int[m];
			for (int i = 0; i < m; i++) {
				this.fingertable[i] = id;
			}
		}

		int getSucc() {
			return succ;
		}

		int getPre() {
			return pre;
		}

		void setSucc(int succ) {
			this.succ = succ;
			this.fingertable[0] = succ;
		}

		void setPre(int pre) {
			this.pre = pre;
		}

		String fingertableToString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < m - 1; i++) {
				sb.append(this.fingertable[i] + ",");
			}
			sb.append(this.fingertable[m - 1]);
			return (sb.toString());
		}

		int find_successor(int id) {
			if (this.succ <= this.id) {
				if ((this.id < id && id <= Math.pow(2, m) - 1) || (0 <= id && id <= this.succ)) {
					return this.succ;
				} else {
					int n = closest_preceding_node(id);
					Node a = chord.get(n);
					return a.find_successor(id);
				}
			} else {
				if (this.id < id && id <= this.succ) {
					return this.succ;
				} else {
					int n = closest_preceding_node(id);
					Node a = chord.get(n);
					return a.find_successor(id);
				}
			}

		}

		int closest_preceding_node(int id) {
			for (int i = m - 1; i >= 0; i--) {
				if (this.id < id) {
					if (this.fingertable[i] > this.id && this.fingertable[i] < id) {
						return (fingertable[i]);
					}
				} else {
					if ((this.fingertable[i] > this.id && this.fingertable[i] <= Math.pow(2, m) - 1)
							|| (this.fingertable[i] >= 0 && this.fingertable[i] < id)) {
						return (fingertable[i]);
					}
				}
			}
			return this.id;
		}

		void create() {
			Node a = new Node(this.id);
			chord.put(this.id, a);
		}

		void join(int n) {
			Node n_node = chord.get(n);
			int a = n_node.find_successor(this.id);
			this.setSucc(a);
			this.setPre(-1);
		}

		void stabilize() {
			if(!chord.containsKey(this.succ)) {
				return;
			}
			int x = chord.get(this.succ).pre;
			if (x != -1) {
				if (this.succ <= this.id) {
					if ((this.id < x && x <= Math.pow(2, m) - 1) || (0 <= x && x < this.succ)) {
						this.setSucc(x);
					}
				} else {
					if (this.id < x && x < this.succ) {
						this.setSucc(x);
					}
				}
			}
			Node successor = chord.get(this.succ);
			successor.notify(this.id);
		}

		void notify(int n) {
			if (this.pre == -1) {
				this.pre = n;
			} else if (this.id <= this.pre) {
				if ((this.pre < n && n <= Math.pow(2, m) - 1) || (0 <= n && n < this.id)) {
					this.pre = n;
				}
			} else {
				if (this.pre < n && n < this.id) {
					this.pre = n;
				}
			}
		}

		void fix_fingers() {
			for (int i = 0; i < m; i++) {
				double n = (this.id + Math.pow(2, i));
				double d = Math.pow(2, m);
				int x = (int) (n % d);
				this.fingertable[i] = this.find_successor(x);
			}
		}

		void check_predecessor() {
			Node a = chord.get(this.pre);
			if (a == null) {
				this.pre = -1;
			}
		}

		void drop() {
			Node successor = chord.get(this.succ);
			if (this.pre != -1) {
				Node predecessor = chord.get(this.pre);
				predecessor.setSucc(this.succ);
			}
			successor.setPre(this.pre);
		}

	}

	static int m = 0;
	static TreeMap<Integer, Node> chord = new TreeMap<Integer, Node>();
	private static Chord instance = null;

	private Chord() {
		// Exists only to defeat instantiation.
	}

	public static Chord getInstance() {
		if (instance == null) {
			instance = new Chord();
		}
		return instance;
	}

	public static void main(String[] args) {
		Scanner sc;
		if(args.length==3 && args[0].equals("-i")) {
			File f = new File(args[1]);
			try {
				sc = new Scanner(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Please give correct file name");
				return;
			}
			try {
				m = Integer.parseInt(args[2]);
				if(m<1 || m>31) {
					System.out.println("ERROR: invalid size of fingertable " + m+ "It should be in range [1,31]");
					sc.close();
					return;
				}
			} catch (Exception e) {
				System.out.println("ERROR: invalid integer " + args[0]);
				sc.close();
				return;
			}
		}
		else if(args.length==1){
			sc = new Scanner(System.in);
			try {
				m = Integer.parseInt(args[0]);
				if(m<1 || m>31) {
					System.out.println("ERROR: invalid size of fingertable " + m+ "It should be in range [1,31]");
					sc.close();
					return;
				}
			} catch (Exception e) {
				System.out.println("ERROR: invalid integer " + args[0]);
				sc.close();
				return;
			}
		}
		else {
			System.out.println("ERROR: Wrong Arguments. Please use chord -i filename m for batch mode and chord m for interactive mode");
			return;
		}
		
		while (sc.hasNextLine()) {
			String curr = sc.nextLine();
			String arr[] = curr.split(" ");
			try {

				if (arr.length > 1) {
					if ((Integer.parseInt(arr[1]) >= Math.pow(2, m)) && (Integer.parseInt(arr[1])<1)) {
						System.out.println("ERROR: node id must be in [0," + (int) Math.pow(2, m) + "]");
						continue;
					}
				}
				if (arr.length > 2) {
					if ((Integer.parseInt(arr[2]) >= Math.pow(2, m)) && (Integer.parseInt(arr[2])<1)) {
						System.out.println("ERROR: node id must be in [0," + (int) (Math.pow(2, m)) + "]");
						continue;
					}
				}
				if (curr.equals("end")) {
					if (arr.length > 1) {
						System.out.println("SYNTAX ERROR: list expects no parameters");
						continue;
					}
					sc.close();
					break;
				}
				if (arr[0].equals("add")) {
					if (arr.length > 2) {
						System.out.println("SYNTAX ERROR: add expects 1 parameter not " + arr.length);
						continue;
					}
					int i = Integer.parseInt(arr[1]);
					if (chord.containsKey(i)) {
						System.out.println("ERROR: Node " + i + " exists");
					} else {
						Chord c = new Chord();
						Chord.Node newNode = c.new Node(i);
						newNode.create();
						System.out.println("Added node " + arr[1]);
					}
				} else if (arr[0].equals("drop")) {
					if (arr.length > 2) {
						System.out.println("SYNTAX ERROR: drop expects 1 parameter not " + arr.length);
						continue;
					}
					int i = Integer.parseInt(arr[1]);
					if (chord.containsKey(i)) {
						chord.get(i).drop();
						chord.remove(i);
						System.out.println("Dropped node " + arr[1]);
					} else {
						System.out.println("ERROR: Node " + i + " does not exist");
					}
				} else if (arr[0].equals("join")) {
					if (arr.length > 3) {
						System.out.println("SYNTAX ERROR: join expects 2 parameters not " + arr.length);
						continue;
					}
					int i = Integer.parseInt(arr[1]);
					int j = Integer.parseInt(arr[2]);
					if (chord.containsKey(i) && chord.containsKey(j)) {
						chord.get(i).join(j);
						// System.out.println("Joined node " + i + " and " + j);
					} else {
						if (!chord.containsKey(i))
							System.out.println("ERROR: Node " + i + " does not exist");
						else
							System.out.println("ERROR: Node " + j + " does not exist");
					}
				} else if (arr[0].equals("fix")) {
					if (arr.length > 2) {
						System.out.println("SYNTAX ERROR: fix expects 1 parameter not " + arr.length);
						continue;
					}
					int i = Integer.parseInt(arr[1]);
					if (chord.containsKey(i)) {
						chord.get(i).fix_fingers();
						// System.out.println("Fixed node " + i);
					} else {
						System.out.println("ERROR: Node " + i + " does not exist");
					}
				} else if (arr[0].equals("stab")) {
					if (arr.length > 2) {
						System.out.println("SYNTAX ERROR: stab expects 1 parameter not " + arr.length);
						continue;
					}
					int i = Integer.parseInt(arr[1]);
					if (chord.containsKey(i)) {
						chord.get(i).stabilize();
						// System.out.println("Stabilized node " + i);
					} else {
						System.out.println("ERROR: Node " + i + " does not exist");
					}
				} else if (arr[0].equals("list")) {
					if (arr.length > 1) {
						System.out.println("SYNTAX ERROR: list expects no parameters");
						continue;
					}
					Set<Integer> nodes = chord.keySet();
					System.out.println("Nodes " + nodes.toString().replace("[", "").replace("]", ""));
				} else if (arr[0].equals("show")) {
					if (arr.length > 2) {
						System.out.println("SYNTAX ERROR: show expects 1 parameter not " + arr.length);
						continue;
					}
					int i = Integer.parseInt(arr[1]);
					if (chord.containsKey(i)) {
						Node a = chord.get(i);
						if (a.getPre() < 0) {
							System.out.println("Node " + arr[1] + ": suc " + a.getSucc() + ", pre None: finger "
									+ a.fingertableToString());
						} else {
							System.out.println("Node " + arr[1] + ": suc " + a.getSucc() + ", pre " + a.getPre()
									+ ": finger " + a.fingertableToString());
						}
					} else {
						System.out.println("ERROR: Node " + i + " does not exist");
					}
				} else {
					System.out.println("Invalid Input");
				}
			} catch (Exception e) {
				System.out.println("ERROR: invalid integer " + arr[1]);
			}
		}
	}

}
/*
 * References: 1.
 * https://stackoverflow.com/questions/10135910/is-there-a-constructor-
 * associated-with-nested-classes
 * 
 */
