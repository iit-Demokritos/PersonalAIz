/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
