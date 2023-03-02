package com.mkp.jce.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Exposes common routines used throughout many of the code examples of the book.
 *   
 * @author Jason R. Weiss
 * @version 1.0
 */
public class CryptoUtil
{
	protected static byte[] getFileBytes(File file) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		
		FileInputStream fis = new FileInputStream(file);
	
		int curByte=0;
		while( (curByte = fis.read()) != -1 )
		{
			baos.write(curByte);
		}
		
		byte[] fileData = baos.toByteArray();
		baos.close();
		
		return fileData;			
	}
	
	/**
	 * Reads a DER encoded certificate from a file
	 * @param file DER encoded certificate file
	 * @return Certificate instance
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final Certificate readDerEncodedX509Certificate(File file)
		throws FileNotFoundException, IOException
	{		
		Certificate cert = null;
		try
		{
			CertificateFactory factory = CertificateFactory.getInstance("X.509");			
			FileInputStream fis = new FileInputStream(file);			
			cert = factory.generateCertificate(fis);			
			fis.close();			
		} catch (CertificateException ce)
		{
			ce.printStackTrace();
		}			
		return cert;	
	}
	
	/**
	 * Reads and extracts a Certificate instance from a file containing a Base64
	 * encoded certificate
	 * 
	 * @param file Base64 encoded ASCII file that contains the certificate
	 * @return Certificate instance
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final Certificate readBase64EncodedX509Certificate(File file) 
		throws FileNotFoundException, IOException
	{
		Certificate cert = null;
		
		try
		{
			CertificateFactory factory = CertificateFactory.getInstance("X.509");

			FileInputStream fis = new FileInputStream(file);

			DataInputStream dis = new DataInputStream(fis);
			byte[] bytes = new byte[dis.available()];

			try
			{
				dis.readFully(bytes);

				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

				while (bais.available() > 0)
				{
					cert = factory.generateCertificate(bais);							
				}

			} catch (IOException ioe)
			{
				ioe.printStackTrace();
			} finally
			{
				try
				{
					dis.close();
					fis.close();
				} catch (Exception e)
				{
					//this is bad!
					e.printStackTrace();
				}
			}

		} catch (CertificateException ce)
		{
			ce.printStackTrace();
		}
		return cert;
	}
	
	/**
	 * Reads a public key from a named file
	 * @param file public key file location
	 * @param algorithm algorithm used to create the public key
	 * @return PublicKey instance
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static final PublicKey loadPublicKey(File file, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException 
	{
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(getFileBytes(file));			
		KeyFactory keyFactoryEngine = KeyFactory.getInstance(algorithm);
		return keyFactoryEngine.generatePublic(keySpec);
	}
	
	/**
	 * Writes out ciphertext to the named file
	 * 
	 * @param file storage location of the cipher text
	 * @param cipherText the ciphertext to write
	 * @return number of characters written, > 0 if successful or -1 if a problem was encountered
	 */
	public static final long writeCipherTextFile(File file, byte[] cipherText)
	{
		long charsWritten = 0;
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(cipherText);
			fos.close();
			charsWritten = cipherText.length;
										
		} catch (IOException ioe)
		{
			//Uh-Oh!
			charsWritten = -1;
		}
		
		return charsWritten;
	}
	
	/**
	 * Reads bytes that represent a digital signature
	 * 
	 * @param file signature data file
	 * @return byte[] representing the signature data
	 */
	public static final byte[] readSignatureFile(File file)
	{
		return readCipherTextFile(file);
	}
	
	/**
	 * Reads a ciphertext file and returns the content as a byte[]
	 * 
	 * @param file ciphertext file to read
	 * @return byte[] representing the ciphertext
	 */
	public static final byte[] readCipherTextFile(File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);				
			byte[] cipherText = new byte[fis.available()];
			fis.read(cipherText);
			fis.close();
			return cipherText;
		} catch (IOException ioe)
		{
			return null;
		}		
	}
	
	/**
	 * Reads a plain text ASCII document
	 * @param file file to read
	 * @return String representing the content of the file
	 */
	public static final String readPlainTextFile(File file)
	{
		StringBuffer buf = new StringBuffer();

		try
		{
			FileInputStream fis = new FileInputStream(file);
	
			int curChar=0;
			while( (curChar = fis.read()) != -1 )
			{
				buf.append((char) curChar);
			}				
		} catch (IOException ioe)
		{
			//Return an empty string!
		}
		
		return buf.toString();		
	}
	
	/**
	 * Converts a byte to hex digit and writes to the supplied buffer
	 * 
	 * @param b the byte
	 * @param buf the buffer that will contain the result
	 */
	public static final void byte2hex(byte b, StringBuffer buf) 
	{
		char[] hexChars = { '0', '1', '2', '3', 
							'4', '5', '6', '7',
							'8', '9', 'A', 'B', 
							'C', 'D', 'E', 'F' };
							
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}

	/**
	 * Converts a byte array to hex string
	 * 
	 * @param block block of bytes to convert to a hex representation 
	 * @return String the hex representation of the block
	 */
	public static final String toHexString(byte[] block) 
	{
		StringBuffer buf = new StringBuffer();
		int len = block.length;

		for (int i = 0; i < len; i++) 
		{
			 byte2hex(block[i], buf);
			 if (i < len-1) 
			 {
				 buf.append(":");
			 }
		} 
		return buf.toString();
	}

	/**
	 * Extracts data from a ByteBuffer and places it into a byte[]
	 * 
	 * @param buf ByteBuffer (already flipped) that contains the data to extract
	 * @param length size of the data in the ByteBuffer to process 
	 * @return a byte[] of the data from the ByteBuffer
	 */
	public static final byte[] toByteArray(ByteBuffer buf, int length)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < length; i++)
		{
			baos.write(buf.get());
		}		
		return baos.toByteArray();
	}
	
	/**
	 * Reads up to 1024 bytes of data from a SocketChannel and returns
	 * it as a byte[]
	 * 
	 * @param sc SocketChannel to read the data from
	 * @return a byte[] array of the raw data
	 * @throws IOException
	 */
	public static final byte[] readFromSocketChannel(SocketChannel sc) throws IOException
	{
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(1024);
		byteBuf.clear();
		int received = sc.read(byteBuf);
		byteBuf.flip();		
		return toByteArray(byteBuf, received);
	}	

}
