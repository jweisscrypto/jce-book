package com.mkp.jce.chap4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 4.8.1</B>
 * <P>
 * Generates a digital signature using a private key
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class ApplySignature
{

	public static void main(String[] args)
	{		
		//
		// NOTE:  This sample relies on a set of RSA keys generated
		// from the com.mkp.jce.chap3.GenerateKeyPair sample code.  It
		// assumes the keys are at /jcebook.pubkey and /jcebook.privkey
		//
		try
		{

			String fileData = CryptoUtil.readPlainTextFile(new File("/velocity.log"));
					
			//Be sure to copy the jcebook.privkey to your working directory
			//or change to an absolute path to ensure the files found			
			FileInputStream fis = new FileInputStream(new File("/jcebook.privkey"));

			ByteArrayOutputStream privKeyBaos = new ByteArrayOutputStream();		
	
			int curByte=0;
			while( (curByte = fis.read()) != -1 )
			{
				privKeyBaos.write(curByte);
			}
			
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyBaos.toByteArray());
			privKeyBaos.close();
			KeyFactory keyFactoryEngine = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactoryEngine.generatePrivate(keySpec);
		
			Signature signatureEngine = Signature.getInstance("MD5withRSA");
		
			//Initialize the signature to be in sign mode		
			signatureEngine.initSign(privateKey);
			
			//Pass in the bytes of our velocity.log sample file
			signatureEngine.update(fileData.getBytes("UTF-8"));
			
			//Obtain the signature
			byte[] signature = signatureEngine.sign();			

			//Save the signature for use in the VerifySignature code sample
			FileOutputStream fos = new FileOutputStream(new File("/signature.data"));
			fos.write(signature);
			fos.close();
			
			System.out.println("MD5 with RSA Signature Length: " + signature.length);
			for(int i =0;i < signature.length;i++)
			{
				System.out.print(signature[i] + " ");
			}
			System.out.println("");
		
		} catch (IOException ioe)
		{
			System.out.println("Couldn't find the file.  Did you adjust the path accordingly?");
			System.exit(0);
		} catch (NoSuchAlgorithmException nsae)
		{
			nsae.printStackTrace();
		} catch (InvalidKeySpecException ikse)
		{
			ikse.printStackTrace();
		} catch (InvalidKeyException ike)
		{
			ike.printStackTrace();
		} catch (SignatureException se)
		{
			se.printStackTrace();
		}
	}
}
