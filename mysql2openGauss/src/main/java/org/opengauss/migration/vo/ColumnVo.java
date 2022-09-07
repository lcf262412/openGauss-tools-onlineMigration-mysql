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

import java.util.Locale;

/**
 * Description: Column vo class
 * @author douxin
 * @date 2022/08/25
 **/
public class ColumnVo {
    private String columnName;
    private String columnType;

    /**
     * Constructor
     *
     * @param columnName
     * @param columnType
     */
    public ColumnVo(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    /**
     * Gets column name
     *
     * @return String the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Gets column type
     *
     * @return String the column type
     */
    public String getColumnType() {
        return columnType;
    }

    /**
     * To string
     *
     * @return String the string
     */
    public String toString() {
        return String.format(Locale.ENGLISH, "columnName is %s and columnType is %s", columnName, columnType);
    }

}
