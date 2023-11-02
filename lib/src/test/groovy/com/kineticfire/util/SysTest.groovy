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
package com.kineticfire.util




import java.nio.file.Path
import java.io.File
import java.util.Map
import java.util.List;
import java.io.IOException;
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

    static final String SCRIPT_NAME  = 'test.sh'


    @TempDir
    Path tempDir

    File script


    def setup( ) {
        script = new File( tempDir.toString( ) + File.separatorChar + SCRIPT_NAME )
    }



    def "validateScript(Path script) for valid script with path as a Path"( ) {

        given: "a valid bash script"
        script <<
            """#!/bin/env bash

            if [ "\$1" == "sayhi" ]; then
                echo "Hi"
            elif [ "\$1" == "sayhello" ]; then
                echo "Hello"
            fi

            exit 0
        """.stripIndent( )

        when: "validate the bash script"
        Path scriptPath = Path.of( script.getAbsolutePath( ) )
        Map<String,String> resultMap = Sys.validateScript( scriptPath )

        then: "map key 'isValid' should be true"
        resultMap.isValid.equals( 'true' )

        and: "map key 'exitValue' should be 0"
        resultMap.exitValue.equals( '0' )

        and: "map key 'out' should be an empty String"
        resultMap.out.equals( '' )

        and: "map key 'err' should not exist"
        !resultMap.containsKey( 'err' )
    }


    def "validateScript(File script) for valid script with path as a File"( ) {

        given: "a valid bash script"
        script <<
            """#!/bin/env bash

            if [ "\$1" == "sayhi" ]; then
                echo "Hi"
            elif [ "\$1" == "sayhello" ]; then
                echo "Hello"
            fi

            exit 0
        """.stripIndent( )

        when: "validate the bash script"
        Map<String,String> resultMap = Sys.validateScript( script )

        then: "map key 'isValid' should be true"
        resultMap.isValid.equals( 'true' )

        and: "map key 'exitValue' should be 0"
        resultMap.exitValue.equals( '0' )

        and: "map key 'out' should be an empty String"
        resultMap.out.equals( '' )

        and: "map key 'err' should not exist"
        !resultMap.containsKey( 'err' )
    }


    def "validateScript(String script) for valid script with path as a String"( ) {

        given: "a valid bash script"
        script <<
            """#!/bin/env bash

            if [ "\$1" == "sayhi" ]; then
                echo "Hi"
            elif [ "\$1" == "sayhello" ]; then
                echo "Hello"
            fi

            exit 0
        """.stripIndent( )

        when: "validate the bash script"
        Map<String,String> resultMap = Sys.validateScript( script.getAbsolutePath( ) )

        then: "map key 'isValid' should be true"
        resultMap.isValid.equals( 'true' )

        and: "map key 'exitValue' should be 0"
        resultMap.exitValue.equals( '0' )

        and: "map key 'out' should be an empty String"
        resultMap.out.equals( '' )

        and: "map key 'err' should not exist"
        !resultMap.containsKey( 'err' )
    }

    def "validateScript(String script) for invalid script returns description of why script is invalid"( ) {

        given:
        script <<
            """#!/bin/env bash

            if [ "\$1" == "sayhi" ]; then
                echo "Hi"
            elif [ "\$1" == "sayhello" ]; then
                echo "Hello"
            # removed 'fi' here to introduce script error

            exit 0
        """.stripIndent( )

        when:
        Map<String,String> resultMap = Sys.validateScript( script.getAbsolutePath( ) )

        then: "map key 'isValid' should be false"
        resultMap.isValid.equals( 'false' )

        and: "map key 'exitValue' should be 1"
        resultMap.exitValue.equals( '1' )

        and: "map key 'out' should contain an explanation of why the script is invalid"
        resultMap.out.contains( "Couldn't find 'fi' for this 'if'" )

        and: "map key 'err' should not exist"
        resultMap.err.equals( '' )
    }

    def "validateScript(String script) for invalid script path returns error"( ) {

        given:
        String badScriptPath = script.getAbsolutePath( ) + '.bad'

        when:
        Map<String,String> resultMap = Sys.validateScript( badScriptPath )

        then: "map key 'isValid' should be false"
        resultMap.isValid.equals( 'false' )

        and: "map key 'exitValue' should be 2"
        resultMap.exitValue.equals( '2' )

        and: "map key 'out' should be empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains the error description"
        resultMap.err.contains( 'does not exist' )
    }

    def "validateScript(String script) for null script path throws exception"( ) {

        given:
        String nullScriptPath = null

        when:
        Map<String,String> resultMap = Sys.validateScript( nullScriptPath )

        then: "exception thrown"
        thrown NullPointerException
    }

}
