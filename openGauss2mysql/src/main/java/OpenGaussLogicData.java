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

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Description: OpenGaussLogicData class
 *
 * @author zhangyaozhong
 * @date 2022/09/09
 **/
@Data
@ToString
public class OpenGaussLogicData {
    public String tableName;
    public String opType;
    public List<String> columnsName;
    public List<String> columnsType;
    public List<String> columnsVal;
    public List<String> oldKeysName;
    public List<String> oldKeysType;
    public List<String> oldKeysVal;


}
