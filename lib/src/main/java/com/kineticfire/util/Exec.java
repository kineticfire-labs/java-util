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


//todo remove references to Groovy's String
//todo update class docs


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


    //todo if redirecting output to a file, then returns empty string regardless of what was captured on standard output and output to a file
    //todo if method throws exception on any error, then can't redirect err to to out or redirect err to file.  because then couldn't see err to throw exception.
    //todo what error is thrown if the task itself errs... TaskExecutionException?
   
   /**
    * Executes the task as a native command line process and returns as a String a successful result, throwing exceptions on any task execution failure.
    * <p>
    * This method provides a convenience wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is equivalent to 'exec(...)', except that method always returns a Map&lt;String,String&gt; result and never throws an exception while this method returns a String if succesful and otherwise throws an exception on any failure.  This method is applicable to cases for executing a command and receiving a String result (without checking for success or presence of the key in the map) and responding to any error in try-catch blocks; or allowing the error to be thrown, which can be useful in test setups.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output when not written to a file and "false" otherwise"; defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; defaults to the current directory from which the program is executed</li>
    *    <li>redirectErrToOut - "true" to redirect the standard error to standard output; otherwise and default is not to redirect standard error; cannot be used in combination with 'redirectErrToFile' otherwise an exception will be thrown</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents</li>
    *    <li>redirectErrFilePath - redirect standard error by providing a file path and name of the error file; must also define 'redirectErrType' otherwise an exception is thrown; cannot use in conjection with 'redirectErrToFile' otherwise an error is thrown</li>
    *    <li>redirectErrType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents</li>
    * </ul>
    * <p>
    * The optional addEnv (which may be null or empty) defines environment variables as key-value pairs to add when executing the task.
    * <p>
    * The optional removeEnv (which may be null or empty) defines environment variables as a list to remove when executing the task.
    * <p>
    * Returns a String result of the task execution on success, and throws an exception on any error.
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments
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
    *    <ol>
    *       <li>if the operating system does not support the creation of processes, or</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ol>
    */
   public static String execWithException( List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv ) throws IOException { 

      String out = ""; // return empty string (unless exception thrown)

      Map<String,String> resultMap = execImpl( task, config, addEnv, removeEnv );
            /* return is as below, or an exception is thrown:
             *    - exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error
             *    - out       - the output returned by the process as a String, which could be an empty String; only present if the output wasn't redirected to a file
             *    - err       - contains the error output returned by the process as a String; only present if an an error occurred, e.g. exitValue is non-zero, and standard error wasn't merged with standard output and standard error wasn't redirected to a file
             */


      if ( resultMap.get( "exitValue" ).equals( "0" ) ) {

         // key 'out' may not be defined, even for exitValue=0, if output redirected to file
         if ( resultMap.containsKey( "out" ) && resultMap.get( "out" ) != null ) {
            out = resultMap.get( "out" );
         }

      } else if ( !resultMap.get( "exitValue" ).equals( "0" ) ) { 
          
         StringBuffer sb = new StringBuffer( );

         sb.append( "Executing command '" + task + "' failed with exit value '" + resultMap.get( "exitValue" ) + "." );

         // key 'err' may not be defined, even in error condition, if error redirected
         if ( resultMap.containsKey( "err" ) && resultMap.get( "err" ) != ( null ) && !resultMap.get( "err" ).equals( "" ) ) {
            sb.append( "  " + resultMap.get( "err" ) );
         }

         throw new IOException( sb.toString( ) );
         //todo throw a TaskExecutionException??
      }


      return( out );

   }


   /**
    * Returns a result as a Map with key-value pairs, ... does not throw an exception.  Returns key 'success'.
    * <ul>
    *    <li>success - boolean "true" if the task execution was successful (exitValue is 0) and false otherwise (exitValue is non-zero); always defined</li>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; defined unless an exception is thrown</li>
    *    <li>out - the output returned by the process as a String (trimmed by default or if 'trimmed' is set to 'true' in the 'config', unless 'trimmed' set to 'false') if standard output wasn't redirected to a file; 'out' is populated even if the task execution resulted in an error (non-zero exitValue), unless stndard output was redirected to a file; not defined if standard output is redirected to a file or if an exception was thrown which is described in the error output</li>
    *    <li>err - the error output returned by the process as a String (trimmed by default or if 'trimmed' is set to 'true' in the 'config', unless 'trimmed' set to 'false') or a description of the exception, if thrown, if standard error wasn't redirected to standard output or to a file; only present if an an execution error occurred (e.g. exitValue is non-zero) or if an exception was thrown, and standard error wasn't redirected to standard output or to a file</li>
    * </ul>
    */


   /**
    * Executes the task as a native command line process and returns a Map result, without throwing exceptions.
    * <p>
    * This method provides a convenience wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * This method is equivalent to 'execWithException(...)', except that method returns a String if successful otherwise throws an exception on any error while this method returns a Map&lt;String,String&gt;result and does not throw exceptions.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output when not written to a file and "false" otherwise"; defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; defaults to the current directory from which the program is executed</li>
    *    <li>redirectErrToOut - "true" to redirect the standard error to standard output; otherwise and default is not to redirect standard error; cannot be used in combination with 'redirectErrToFile' otherwise an exception will be thrown</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents</li>
    *    <li>redirectErrFilePath - redirect standard error by providing a file path and name of the error file; must also define 'redirectErrType' otherwise an exception is thrown; cannot use in conjection with 'redirectErrToFile' otherwise an error is thrown</li>
    *    <li>redirectErrType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents</li>
    * </ul>
    * <p>
    * The optional addEnv (which may be null or empty) defines environment variables as key-value pairs to add when executing the task.
    * <p>
    * The optional removeEnv (which may be null or empty) defines environment variables as a list to remove when executing the task.
    * <p>
    * Returns a Map with key-value pairs, unless an exception is thrown:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a String, which could be an empty String; defined unless the output was redirected to a file</li>
    *    <li>err - contains the error output returned by the process as a String; defined unless an an error occurred (e.g. exitValue is non-zero), standard error wasn't merged with standard output, and standard error wasn't redirected to a file</li>
    * </ul>
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments
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
    *    <ol>
    *       <li>if the operating system does not support the creation of processes, or</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ol>
    */
   public static Map<String,String> exec( List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv ) { 

      Map<String,String> resultMap = new HashMap<String,String>( );
      resultMap.put( "success", "false" );

      try {

         resultMap = execImpl( task, config, addEnv, removeEnv );
            /* return is as below, or an exception is thrown:
             *    - exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error
             *    - out       - the output returned by the process as a String, which could be an empty String; only present if the output wasn't redirected to a file
             *    - err       - contains the error output returned by the process as a String; only present if an an error occurred, e.g. exitValue is non-zero, and standard error wasn't merged with standard output and standard error wasn't redirected to a file
             */
         

         if ( resultMap.get( "exitValue" ).equals( "0" ) ) { 
            resultMap.put( "success", "true" );
         }
         // else: already set success->false
      
      } catch ( NullPointerException e ) {
         resultMap.put( "err", generateErrorMessageFromException( e ) );
      } catch ( IndexOutOfBoundsException e ) {
         resultMap.put( "err", generateErrorMessageFromException( e ) );
      } catch ( SecurityException e ) {
         resultMap.put( "err", generateErrorMessageFromException( e ) );
      } catch ( UnsupportedOperationException e ) {
         resultMap.put( "err", generateErrorMessageFromException( e ) );
      } catch ( IOException e ) {
         resultMap.put( "err", generateErrorMessageFromException( e ) );
      } catch ( Exception e ) {
         resultMap.put( "err", generateErrorMessageFromException( e ) );
      }
      //todo are these all exceptions?
      //todo are these all exceptions needed, or will getClass().getName() be sufficient?


      return( resultMap );

   }



   private static String generateErrorMessageFromException( Exception e ) {

      StringBuilder sb = new StringBuilder( );

      sb.append( e.getClass( ).getName( ) );

      if ( e.getMessage( ) != null && e.getMessage( ) != "" ) {
         sb.append( ": " + e.getMessage( ) );
      }

      return( sb.toString( ) );
   }


   /*
    * Executes the task as a native command line process and returns a Map result, throwing exceptions if they occur.
    * <p>
    * This method provides a convenience wrapper around Java's ProcessBuilder and Process for simplifying configuration through convention, handling access to and buffering process outputs, and promptly writing to the input stream and reading from the output stream to prevent process block or deadlock.
    * <p>
    * The first item in the task list is treated as the command and any additional items are treated as parameters.
    * <p>
    * The optional config (which may be null or empty) defines configuration as key-value pairs as follows:
    * <ul>
    *    <li>trim - "true" to trim standard output and error output when not written to a file and "false" otherwise"; defaults to "true"</li>
    *    <li>directory - the working directory in which the task should execute; defaults to the current directory from which the program is executed</li>
    *    <li>redirectErrToOut - "true" to redirect the standard error to standard output; otherwise and default is not to redirect standard error; cannot be used in combination with 'redirectErrToFile' otherwise an exception will be thrown</li>
    *    <li>redirectOutFilePath - redirect standard output by providing a file path and name of the output file; must also define 'redirectOutType' otherwise an exception is thrown</li>
    *    <li>redirectOutType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents</li>
    *    <li>redirectErrFilePath - redirect standard error by providing a file path and name of the error file; must also define 'redirectErrType' otherwise an exception is thrown; cannot use in conjection with 'redirectErrToFile' otherwise an error is thrown</li>
    *    <li>redirectErrType - 'overwrite' to overwrite the contents of the file and 'append' to append additional output to existing file contents</li>
    * </ul>
    * <p>
    * The optional addEnv (which may be null or empty) defines environment variables as key-value pairs to add when executing the task.
    * <p>
    * The optional removeEnv (which may be null or empty) defines environment variables as a list to remove when executing the task.
    * <p>
    * Returns a Map with key-value pairs, unless an exception is thrown:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a String, which could be an empty String; defined unless the output was redirected to a file</li>
    *    <li>err - contains the error output returned by the process as a String; defined unless an an error occurred (e.g. exitValue is non-zero), standard error wasn't merged with standard output, and standard error wasn't redirected to a file</li>
    * </ul>
    *
    * @param task
    *    the task to execute as a String List, where the first item is the command and any subsequent items are arguments
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
    *    <ol>
    *       <li>if the operating system does not support the creation of processes, or</li>
    *       <li>if configuring environment variables and the system does not allow such modifications</li>
    *    </ol>
    */
   private static Map<String,String> execImpl( List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv ) throws IOException { 

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
     * @param self a Process
     * @param output an Appendable to capture the process stdout
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
     * @param self a Process
     * @param error an Appendable to capture the process stderr
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
