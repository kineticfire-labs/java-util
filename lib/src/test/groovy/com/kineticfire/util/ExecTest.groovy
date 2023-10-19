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




import java.util.Map
import java.util.HashMap
import static java.util.concurrent.TimeUnit.MINUTES
import java.io.IOException
import java.nio.file.Path

import spock.lang.Specification
import spock.lang.Timeout
import spock.lang.TempDir




/**
 * Unit tests.
 */
@Timeout( value = 1, unit = MINUTES )
class ExecUtilsTest extends Specification {

    @TempDir
    Path tempDir



    /* todo ... notes

        task     : valid return 0, invalid return non-0, invalid exception; command only, command + args
        config   : empty, trim, directory
        addEnv   : empty, 1, >1, does/doesnt exist
        removeEnv: empty, 1, >1, does/doesnt exist


       */


    // ********************************************************
    // execImpl
    //      - task, x, x, x
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task without CL arguments returns exitValue of 0"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task with one CL argument returns exitValue of 0"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task returns non-zero exitValue"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, null )

        then: "map key 'exitValue' is '2'"
        resultMap.exitValue.equals( '2' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, null )

        then: "thrown exception"
        thrown IOException
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, null )

        then: "thrown exception"
        thrown NullPointerException
    }


    // ********************************************************
    // execImpl
    //      - x, config, x, x
    //           (empty)
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'config' is empty"( ) {

        given: "command to execute to get the current username, with empty config"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // execImpl
    //      - x, config, x, x
    //           - trim
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output trimmed"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns untrimmed output when trim=false"( ) {

        given: "command to execute to get the current username, and trim set to false"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output untrimmed"
        String usernameExpected = System.properties[ 'user.name' ] + '\n';
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for invalid trim value"( ) {

        given: "command to execute to get the current username, and invalid trim setting"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'blah' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "thrown exception"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execImpl
    //      - x, config, x, x
    //           - directory
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'pwd' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'directory', tempDir.toString( ) )

        when: "execute the command"
        String oldWorkingDir = Exec.execImpl( task, null, null, null ).out
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

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
    // execImpl
    //      - x, config, x, x
    //           - redirectErrToOut
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when redirecting error to output for valid task returns with no error output property"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when redirecting error to output for invalid task returns error output in standard output"( ) {

        given: "invalid command to run"
        List<String> task = Arrays.asList( 'id', '-i' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'true' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' contains 'invalid option'"
        resultMap.out.contains( 'invalid option' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when explicitly not redirecting error to output (redirectErrToOut=false) for valid task returns with no error output property"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when explicitly not redirecting error to output (redirectErrToOut=false) for invalid task returns error output property"( ) {

        given: "invalid command to run"
        List<String> task = Arrays.asList( 'id', '-i' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'false' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) when given an illegal value for redirectErrToOut throws an exception"( ) {

        given: "valid command to run, with invalid 'redirectErrToOut' value"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'redirectErrToOut', 'blah' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, cfg, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }



    /* todo
        - out to file
            - error to out, when out is to file
        - err to file
    */



    // ********************************************************
    // execImpl
    //      - x, x, addEnv x
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'addEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty addEnv"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> addEnv = new HashMap<String,String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, addEnv, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, addEnv, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the env var in map key 'out'"
        resultMap.out.contains( 'GREET=HOWDY' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, addEnv, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the env var in map key 'out'"
        resultMap.out.contains( 'GREET1=HI' )
        resultMap.out.contains( 'GREET2=HEY' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // execImpl
    //      - x, x, x, removeEnv
    // ********************************************************

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'removeEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty removeEnv"
        List<String> task = Arrays.asList( 'whoami' )
        List<String> removeEnv = new ArrayList<String>( )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, removeEnv )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    //todo

    /*
    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'RMME' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, removeEnv )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'RMME1' )
        removeEnv.add( 'RMME2' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, removeEnv )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }
    */

}
