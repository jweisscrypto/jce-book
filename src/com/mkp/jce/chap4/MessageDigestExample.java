package com.mkp.jce.chap4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <B>Section 4.5.1</B>
 * <P>
 * Generates a message digest for a file
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class MessageDigestExample
{

	public static void main(String[] args)
	{
		StringBuffer buf = new StringBuffer();

		try
		{
			//Be sure to copy the velocity.log to your root directory
			//or change to an absolute path to ensure the files found			
			FileInputStream fis = new FileInputStream(new File("/velocity.log"));
		
			int curChar=0;
			while( (curChar = fis.read()) != -1 )
			{
				buf.append((char) curChar);
			}				
		} catch (IOException ioe)
		{
			//Handle this!
			System.out.println("Couldn't find the file.  Did you adjust the path accordingly?");
			System.exit(0);
		} 
		
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");			
			md5.update(buf.toString().getBytes("UTF-8"));
			
			byte[] digest = md5.digest(buf.toString().getBytes());
			System.out.println("MD5 Digest Length: " + digest.length);
			
			for(int i =0;i<digest.length;i++)
			{
				System.out.print(digest[i] + " ");
			}
			System.out.println("");		

		} catch (NoSuchAlgorithmException nsae)
		{
			nsae.printStackTrace();
		} catch (UnsupportedEncodingException uee)
		{
			uee.printStackTrace();
		}
	}
}
