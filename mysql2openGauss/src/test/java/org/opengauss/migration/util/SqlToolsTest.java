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

package org.opengauss.migration.util;

import org.junit.Assert;
import org.junit.Test;
import org.opengauss.migration.vo.ColumnVo;
import org.opengauss.migration.vo.TableVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: SqlToolsTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class SqlToolsTest {
    private TableVo tableVo;
    private List<Serializable[]> multipleRows;
    private List<Serializable[]> multipleRowsAfter;

    public void constructData() {
        ColumnVo columnVo1 = new ColumnVo("col1", "int");
        ColumnVo columnVo2 = new ColumnVo("col2", "char");
        List<ColumnVo> columnVoList = new ArrayList<>();
        columnVoList.add(columnVo1);
        columnVoList.add(columnVo2);
        tableVo = new TableVo(1, "schema", "table", columnVoList);

        Serializable[] row1 = new Serializable[]{"1", "abc"};
        Serializable[] row2 = new Serializable[]{"2", "abc"};
        multipleRows = new ArrayList<>();
        multipleRows.add(row1);
        multipleRows.add(row2);

        Serializable[] row3 = new Serializable[]{"3", "aaa"};
        Serializable[] row4 = new Serializable[]{"4", "aaa"};
        multipleRowsAfter = new ArrayList<>();
        multipleRowsAfter.add(row3);
        multipleRowsAfter.add(row4);
    }

    @Test
    public void testDataType() {
        Assert.assertEquals(2, SqlTools.varcharTypeSet.size());
        Assert.assertEquals(9, SqlTools.digitalTypeSet.size());
        Assert.assertEquals(8, SqlTools.geometryTypeSet.size());
        Assert.assertEquals(4, SqlTools.datetimeTypeSet.size());
        Assert.assertEquals(4, SqlTools.binaryTypeSet.size());
        Assert.assertEquals(4, SqlTools.otherTypeSet.size());
    }

    @Test
    public void testSql() {
        constructData();

        List<String> insertSql = SqlTools.getInsertSql(tableVo, multipleRows);
        Assert.assertEquals(2, insertSql.size());
        String expectInsertSql = "insert into \"schema\".\"table\" values (1, 'abc');";
        Assert.assertEquals(expectInsertSql, insertSql.get(0));

        List<String> deleteSql = SqlTools.getDeleteSql(tableVo, multipleRows);
        Assert.assertEquals(2, insertSql.size());
        String expectDeleteSql = "delete from \"schema\".\"table\" where col1 = 1 and col2 = 'abc'";
        Assert.assertEquals(expectDeleteSql, deleteSql.get(0));

        List<String> updateSql = SqlTools.getUpdateSql(tableVo, multipleRows, multipleRowsAfter);
        Assert.assertEquals(2, updateSql.size());
        String expectUpdateSql = "update \"schema\".\"table\" set col1 = 3, col2 = 'aaa' " +
                "where col1 = 1 and col2 = 'abc'";
        Assert.assertEquals(expectUpdateSql, updateSql.get(0));
    }
}
