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
package com.kineticfire.util


/**
 * Provides system utilities.
 *
 */
final class SystemUtils {


   /**
    * Returns the username for the current active user as a String.
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
    * @return username for the current active user as a String
    */
   static String getUserName( ) {
      return( System.properties[ 'user.name' ] )
   }


   /**
    * Returns the UID of the current active user as an int or -1 if an error occurred.
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
    * @return the UID of the current active user as an int or -1 if an error occurred
    */
   static int getUid( ) {
      return( getUid( getUserName( ) ) )
   }


   /**
    * Returns the UID of the user with username 'username'.
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
    * @param username
    *    username of the user for whom to retrieve the UID
    * @return the UID of the current active user as an int or -1 if an error occurred
    */
   static int getUid( String username ) {

      int uid = -1;

      String[] commandArray = [ 'id', '-u', username ]

      def responseMap = ExecUtils.exec( commandArray )

      if ( responseMap.get( 'exitValue' ) == 0 ) {

         try {
            uid = Integer.parseInt( responseMap.get( 'out' ) )
         } catch ( NumberFormatException e ) {
            uid = -1
         }

      }

      return( uid )
   }


   /**
    * Validates the script at 'scriptPath' and returns a Map of the result.
    * <p>
    * Uses 'shellcheck' for static analysis and linting tool for sh/bash scripts.
    * <p>
    * Returns a result as a Map with key-value pairs:
    * <ul>
    *    <ol>ok - boolean true if the script passed validation (exitValue will be 0) and false otherwise (exitValue will be non-zero)</ol>
    *    <ol>exitValue - the integer exit value returned by the process on range of [0,255]; 0 for successful script validation and other values indicate an error</ol>
    *    <ol>out - the output returned by the process as a trimmed String, which could be an empty String</ol>
    *    <ol>err - contains the error output returned by the process as a trimmed String; only present if an an error occurred e.g. 'ok' is false and 'exitValue' is non-zero</ol>
    * </ul>
    * <p>
    * Requirements:
    * <ol>
    *    <li>Groovy</li>
    *    <li>shellcheck</li>
    * </ol>
    * <p>
    * Limitations:
    * <ol>
    *    <li>Linux</li>
    *    <li>bash/sh scripts</li>
    * </ol>
    *
    * @param script
    *    the path as a String to the sh/bash script to validate
    * @return a Map of the result of the script validation
    */
   static def validateScript( String scriptPath ) {

      String command = 'shellcheck ' + scriptPath

      def responseMap = ExecUtils.exec( command )

      if ( responseMap.get( 'exitValue' ) == 0 ) {
         responseMap.ok = true
      } else {
         responseMap.ok = false
      }

      responseMap.remove( 'success' )

      return( responseMap )
   }


   private SystemUtils( ) { }
}
