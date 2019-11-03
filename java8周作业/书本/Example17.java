public class Example17 {
	public static void main(String[] args) {
		Storage st = new Storage(); // 创建数据存储类对象
		Input input = new Input(st);	// 创建Input对象传入Storage对象
		Output output = new Output(st); 	// 创建Output对象传入Storage对象
		new Thread(input).start();		// 开启新线程
		new Thread(output).start();		// 开启新线程
	}
}
class Input implements Runnable{		    // 输入线程类
	private Storage st ;
     private int num;                           // 定义一个变量num 
	Input(Storage st){                         // 通过构造方法接收一个Storage对象
		this.st = st;
	}
	public void run(){        
		while(true){
			st.put(num++);                   // 将num存入数组，每次存入后num自增
		}
	}
}

class Output implements Runnable{	       //输出线程类
	private Storage st ;
	Output(Storage st){
		this.st = st;
	}
	public void run(){
		while(true){
			st.get();
		}
	}
}

class Storage {
	private int[] cells = new int[10];   // 数据存储数组
	private int inPos, outPos;            // inPos存入时数组下标，outPos取出时数组下标
	private int count;                      // 存入或者取出数据的数量
	public synchronized void put(int num) {
		try {
			// 如果放入数据等于cells的长度，此线程等待
			while (count == cells.length) {
				this.wait();
			}
			cells[inPos] = num;          // 向数组中放入数据
			System.out.println("在cells[" + inPos + "]中放入数据---" + cells[inPos]);
			inPos++;// 存完元素让位置加1
			if (inPos == cells.length) // 当在cells[9]放完数据后再从cells[0]开始
				inPos = 0;
			count++; // 每放一个数据count加1
			this.notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public synchronized void get() {
		try {
			while (count == 0) {          // 如果 count为0，此线程等待
				this.wait();
			}
			int data = cells[outPos];    // 从数组中取出数据
			System.out.println("从cells[" + outPos + "]中取出数据" + data);
			cells[outPos] = 0;            // 取出后，当前位置的数据置0
			outPos++;                       // 取完元素让位置加1
			if (outPos == cells.length) // 当从cells[9]取完数据后再从cells[0]开始
				outPos = 0;
			count--;                        // 每取出一个元素count减1
			this.notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
