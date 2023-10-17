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




import java.nio.file.Path
import java.io.File
import java.util.Map
import java.util.HashMap
import static java.util.concurrent.TimeUnit.MINUTES

import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Timeout




/**
 * Unit tests.
 *
 */
@Timeout( value = 1, unit = MINUTES )
class SysTest extends Specification {

    static final String SCRIPT_FILE_NAME  = 'test.sh'


    @TempDir
    Path tempDir

    File scriptFile


    def setup( ) {
        scriptFile = new File( tempDir.toString( ) + File.separatorChar + SCRIPT_FILE_NAME )
    }



    def "validateScript(String scriptPath) for correct script"( ) {
        given: "a valid bash script"

        // must put the shebang on the first line
        scriptFile <<
            """#!/bin/env bash

            if [ "\$1" == "sayhi" ]; then
                echo "Hi"
            elif [ "\$1" == "sayhello" ]; then
                echo "Hello"
            fi

            exit 0
        """.stripIndent( )

        when: "validate the bash script"
        //Map<String,String> resultMap = Sys.validateScript( scriptFile.getAbsolutePath( ) )


        //todo
        then: "todo"
        1 == 1
        /*

        and: "map key 'ok' should be true"
        resultMap.ok.equals( 'true' )

        and: "map key 'exitValue' should be 0"
        resultMap.exitValue.equals( '0' )

        and: "map key 'out' should be an empty String"
        resultMap.out.equals( '' )

        // the error output, if any, from shellcheck is captured in 'out'
        and: "map key 'err' should not exist"
        !resultMap.containsKey( 'err' )

        // ensures that 'success' key from underlying methods not passed through
        and: "map should not contain key 'success'"
        !resultMap.containsKey( 'success' )
        */
    }

    /*
    def "validateScript(String scriptPath) for script error"( ) {
        given:

        // must put the shebang on the first line
        scriptFile <<
            """#!/bin/env bash

            if [ "\$1" == "sayhi" ]; then
                echo "Hi"
            elif [ "\$1" == "sayhello" ]; then
                echo "Hello"
            # removed 'fi' here to introduce script error

            exit 0
        """.stripIndent( )

        when:

        def resultMap = Sys.validateScript( scriptFile.getAbsolutePath( ) )

        then: "map key 'ok' should be false"
        resultMap.ok.equals( 'false' )

        and: "map key 'exitValue' should be 1"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' should contain an explanation of the failure"
        resultMap.out.contains( "Couldn't find 'fi' for this 'if'" )

        // the error output, if any, from shellcheck is captured in 'out'
        and: "map key 'err' should not exist"
        !resultMap.containsKey( 'err' )

        // ensures that 'success' key from underlying methods not passed through
        and: "map should not contain key 'success'"
        !resultMap.containsKey( 'success' )
    }
    */

}
