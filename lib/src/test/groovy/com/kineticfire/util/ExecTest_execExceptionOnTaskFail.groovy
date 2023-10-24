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


import com.kineticfire.util.Exec


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
 * Unit tests for 'Exec.exceptionOnTaskFail(...)'.
 *
 */
@Timeout( value = 1, unit = MINUTES )
class ExecTest_execExceptionOnTaskFail extends Specification {

    @TempDir
    Path tempDir


    // ********************************************************
    // execExceptionOnTaskFail
    //      - task
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task) for valid task without CL arguments returns correct result"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task );

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );
    }

    def "execExceptionOnTaskFail(List<String> task) for valid task with one CL argument returns correct result"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task )

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );

    }

    def "execExceptionOnTaskFail(List<String> task) for invalid task returns exception"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task )

        then: "thrown exception"
        def e = thrown( TaskExecutionException )

        and: "thrown exception contains error message"
        e.message.contains( "invalid option" )
    }

    def "execExceptionOnTaskFail(List<String> task) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task )

        then: "thrown exception"
        thrown IOException
    }

    def "execExceptionOnTaskFail(List<String> task) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task )

        then: "thrown exception"
        thrown NullPointerException
    }



    // **********************************************************************************
    // **********************************************************************************



    // ********************************************************
    // execExceptionOnTaskFail
    //      - task, x
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) for valid task without CL arguments returns correct result"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null );

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) for valid task with one CL argument returns correct result"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null )

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );

    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) for invalid task returns exception"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null )

        then: "thrown exception"
        def e = thrown( TaskExecutionException )

        and: "thrown exception contains error message"
        e.message.contains( "invalid option" )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null )

        then: "thrown exception"
        thrown IOException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null )

        then: "thrown exception"
        thrown NullPointerException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config
    //           (empty)
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) returns correctly when 'config' is empty"( ) {

        given: "command to execute to get the current username, with empty config"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "returns the correct username from executing the command"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( result )
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config
    //           - trim
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'true' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "returns the correct username from executing the command, with output trimmed"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( result )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) returns untrimmed output when trim=false"( ) {

        given: "command to execute to get the current username, and trim set to false"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'false' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "returns the correct username from executing the command, with output untrimmed"
        String usernameExpected = System.properties[ 'user.name' ] + System.lineSeparator( );
        usernameExpected.equals( result )

    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception for invalid trim value"( ) {

        given: "command to execute to get the current username, and invalid trim setting"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "thrown exception"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config
    //           - directory
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) sets new working directory"( ) {

        given: "command to execute to get the current username, and config to set new working directory"
        List<String> task = Arrays.asList( 'pwd' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'directory', tempDir.toString( ) )

        when: "execute the command"
        String oldWorkingDir = Exec.exec( task, null, null, null ).out
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "returns the correct new working directory"
        tempDir.toString( ).equals( result )

        and: "previously had a different working directory than the new working directory"
        System.getProperty( 'user.dir' ).equals( oldWorkingDir )
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config
    //           - redirectOutFilePath
    //           - redirectOutType
    // ********************************************************


    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) for valid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "result is empty String"
        result.equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) for invalid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "thrown exception"
        def e = thrown( TaskExecutionException )

        and: "thrown exception contains error message"
        e.message.contains( "invalid option" )

        and: "output file has no contents"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) overwrites existing file content when output redirected to file with option 'overwrite'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "result is empty String"
        result.equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) appends to existing file when output redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( originalContent + 'user' )

        and: "result is empty String"
        result.equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception when given redirectOutFilePath but no redirectOutType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception when given redirectOutType but no redirectOutFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config
    //           - redirectErrToOut
    //           - redirectErrFilePath
    //           - redirectErrType
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception if specify 'redirectErrToOut=true'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception if specify 'redirectErrToOut' is an illegal value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrToOut', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) returns correctly if specify 'redirectErrToOut=false'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception if specify 'redirectErrFilePath'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config) throws exception if specify 'redirectErrType'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrType', errFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg )

        then: "exception thrown"
        thrown IllegalArgumentException
    }



    // *****************************************************************************************
    // *****************************************************************************************



    // ********************************************************
    // execExceptionOnTaskFail
    //      - task, x, x, x
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task without CL arguments returns correct result"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null );

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task with one CL argument returns correct result"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );

    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task returns exception"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "thrown exception"
        def e = thrown( TaskExecutionException )

        and: "thrown exception contains error message"
        e.message.contains( "invalid option" )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "thrown exception"
        thrown IOException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "thrown exception"
        thrown NullPointerException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           (empty)
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'config' is empty"( ) {

        given: "command to execute to get the current username, with empty config"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "returns the correct username from executing the command"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( result )
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - trim
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'true' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "returns the correct username from executing the command, with output trimmed"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( result )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns untrimmed output when trim=false"( ) {

        given: "command to execute to get the current username, and trim set to false"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'false' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "returns the correct username from executing the command, with output untrimmed"
        String usernameExpected = System.properties[ 'user.name' ] + System.lineSeparator( );
        usernameExpected.equals( result )

    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for invalid trim value"( ) {

        given: "command to execute to get the current username, and invalid trim setting"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "thrown exception"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - directory
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) sets new working directory"( ) {

        given: "command to execute to get the current username, and config to set new working directory"
        List<String> task = Arrays.asList( 'pwd' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'directory', tempDir.toString( ) )

        when: "execute the command"
        String oldWorkingDir = Exec.exec( task, null, null, null ).out
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "returns the correct new working directory"
        tempDir.toString( ).equals( result )

        and: "previously had a different working directory than the new working directory"
        System.getProperty( 'user.dir' ).equals( oldWorkingDir )
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - redirectOutFilePath
    //           - redirectOutType
    // ********************************************************


    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "result is empty String"
        result.equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "thrown exception"
        def e = thrown( TaskExecutionException )

        and: "thrown exception contains error message"
        e.message.contains( "invalid option" )

        and: "output file has no contents"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) overwrites existing file content when output redirected to file with option 'overwrite'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "result is empty String"
        result.equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) appends to existing file when output redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( originalContent + 'user' )

        and: "result is empty String"
        result.equals( '' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception when given redirectOutFilePath but no redirectOutType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception when given redirectOutType but no redirectOutFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - redirectErrToOut
    //           - redirectErrFilePath
    //           - redirectErrType
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception if specify 'redirectErrToOut=true'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception if specify 'redirectErrToOut' is an illegal value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrToOut', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly if specify 'redirectErrToOut=false'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception if specify 'redirectErrFilePath'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrFilePath', errFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception if specify 'redirectErrType'"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String errFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectErrType', errFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, x, addEnv x
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'addEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty addEnv"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> addEnv = new HashMap<String,String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, addEnv, null )

        then: "returns the correct username from executing the command"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( result )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, addEnv, null )

        then: "returns the env var"
        result.contains( 'GREET=HOWDY' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, addEnv, null )

        then: "returns the env var"
        result.contains( 'GREET1=HI' )
        result.contains( 'GREET2=HEY' )
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, x, x, removeEnv
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'removeEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty removeEnv"
        List<String> task = Arrays.asList( 'whoami' )
        List<String> removeEnv = new ArrayList<String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, removeEnv )

        then: "returns the correct username from executing the command"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( result )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'GREET' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, removeEnv )

        then: "returns the environment variables without the 'GREET' env var"
        !result.contains( 'GREET=' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'GREET1' )
        removeEnv.add( 'GREET2' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, removeEnv )

        then: "returns the environment variables without the 'GREET1' and 'GREET2' env vars"
        !result.contains( 'GREET1=' )
        !result.contains( 'GREET2=' )
    }

}
