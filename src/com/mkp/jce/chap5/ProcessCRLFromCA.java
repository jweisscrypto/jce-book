package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.util.Iterator;
import java.util.Set;

/**
 * <B>Section 5.4.2</B>
 * <P>
 * Demonstrates loading a manually downloaded CRL from CA which could
 * be used to verify that a certificate in hand hasn't been revoked.
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class ProcessCRLFromCA
{

	public static void main(String[] args)
	{
		try
		{
			FileInputStream fis = new FileInputStream(new File("/ThawteServerCA.crl"));
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509CRL crl = (X509CRL) factory.generateCRL(fis);
			fis.close();
			
			Set crlSet = crl.getRevokedCertificates();
			Iterator iter = crlSet.iterator();
			while(iter.hasNext())
			{
				X509CRLEntry entry = (X509CRLEntry) iter.next();
				System.out.print("Certificate with serial #");
				System.out.print(entry.getSerialNumber());
				System.out.print(" was revoked on ");
				System.out.println(entry.getRevocationDate());				
			}
			
		} catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		} catch (CertificateException e)
		{
			e.printStackTrace();
		} catch (CRLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
