/* ExampleService.java
 *
 * Copyright 2009-2010 Comcast Interactive Media, LLC.
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
package org.fishwife.jrugged.examples.spring;

public class ExampleService {

    long loopCounter = 0;

    public void exampleMethodCallOne() throws Exception {
        loopCounter++;
        if (loopCounter % 25 == 0) {
            throw new Exception("Duh");
        }
    }

    public void exampleMethodCallTwo() {
        System.out.println("Test Circuit One");
    }
}
