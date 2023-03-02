package com.mkp.jce.chap3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * <B>Section 3.5.1</B>
 * <P>
 * Sample that demonstrates Eve intercepting the communications between Alice and Bob.
 * Eve is able to see only the public aspects of the key exchange, and not the resulting
 * shared secret.  As a result, even if Eve knows which cipher algorithm Alice and Bob agreed
 * to use, she doesn't know the key and is lost as to what important messages are being communicated
 * between Alice and Bob.
 * 
 * @see com.mkp.jce.chap3.AliceClient
 * @author Jason R. Weiss
 * @version 1.0
 */
public class EvilEve
{		
	/*
	 * Dumps the data read from a SocketChannel to the screen
	 */
	private static void printData(ByteBuffer buf, int length, String whoFrom)
	{
		ByteBuffer copy = buf.duplicate();		
		System.out.println("--------BEGIN---------     **" + whoFrom + "**");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < length; i++)
		{
			baos.write(copy.get());
		}
		
		System.out.println(new String(baos.toByteArray()));
		System.out.println("---------END----------");
	}
	

	public static void main(String[] args)
	{
		try
		{
			ByteBuffer aliceBuf = ByteBuffer.allocateDirect(1024);
			ByteBuffer bobBuf = ByteBuffer.allocateDirect(1024);

			//Setup a server to "act" like Bob on the call and
			//wait for Alice to call us
			System.out.println("Waiting for Alice's call...");
			ServerSocketChannel ssc = ServerSocketChannel.open();
			InetSocketAddress aliceIsa = new InetSocketAddress(InetAddress.getLocalHost(), 8787);
			ssc.socket().bind(aliceIsa);			
			SocketChannel aliceSc = ssc.accept();

			//Setup a client to "act" like Alice on the call and
			//wait for Alice to call us, then forwarded her bytes
			//along unmodified
			InetSocketAddress bobIsa = new InetSocketAddress(InetAddress.getLocalHost(), 8888);
			SocketChannel bobSc = SocketChannel.open();
			bobSc.connect(bobIsa);

			try
			{
				//Loop forever until Alice and Bob end their conversation
				for(;;)
				{
					//
					// Receive from Alice
					//
					aliceBuf.clear();
					int received = aliceSc.read(aliceBuf);				
					aliceBuf.flip();

					if (-1 == received) System.exit(0);

					System.out.println("Received " + received + " bytes from Alice");
					printData(aliceBuf, received, "Alice");
					
					//
					// Forward to Bob
					//
					System.out.println("Forwarding to Bob...");
					int sent = bobSc.write(aliceBuf);
					
					//
					// Receive response from Bob
				 	//
					bobBuf.clear();
					received = bobSc.read(bobBuf);
					bobBuf.flip();

					if (-1 == received) System.exit(0);
					
					System.out.println("Received " + received + " bytes from Bob");
					printData(bobBuf, received, "Bob");
					
					//
					// Forward to Alice
					//
					System.out.println("Forwarding to Alice...");
					sent = aliceSc.write(bobBuf);
				}
				
			} finally
			{
				if (bobSc != null)	bobSc.close();
				if (aliceSc != null)	aliceSc.close();
			}
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
