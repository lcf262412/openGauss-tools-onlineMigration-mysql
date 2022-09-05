/*
 * Copyright (c) 2022-2022 Huawei Technologies Co.,Ltd.
 *
 * openGauss is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 *           http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.opengauss.migration.vo;

/**
 * Description: Column data vo class
 * @author douxin
 * @date 2022/08/27
 **/
public class ColumnDataVo {
    private ColumnVo columnVo;
    private Object value;

    /**
     * Constructor
     *
     * @param ColumnVo the column vo
     * @param Object the value
     */
    public ColumnDataVo (ColumnVo columnVo, Object value) {
        this.columnVo = columnVo;
        this.value = value;
    }

    /**
     * Sets column vo
     *
     * @param ColumnVo the column vo
     */
    public void setColumnVo(ColumnVo columnVo) {
        this.columnVo = columnVo;
    }

    /**
     * Gets column vo
     *
     * @return ColumnVo the column vo
     */
    public ColumnVo getColumnVo() {
        return columnVo;
    }

    /**
     * Sets value
     *
     * @param Object the value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets value
     *
     * @return Object the value
     */
    public Object getValue() {
        return value;
    }
}
