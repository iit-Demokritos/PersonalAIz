/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage;

import gr.demokritos.iit.pserver.storage.interfaces.IQueryParser;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apache.hadoop.hbase.filter.FilterList;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class QueryParser implements IQueryParser<FilterList> {

    @Override
    public FilterList getParsedQuery(String sExpression) {
        
        
        // Initialize a parent stack, that contains a filter list
            // AND the current state (are we in an OR clause?)
        LinkedList<FilterList> parentStack = new LinkedList<>();
        // Create new filter list
        FilterList list = new FilterList();
        // Set the list as the current list
        parentStack.push(list);

        StringTokenizer st = new StringTokenizer(sExpression, "|\\|");
        
        // While there is more to parse
        while(st.hasMoreTokens()){
            
            // Read next token
            // If token is an opening parenthesis
                // Create new FilterList
                // Push the current list to the parent stack TOGETHER
                    // with the current STATE (OR), and forget the current state
            // If token is a closing parenthesis
                // Add the current list to the parent list
                // Pop the current list from the parent stack AND SET 
                    // the current state based on the stack info
            // If the token is a variable name or literal
                // If we are in a comparison
                    // Use the name as a second operand
                    // Create filter
                    // Push to current filter list
                    // If in an OR
                        // Add current filter list to parent stack
                        // Pop current list from parent stack
                // else
                    // Use the name as the first operand
                // Keep the name
            // If the token is a comparison operator
                // Keep the operator
                // Signal a comparison
            // If the token is a logical operator
                // If operator is the OR operator
                    // Get the previous Filter from the current list
                    // Create new filterlist with not-pass-all
                    // Add previous Filter to new filter list
                    // Push current list as parent
                    // Set new (not-pass-all) filterlist as current
                    // Signal an OR
                // else (if it is an AND)
                    // continue     
        
        
        }
        
        
        return null;
    }
    
}
