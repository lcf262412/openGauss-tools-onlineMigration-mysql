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
import java.util.Locale;

/**
 * Description: Table vo class
 * @author douxin
 * @date 2022/08/25
 **/
public class TableVo {
    private long tableOid;
    private String schemaName;
    private String tableName;
    private List<ColumnVo> columnVoList;

    /**
     * Constructor
     *
     * @param long the table oid
     * @param String the schema name
     * @param String the table name
     * @param List<ColumnVo> the column vo list
     */
    public TableVo(long tableOid, String schemaName, String tableName, List<ColumnVo> columnVoList) {
        this.tableOid = tableOid;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnVoList = columnVoList;
    }

    /**
     * Sets table oid
     *
     * @param long the table oid
     */
    public void setTableOid(long tableOid) {
        this.tableOid = tableOid;
    }

    /**
     * Gets table oid
     *
     * @return long the table oid
     */
    public long getTableOid() {
        return tableOid;
    }

    /**
     * Sets schema name
     *
     * @param String the schema name
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Gets schema name
     *
     * @return String the schema name
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Sets table name
     *
     * @param String the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets table name
     *
     * @return String the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets column vo list
     *
     * @param List<ColumnVo> the column vo list
     */
    public void setColumnVoList(List<ColumnVo> columnVoList) {
        this.columnVoList = columnVoList;
    }

    /**
     * Gets column vo list
     *
     * @return List<ColumnVo> the column vo list
     */
    public List<ColumnVo> getColumnVoList() {
        return columnVoList;
    }

    /**
     * To string
     *
     * @return String the string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ENGLISH, "tableOid is %s, schemaName is %s and tableName is %s",
                tableOid, schemaName, tableName));
        sb.append(" and columnVoList is\n");
        for (ColumnVo columnVo : columnVoList) {
            sb.append(columnVo.toString()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
