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




import com.kineticfire.util.TaskExecutionException;


import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;



/**
 * Provides command line task execution utilities.
 * <p>
 * The methods execute a task as a native command line process, passed as a List&lt;String&gt; argument to the method, and then return the String output of that command.  The methods differ primarily in return type mechanisms, error handling, and ability to redirect standard error.
 *
 */
public final class Exec {


   /**
    * Executes a task as a native command line process and returns a Map result, returning error output from the process, if any, or redirecting it to a file.
    * <p>
    * This method provides a wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is a convenience method for 'exec(List&lt;String&gt;,null,null,null)' which is similar to 'execExceptionOnTaskFail(...)', however that method returns a String result or an exception on any failure (to include the command line task itself) while this method returns a Map&lt;String,String&gt; result for command line task success or failure else throws an exception for other failure cases.  Other 'exec(...)' methods allow standard error to be redirected to a file or to standard error, while 'execExceptionOnTaskFail(...)' does not.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.  The task is required.
    * <p>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a String, which could be an empty String; always defined</li>
    *    <li>err - contains the error output returned by the process as a String; defined if an error occurred (e.g. exitValue is non-zero)</li>
    * </ul>
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments; required
    * @return a Map of the result of the command execution
    * @throws IllegalArgumentException
    *    <ul>
    *       <li>if an illegal or innapropriate argument was passed to this method</li>
    *    </ul>
    * @throws IndexOutOfBoundsException
    *    if the task is an empty list
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    if an element in task list is null
    * @throws SecurityException if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process, its checkExec method doesn't allow creation of the subprocess, or</li>
    *       <li>its checkPermission method doesn't allow access to the process environment</li>
    *    </ul>
    * @throws UnsupportedOperationException
    *    if the operating system does not support the creation of processes
    */
   public static Map<String,String> exec( List<String> task )
        throws IOException { 

        return( exec( task, null, null, null ) );

   }


   /**
    * Executes a task as a native command line process and returns a Map result, returning error output from the process, if any, or redirecting it to a file.
    * <p>
    * This method provides a wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is a convenience method for 'exec(List&lt;String&gt;,Map&lt;String,String&gt;,null,null)' which is similar to 'execExceptionOnTaskFail(...)', however that method returns a String result or an exception on any failure (to include the command line task itself) while this method returns a Map&lt;String,String&gt; result for command line task success or failure else throws an exception for other failure cases.  This method allows standard error to be redirected to a file or to standard error, while 'execExceptionOnTaskFail(...)' does not.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.  The task is required.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output when not written to a file and "false" otherwise"; optional, defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; optional, defaults to the current directory from which the program is executed</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown; optional, defaults to returning standard output as  String in Map key 'out'</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents; required if defining 'redirectOutFilePath', otherwise defining will throw an exception</li>
    *    <li>redirectErrToOut - "true" to redirect the standard error to standard output; optional, default is not to redirect standard error; cannot be used in combination with 'redirectErrToFile' otherwise an exception will be thrown</li>
    *    <li>redirectErrFilePath - redirect standard error by providing a file path and name of the error file; must also define 'redirectErrType' otherwise an exception is thrown; cannot use in conjection with 'redirectErrToFile' otherwise an error is thrown; optional, defaults to return standard error in Map key 'err'</li>
    *    <li>redirectErrType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents; required if defining 'redirectErrFilePath', otherwise defining will throw an exception</li>
    * </ul>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a String, which could be an empty String; defined unless the output was redirected to a file</li>
    *    <li>err - contains the error output returned by the process as a String; defined if an error occurred (e.g. exitValue is non-zero), standard error wasn't merged with standard output, and standard error wasn't redirected to a file</li>
    * </ul>
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments; required
    * @param config
    *    a Map of key-value pairs defining the configuration; optional, can be empty or null
    * @return a Map of the result of the command execution
    * @throws IllegalArgumentException
    *    if an illegal or innapropriate argument was passed to this method
    * @throws IndexOutOfBoundsException
    *    if the task is an empty list
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    <ul>
    *       <li>if an element in task list is null, or</li>
    *       <li>if defining an output file with a null pathname</li>
    *    </ul>
    * @throws SecurityException if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process</li>
    *       <ul>
    *          <li>its checkExec method doesn't allow creation of the subprocess, or</li>
    *          <li>the standard input to the subprocess was redirected from a file and the security manager's checkRead method denies read access to the file, or</li>
    *          <li>the standard output or standard error of the subprocess was redirected to a file and the security manager's checkWrite method denies write access to the file, or</li>
    *       </ul>
    *       <ul>
    *          <li>its checkPermission method doesn't allow access to the process environment</li>
    *       </ul>
    *    </ul>
    * @throws UnsupportedOperationException
    *    if the operating system does not support the creation of processes
    */
   public static Map<String,String> exec( List<String> task, Map<String,String> config )
        throws IOException { 

        return( exec( task, config, null, null ) );

   }


   /**
    * Executes a task as a native command line process and returns a Map result, returning error output from the process, if any, or redirecting it to a file.
    * <p>
    * This method provides a wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is similar to 'execExceptionOnTaskFail(...)', however that method returns a String result or an exception on any failure (to include the command line task itself) while this method returns a Map&lt;String,String&gt; result for command line task success or failure else throws an exception for other failure cases.  This method allows standard error to be redirected to a file or to standard error, while 'execExceptionOnTaskFail(...)' does not.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.  The task is required.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output when not written to a file and "false" otherwise"; optional, defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; optional, defaults to the current directory from which the program is executed</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown; optional, defaults to returning standard output as  String in Map key 'out'</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents; required if defining 'redirectOutFilePath', otherwise defining will throw an exception</li>
    *    <li>redirectErrToOut - "true" to redirect the standard error to standard output; optional, default is not to redirect standard error; cannot be used in combination with 'redirectErrToFile' otherwise an exception will be thrown</li>
    *    <li>redirectErrFilePath - redirect standard error by providing a file path and name of the error file; must also define 'redirectErrType' otherwise an exception is thrown; cannot use in conjection with 'redirectErrToFile' otherwise an error is thrown; optional, defaults to return standard error in Map key 'err'</li>
    *    <li>redirectErrType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents; required if defining 'redirectErrFilePath', otherwise defining will throw an exception</li>
    * </ul>
    * <p>
    * The optional addEnv (which may be null or empty) defines environment variables as key-value pairs to add when executing the task.
    * <p>
    * The optional removeEnv (which may be null or empty) defines environment variables as a list to remove when executing the task.
    * <p>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a String, which could be an empty String; defined unless the output was redirected to a file</li>
    *    <li>err - contains the error output returned by the process as a String; defined if an error occurred (e.g. exitValue is non-zero), standard error wasn't merged with standard output, and standard error wasn't redirected to a file</li>
    * </ul>
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments; required
    * @param config
    *    a Map of key-value pairs defining the configuration; optional, can be empty or null
    * @param addEnv
    *    a Map of key-value of environment variables to add; optional, can be empty or null
    * @param removeEnv
    *    a List of environment variables to remove; optional, can be empty or null
    * @return a Map of the result of the command execution
    * @throws IllegalArgumentException
    *    <ul>
    *       <li>if an illegal or innapropriate argument was passed to this method</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ul>
    * @throws IndexOutOfBoundsException
    *    if the task is an empty list
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    <ul>
    *       <li>if an element in task list is null, or</li>
    *       <li>attempting to add null key environment variables, or</li> 
    *       <li>if defining an output file with a null pathname</li>
    *    </ul>
    * @throws SecurityException if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process</li>
    *       <ul>
    *          <li>its checkExec method doesn't allow creation of the subprocess, or</li>
    *          <li>the standard input to the subprocess was redirected from a file and the security manager's checkRead method denies read access to the file, or</li>
    *          <li>the standard output or standard error of the subprocess was redirected to a file and the security manager's checkWrite method denies write access to the file, or</li>
    *       </ul>
    *       <li>when attemping to configure the environment variables</li>
    *       <ul>
    *          <li>its checkPermission method doesn't allow access to the process environment</li>
    *       </ul>
    *    </ul>
    * @throws UnsupportedOperationException
    *    <ul>
    *       <li>if the operating system does not support the creation of processes, or</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ul>
    */
   public static Map<String,String> exec( List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv )
        throws IOException { 

      Map<String,String> resultMap = new HashMap<String,String>( );

      // define flags
      boolean outToFile = false;   // 'true' if standard output is redirected to a file and false otherwise
      boolean errToOut = false;    // 'true' if standard error is redirected to standard output and false otherwise
      boolean errToFile = false;   // 'true' if standard error is redirected to a file and false otherwise
      boolean errRedirect = false; // 'true' if standard error is being redirected to either standard output or to a file and false otherwise
      boolean trim = true;         // 'true' to trim returned output and error streams and 'false' otherwise; does not apply to standard output and error redirected to a file


      ProcessBuilder processBuilder = new ProcessBuilder( task );

      if ( config != null ) {

         if ( config.get( "trim" ) != null ) {
            if ( config.get( "trim" ).equals( "true" ) ) {
               // do nothing
            } else if ( config.get( "trim" ).equals( "false" ) ) {
               trim = false;
            } else {
               throw new IllegalArgumentException( "Illegal value '" + config.get( "trim" ) + "' for 'trim' in 'config'." );
            }
         }

         // if specified, then configure working directory for running the task
            // let Process throw exception if directory doesn't exist, pemissions issue,etc.
         if ( config.get( "directory" ) != null ) {
            processBuilder.directory( new File( config.get( "directory" ) ) );
         }


         if ( config.get( "redirectOutFilePath" ) != null && config.get( "redirectOutType" ) == null ) {
               throw new IllegalArgumentException( "Field 'redirectOutFilePath' is set in 'config', but field 'redirectOutType' is not set or null.  Must specify redirect output type as either 'overwrite' or 'append'." );
         } else if ( config.get( "redirectOutType" ) != null && config.get( "redirectOutFilePath" ) == null ) {
               throw new IllegalArgumentException( "Field 'redirectOutType' is set in 'config', but field 'redirectOutFilePath' is not set or null." );
         }

         // if specified, then redirect standard output to a file
         if ( config.get( "redirectOutFilePath" ) != null ) {

            outToFile = true;

            // set the output file
            File outFile = new File( config.get( "redirectOutFilePath" ) );

            if ( config.get( "redirectOutType" ).equalsIgnoreCase( "overwrite" ) ) {
               // create file if it doesn't exist; if file exists, then discard previous contents
               processBuilder.redirectOutput( Redirect.to( outFile ) );
            } else if ( config.get( "redirectOutType" ).equalsIgnoreCase( "append" ) ) {
               // create file if it doesn't exist; if file exists, then add contents to the end of existing contents
               processBuilder.redirectOutput( Redirect.appendTo( outFile ) );
            } else {
               throw new IllegalArgumentException( "Illegal value '" + config.get( "redirectOutType" ) + "' for 'redirectOutType' in 'config'." );
            }

         }


         if ( config.get( "redirectErrFilePath" ) != null && config.get( "redirectErrType" ) == null ) {
               throw new IllegalArgumentException( "Field 'redirectErrFilePath' is set in 'config', but field 'redirectErrType' is not set or null.  Must specify redirect output type as either 'overwrite' or 'append'." );
         } else if ( config.get( "redirectErrType" ) != null && config.get( "redirectErrFilePath" ) == null ) {
               throw new IllegalArgumentException( "Field 'redirectErrType' is set in 'config', but field 'redirectErrFilePath' is not set or null." );
         }

         // if specified, then redirect standard output to a file
         if ( config.get( "redirectErrFilePath" ) != null ) {

            errToFile = true;

            // set the output file
            File errFile = new File( config.get( "redirectErrFilePath" ) );

            if ( config.get( "redirectErrType" ).equalsIgnoreCase( "overwrite" ) ) {
               // create file if it doesn't exist; if file exists, then discard previous contents
               processBuilder.redirectError( Redirect.to( errFile ) );
            } else if ( config.get( "redirectErrType" ).equalsIgnoreCase( "append" ) ) {
               // create file if it doesn't exist; if file exists, then add contents to the end of existing contents
               processBuilder.redirectError( Redirect.appendTo( errFile ) );
            } else {
               throw new IllegalArgumentException( "Illegal value '" + config.get( "redirectErrType" ) + "' for 'redirectErrType' in 'config'." );
            }

         }


         // redirect error stream to stdout if "true"
         if ( config.get( "redirectErrToOut" ) != null ) {

            if ( config.get( "redirectErrToOut" ).equalsIgnoreCase( "true" ) ) {

               errToOut = true;

               if ( errToFile ) {
                  throw new IllegalArgumentException( "Illegal configuration in 'config'.  Can't both redirect standard error to standard output ('redirectErrToOut' is 'true') and redirect standard error to a file ('redirectErrToFile' is 'true')." );
               }

               processBuilder.redirectErrorStream( true );

            } else if ( config.get( "redirectErrToOut" ).equalsIgnoreCase( "false" ) ) {
               // do nothing
            } else {
               throw new IllegalArgumentException( "Illegal value '" + config.get( "redirectErrToOut" ) + "' for 'redirectErrToOut' in 'config'." );
            }
         }

         if ( errToOut  || errToFile ) {
            errRedirect = true;
         }

      }

      if ( addEnv != null || removeEnv != null ) {

         Map<String,String> env = processBuilder.environment( );

         // if specified, add environment variables
         if ( addEnv != null ) {
            for ( String key : addEnv.keySet( ) ) {
               env.put( key, addEnv.get( key ) );
            }
         }

         // if specified, remove environment variables
         if ( removeEnv != null ) {
            for ( String value : removeEnv ) {
               env.remove( value );
            }
         }

      }

      Process proc = processBuilder.start( );

      StringBuffer outSb = new StringBuffer( );
      StringBuffer errSb = new StringBuffer( );

      waitForProcessOutput( proc, outSb, errSb );

      resultMap.put( "exitValue", Integer.toString( proc.exitValue( ) ) );

      if ( !outToFile ) {
         // if output wasn't redirected to a file, then output is captured in the 'outSb' string buffer (which could be an empty string)
            // 'out' will be defined unless (1) standard output was redirected to a file or (2) an exception occurred

         String outString;

         if ( trim ) {
            outString = outSb.toString( ).trim( );
         } else {
            outString = outSb.toString( );
         }

         resultMap.put( "out", outString );

      }

      if ( proc.exitValue( ) != 0 ) {

         if ( !errRedirect ) {
            // if the process indicated an error (exit value > 0) and standard error wasn't redirected (to a file or to standard output), so error output is captured in the 'errSb' string buffer (which could be an empty string)
               // 'err' will only be defined when a process exit value was non-zero and not redirected. So 'err' is only defined when (1) an exception didn't occur so the task ran, (2) the task produced a non-zero exit value, (3) error was not redirected to standard out, and (4) error was not directed to a file

            String errString;

            if ( trim ) {
               errString = errSb.toString( ).trim( );
            } else {
               errString = errSb.toString( );
            }

            resultMap.put( "err", errString );
         }

      }

      return( resultMap );
   }


   /**
    * Executes a task as a native command line process and returns the output as a String on success, throwing exceptions on any task execution failure.
    * <p>
    * This method provides a wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is a convenience method for 'execExceptionOnTaskFail(List&lt;String&gt;,null,null,null)' which is similar to 'exec(...)', except that method returns a Map&lt;String,String&gt; result for successful or failed command line task and an exception in other cases while this method returns the output of the task as a String if succesful and otherwise throws an exception on any failure (including command line task failure).  A limitation of 'execExceptionOnTaskFail(...)' is that standard error cannot be redirected--to standard out or to a file--as with 'exec(...)' because task errors need to be observed by this method in order to throw the exception.  Information from standard error is available in the thrown exception.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.  Required.
    * <p>
    * <p>
    * Returns a String result of the task execution on success, and throws an exception on any error.  An empty String may be returned by the task.
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments; required
    * @return a String result of the command execution
    * @throws IllegalArgumentException
    *    if an illegal or innapropriate argument was passed to this method
    * @throws IndexOutOfBoundsException
    *    if the task is an empty list
    * @throws IOException
    *    if an I/O error occurs
    * @throws TaskExecutionException
    *    if the task run as a command line process failed, e.g. it returned a non-zero exit value
    * @throws NullPointerException
    *    if an element in task list is null
    * @throws SecurityException if a security manager exists and
    *    when attemping to start the process, its checkExec method doesn't allow creation of the subprocess
    * @throws UnsupportedOperationException
    *    if the operating system does not support the creation of processes
    */
   public static String execExceptionOnTaskFail( List<String> task ) 
           throws IOException, TaskExecutionException {

      return( execExceptionOnTaskFail( task, null, null, null ) );

   }


   /**
    * Executes a task as a native command line process and returns the output as a String on success, throwing exceptions on any task execution failure.
    * <p>
    * This method provides a wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is a convenience method for 'execExceptionOnTaskFail(List&lt;String&gt;,Map&lt;String,String&gt;,null,null)' which is similar to 'exec(...)', except that method returns a Map&lt;String,String&gt; result for successful or failed command line task and an exception in other cases while this method returns the output of the task as a String if succesful and otherwise throws an exception on any failure (including command line task failure).  A limitation of this method is that standard error cannot be redirected--to standard out or to a file--as with 'exec(...)' because task errors need to be observed by this method in order to throw the exception.  Information from standard error is available in the thrown exception.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.  Required.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output and "false" otherwise"; optional, defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; optional, defaults to the current directory from which the program is executed</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown; optional, defaults to returning standard output as  String in Map key 'out'</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents; required if defining 'redirectOutFilePath', otherwise defining will throw an exception</li>
    * </ul>
    * <p>
    * Returns a String result of the task execution on success, and throws an exception on any error.  An empty String may be returned by the task or when standard output is redirected to a file.
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments; required
    * @param config
    *    a Map of key-value pairs defining the configuration; optional, can be empty or null
    * @return a String result of the command execution
    * @throws IllegalArgumentException
    *    if an illegal or innapropriate argument was passed to this method
    * @throws IndexOutOfBoundsException
    *    if the task is an empty list
    * @throws IOException
    *    if an I/O error occurs
    * @throws TaskExecutionException
    *    if the task run as a command line process failed, e.g. it returned a non-zero exit value
    * @throws NullPointerException
    *    <ul>
    *       <li>if an element in task list is null, or</li>
    *       <li>if defining an output file with a null pathname</li>
    *    </ul>
    * @throws SecurityException if a security manager exists and
    *    when attemping to start the process, its checkExec method doesn't allow creation of the subprocess
    * @throws UnsupportedOperationException
    *    if the operating system does not support the creation of processes
    */
   public static String execExceptionOnTaskFail( List<String> task, Map<String,String> config ) 
           throws IOException, TaskExecutionException {
      return( execExceptionOnTaskFail( task, config, null, null ) );
   }


   /**
    * Executes a task as a native command line process and returns the output as a String on success, throwing exceptions on any task execution failure.
    * <p>
    * This method provides a wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is equivalent to 'exec(...)', except that method returns a Map&lt;String,String&gt; result for successful or failed command line task and an exception in other cases while this method returns the output of the task as a String if succesful and otherwise throws an exception on any failure (including command line task failure).  A limitation of this method is that standard error cannot be redirected--to standard out or to a file--as with 'exec(...)' because task errors need to be observed by this method in order to throw the exception.  Information from standard error is available in the thrown exception.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.  Required.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output and "false" otherwise"; optional, defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; optional, defaults to the current directory from which the program is executed</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown; optional, defaults to returning standard output as  String in Map key 'out'</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents; required if defining 'redirectOutFilePath', otherwise defining will throw an exception</li>
    * </ul>
    * <p>
    * The optional addEnv (which may be null or empty) defines environment variables as key-value pairs to add when executing the task.
    * <p>
    * The optional removeEnv (which may be null or empty) defines environment variables as a list to remove when executing the task.
    * <p>
    * Returns a String result of the task execution on success, and throws an exception on any error.  An empty String may be returned by the task or when standard output is redirected to a file.
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments; required
    * @param config
    *    a Map of key-value pairs defining the configuration; optional, can be empty or null
    * @param addEnv
    *    a Map of key-value of environment variables to add; optional, can be empty or null
    * @param removeEnv
    *    a List of environment variables to remove; optional, can be empty or null
    * @return a String result of the command execution
    * @throws IllegalArgumentException
    *    <ul>
    *       <li>if an illegal or innapropriate argument was passed to this method</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ul>
    * @throws IndexOutOfBoundsException
    *    if the task is an empty list
    * @throws IOException
    *    if an I/O error occurs
    * @throws TaskExecutionException
    *    if the task run as a command line process failed, e.g. it returned a non-zero exit value
    * @throws NullPointerException
    *    <ul>
    *       <li>if an element in task list is null, or</li>
    *       <li>attempting to add null key environment variables, or</li> 
    *       <li>if defining an output file with a null pathname</li>
    *    </ul>
    * @throws SecurityException if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process, its checkExec method doesn't allow creation of the subprocess, or</li>
    *       <li>when attemping to configure the environment variables, its checkPermission method doesn't allow access to the process environment, or</li>
    *    </ul>
    * @throws UnsupportedOperationException
    *    <ul>
    *       <li>if the operating system does not support the creation of processes, or</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ul>
    */
   public static String execExceptionOnTaskFail( List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv ) 
           throws IOException, TaskExecutionException {

      String out = ""; // return empty string, unless 'out' has data or an is exception thrown

      if ( config != null ) {
         // if a config was provided, then need to check for key-value pairs that shouldn't be set

         // if 'redirectErrToOut' defined and set to anything but 'false', then throw exception
         if ( config.get( "redirectErrToOut" ) != null && !config.get( "redirectErrToOut" ).equals( "false" ) ) {
             throw new IllegalArgumentException( "Illegal configuration in 'config'.  Either do not define 'redirectErrToOut' or set to 'false'." );
         }

         // if 'redirectErrFilePath' defined and not set to null, then throw exception
         if ( config.get( "redirectErrFilePath" ) != null ) {
             throw new IllegalArgumentException( "Illegal configuration in 'config'.  Cannot define 'redirectErrFilePath'." );
         }

         // if 'redirectErrType' defined and not set to null, then throw exception
         if ( config.get( "redirectErrType" ) != null ) {
             throw new IllegalArgumentException( "Illegal configuration in 'config'.  Cannot define 'redirectErrType'." );
         }

      }


      Map<String,String> resultMap = exec( task, config, addEnv, removeEnv );
            /* return is as below (unless an exception was thrown):
             *    - exitValue: the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined
             *    - out: the output returned by the process as a String, which could be an empty String; defined unless the output was redirected to a file
             *    - err: contains the error output returned by the process as a String; defined if an error occurred (e.g. exitValue is non-zero), standard error wasn't merged with standard output, and standard error wasn't redirected to a file
             */


      // 'exitValue' always defined
      if ( resultMap.get( "exitValue" ).equals( "0" ) ) {

         if ( resultMap.get( "out" ) != null ) {
            // 'out' defined, IF not redirected to a file
            out = resultMap.get( "out" );
         }

      } else {

         int exitValue;

         try {
             // 'exitValue' always defined
             exitValue = Integer.parseInt( resultMap.get( "exitValue" ) );
         } catch ( NumberFormatException ignore ) {
             exitValue = -1;
         }


         StringBuffer messageSb = new StringBuffer( );
         StringBuffer taskSb = new StringBuffer( );

         taskSb.append( "[" );

         // 'task' can't be null, otherwise Process would have thrown NullPointerException
         for ( String item : task ) {
             taskSb.append( item + "," );
         }

         taskSb.deleteCharAt( taskSb.length( ) - 1 ); // remove dangling ','

         taskSb.append( "]" );


         messageSb.append( "Executing task '" + taskSb.toString( ) + "' failed with exit value '" + exitValue + "." );

         // key 'err' always defined in this case, since error cannot be redirected output or a file; 'err' may be empty String
         if ( !resultMap.get( "err" ).equals( "" ) ) {
            messageSb.append( "  " + resultMap.get( "err" ) );
         }

        throw( new TaskExecutionException( messageSb.toString( ), exitValue ) );
          
      }


      return( out );

   }


   /**
    * Gets the output and error streams from a process and reads them
    * to keep the process from blocking due to a full output buffer.
    * The processed stream data is appended to the supplied Appendable.
    * For this, two Threads are started, but join()ed, so we wait.
    * As implied by the waitFor... name, we also wait until we finish
    * as well. Finally, the input, output and error streams are closed.
    *
    * @param self
    *    a Process
    * @param output
    *    an Appendable to capture the process stdout
    * @param error
    *    an Appendable to capture the process stderr
    */
   private static void waitForProcessOutput( Process proc, Appendable output, Appendable error ) {
      /*
       * From: Groovy 4.0.15
       * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
       * Downloaded: 2023-10-15
       * License: Apache License 2.0
       */

      Thread outThread = consumeProcessOutputStream( proc, output );
      Thread errThread = consumeProcessErrorStream( proc, error );


      boolean interrupted = false;

      try {
         try { outThread.join( ); } catch ( InterruptedException ignore ) { interrupted = true; }
         try { errThread.join( ); } catch ( InterruptedException ignore ) { interrupted = true; }
         try { proc.waitFor( ); } catch ( InterruptedException ignore ) { interrupted = true; }
         closeStreams( proc );
      } finally {
         if ( interrupted ) Thread.currentThread( ).interrupt( );
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
     * @param self
     *   a Process
     * @param output
     *   an Appendable to capture the process stdout
     * @return the Thread
     */
    private static Thread consumeProcessOutputStream( Process proc, Appendable output ) {
      /*
       * From: Groovy 4.0.15
       * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
       * Downloaded: 2023-10-15
       * License: Apache License 2.0
       */

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
     * @param self
     *   a Process
     * @param error
     *   an Appendable to capture the process stderr
     * @return the Thread
     */
    public static Thread consumeProcessErrorStream( Process proc, Appendable error ) {
      /*
       * From: Groovy 4.0.15
       * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
       * Downloaded: 2023-10-15
       * License: Apache License 2.0
       */

        Thread thread = new Thread( new TextDumper( proc.getErrorStream( ), error ) );
        thread.start( );
        return( thread );
    }


    /*
     * Captures text output.
     *
     */
    private static class TextDumper implements Runnable {
      /*
       * From: Groovy 4.0.15
       * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
       * Downloaded: 2023-10-15
       * License: Apache License 2.0
       */

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
    * @param proc
    *    process
    */
   private static void closeStreams( Process proc ) {
      /*
       * From: Groovy 4.0.15
       * Link: https://github.com/apache/groovy/blob/GROOVY_4_0_15/src/main/java/org/codehaus/groovy/runtime/ProcessGroovyMethods.java
       * Downloaded: 2023-10-15
       * License: Apache License 2.0
       */

        try { proc.getErrorStream( ).close( ); } catch ( IOException ignore ) { }
        try { proc.getInputStream( ).close( ); } catch ( IOException ignore ) { }
        try { proc.getOutputStream( ).close( ); } catch ( IOException ignore ) { }
    }


   private Exec( ) { 
      throw new UnsupportedOperationException( "Class instantiation not supported" );
   }
}
