package com.mkp.jce.chap5;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
 * Loads a Base64 encoded digital certificate from disk
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class ReadBase64EncodedCertificate
{

	public static void main(String[] args)
	{
		try
		{
			CertificateFactory factory =
				CertificateFactory.getInstance("X.509");

			try
			{
				FileInputStream fis =
					new FileInputStream(new File("/jceDSAcert.b64"));

				try
				{
					DataInputStream dis = new DataInputStream(fis);

					byte[] bytes = new byte[dis.available()];

					try
					{
						dis.readFully(bytes);

						ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

						while (bais.available() > 0)
						{
							Certificate cert = factory.generateCertificate(bais);
							System.out.println(
								"Certificate Loaded:\n" + cert.toString());
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

				} catch (IOException ioe)
				{
					ioe.printStackTrace();
				}

			} catch (FileNotFoundException fnfe)
			{
				fnfe.printStackTrace();
			}
		} catch (CertificateException ce)
		{
			ce.printStackTrace();
		}
	}
}
