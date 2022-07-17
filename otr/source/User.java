package otr.source;

import otr.Test;
import otr.buffer.Buffer;

public class User extends Thread {
	
	private Source mySource;
	private Buffer<Long> gettingBuffer;
	private long[] recievedInfo;
	private int workingSize;

	public void setGettingBuffer(Buffer<Long> gettingBuffer) {
		this.gettingBuffer = gettingBuffer;
	}

	public Source getMySource() {
		return mySource;
	}

	public User(Source source, int size) {
		this.mySource = source;
		this.recievedInfo = new long[size];
		this.workingSize = size;
	}
	
	public static int compareInfo(long[] arr1, long[] arr2) {
		int cnt = 0;
		for (int i = 0; i < arr2.length; i++) {
			if (arr1[i] != arr2[i]) cnt++;
		}
		return cnt;
	}

	@Override
	public void run() {
		int index = 0;
		int i = this.workingSize;
		int CRCErrorsDetected = 0;
		while(i > 0) {
			long a = gettingBuffer.get();
			if (a == Long.MAX_VALUE - 1) CRCErrorsDetected++;
			recievedInfo[index++] = a;
			//System.out.println("User: " + Long.toBinaryString(a));
			i--;
		}
		int realErrors = compareInfo(recievedInfo, this.mySource.getGeneratedInfo());
		//System.out.println("Number of CRC errors: " + CRCErrorsDetected);
		//System.out.println("Number of undetected errors: " + (realErrors - CRCErrorsDetected));
		Test.putNumOfErrors(CRCErrorsDetected, (realErrors - CRCErrorsDetected));
	}
}
