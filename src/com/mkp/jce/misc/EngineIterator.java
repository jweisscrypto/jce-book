package com.mkp.jce.misc;

import java.security.Provider;
import java.util.Iterator;
import java.util.Map;

/**
 * Iterator useful for stepping over provider's supported engine meta-data
 * 
 * @author Jason R. Weiss
 * @version 1.0
 */
public final class EngineIterator implements Iterator
{
	public static final String ENGINE_SIGNATURE = "Signature";
	public static final String ENGINE_KEY_GENERATOR = "KeyGenerator";
	public static final String ENGINE_KEY_PAIR_GENERATOR = "KeyPairGenerator";
	public static final String ENGINE_CIPHER = "Cipher";
	public static final String ENGINE_KEY_AGREEMENT = "KeyAgreement";
	public static final String ENGINE_KEY_FACTORY = "KeyFactory";
	public static final String ENGINE_SECRET_KEY_FACTORY = "SecretKeyFactory";
	public static final String ENGINE_MESSAGE_DIGEST = "MessageDigest";
	public static final String ENGINE_MAC = "Mac";
	public static final String ENGINE_ALGORITHM_PARAMETERS = "AlgorithmParameters";	
	
	public static final String ALGORITHM_ALIAS = "Alg.Alias.";	

	private Provider _provider;	
	private Iterator _peekIter;
	private Iterator _actualIter;
	private String _engineType;
	private Map.Entry _next;
	private boolean _aliased = false;
	
	public EngineIterator(Provider provider, String engineType)
	{
		_provider = provider;
		_actualIter = provider.entrySet().iterator();
		_peekIter = provider.entrySet().iterator();
		_engineType = engineType;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		boolean hasNext = false;
		while(_peekIter.hasNext())
		{
			Map.Entry entry = (Map.Entry) _peekIter.next();
			if(entry.getKey().toString().startsWith(_engineType))
			{
				//match found; let's stop iterating
				//_actualIter will point to the next entry
				hasNext = true;
				_aliased = false;
				break;				
			} else if (entry.getKey().toString().startsWith(ALGORITHM_ALIAS + _engineType) )
			{
				//match found (alias); let's stop iterating
				//_actualIter will point to the next aliased entry
				hasNext = true;
				_aliased = true;
				break;
				
			} else
			{
				//no match yet, iterate the actual
				_actualIter.next();				
			}
		}
		return hasNext;
	}
	
	public boolean isAlias() { return _aliased; }

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		return _actualIter.next();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		_actualIter.remove();
	}

}
