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
package gr.demokritos.iit.pserver.storage;

import gr.demokritos.iit.pserver.storage.interfaces.IQueryParser;
import gr.demokritos.iit.utilities.configuration.PersonalAIzHBaseConfiguration;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;

/**
 * This class implement a Query Parser
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class QueryParser implements IQueryParser<FilterList> {

    private HTable table = null;
    private Configuration config;
    private String table_Users = "Users";

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QueryParser.class);

    /**
     * Query parser constructor
     */
    public QueryParser() {
        config = new PersonalAIzHBaseConfiguration().getHBaseConfig();
    }

    /**
     * Get the HBase filter List based on given Expression
     *
     * @param sExpression
     * @return
     */
    @Override
    public FilterList getParsedQuery(String sExpression) {

        String tmp = sExpression.replaceAll("\\|AND\\|", "&");
        sExpression = tmp.replaceAll("\\|OR\\|", "\\|");
        sExpression = tmp.replaceAll(">:", "}");
        sExpression = tmp.replaceAll("<:", "{");
        StringTokenizer st = new StringTokenizer(sExpression, "(\\|&)<>:!}{", true);

        //Help variables
        CompareFilter.CompareOp operator = null;
        String operant1 = null;
        String operant2;

        //Flags
        boolean fComparison = false;
        boolean fOROperator = false;

        // Initialize a parent stack, that contains a filter list
        // AND the current state (are we in an OR clause?)
        LinkedList<FilterListObject> parentStack = new LinkedList();
        FilterListObject currentState;

        // Create new filter list
        FilterListObject list = new FilterListObject(
                new FilterList(FilterList.Operator.MUST_PASS_ALL), fOROperator);
        // Set the list as the current list
        currentState = list;

        // While there is more to parse
        while (st.hasMoreTokens()) {

            // Read next token
            String cToken = st.nextToken();
            LOGGER.debug("Netxt Token on query parser " + cToken);
            // If token is an opening parenthesis
            if (cToken.equals("(")) {
                // Create new FilterList
                list = new FilterListObject(
                        new FilterList(FilterList.Operator.MUST_PASS_ALL), fOROperator);
                // Push the current list to the parent stack TOGETHER
                parentStack.push(currentState);
                // with the current STATE (OR), and forget the current state
                currentState = list;
            } else if (cToken.equals(")")) {
                // If token is a closing parenthesis
                // Add the current list to the parent list
                FilterListObject tmpParentStack = parentStack.pop();
                tmpParentStack.getList().addFilter(currentState.getList());
                tmpParentStack.setIsOR(currentState.isOR());
                // Pop the current list from the parent stack AND SET 
                // the current state based on the stack info
                currentState = tmpParentStack;
            } else if (cToken.equals("<") || cToken.equals(">")
                    || cToken.equals("{") || cToken.equals("}")
                    || cToken.equals(":") || cToken.equals("!")) {
                // If the token is a comparison operator
                // Keep the operator
                if (cToken.equals("<")) {
                    operator = CompareFilter.CompareOp.LESS;
                } else if (cToken.equals("}")) {
                    operator = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                } else if (cToken.equals("{")) {
                    operator = CompareFilter.CompareOp.LESS_OR_EQUAL;
                } else if (cToken.equals(">")) {
                    operator = CompareFilter.CompareOp.GREATER;
                } else if (cToken.equals(":")) {
                    operator = CompareFilter.CompareOp.EQUAL;
                } else if (cToken.equals("!")) {
                    operator = CompareFilter.CompareOp.NOT_EQUAL;
                }
                // Signal a comparison
                fComparison = true;
            } else if (cToken.equals("|") || cToken.equals("&")) {
                // If the token is a logical operator
                // If operator is the OR operator
                if (cToken.equals("|")) {
                    // Get the previous Filter from the current list
                    List<Filter> filtersList = currentState.getList().getFilters();
                    // Create new filterlist with not-pass-all
                    FilterListObject newFilterList = new FilterListObject(
                            new FilterList(FilterList.Operator.MUST_PASS_ONE), true);
                    // Add previous Filter to new filter list
                    for (Filter cFilter : filtersList) {
                        newFilterList.getList().addFilter(cFilter);
                    }
                    // Push current list as parent
                    parentStack.push(newFilterList);
                    // Set new (not-pass-all) filterlist as current
                    currentState = newFilterList;
                    // Signal an OR
                    fOROperator = true;
                } else {
                    // else (if it is an AND)
                    // continue
                    continue;
                }
            } else // If the token is a variable name or literal
            // If we are in a comparison
            if (fComparison) {
                // Use the name as a second operand
                operant2 = cToken;
                SingleColumnValueFilter filter;
                filter = new SingleColumnValueFilter(
                        Bytes.toBytes("Attributes"),
                        Bytes.toBytes(operant1),
                        operator,
                        new BinaryComparator(Bytes.toBytes(operant2))
                );

                // Push to current filter list
                currentState.getList().addFilter(filter);
                // If in an OR
                if (fOROperator) {
                    // Pop current list from parent stack
                    parentStack.pop();
                    // Add current filter list to parent stack
                    parentStack.push(currentState);
                }
                fComparison = false;
            } else {
                // else
                // Use the name as the first operand
                // Keep the name
                operant1 = cToken;
            }
        }

        return currentState.getList();
    }

}
