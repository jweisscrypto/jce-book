package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * <B>Section 5.4</B>
 * <P>
 * Loads a DER encoded digital certificate from disk
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class ReadDERCertificate
{

	public static void main(String[] args)
	{
		try
		{
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			
			FileInputStream fis = new FileInputStream(new File("/jceRSAcert.der"));
			
			Certificate cert = factory.generateCertificate(fis);
			
			System.out.println("Certificate Loaded:\n" + cert.toString());
			fis.close();
			
		} catch (CertificateException ce)
		{
			ce.printStackTrace();
		} catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
