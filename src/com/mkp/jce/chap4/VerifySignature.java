package com.mkp.jce.chap4;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 4.8.2</B>
 * <P>
 * Verifies a digital signature using a public key
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class VerifySignature
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

			PublicKey publicKey = CryptoUtil.loadPublicKey(new File("/jcebook.pubkey"), "RSA");
					
			Signature signatureEngine = Signature.getInstance("MD5withRSA");
		
			//Initialize the signature to be in sign mode		
			signatureEngine.initVerify(publicKey);
			
			//Pass in the bytes of our velocity.log sample file
			signatureEngine.update(fileData.getBytes("UTF-8"));
			
			byte[] signatureFromAlice = CryptoUtil.readSignatureFile(new File("/signature.data"));

			boolean isVerified = false;
			try
			{
				isVerified = signatureEngine.verify(signatureFromAlice);
			} catch (SignatureException se)
			{
				//assuredly the document isn't valid if you get here!
				System.out.println("Uh-oh.  Looks like the signature wasn't verified!");
			}
			
			System.out.println("Verified: " + isVerified);			
		
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
