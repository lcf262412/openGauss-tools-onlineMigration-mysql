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
 * Description: RowVoTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class RowVoTest {
    @Test
    public void test() {
        ColumnVo columnVo = new ColumnVo("col1", "int");
        Object value = 123;
        ColumnDataVo columnDataVo = new ColumnDataVo(columnVo, value);
        List<ColumnDataVo> rowValue = new ArrayList<>();
        rowValue.add(columnDataVo);
        rowValue.add(columnDataVo);
        RowVo rowVo = new RowVo(rowValue);
        Assert.assertEquals(2, rowVo.getRowValue().size());
        rowValue.add(columnDataVo);
        rowVo.setRowValue(rowValue);
        Assert.assertEquals(3, rowVo.getRowValue().size());
    }
}
