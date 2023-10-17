/*
 * (c) Copyright 2023 KineticFire. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kineticfire.util;



import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;


//todo remove references to Groovy's String



/**
 * Provides command line execution utilities.
 * <p>
 * The methods execute a given command, passed as an argument to the method, and then return the String output of that command.  The methods differ in return types, error handling, and the type of argument for the command.
 * <p>
 * Two types of methods, differing in error handling and return values, are available to execute commands:
 * <ol>
 *   <li>exec(...) returns a Map of the result of the command that includes a boolean value if the command was successful or not based on the exit value, the integer exit value, the output as a String, and (if an error did occur) the error output as a String.</li>
 *   <li>execWithException(...) returns the output of the command as a String or, if an error occurred, throws an exception.  The exit value and error output are captured in the exception's message.</li>
 * </ol>
 * <p>
 * Methods accepting two types of arguments for the command to execute are available:
 * <ol>
 *   <li>argument 'String' is generally simpler of the two methods to use, however complex commands/arguments may not work as expected</li>
 *   <li>argument 'String[]' will work as expected for more complex commands/arguments</li>
 * </ol>
 *
 */
public final class Exec {


   /**
    * Executes the command as a command line process under the current working directory using Groovy's String[].execute() method, and either returns a String result on success or throws an exception on failure.
    * <p>
    * Calls Groovy's toString() method on each item in the array.  The first item in the array is treated as the command and executed with Groovy's String.execute() method and any additional array items are treated as parameters.
    * <p>
    * Returns the output returned by the process as a trimmed String, which could be an empty String if the command didn't generate output.
    * <p>
    * This method is needed to use over the simpler 'exec( String task )' when using complex commands/arguments.  This method is similar to 'exec( String[] task )', except that method returns a Map with results (success or failure) while this method returns a String result on succcess and throws an exception on failure.
    * <p>
    * Requirements:
    * <ol>
    *    <li>Groovy</li>
    * </ol>
    * <p>
    * Limitations:
    * <ol>
    *    <li>Linux</li>
    * </ol>
    *
    * @param task
    *    the task (or command) to execute as a String array, where the first item is the command and any subsequent items are arguments
    * @return a String result of the command execution's output
    * @throws IOException
    *    if the task execution returned a non-zero exit value
    */
   public static String execWithException( String[] task, Map<String,String> config ) throws IOException { 

      String out = ""; // return empty string, if redirect output to files

      List<String> taskList = Arrays.asList( task );

      StringBuffer outSb = new StringBuffer( );
      StringBuffer errSb = new StringBuffer( );

      ProcessBuilder processBuilder = new ProcessBuilder( taskList );

      // if specified, then configure working directory for running the task
      if ( config.get( "directory" ) != null ) {
         processBuilder.directory( new File( config.get("directory" ) ) );
      }

      //todo
      // if specified, add and remove environment variables
      /*
      if ( config.get( "environment" ) != null ) {

         Map<String,String> env = processBuilder.environment( );
         Map<String,String> cfgEnv = config.get( "environment" );

         if ( cfgEnv.get( "add" ) != null ) {
            Map<String,String> addEnv = cfgEnv.get( "add" );
            for ( String key : addEnv.keySet( ) ) {
               env.put( key, addEnv.get( key ) );
            }
         }

         if ( cfgEnv.get( "remove" ) != null ) {
            Map<String,String> removeEnv = cfgEnv.get( "remove" );
            for ( String key : removeEnv.keySet( ) ) {
               env.remove( key, removeEnv.get( key ) );
            }
         }

      }
      */

      // redirect error stream to stdout if "true"
      if ( config.get( "redirectErrorStream" ) != null ) {
         if ( config.get( "redirectErrorStream" ).equalsIgnoreCase( "true" ) ) {
            processBuilder.redirectErrorStream( true );
         } else if ( config.get( "redirectErrorStream" ).equalsIgnoreCase( "true" ) ) {
            // do nothing
         } else {
            // todo throw exception
         }
      }


      Process proc = processBuilder.start( );

      waitForProcessOutput( proc, outSb, errSb );

      if ( proc.exitValue( ) != 0 ) {
         throw new IOException( "Executing command '" + task + "' failed with exit value " + proc.exitValue( ) + "." );


         /* todo: original groovy code for adding information about the error into the exception, if present
         StringBuffer sb = new StringBuffer( )

         sb.append( 'Executing command "' + task + '" failed with exit value ' + resultMap.get( 'exitValue' ) + '.' )

         if ( !resultMap.get( 'err' ).equals( '' ) ) {
            sb.append( '  ' + resultMap.get( 'err' ) )
         }

         throw new IOException( sb.toString( ) )
         */

      }

      return( outSb.toString( ) );

   }


   /**
    * Executes the command as a command line process under the current working directory using Groovy's String[].execute() method, and returns a Map result.
    * <p>
    * Calls Groovy's toString() method on each item in the array.  The first item in the array is treated as the command and executed with Groovy's String.execute() method and any additional array items are treated as parameters.
    * <p>
    * Returns a result as a Map with key-value pairs:
    * <ul>
    *    <ol>success - boolean true if the exec process was successful (exitValue is 0) and false otherwise (exitValue is non-zero)</ol>
    *    <ol>exitValue - the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error</ol>
    *    <ol>out - the output returned by the process as a trimmed String, which could be an empty String</ol>
    *    <ol>err - contains the error output returned by the process as a trimmed String; only present if an an error occurred e.g. exitValue is non-zero</ol>
    * </ul>
    * <p>
    * This method is needed to use over the simpler 'exec( String task )' when using complex commands/arguments. This method is similar to 'execWithException( String[] task )', except that method returns a String result on success and an exception on failure while this method returns a Map with results (success or failure).
    * <p>
    * Requirements:
    * <ol>
    *    <li>Groovy</li>
    * </ol>
    * <p>
    * Limitations:
    * <ol>
    *    <li>Linux</li>
    * </ol>
    *
    * @param task
    *    the task (or command) to execute as a String array, where the first item is the command and any subsequent items are arguments
    * @return a Map of the result of the command execution
    */
   public static Map<String,String> exec( String[] task ) { 

      Map<String,String> resultMap = new HashMap<String,String>( );
      /*
      String exitValue = Integer.toString( proc.exitValue( ) );
      resultMap.put( "exitValue", exitValue );

      resultMap.put( "out", outSb.toString( ).trim( ) );

      if ( exitValue.equals( "0" ) ) { 
         resultMap.put( "success", "true" );
      } else {
         resultMap.put( "success", "false" );
         resultMap.put( "err", errSb.toString( ).trim( ) );
      }   
      */

      /*
         //todo catches added for debug, but may be better to do so from execWithException()?
         //todo document throws!
      } catch ( NullPointerException e ) {
         System.out.println( "NullPointerException" );
      } catch ( IndexOutOfBoundsException e ) {
         System.out.println( "IndexOutOfBoundsException" );
      } catch ( SecurityException e ) {
         System.out.println( "SecurityException" );
      } catch ( UnsupportedOperationException e ) {
         System.out.println( "UnsupportedOperationException" );
      } catch ( IOException e ) {
         System.out.println( "IOException" + e.getMessage( ) );
      }
   */


      return( resultMap );

   }


   /**
    * Gets the output and error streams from a process and reads them
    * to keep the process from blocking due to a full output buffer.
    * The processed stream data is appended to the supplied Appendable.
    * For this, two Threads are started, but join()ed, so we wait.
    * As implied by the waitFor... name, we also wait until we finish
    * as well. Finally, the input, output and error streams are closed.
    *
    * @param self a Process
    * @param output an Appendable to capture the process stdout
    * @param error an Appendable to capture the process stderr
    *
    * From: Groovy 4.0.15
    * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
    * License: Apache License 2.0
    */
   private static void waitForProcessOutput( Process proc, Appendable output, Appendable error ) {

      Thread outThread = consumeProcessOutputStream( proc, output );
      Thread errThread = consumeProcessErrorStream( proc, error );


      boolean interrupted = false;

      try {

         try { 
            outThread.join( );
         } catch ( InterruptedException ignore ) { 
            interrupted = true;
         }

         try { 
            errThread.join( ); 
         } catch ( InterruptedException ignore ) { 
            interrupted = true; 
         }

         try { 
            proc.waitFor( );
         } catch ( InterruptedException ignore ) { 
            interrupted = true; 
         }

         closeStreams( proc );

      } finally {
         if ( interrupted ) {
            Thread.currentThread( ).interrupt( );
         }
      }

   }


    /**
     * Gets the output stream from a process and reads it
     * to keep the process from blocking due to a full output buffer.
     * The processed stream data is appended to the supplied Appendable.
     * A new Thread is started, so this method will return immediately.
     *
     * From: Groovy 4.0.15
     * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
     * License: Apache License 2.0
     *
     * @param self a Process
     * @param output an Appendable to capture the process stdout
     * @return the Thread
     */
    private static Thread consumeProcessOutputStream( Process proc, Appendable output ) {
        Thread thread = new Thread( new TextDumper( proc.getInputStream( ), output ) );
        thread.start( );
        return( thread );
    }


    /**
     * Gets the error stream from a process and reads it
     * to keep the process from blocking due to a full buffer.
     * The processed stream data is appended to the supplied Appendable.
     * A new Thread is started, so this method will return immediately.
     *
     * From: Groovy 4.0.15
     * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
     * License: Apache License 2.0
     *
     * @param self a Process
     * @param error an Appendable to capture the process stderr
     * @return the Thread
     */
    public static Thread consumeProcessErrorStream( Process proc, Appendable error ) {
        Thread thread = new Thread( new TextDumper( proc.getErrorStream( ), error ) );
        thread.start( );
        return( thread );
    }


    /*
     * From: Groovy 4.0.15
     * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
     * License: Apache License 2.0
     */
    private static class TextDumper implements Runnable {
        final InputStream in;
        final Appendable app;

        public TextDumper( InputStream in, Appendable app ) {
            this.in = in;
            this.app = app;
        }

        @Override
        public void run( ) {
            InputStreamReader isr = new InputStreamReader( in );
            BufferedReader br = new BufferedReader( isr );
            String next;
            try {
                while ( ( next = br.readLine( ) ) != null ) {
                    if ( app != null ) {
                        app.append( next );
                        app.append( "\n" );
                    }
                }
            } catch ( IOException e ) {
                throw new RuntimeException("Exception while reading process stream", e);
            }
        }
    }


   /*
    * Closes all streams associated with the process, ignoring any IOExceptions
    *
    *
    * From: Groovy 4.0.15
    * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
    * License: Apache License 2.0
    *
    * @param proc a Process
    */
   private static void closeStreams( Process proc ) {
        try { proc.getErrorStream( ).close( ); } catch ( IOException ignore ) { }
        try { proc.getInputStream( ).close( ); } catch ( IOException ignore ) { }
        try { proc.getOutputStream( ).close( ); } catch ( IOException ignore ) { }
    }


   private Exec( ) { 
      throw new UnsupportedOperationException( "Class instantiation not supported" );
   }
}
