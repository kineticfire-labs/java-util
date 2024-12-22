/*
 * (c) Copyright 2023-2025 KineticFire. All rights reserved.
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
/*
 * KineticFire Labs: https://labs.kineticfire.com/
 *     project site: https://github.com/kineticfire-labs/java-util/
 *
 */
package com.kineticfire.util




import static java.util.concurrent.TimeUnit.MINUTES

import spock.lang.Specification
import spock.lang.Timeout




/**
 * Unit tests.
 *
 */
@Timeout( value = 1, unit = MINUTES )
class TaskExecutionExceptionTest extends Specification {

    def "TaskExecutionException()"( ) {

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( )

        then: "verify values"
        e.getMessage( ) == null
        e.getCause( ) == null
        e.getExitValue( ) == -1
    }

    def "TaskExecutionException(int exitValue)"( ) {

        given: "arguments"
        int exitValue = 5

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( exitValue )

        then: "verify values"
        e.getMessage( ) == null
        e.getCause( ) == null
        e.getExitValue( ) == exitValue
    }

    def "TaskExecutionException(String message)"( ) {

        given: "arguments"
        String message = "An error message"

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( message )

        then: "verify values"
        e.getMessage( ).equals( message )
        e.getCause( ) == null
        e.getExitValue( ) == -1
    }

    def "TaskExecutionException(String message, int exitValue)"( ) {

        given: "arguments"
        String message = "An error message"
        int exitValue = 5

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( message, exitValue )

        then: "verify values"
        e.getMessage( ).equals( message )
        e.getCause( ) == null
        e.getExitValue( ) == exitValue
    }

    def "TaskExecutionException(String message, Throwable cause)"( ) {

        given: "arguments"
        String message = "An error message"
        Throwable cause = new Throwable( )

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( message, cause )

        then: "verify values"
        e.getMessage( ).equals( message )
        e.getCause( ) == cause 
        e.getExitValue( ) == -1
    }

    def "TaskExecutionException(String message, Throwable cause, int exitValue)"( ) {

        given: "arguments"
        String message = "An error message"
        Throwable cause = new Throwable( )
        int exitValue = 5

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( message, cause, exitValue )

        then: "verify values"
        e.getMessage( ).equals( message )
        e.getCause( ) == cause 
        e.getExitValue( ) == exitValue
    }

    def "TaskExecutionException(Throwable cause)"( ) {

        given: "arguments"
        Throwable cause = new Throwable( )

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( cause )

        then: "verify values"
        e.getMessage( ).equals( "java.lang.Throwable" )
        e.getCause( ) == cause 
        e.getExitValue( ) == -1
    }

    def "TaskExecutionException(Throwable cause, int exitValue)"( ) {

        given: "arguments"
        Throwable cause = new Throwable( )
        int exitValue = 5

        when: "instantiate"
        TaskExecutionException e = new TaskExecutionException( cause, exitValue )

        then: "verify values"
        e.getMessage( ).equals( "java.lang.Throwable" )
        e.getCause( ) == cause 
        e.getExitValue( ) == 5
    }

    def "getExitValue() returns correct value when no exit value is specified in exception constructor"( ) {

        given: "arguments"
        TaskExecutionException e = new TaskExecutionException( )

        when: "instantiate"
        int exitValue = e.getExitValue( )

        then: "verify values"
        exitValue == -1
    }

    def "getExitValue() returns correct value when exit value specified in exception constructor"( ) {

        given: "arguments"
        int exitValueExpected = 5
        TaskExecutionException e = new TaskExecutionException( exitValueExpected )

        when: "instantiate"
        int exitValue = e.getExitValue( )

        then: "verify values"
        exitValue == exitValueExpected
    }

}
