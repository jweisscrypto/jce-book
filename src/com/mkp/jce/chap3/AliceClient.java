package com.mkp.jce.chap3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 3.5.1</B>
 * <P>
 * 
 * Alice wants to communicate important sensitive information to Bob, but
 * unfortunately they didn't exchange keys ahead of time.  Alice and Bob
 * realize that they can use a KeyAgreement engine to establish a shared
 * secret key without divulging what that shared secret is.  Bob will setup
 * a server in anticipation of Alice's call, and Alice will act as the client.
 * They agree on a very simple protocol for sharing their data:
 * 
 * <ol>
 *   <li>Alice sends a <b>Hello</b> message to Bob's server
 *   <li>Bob responds back with a similar <b>Hello</b> message to Alice
 *   <li>Alice forwards to Bob her encoded Diffie-Hellman public key
 *   <li>Bob, using Alice's key, generates his encoded Diffie-Hellman public key and responds back with it
 *   <li>Alice and Bob both calculate their shared secret using the exchanged keys
 *   <li>Alice sends Bob a message encrypted, for example, with DES/ECB/PKC8Padding
 *   <li>Bob deciphers the important message, and responds with his message
 *   <li>Alice receives Bob's message, and ends the call by sending <b>Bye-Bye</b>
 *   <li>Bob acknowledges the end of the conversation by responding with <b>Bye-Bye</b>
 * </ol>
 * 
 * Use the EvilEve client to watch the handshaking in plaintext and the ciphertext go bye.
 * 
 * <h2>Demo</h2>
 * <ol>
 *   <li>Open up 3 separate command or shell windows
 *   <li>Start up Bob's server first:  java com.mkp.jce.chap4.BobServer
 *   <li>Start up EvilEve next: java com.mkp.jce.chap4.EvilEve
 *   <li>Start up AliceClient last: java com.mkp.jce.chap4.AliceClient
 * </ol> 
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class AliceClient
{
	public static void main(String[] args)
	{
		try
		{
			InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), 8787);

			// Connect
			SocketChannel sc = SocketChannel.open();
			sc.connect(isa);
				
			try
			{
				System.out.println("Creating DH parameters (be patient!)...");
				AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
				paramGen.init(512);
				AlgorithmParameters params = paramGen.generateParameters();
				
				DHParameterSpec dhSkipParamSpec = 
					(DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
				
				
				System.out.println("Generating a DH KeyPair...");
				KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
				aliceKpairGen.initialize(dhSkipParamSpec);
				KeyPair aliceKpair = aliceKpairGen.generateKeyPair();
				
				System.out.println("Initializing the KeyAgreement Engine with DH private key");
				KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
				aliceKeyAgree.init(aliceKpair.getPrivate());
				
				byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();
				 
				System.out.println("Sending Hello");
				ByteBuffer helloBuf = ByteBuffer.wrap("Hello\n".getBytes("UTF-8"));
				int sent = sc.write(helloBuf);
				
				String response = new String(CryptoUtil.readFromSocketChannel(sc));
				System.out.print("Response: " + response);
				
				System.out.println("Sending my encoded public key- " + alicePubKeyEnc.length + " bytes");
				ByteBuffer encodedCert = ByteBuffer.wrap(alicePubKeyEnc);
				sent = sc.write(encodedCert);
				
				byte[] bobPubKeyEnc = CryptoUtil.readFromSocketChannel(sc);
				System.out.println("Response: Bob's Encoded Public Key: " + bobPubKeyEnc.length + " bytes");
												
				/*
				 * Use Bob's public key for the first (and only) phase
				 * of the DH protocol.  Instanticate a DH public key
				 * from Bob's encoded key material.
				 */
				KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
				PublicKey bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
				System.out.println("Executing PHASE1 of key agreement...");
				aliceKeyAgree.doPhase(bobPubKey, true);
				
				/*
				 * DH key agreement protocol complete.
				 * Generate the shared secret.
				 */
				byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();
				System.out.println("Alice secret (DEBUG ONLY):" + CryptoUtil.toHexString(aliceSharedSecret));
						
				// The previous invocation of generateSecret() reset the key
				// agreement object, so we call doPhase again prior to another
				// generateSecret call
				aliceKeyAgree.doPhase(bobPubKey, true);
				SecretKey aliceDesKey = aliceKeyAgree.generateSecret("DES");						
					
				/*
				* Alice encrypts, using DES in ECB mode
				*/
				Cipher aliceCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
				aliceCipher.init(Cipher.ENCRYPT_MODE, aliceDesKey);
				
				byte[] plaintext = "Go Buckeyes!".getBytes("UTF-8");
				byte[] ciphertext = aliceCipher.doFinal(plaintext);
				   
				System.out.println("Sending encrypted message");
				ByteBuffer cipherBuf = ByteBuffer.wrap(ciphertext);
				sent = sc.write(cipherBuf);
				   
				byte[] recoveredPlaintext = CryptoUtil.readFromSocketChannel(sc);
				
				aliceCipher.init(Cipher.DECRYPT_MODE, aliceDesKey);
				String recovered = new String(aliceCipher.doFinal(recoveredPlaintext));
					
				System.out.println("Decrypted Message: " + recovered);
				   
				System.out.println("Sending Bye-Bye");
				ByteBuffer byebyeBuf = ByteBuffer.wrap("Bye-Bye\n".getBytes("UTF-8"));
				sc.write(byebyeBuf);
				sc.close();
				
			} catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			} catch (InvalidKeySpecException e)
			{
				e.printStackTrace();
			} catch (InvalidKeyException e)
			{
				e.printStackTrace();
			} catch (IllegalStateException e)
			{
				e.printStackTrace();
			} catch (InvalidParameterSpecException e)
			{
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e)
			{
				e.printStackTrace();
			} catch (NoSuchPaddingException e)
			{
				e.printStackTrace();
			} catch (IllegalBlockSizeException e)
			{
				e.printStackTrace();
			} catch (BadPaddingException e)
			{
				e.printStackTrace();
			} finally
			{
				// Make sure we close the channel (and hence the socket)
				if (sc != null)
					sc.close();
			}
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}		
		System.out.println("Done.");
	}
}