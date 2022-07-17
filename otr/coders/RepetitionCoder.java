package otr.coders;

import otr.buffer.Buffer;

public class RepetitionCoder extends Thread {

	private Buffer<Long> gettingBuffer;
	private Buffer<Long> puttingBuffer;

	public RepetitionCoder(Buffer<Long> gettingBuffer, Buffer<Long> puttingBuffer) {
		this.gettingBuffer = gettingBuffer;
		this.puttingBuffer = puttingBuffer;
	}

	@Override
	public void run() {
		long temp = 0;
		while ((temp = gettingBuffer.get()) != Long.MAX_VALUE) {
			String a = Long.toBinaryString(temp);
			char[] c = a.toCharArray();
			long res = 0;
			for (char d : c) {
				if (d == '1') {
					for (int i = 0; i < 3; i++) {
						res = res | 1;
						res = res << 1;
					}
				} else {
					for (int i = 0; i < 3; i++) {
						res = res << 1;
					}
				}
			}
			res = res >> 1;
			//System.out.println("Repetition coder: " + Long.toBinaryString(res));
			puttingBuffer.put(res);
		}
		puttingBuffer.put(Long.MAX_VALUE);
	}

}
