/* 
 * Copyright 2016 IIT , NCSR Demokritos - http://www.iit.demokritos.gr,
 *                      SciFY NPO http://www.scify.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.demokritos.iit.utilities.json;

import gr.demokritos.iit.utilities.utils.Utilities;

/**
 * This class creates an Output object to help us jsonize the PServer output
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Output {

    private Integer outputCode;
    private String outputMessage;
    private Object output;

    public Output() {
    }

    /**
     * Set the output code
     *
     * @param outputCode The output code if functions run properly
     */
    public void setOutputCode(Integer outputCode) {
        Utilities u = new Utilities();
        this.outputMessage = u.getMessageCode(outputCode);
        this.outputCode = outputCode;
    }

    /**
     * The outcome's message
     *
     * @param outputMessage A string with the message
     */
    public void setCustomOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    /**
     * The main output if PServer returns something
     *
     * @param output The returned object
     */
    public void setOutput(Object output) {
        this.output = output;
    }

}
