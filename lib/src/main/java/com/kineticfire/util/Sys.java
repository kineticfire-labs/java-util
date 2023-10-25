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



//todo comments
import java.nio.file.Path; //interface: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/Path.html
import java.io.File; // class: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/File.html
import java.util.Map;


//todo for testing
import java.util.HashMap;


/**
 * Provides system utilities.
 *
 */
public final class Sys {


   public static Map<String,String> validateScript( Path script ) {
      return( validateScript( script.toString( ) ) );
   }


   public static Map<String,String> validateScript( File script ) {
      return( validateScript( script.toString( ) ) );
   }


   public static Map<String,String> validateScript( String script ) {

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
    * Validates the script and returns a Map result.
    * <p>
    * Uses the 'shellcheck' utility for static analysis and linting tool for sh/bash scripts.
    * <p>
    * Returns a Map&lt;String,String&gt; result with key-value pairs:
    * <ul>
    *    <ol>ok - boolean true if the script passed validation (exitValue will be 0) and false otherwise (exitValue will be non-zero)</ol>
    *    <ol>exitValue - the integer exit value returned by the process on range of [0,255]; 0 for successful script validation and other values indicate an error</ol>
    *    <ol>out - the output returned by the process as a trimmed String, which could be an empty String</ol>
    *    <ol>err - contains the error output returned by the process as a trimmed String; only present if an an error occurred e.g. 'ok' is false and 'exitValue' is non-zero</ol>
    * </ul>
    * <p>
    * Requirements:
    * <ul>
    *    <li>Unix-like system</li>
    *    <li>'shellcheck' is installed</li>
    *    <li>script is a bash/sh script</li>
    * </ul>
    *
    * @param script
    *    the path as a String to the sh/bash script to validate
    * @return result as a Map of the script validation
    */
   private static Map<String,String> validateScriptForUnixLikePlatform( String script ) {

      String[] commandArray = { "shellcheck", script };

      /*

      Map<String,String> responseMap = ExecUtils.exec( commandArray )

      if ( responseMap.get( "exitValue" ) == 0 ) {
         responseMap.ok = true
      } else {
         responseMap.ok = false
      }

      responseMap.remove( "success" )

      return( responseMap )
      */

      return( new HashMap<String,String>( ) );

   }


   private Sys( ) { 
      throw new UnsupportedOperationException( "Class instantiation not supported" );
   }

}
