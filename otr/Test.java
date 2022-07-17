package otr;

import otr.buffer.Buffer;
import otr.channel.Channel;
import otr.coders.CRCCoder;
import otr.coders.RepetitionCoder;
import otr.decoders.CRCDecoder;
import otr.decoders.RepetitionDecoder;
import otr.source.Source;
import otr.source.User;

public class Test {
	private static int index = 0;
	private static int[] numOfCRCErrors = new int[100];
	private static int[] numOfUndetectedErrors = new int[100];
	
	public static void putNumOfErrors(int a, int b) {
		Test.numOfCRCErrors[Test.index] = a;
		Test.numOfUndetectedErrors[Test.index++] = b;
	}
	
	public static double mean(int[] arr) {
		int sum = 0;
		for (int i : arr) {
			sum += i;
		}
		return sum * 1.0 / arr.length;
	}

	public static void main(String[] args) {
		int abc = 100;
		while(abc > 0) {
			//baferi		
			Buffer<Long> sourceCRCBuffer = new Buffer<>(5);
			Buffer<Long> CRCRepetitionBuffer = new Buffer<>(5);
			Buffer<Long> repetitionChannelBuffer = new Buffer<>(5);
			Buffer<Long> channelRepetitionBuffer = new Buffer<>(5);
			Buffer<Long> repetitionCRCBuffer = new Buffer<>(5);
			Buffer<Long> CRCuserBuffer = new Buffer<>(5);
			
			//strana predajnika
			Source source = new Source(sourceCRCBuffer, 1000);
			CRCCoder crcCoder = new CRCCoder(sourceCRCBuffer, CRCRepetitionBuffer, 0b1011);
			RepetitionCoder repCoder = new RepetitionCoder(CRCRepetitionBuffer, repetitionChannelBuffer);
			
			//kanal
			Channel channel = new Channel(repetitionChannelBuffer, channelRepetitionBuffer, 0.9); // treæi parametar se mijenja
			
			//strana prijemnika
			RepetitionDecoder repDecoder = new RepetitionDecoder(channelRepetitionBuffer, repetitionCRCBuffer);
			CRCDecoder crcDecoder = new CRCDecoder(repetitionCRCBuffer, CRCuserBuffer, 0b1011);
			User user = source.getMyUser(); 
			user.setGettingBuffer(CRCuserBuffer);
			
			source.start();
			crcCoder.start();
			repCoder.start();
			channel.start();
			repDecoder.start();
			crcDecoder.start();
			user.start();
			
			try {
				source.join();
				crcCoder.join();
				repCoder.join();
				channel.join();
				repDecoder.join();
				crcDecoder.join();
				user.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			abc--;
		}
		
		System.out.println("Average number of CRC errors: " + Test.mean(Test.numOfCRCErrors));
		System.out.println("Average number of undetected errors: " + Test.mean(Test.numOfUndetectedErrors));
	}
}
