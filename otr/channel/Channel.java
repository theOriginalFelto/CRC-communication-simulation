package otr.channel;

import otr.buffer.Buffer;

public class Channel extends Thread {
	
	private double errorRate;
	private Buffer<Long> gettingBuffer;
	private Buffer<Long> puttingBuffer;

	public Channel(Buffer<Long> gettingBuffer, Buffer<Long> puttingBuffer, double er) {
		this.gettingBuffer = gettingBuffer;
		this.puttingBuffer = puttingBuffer;
		this.errorRate = er;
	}
	
	public static int bitCount(long a) {
		int cnt = 0;
		while(a != 0) {
			a = a >>> 1;
			cnt++;
		}
		return cnt;
	}
	
	@Override
	public void run() {
		long temp;
		int mask;
		while((temp = gettingBuffer.get()) != Long.MAX_VALUE) {
			int bits = Channel.bitCount(temp);
			mask = 1;
			for (int i = 0; i < bits; i++) {
				if (Math.random() < this.errorRate) { //invertujemo bit
					if ((temp & mask) == 0) temp = temp | mask;
					else temp = temp & (~mask);
				}
				mask = mask << 1;
			}
			//System.out.println("Channel: " + Long.toBinaryString(temp));
			puttingBuffer.put(temp);
		}
		puttingBuffer.put(Long.MAX_VALUE);
	}

}
