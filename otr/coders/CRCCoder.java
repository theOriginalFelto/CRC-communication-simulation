package otr.coders;

import otr.buffer.Buffer;
import otr.channel.Channel;

public class CRCCoder extends Thread {

	private Buffer<Long> gettingBuffer;
	private Buffer<Long> puttingBuffer;
	private int generic;

	public CRCCoder(Buffer<Long> gettingBuffer, Buffer<Long> puttingBuffer, int gen) {
		this.gettingBuffer = gettingBuffer;
		this.puttingBuffer = puttingBuffer;
		this.generic = gen;
	}

	public static int[] numberToBitArray(long num) {
		int bitNum = Channel.bitCount(num);
		int[] arr = new int[bitNum];
		long mask = 1;
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (num & mask);
			num = num >>> 1;
		}
		CRCCoder.reverse(arr, bitNum);
		return arr;
	}
	
	public static long bitArrayToNumber(int[] arr) {
		long press = 1;
		long res = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 1) res = res | press;
			res = res << 1;
		}
		res = res >>> 1;
		return res;
	}
	
	public static void reverse(int a[], int n)
    {
		int i, k, t;
        for (i = 0; i < n / 2; i++) {
            t = a[i];
            a[i] = a[n - i - 1];
            a[n - i - 1] = t;
        }
    }

	public static int xor(int x, int y) {
		if (x == y) return 0;
		return 1;
	}
	
	public static int[] divideDataWithDivisor(int info[], int divisor[]) { 
        int remainder[] = new int[divisor.length];  
        int i;
        int data[] = new int[info.length + divisor.length];  

        System.arraycopy(info, 0, data, 0, info.length);  
        System.arraycopy(data, 0, remainder, 0, divisor.length);  
  
        for(i = 0; i < info.length; i++) {
            if(remainder[0] == 1) { 
                for(int j = 1; j < divisor.length; j++) {  
                	remainder[j-1] = CRCCoder.xor(remainder[j], divisor[j]);  
                }  
            }  
            else { 
                for(int j = 1; j < divisor.length; j++) {  
                	remainder[j-1] = CRCCoder.xor(remainder[j], 0);
                }  
            }  
            remainder[divisor.length-1] = data[i+divisor.length];
        }  
        return remainder;  
    }  

	@Override
	public void run() {
		long temp = 0;
		int[] gen = CRCCoder.numberToBitArray(generic);
		
		while ((temp = gettingBuffer.get()) != Long.MAX_VALUE) {
			int[] info = CRCCoder.numberToBitArray(temp);
			int[] remainder = CRCCoder.divideDataWithDivisor(info, gen);
			int[] data = new int[info.length + gen.length - 1];
			System.arraycopy(info, 0, data, 0, info.length);
			System.arraycopy(remainder, 0, data, info.length, remainder.length - 1);
			
			long res = CRCCoder.bitArrayToNumber(data);
			//System.out.println("CRC Coder: " + Long.toBinaryString(res));
			puttingBuffer.put(res);
		}
		puttingBuffer.put(Long.MAX_VALUE);
	}

	public static void main(String[] args) {
		int[] arr1 = new int[7];
		int[] arr2 = new int[3];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = 1;
		}
		for (int i = 0; i < arr1.length; i++) {
			arr1[i] = 0;
		}
		int[] arr3 = new int[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, arr3, 0, arr1.length);
		System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);
		for (int i = 0; i < arr3.length; i++) {
			System.out.print(arr3[i]);
		}
		System.out.println();
		CRCCoder.reverse(arr3, arr3.length);
		for (int i = 0; i < arr3.length; i++) {
			System.out.print(arr3[i]);
		}
		System.out.println();
		
		long a = CRCCoder.bitArrayToNumber(arr3);
		System.out.println(Long.toBinaryString(a));
		
		arr3 = CRCCoder.numberToBitArray(a);
		for (int i = 0; i < arr3.length; i++) {
			System.out.print(arr3[i]);
		}
	}
}
