/*
 * Copyright (c) 2023 KineticFire. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kineticfire.util;


/**
 * Indicates that a task, executed as a native command line process, resulted in an error.
 * <p>
 * The exit value returned by the process, if set in the exception, may be retrieved with 'getExitValue()'.  If not set, the returned exit value is -1.
 *
 */
public class TaskExecutionException extends Exception {

   /** the exit value returned by the process */
   private int exitValue;


   /**
    * Constructs a TaskExecutionException with null as its error detail message and an invalid exit value of -1.
    *
    */
   public TaskExecutionException( ) {
      exitValue = -1;
   }


   /**
    * Constructs a TaskExecutionException with null as its error detail message and specified exit value.
    *
    * @param exitValue
    *    exit value associated with the exception
    */
   public TaskExecutionException( int exitValue ) {
      this.exitValue = exitValue;
   }


   /**
    * Constructs a TaskExecutionException with the specified error detail message and an invalid exit value of -1.
    *
    * @param message
    *    the detail message
    */
   public TaskExecutionException( String message ) {
      super( message );
      exitValue = -1;
   }


   /**
    * Constructs a TaskExecutionException with the specified error detail message and exit value.
    *
    * @param message
    *    the detail message
    * @param exitValue
    *    exit value associated with the exception
    */
   public TaskExecutionException( String message, int exitValue ) {
      super( message );
      this.exitValue = exitValue;
   }


   /**
    * Constructs a TaskExecutionException with the specified error detail message and cause and an invalid exit value of -1.
    * <p>
    * Note that the detail message associated with cause is not automatically incorporated into this exception's detail message.
    *
    * @param message
    *    the detail message
    * @param cause
    *    the cause (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
    */
   public TaskExecutionException( String message, Throwable cause ) {
      super( message, cause );
      exitValue = -1;
   }


   /**
    * Constructs a TaskExecutionException with the specified error detail message, cause, and exit value.
    * <p>
    * Note that the detail message associated with cause is not automatically incorporated into this exception's detail message.
    *
    * @param message
    *    the detail message
    * @param cause
    *    the cause (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
    * @param exitValue
    *    exit value associated with the exception
    */
   public TaskExecutionException( String message, Throwable cause, int exitValue ) {
      super( message, cause );
      this.exitValue = exitValue;
   }


   /**
    * Constructs a TaskExecutionException with the specified cause and detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause) and an invalid exit value of -1.
    *
    * @param cause
    *    the cause (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
    */
   public TaskExecutionException( Throwable cause ) {
      super( cause );
      exitValue = -1;
   }


   /**
    * Constructs a TaskExecutionException with the specified cause, detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause), and exit value.
    *
    * @param cause
    *    the cause (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
    * @param exitValue
    *    exit value associated with the exception
    */
   public TaskExecutionException( Throwable cause, int exitValue ) {
      super( cause );
      this.exitValue = exitValue;
   }


   /**
    * Returns the exit value associate with the exception.
    *
    * @return integer exit value
    */
   public int getExitValue( ) {
      return( exitValue );
   }

}
