package com.mkp.jce.chap3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 3.5.1</B>
 * <P>
 * Bob establishes a server to listen for Alice's communication request.
 * 
 * @see com.mkp.jce.chap3.AliceClient 
 * @author Jason R. Weiss
 * @version 1.0
 */
public class BobServer
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Waiting for Alice's call...");

			ServerSocketChannel ssc = ServerSocketChannel.open();
			InetSocketAddress isa =
				new InetSocketAddress(InetAddress.getLocalHost(), 8888);
			ssc.socket().bind(isa);
			SocketChannel sc = ssc.accept();

			try
			{
				String response = new String(CryptoUtil.readFromSocketChannel(sc));
				System.out.print("Received: " + response);

				System.out.println("Sending Hello Back");
				ByteBuffer helloBuf = ByteBuffer.wrap("Hello\n".getBytes("UTF-8"));
				int sent = sc.write(helloBuf);
				
				byte[] alicePubKeyEnc = CryptoUtil.readFromSocketChannel(sc);
				System.out.println("Received Alice's Encoded Public Key: " + alicePubKeyEnc.length + " bytes");

				System.out.println("Sending my Encoded Public Key");				

				KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);
				PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec);
				DHParameterSpec dhParamSpec = ((DHPublicKey) alicePubKey).getParams();

				System.out.println("Generate DH keypair ...");
				KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
				bobKpairGen.initialize(dhParamSpec);
				KeyPair bobKpair = bobKpairGen.generateKeyPair();

				System.out.println("Initializing KeyAgreement engine...");
				KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
				bobKeyAgree.init(bobKpair.getPrivate());

				byte[] bobPubKeyEnc = bobKpair.getPublic().getEncoded();

				System.out.println("Sending Encoded Public Key- " + bobPubKeyEnc.length + " bytes");
				ByteBuffer encodedCert = ByteBuffer.wrap(bobPubKeyEnc);
				sent = sc.write(encodedCert);

				System.out.println("Execute PHASE1 ...");
				bobKeyAgree.doPhase(alicePubKey, true);

				byte[] bobSharedSecret = bobKeyAgree.generateSecret();
				System.out.println("Shared secret (DEBUG ONLY): " + CryptoUtil.toHexString(bobSharedSecret));
				  
				bobKeyAgree.doPhase(alicePubKey, true);
				SecretKey bobDesKey = bobKeyAgree.generateSecret("DES");
				  
				byte[] recoveredPlaintext = CryptoUtil.readFromSocketChannel(sc);				  
				  
				Cipher bobCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
				bobCipher.init(Cipher.DECRYPT_MODE, bobDesKey);
				String recovered = new String(bobCipher.doFinal(recoveredPlaintext));
				
				System.out.println("Decrypted Message: " + recovered);
				
				
				bobCipher.init(Cipher.ENCRYPT_MODE, bobDesKey);
				byte[] plaintext = "Beat Michigan!".getBytes("UTF-8");
				byte[] ciphertext = bobCipher.doFinal(plaintext);
			   
				System.out.println("Sending encrypted response");
				ByteBuffer cipherBuf = ByteBuffer.wrap(ciphertext);
				sent = sc.write(cipherBuf);
				
				response = new String(CryptoUtil.readFromSocketChannel(sc));
				System.out.print("Received: " + response);

			} catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			} catch (InvalidKeySpecException e)
			{
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e)
			{
				e.printStackTrace();
			} catch (InvalidKeyException e)
			{
				e.printStackTrace();
			} catch (NoSuchPaddingException e)
			{
				e.printStackTrace();
			} catch (IllegalStateException e)
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
		System.out.println("Done");
	}
}