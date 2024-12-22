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




import java.util.Map
import java.util.HashMap
import static java.util.concurrent.TimeUnit.MINUTES
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Files

import spock.lang.Specification
import spock.lang.Timeout
import spock.lang.TempDir




/**
 * Unit tests for 'Exec.exec(...)'
 *
 */
@Timeout( value = 1, unit = MINUTES )
class ExecTest_exec extends Specification {

    @TempDir
    Path tempDir


    // ********************************************************
    // exec
    //      - task
    // ********************************************************

    def "exec(List<String> task) for valid task without CL arguments returns exitValue of 0"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task) for valid task with one CL argument returns exitValue of 0"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task) for invalid task returns non-zero exitValue"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task )

        then: "map key 'exitValue' is '2'"
        resultMap.exitValue.equals( '2' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task )

        then: "thrown exception"
        thrown IOException
    }

    def "exec(List<String> task) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task )

        then: "thrown exception"
        thrown NullPointerException
    }



    // *******************************************************************************
    // *******************************************************************************


    // ********************************************************
    // exec
    //      - task, x
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) for valid task without CL arguments returns exitValue of 0"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String>, config) for valid task with one CL argument returns exitValue of 0"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) for invalid task returns non-zero exitValue"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null )

        then: "map key 'exitValue' is '2'"
        resultMap.exitValue.equals( '2' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null )

        then: "thrown exception"
        thrown IOException
    }

    def "exec(List<String> task, Map<String,String> config) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null )

        then: "thrown exception"
        thrown NullPointerException
    }


    // ********************************************************
    // exec
    //      - x, config
    //           (empty)
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) returns correctly when 'config' is empty"( ) {

        given: "command to execute to get the current username, with empty config"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // exec
    //      - x, config
    //           - trim
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output trimmed"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) returns untrimmed output when trim=false"( ) {

        given: "command to execute to get the current username, and trim set to false"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output untrimmed"
        String usernameExpected = System.properties[ 'user.name' ] + System.lineSeparator( );
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) throws exception for invalid trim value"( ) {

        given: "command to execute to get the current username, and invalid trim setting"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "thrown exception"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config
    //           - directory
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) sets corrected working directory"( ) {

        given: "command to execute to get the current username, and config to set new working directory"
        List<String> task = Arrays.asList( 'pwd' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'directory', tempDir.toString( ) )

        when: "execute the command"
        String oldWorkingDir = Exec.exec( task, null, null, null ).out
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct new working directory in map key 'out'"
        tempDir.toString( ).equals( resultMap.out )

        and: "previously had a different working directory than the new working directory"
        System.getProperty( 'user.dir' ).equals( oldWorkingDir )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    // ********************************************************
    // exec
    //      - x, config
    //           - redirectErrToOut (to returned string, not file)
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) when redirecting error to output (returned string, not file) for valid task returns with no error output property"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) when redirecting error to output (returned string, not file) for invalid task returns error output in standard output"( ) {

        given: "invalid command to run"
        List<String> task = Arrays.asList( 'id', '-i' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' contains 'invalid option'"
        resultMap.out.contains( 'invalid option' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) when explicitly not redirecting error to output (returned string, not file) (redirectErrToOut=false) for valid task returns with no error output property"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) when explicitly not redirecting error to output (returned string, not file) (redirectErrToOut=false) for invalid task returns error output property"( ) {

        given: "invalid command to run"
        List<String> task = Arrays.asList( 'id', '-i' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config) when given an illegal value for redirectErrToOut throws an exception"( ) {

        given: "valid command to run, with invalid 'redirectErrToOut' value"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config
    //           - redirectOutFilePath
    //           - redirectOutType
    // ********************************************************


    def "exec(List<String> task, Map<String,String> config) for valid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) for invalid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "output file has no contents"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( '' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' contains error message"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config) overwrites existing file content when output redirected to file with option 'overwrite'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) appends to existing file when output redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( originalContent + 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) throws error when given redirectOutFilePath but no redirectOutType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config) throws error when given redirectOutType but no redirectOutFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config) throws error when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config
    //           - redirectErrToOut (to file, not to returned string)
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) for valid task when redirecting error to out, and output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config) for invalid task when redirecting error to out, and output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "output file has error output"
        Files.readString( Path.of( outFilePath ) ).trim( ).contains( 'invalid option' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // exec
    //      - x, config
    //           - redirectErrFilePath
    //           - redirectErrType
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) for valid task when redirecting error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "map key 'out' has contents 'user'"
        resultMap.out.equals( 'user' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has no contents"
        Files.readString( Path.of( errFilePath ) ).equals( '' )
    }

    def "exec(List<String> task, Map<String,String> config) for invalid task when redirecting error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has error output"
        Files.readString( Path.of( errFilePath ) ).contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config) overwrites existing file content when error redirected to file with option 'overwrite'"( ) {

        given: "invalid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( errFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has error output"
        Files.readString( Path.of( errFilePath ) ).trim( ).contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config) appends to existing file when error redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( errFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "map key 'err' has original contents plus error output"
        Files.readString( Path.of( errFilePath ) ).trim( ).contains( originalContent + 'id: invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config) throws error when given redirectErrFilePath but no redirectErrType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config) throws error when given redirectErrType but no redirectErrFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config) throws error when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config
    //           - both output and error to separate files
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config) for valid task when redirecting both output and error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'testOut.txt'
        String errFilePath = tempDir.toString( ) + File.separator + 'testErr.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "out file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has no contents"
        Files.readString( Path.of( errFilePath ) ).equals( '' )
    }

    def "exec(List<String> task, Map<String,String> config) for invalid task when redirecting both output and error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'testOut.txt'
        String errFilePath = tempDir.toString( ) + File.separator + 'testErr.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "out file has no contents"
        Files.readString( Path.of( outFilePath ) ).equals( '' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has error output"
        Files.readString( Path.of( errFilePath ) ).contains( 'invalid option' )
    }



    // *******************************************************************************
    // *******************************************************************************



    // ********************************************************
    // exec
    //      - task, x, x, x
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task without CL arguments returns exitValue of 0"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task with one CL argument returns exitValue of 0"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task returns non-zero exitValue"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, null, null )

        then: "map key 'exitValue' is '2'"
        resultMap.exitValue.equals( '2' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, null, null )

        then: "thrown exception"
        thrown IOException
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, null, null )

        then: "thrown exception"
        thrown NullPointerException
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           (empty)
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'config' is empty"( ) {

        given: "command to execute to get the current username, with empty config"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - trim
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output trimmed"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns untrimmed output when trim=false"( ) {

        given: "command to execute to get the current username, and trim set to false"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output untrimmed"
        String usernameExpected = System.properties[ 'user.name' ] + System.lineSeparator( );
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for invalid trim value"( ) {

        given: "command to execute to get the current username, and invalid trim setting"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "thrown exception"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - directory
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) sets corrected working directory"( ) {

        given: "command to execute to get the current username, and config to set new working directory"
        List<String> task = Arrays.asList( 'pwd' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'directory', tempDir.toString( ) )

        when: "execute the command"
        String oldWorkingDir = Exec.exec( task, null, null, null ).out
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct new working directory in map key 'out'"
        tempDir.toString( ).equals( resultMap.out )

        and: "previously had a different working directory than the new working directory"
        System.getProperty( 'user.dir' ).equals( oldWorkingDir )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - redirectErrToOut (to returned string, not file)
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when redirecting error to output (returned string, not file) for valid task returns with no error output property"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when redirecting error to output (returned string, not file) for invalid task returns error output in standard output"( ) {

        given: "invalid command to run"
        List<String> task = Arrays.asList( 'id', '-i' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' contains 'invalid option'"
        resultMap.out.contains( 'invalid option' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when explicitly not redirecting error to output (returned string, not file) (redirectErrToOut=false) for valid task returns with no error output property"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when explicitly not redirecting error to output (returned string, not file) (redirectErrToOut=false) for invalid task returns error output property"( ) {

        given: "invalid command to run"
        List<String> task = Arrays.asList( 'id', '-i' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when given an illegal value for redirectErrToOut throws an exception"( ) {

        given: "valid command to run, with invalid 'redirectErrToOut' value"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - redirectOutFilePath
    //           - redirectOutType
    // ********************************************************


    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "output file has no contents"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( '' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' contains error message"
        resultMap.err.contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) overwrites existing file content when output redirected to file with option 'overwrite'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) appends to existing file when output redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( originalContent + 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given redirectOutFilePath but no redirectOutType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given redirectOutType but no redirectOutFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - redirectErrToOut (to file, not to returned string)
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task when redirecting error to out, and output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task when redirecting error to out, and output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "output file has error output"
        Files.readString( Path.of( outFilePath ) ).trim( ).contains( 'invalid option' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - redirectErrFilePath
    //           - redirectErrType
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task when redirecting error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "map key 'out' has contents 'user'"
        resultMap.out.equals( 'user' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has no contents"
        Files.readString( Path.of( errFilePath ) ).equals( '' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task when redirecting error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has error output"
        Files.readString( Path.of( errFilePath ) ).contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) overwrites existing file content when error redirected to file with option 'overwrite'"( ) {

        given: "invalid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( errFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has error output"
        Files.readString( Path.of( errFilePath ) ).trim( ).contains( 'invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) appends to existing file when error redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( errFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "map key 'err' has original contents plus error output"
        Files.readString( Path.of( errFilePath ) ).trim( ).contains( originalContent + 'id: invalid option' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given redirectErrFilePath but no redirectErrType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given redirectErrType but no redirectErrFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'illegal-value' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // exec
    //      - x, config, x, x
    //           - both output and error to separate files
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task when redirecting both output and error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'testOut.txt'
        String errFilePath = tempDir.toString( ) + File.separator + 'testErr.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "out file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has no contents"
        Files.readString( Path.of( errFilePath ) ).equals( '' )
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task when redirecting both output and error to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'testOut.txt'
        String errFilePath = tempDir.toString( ) + File.separator + 'testErr.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )
        cfg.put( 'redirectErrFilePath', errFilePath )
        cfg.put( 'redirectErrType', 'overwrite' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "out file has no contents"
        Files.readString( Path.of( outFilePath ) ).equals( '' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false

        and: "err file has error output"
        Files.readString( Path.of( errFilePath ) ).contains( 'invalid option' )
    }


    // ********************************************************
    // exec
    //      - x, x, addEnv x
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'addEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty addEnv"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> addEnv = new HashMap<String,String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, addEnv, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, addEnv, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the env var in map key 'out'"
        resultMap.out.contains( 'GREET=HOWDY' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, addEnv, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the env var in map key 'out'"
        resultMap.out.contains( 'GREET1=HI' )
        resultMap.out.contains( 'GREET2=HEY' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // exec
    //      - x, x, x, removeEnv
    // ********************************************************

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'removeEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty removeEnv"
        List<String> task = Arrays.asList( 'whoami' )
        List<String> removeEnv = new ArrayList<String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, null, removeEnv )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'GREET' )


        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, addEnv, removeEnv )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns in 'out' the environment variables without the 'GREET' env var"
        !resultMap.out.contains( 'GREET=' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "exec(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'GREET1' )
        removeEnv.add( 'GREET2' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task, null, addEnv, removeEnv )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns in 'out' the environment variables without the 'GREET1' and 'GREET2' env vars"
        !resultMap.out.contains( 'GREET1=' )
        !resultMap.out.contains( 'GREET2=' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

}
