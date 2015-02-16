/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.utils;


/**
 * This class creates an Output object to help us jsonize the PServer output
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Output {

    private Integer outputCode;
    private String outputMessage;
    private Object output;


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
