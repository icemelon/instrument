import java.lang.management.ManagementFactory;

public class Test {
	
	private Thread[] threadPool;
	int x;
	
	/*public Test() {
		
		threadPool = new Thread[5];
		
		for (int i = 0; i < 5; i++) {
			threadPool[i] = new Thread(new TestRunnable());
			threadPool[i].start();
		}		
	}
	
	private final class TestRunnable implements Runnable {
		@Override
		public void run() {
			System.out.println(ManagementFactory.getRuntimeMXBean().getName() + ":" + Thread.currentThread().getId());
			
			while (true) ;
		}
	}*/
	
	private class A {
		public String run() {
			int x = 0;
			return new String(x);
		}
	}
	
	private int aaa(int a, int b) {
		return (a + b);
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
