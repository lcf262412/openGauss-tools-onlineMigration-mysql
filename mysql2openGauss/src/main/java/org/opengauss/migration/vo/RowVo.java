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

import java.util.List;

/**
 * Description: Row vo class
 * @author douxin
 * @date 2022/08/27
 **/
public class RowVo {
    private List<ColumnDataVo> rowValue;

    /**
     * Constructor
     *
     * @param List<ColumnDataVo> the row value
     */
    public RowVo(List<ColumnDataVo> rowValue) {
        this.rowValue = rowValue;
    }

    /**
     * Sets row value
     *
     * @param List<ColumnDataVo> the row value
     */
    public void setRowValue(List<ColumnDataVo> rowValue) {
        this.rowValue = rowValue;
    }

    /**
     * Gets row value
     *
     * @return List<ColumnDataVo> the row value
     */
    public List<ColumnDataVo> getRowValue() {
        return rowValue;
    }
}
