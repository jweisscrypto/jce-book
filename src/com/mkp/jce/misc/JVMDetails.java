package com.mkp.jce.misc;

/**
 * <p>
 * Provides a looking-glass view of all of the system properties of the JVM its run under.
 * </p>
 *
 * @version 1.0
 * @author Jason R. Weiss
 */
public class JVMDetails 
{

    public static void main(String[] args) 
    {
		java.util.Properties props	= System.getProperties();

    	try
    	{
    		if(args[0].equals("-?")) usage();
    		
    		String value = props.getProperty(args[0]);
    		
			System.out.print("'" + args[0] + "' ");
			System.out.println( (null == value)? "couldn't be located in the system properties." : "= " + value );
			
    	} catch (ArrayIndexOutOfBoundsException aiobe)
    	{
    		//No command line args, so print out all the properties!
			props.list(System.out);

			System.out.println( "--Full CLASSPATH--" );
			System.out.println( System.getProperty("java.class.path"));
			System.out.println( "--Full BOOTCLASSPATH--" );
			System.out.println( System.getProperty("sun.boot.class.path"));
    	}
	}
	
	public static void usage()
	{
		System.out.println("Usage:");
		System.out.println("  JVMDetails [some.known.property]");
		System.out.println("");
		System.out.println("  If no command line arguments are specified, a listing of all known properties is printed.");
		System.exit(0);
	}
	

}