package otr.decoders;

import otr.buffer.Buffer;
import otr.coders.CRCCoder;

public class CRCDecoder extends Thread {

	private Buffer<Long> gettingBuffer;
	private Buffer<Long> puttingBuffer;
	private int generic;

	public CRCDecoder(Buffer<Long> gettingBuffer, Buffer<Long> puttingBuffer, int gen) {
		this.gettingBuffer = gettingBuffer;
		this.puttingBuffer = puttingBuffer;
		this.generic = gen;
	}

	public static int receive(int data[], int divisor[]) {
		int remainder[] = CRCCoder.divideDataWithDivisor(data, divisor);

		for (int i = 0; i < remainder.length; i++) {
			if (remainder[i] != 0) {
				return -1;
			}
		}
		return 0;
	}

	@Override
	public void run() {
		long temp = 0;
		int[] gen = CRCCoder.numberToBitArray(generic);
		
		while ((temp = gettingBuffer.get()) != Long.MAX_VALUE) {
			int[] info = CRCCoder.numberToBitArray(temp);
			
			int ret = CRCDecoder.receive(info, gen);
			long res;
			if (ret == 0 && (info.length - gen.length + 1) > 0) {
				int[] data = new int[info.length - gen.length + 1];
				System.arraycopy(info, 0, data, 0, info.length - gen.length + 1);
				res = CRCCoder.bitArrayToNumber(data);
			}
			else res = Long.MAX_VALUE - 1;

			//System.out.println("CRC Decoder: " + Long.toBinaryString(res));
			puttingBuffer.put(res);
		}
		puttingBuffer.put(Long.MAX_VALUE);
	}

}
