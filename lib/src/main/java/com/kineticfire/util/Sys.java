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



import java.nio.file.Path;
import java.io.File;
import java.util.Map;



/**
 * Provides system utilities.
 *
 */
public final class Sys {


   /**
    * Validates the script using a native command line process to run an OS-specific validation utility and returns a Map result, including any error output from the process.
    * <p>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process; OS-specific meaning, where for Unix-like platforms the value is on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a trimmed String, which could be an empty String; always defined</li>
    *    <li>err - contains the error output returned by the process as a trimmed String; defined if an error occurred (e.g. exitValue is non-zero)</li>                                                  
    * </ul>
    *
    * @param script
    *    the path as a Path to the script to validate
    * @return result as a Map of the script validation
    * @throws IllegalArgumentException
    *    if an illegal or inappropriate argument was passed to this method
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    if an element in task list is null
    * @throws SecurityException
    *    if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process, its checkExec method doesn't allow creation of the subprocess, or</li>
    *       <li>its checkPermission method doesn't allow access to the process environment</li>
    *    </ul>
    * @throws UnsupportedOperationException
    *    <ul>
    *       <li>if the operating system does not support the creation of processes</li>
    *       <li>if the operating system is not supported by this method</li>
    *    </ul>
    */
   public static Map<String,String> validateScript( Path script ) {
      return( validateScript( script.toString( ) ) );
   }


   /**
    * Validates the script using a native command line process to run an OS-specific validation utility and returns a Map result, including any error output from the process.
    * <p>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process; OS-specific meaning, where for Unix-like platforms the value is on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a trimmed String, which could be an empty String; always defined</li>
    *    <li>err - contains the error output returned by the process as a trimmed String; defined if an error occurred (e.g. exitValue is non-zero)</li>                                                  
    * </ul>
    *
    * @param script
    *    the path as a File to the script to validate
    * @return result as a Map of the script validation
    * @throws IllegalArgumentException
    *    if an illegal or inappropriate argument was passed to this method
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    if an element in task list is null
    * @throws SecurityException
    *    if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process, its checkExec method doesn't allow creation of the subprocess, or</li>
    *       <li>its checkPermission method doesn't allow access to the process environment</li>
    *    </ul>
    * @throws UnsupportedOperationException
    *    <ul>
    *       <li>if the operating system does not support the creation of processes</li>
    *       <li>if the operating system is not supported by this method</li>
    *    </ul>
    */
   public static Map<String,String> validateScript( File script )
         throws IOException {

      return( validateScript( script.toString( ) ) );

   }


   /**
    * Validates the script using a native command line process to run an OS-specific validation utility and returns a Map result, including any error output from the process.
    * <p>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process; OS-specific meaning, where for Unix-like platforms the value is on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a trimmed String, which could be an empty String; always defined</li>
    *    <li>err - contains the error output returned by the process as a trimmed String; defined if an error occurred (e.g. exitValue is non-zero)</li>                                                  
    * </ul>
    *
    * @param script
    *    the path as a String to the script to validate
    * @return result as a Map of the script validation
    * @throws IllegalArgumentException
    *    if an illegal or inappropriate argument was passed to this method
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    if an element in task list is null
    * @throws SecurityException
    *    if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process, its checkExec method doesn't allow creation of the subprocess, or</li>
    *       <li>its checkPermission method doesn't allow access to the process environment</li>
    *    </ul>
    * @throws UnsupportedOperationException
    *    <ul>
    *       <li>if the operating system does not support the creation of processes</li>
    *       <li>if the operating system is not supported by this method</li>
    *    </ul>
    */
   public static Map<String,String> validateScript( String script )
         throws IOException {

      Map<String,String> responseMap = new HashMap<String,String>( );

      String os = System.getProperty( "os.name" ).toLowerCase( );

      if ( os.indexOf( "win" ) >= 0 ) {
         throw new UnsupportedOperationException( "Script validation not supported on Windows." );
      } else if ( os.indexOf( "mac" ) >= 0 ) {
         throw new UnsupportedOperationException( "Script validation not supported on Mac." );
      } else if ( os.indexOf( "nix" ) >= 0 ) {
         responseMap = validateScriptForUnixLikePlatform( script );
      } else if ( os.indexOf( "sunos" ) >= 0 ) {
         throw new UnsupportedOperationException( "Script validation is not supported on SunOS." );
      }

      return( responseMap );
   }


   /*
    * Validates the script using a native command line process to run the 'shellcheck' utility and returns a Map result, including any error output from the process.
    * <p>
    * Uses the 'shellcheck' utility for static analysis and linting tool for sh/bash scripts.
    * <p>
    * Returns a Map (unless an exception is thrown) with key-value pairs:
    * <ul>
    *    <li>exitValue - the String representation of the integer exit value returned by the process on the range of [0,255]; 0 for success and other values indicate an error; always defined</li>
    *    <li>out - the output returned by the process as a trimmed String, which could be an empty String; always defined</li>
    *    <li>err - contains the error output returned by the process as a trimmed String; defined if an error occurred (e.g. exitValue is non-zero)</li>                                                  
    * </ul>
    * <p>
    * Requirements:
    * <ul>
    *    <li>Unix-like system</li>
    *    <li>'shellcheck' utility is installed</li>
    *    <li>script to validate is a bash/sh script</li>
    * </ul>
    *
    * @param script
    *    the path as a String to the sh/bash script to validate
    * @return result as a Map of the script validation
    * @throws IllegalArgumentException
    *    if an illegal or inappropriate argument was passed to this method
    * @throws IOException
    *    if an I/O error occurs
    * @throws NullPointerException
    *    if an element in task list is null
    * @throws SecurityException
    *    if a security manager exists and
    *    <ul>
    *       <li>when attemping to start the process, its checkExec method doesn't allow creation of the subprocess, or</li>
    *       <li>its checkPermission method doesn't allow access to the process environment</li>
    *    </ul>
    * @throws UnsupportedOperationException
    *    if the operating system does not support the creation of processes
    */
   private static Map<String,String> validateScriptForUnixLikePlatform( String script ) 
      throws IOException {

      List<String> task = Arrays.asList( "shellcheck", script )

      Map<String,String> responseMap = ExecUtils.exec( task );

      if ( responseMap.get( "exitValue" ).equals( "0" ) ) {
         responseMap.ok = "true"
      } else {
         responseMap.ok = "false"
      }

      return( responseMap )

   }


   private Sys( ) { 
      throw new UnsupportedOperationException( "Class instantiation not supported" );
   }

}
