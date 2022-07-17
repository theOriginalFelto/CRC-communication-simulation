package otr.decoders;

import otr.buffer.Buffer;

public class RepetitionDecoder extends Thread {

	private Buffer<Long> gettingBuffer;
	private Buffer<Long> puttingBuffer;

	public RepetitionDecoder(Buffer<Long> gettingBuffer, Buffer<Long> puttingBuffer) {
		this.gettingBuffer = gettingBuffer;
		this.puttingBuffer = puttingBuffer;
	}

	@Override
	public void run() {
		long temp;
		while ((temp = gettingBuffer.get()) != Long.MAX_VALUE) {
			long mask = 0b111; // za dekodovanje
			long result = 0;
			long press = 1; // za upisivanje dekodovane informacije
			
			while (temp != 0 && temp != 1) {
				int cond = (int) (temp & mask);
				if (cond == 7 || cond == 6 || cond == 5 || cond == 3) {
					result = result | press;
				}
				press = press << 1;
				temp = temp >>> 3;
			}
			//System.out.println("Repetition decoder: " + Long.toBinaryString(result));
			puttingBuffer.put(result);
		}
		puttingBuffer.put(Long.MAX_VALUE);
	}
}
