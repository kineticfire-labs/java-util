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

import spock.lang.Specification
import spock.lang.Timeout




/**
 * Unit tests.
 */
@Timeout( value = 1, unit = MINUTES )
class ExecUtilsTest extends Specification {

    def "exec(String task) for successful command returns correct exit value and output"( ) {
        given: "command to execute to get the current username"
        String[] task = [ 'whoami' ]
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        String result = Exec.execWithException( task, cfg )
        println "*****"
        println result
        println "*****"

        then: "map key 'success' is true"
        result.equals( "todo" )
    }

    /*
    def "exec(String task) for successful command returns correct exit value and output"( ) {
        given: "command to execute to get the current username"
        String[] task = [ 'whoami' ]

        when: "execute the command"
        Map<String,String> resultMap = Exec.exec( task )

        then: "map key 'success' is true"
        resultMap.success.equals( "true" )

        and: "map key 'exitValue' is 0"
        resultMap.exitValue.equals( "0" )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.get( 'out' ) )

        //todo need "does not contain key" not == null
        and: "map key 'err' is not present e.g null"
        resultMap.get( 'err' ) == null
    }
    */

}
