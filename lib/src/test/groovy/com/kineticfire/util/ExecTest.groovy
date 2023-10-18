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

        //todo
        - err to out
        - err to file
        - out to file


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

        and: "map key 'outToFile' is not present"
        resultMap.containsKey( 'outToFile' ) == false

        and: "map key 'errToFile' is not present"
        resultMap.containsKey( 'errToFile' ) == false

        and: "map key 'errRedirect' is not present"
        resultMap.containsKey( 'errRedirect' ) == false
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

        and: "map key 'outToFile' is not present"
        resultMap.containsKey( 'outToFile' ) == false

        and: "map key 'errToFile' is not present"
        resultMap.containsKey( 'errToFile' ) == false

        and: "map key 'errRedirect' is not present"
        resultMap.containsKey( 'errRedirect' ) == false
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

        and: "map key 'err' is 'invalid option'"
        resultMap.err.contains( 'invalid option' )

        and: "map key 'outToFile' is not present"
        resultMap.containsKey( 'outToFile' ) == false

        and: "map key 'errToFile' is not present"
        resultMap.containsKey( 'errToFile' ) == false

        and: "map key 'errRedirect' is not present"
        resultMap.containsKey( 'errRedirect' ) == false
    }

    def "execImpl(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        Map<String,String> resultMap = Exec.execImpl( task, null, null, null )

        then: "map key 'exitValue' is '0'"
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

        and: "map key 'outToFile' is not present"
        resultMap.containsKey( 'outToFile' ) == false

        and: "map key 'errToFile' is not present"
        resultMap.containsKey( 'errToFile' ) == false

        and: "map key 'errRedirect' is not present"
        resultMap.containsKey( 'errRedirect' ) == false
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

        and: "map key 'outToFile' is not present"
        resultMap.containsKey( 'outToFile' ) == false

        and: "map key 'errToFile' is not present"
        resultMap.containsKey( 'errToFile' ) == false

        and: "map key 'errRedirect' is not present"
        resultMap.containsKey( 'errRedirect' ) == false
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

        and: "map key 'outToFile' is not present"
        resultMap.containsKey( 'outToFile' ) == false

        and: "map key 'errToFile' is not present"
        resultMap.containsKey( 'errToFile' ) == false

        and: "map key 'errRedirect' is not present"
        resultMap.containsKey( 'errRedirect' ) == false
    }


}
