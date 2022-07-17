package otr.source;

import otr.buffer.Buffer;

public class Source extends Thread {

	private User myUser;
	private Buffer<Long> puttingBuffer;
	private long[] generatedInfo;
	private int workingSize;

	public long[] getGeneratedInfo() {
		return this.generatedInfo;
	}

	public User getMyUser() {
		return myUser;
	}

	public Source(Buffer<Long> buffer, int size) {
		this.myUser = new User(this, size);
		this.puttingBuffer = buffer;
		this.generatedInfo = new long[size];
		this.workingSize = size;
	}

	@Override
	public void run() {
		int index = 0;
		int i = this.workingSize;
		while (i > 0) {
			byte a = (byte) (Math.random() * Byte.MAX_VALUE);
			if (a < 7) a += 7;
			generatedInfo[index++] = (long) a;
			//System.out.println("Source: " + Long.toBinaryString(a));
			puttingBuffer.put((long) a);

			try {
				Thread.sleep((long) Math.random() * 2000/* + 3000*/);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i--;
		}

		puttingBuffer.put(Long.MAX_VALUE);
	}
}
