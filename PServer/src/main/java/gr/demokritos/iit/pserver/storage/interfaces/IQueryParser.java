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
package gr.demokritos.iit.pserver.storage.interfaces;

/**
 * Represents a parser that reads a query and returns an object which can be
 * used in a Storage class to filter records. The template type T should
 * indicate the return type of the parsing process.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IQueryParser<T> {

    public T getParsedQuery(String sExpression);
}
