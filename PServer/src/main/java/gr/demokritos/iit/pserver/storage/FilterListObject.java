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

import org.apache.hadoop.hbase.filter.FilterList;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class FilterListObject {

    private boolean fOR = true;
    private FilterList list;

    public FilterListObject(FilterList list, boolean fOR) {
        this.list = list;
        this.fOR = fOR;
    }

    public FilterList getList() {
        return list;
    }

    public void setIsOR(boolean fOR) {
        this.fOR = fOR;
    }

    public boolean isOR() {
        return fOR;
    }

    @Override
    public String toString() {
        return "FilterListObject{" + "fOR=" + fOR + ", list=" + list + '}';
    }
}
