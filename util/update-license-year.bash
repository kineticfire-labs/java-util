#!/usr/bin/env bash
#
# Copyright (c) 2023 KineticFire. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


# Updates the following files with the year (a single year or a range) provided as the first and only argument:
#   - *.java
#   - *.groovy
#   - *.bash (including this script)
#   - *.gradle (including build.gradle)
#   - *.properties (including gradle.properties)
#   - resources/LICENSE_HEADER
#
# And updates the properties in the following files with the year, as above:
#   - lib/gradle.properties



function getSearchReplaceRegexString {
   result="s/Copyright (c) 20[2-9][0-9]\(-20[2-9][0-9]\)\? KineticFire. All rights reserved./Copyright (c) $year KineticFire. All rights reserved./g"
   echo $result
}


function replaceByFileExtension {

   # To search and replace the license years in files by extension, add below as a comma-space separated list
   readonly fileExtensions="java, groovy, bash, gradle, properties"


   IFS=', ' read -r -a fileExtensionsArray <<< "$fileExtensions"

   fileExtensionsPattern=''

   for element in "${fileExtensionsArray[@]}"; do
      if [ -z "$fileExtensionsPattern" ]; then
         fileExtensionsPattern="$element"
      else
         fileExtensionsPattern="$fileExtensionsPattern\|$element"
      fi
   done


   year="$1"

   searchReplaceRegexString=$(getSearchReplaceRegexString)

   echo "Updating year to '$year' for files with extensions of: '$fileExtensions'"

   find ../. -type f -regex ".*\.\($fileExtensionsPattern\)" -print0 | xargs -0 sed -i'' -e "$searchReplaceRegexString"
}


function replaceBySpecificFiles {

   # To search and replace the license yeras in specific files, add below as comma-space separated list
   readonly fileList="../resources/LICENSE_HEADER"

   IFS=', ' read -r -a fileArray <<< "$fileList"

   echo "Updating year to '$year' for files:"

   for element in "${fileArray[@]}"; do
      echo "   $element"
      sed -i'' -e "$searchReplaceRegexString" "$element"
   done

}


function replaceProperty {
   echo "Updating year to '$year' for property 'project_copyrightYears' in file '../lib/gradle.properties'"
   sed -i'' -e "s/project_copyrightYears=20[2-9][0-9]\(-20[2-9][0-9]\)\?/project_copyrightYears=$year/g" "../lib/gradle.properties"
}


function help {

cat << EOF

Usage  : ${scriptName} <year like "XXXX" or year range like "XXXX-YYYY" to set>

EOF

}


function main( ) {

    if [[ $# -eq 1 ]]; then
        replaceByFileExtension "$@"
        replaceBySpecificFiles "$@"
        replaceProperty "$@"
    else
        echo "Error:  Invalid arguments"
        help
        exit 0
    fi

}


main "$@"
