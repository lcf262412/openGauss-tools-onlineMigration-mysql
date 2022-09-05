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

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: TableVoTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class TableVoTest {
    @Test
    public void test() {
        ColumnVo columnVo = new ColumnVo("col1", "int");
        List<ColumnVo> columnVoList = new ArrayList<>();
        columnVoList.add(columnVo);
        TableVo tableVo = new TableVo(1, "schema", "table", columnVoList);
        Assert.assertNotNull(tableVo);
        tableVo.setTableOid(2);
        tableVo.setSchemaName("new_schema");
        tableVo.setTableName("new_table");
        columnVoList.add(columnVo);
        tableVo.setColumnVoList(columnVoList);
        Assert.assertEquals(2, tableVo.getTableOid());
        Assert.assertEquals("new_schema", tableVo.getSchemaName());
        Assert.assertEquals("new_table", tableVo.getTableName());
        Assert.assertEquals(2, tableVo.getColumnVoList().size());
        Assert.assertTrue(tableVo.toString().contains("new_schema"));
    }
}
