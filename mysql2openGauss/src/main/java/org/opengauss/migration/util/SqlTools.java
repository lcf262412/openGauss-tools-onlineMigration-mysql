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

import org.opengauss.migration.vo.ColumnDataVo;
import org.opengauss.migration.vo.RowVo;
import org.opengauss.migration.vo.TableVo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Description: Sql tools class
 * @author douxin
 * @date 2022/08/27
 **/
public class SqlTools {
    /**
     * Varchar type set
     */
    public static Set<String> varcharTypeSet = new HashSet(){
        {
            add("varchar");
            add("char");
        }
    };

    /**
     * Digital type set
     */
    public static Set<String> digitalTypeSet = new HashSet() {
        {
            add("int");
            add("tinyint");
            add("smallint");
            add("mediumint");
            add("bigint");
            add("float");
            add("double");
            add("decimal");
            add("year");
        }
    };

    /**
     * Geometry type set
     */
    public static Set<String> geometryTypeSet = new HashSet() {
        {
            add("geometry");
            add("point");
            add("linestring");
            add("polygon");
            add("multipoint");
            add("multilinestring");
            add("multipolygon");
            add("geometrycollection");
        }
    };

    /**
     * Date time type set
     */
    public static Set<String> datetimeTypeSet = new HashSet() {
        {
            add("date");
            add("time");
            add("datetime");
            add("timestamp");
        }
    };

    /**
     * Binary type set
     */
    public static Set<String> binaryTypeSet = new HashSet() {
        {
            add("binary");
            add("varbinary");
            add("text");
            add("blob");
        }
    };

    /**
     * Other type set
     */
    public static Set<String> otherTypeSet = new HashSet() {
        {
            add("bit");
            add("enum");
            add("set");
            add("json");
        }
    };

    /**
     * Get insert sql
     *
     * @param TableVo the table vo
     * @param List<Serializable[]> the multiple rows
     * @return List<String> the insert sql list
     */
    public static List<String> getInsertSql(TableVo tableVo, List<Serializable[]> multipleRows) {
        List<String> sqlList = new ArrayList<>();
        List<RowVo> rowVoList = getRowVoList(tableVo, multipleRows);
        for (RowVo rowVo : rowVoList) {
            String valueContent = rowVo.getRowValue().stream().map(SqlTools::getSingleValue)
                    .collect(Collectors.joining(", "));
	        StringBuilder sb = new StringBuilder();
	        sb.append("insert into \"").append(tableVo.getSchemaName()).append("\".\"").append(tableVo.getTableName())
                    .append("\"").append(" values (").append(valueContent).append(");");
	        sqlList.add(sb.toString());
        }
        return sqlList;
    }

    /**
     * Get delete sql
     *
     * @param TableVo the table vo
     * @param List<Serializable[]> the multiple rows
     * @return List<String> the delete sql list
     */
    public static List<String> getDeleteSql(TableVo tableVo, List<Serializable[]> multipleRows) {
        List<String> sqlList = new ArrayList<>();
        List<RowVo> rowVoList = getRowVoList(tableVo, multipleRows);
        for (RowVo rowVo : rowVoList) {
            String whereContent = rowVo.getRowValue().stream().map(SqlTools::getSingleNameAndValueForWhere)
                    .collect(Collectors.joining(" and "));
            StringBuilder sb = new StringBuilder();
            sb.append("delete from \"").append(tableVo.getSchemaName()).append("\".\"").append(tableVo.getTableName())
                    .append("\" where ").append(whereContent);
            sqlList.add(sb.toString());
        }
        return sqlList;
    }

    /**
     * Get update sql
     *
     * @param TableVo the table vo
     * @param List<Serializable[]> the multiple rows before
     * @param List<Serializable[]> the multiple rows after
     * @return List<String> the update sql list
     */
    public static List<String> getUpdateSql(TableVo tableVo, List<Serializable[]> multipleRowsBefore,
                                            List<Serializable[]> multipleRowsAfter) {
        List<String> sqlList = new ArrayList<>();
        List<RowVo> rowVoListBefore = getRowVoList(tableVo, multipleRowsBefore);
        List<RowVo> rowVoListAfter = getRowVoList(tableVo, multipleRowsAfter);
        for (int i = 0; i < multipleRowsBefore.size(); i++) {
            String setContent = rowVoListAfter.get(i).getRowValue().stream().map(SqlTools::getSingleNameAndValueForSet)
                    .collect(Collectors.joining(", "));
            String whereContent = rowVoListBefore.get(i).getRowValue().stream().map(SqlTools::getSingleNameAndValueForWhere)
                    .collect(Collectors.joining(" and "));
            StringBuilder sb = new StringBuilder();
            sb.append("update \"").append(tableVo.getSchemaName()).append("\".\"").append(tableVo.getTableName())
                    .append("\" set ").append(setContent).append(" where ").append(whereContent);
            sqlList.add(sb.toString());
        }
        return sqlList;
    }

    /**
     * Get row vo list
     *
     * @param TableVo the table vo
     * @param List<Serializable[]> the multiple rows
     * @return List<RowVo> the row vo list
     */
    public static List<RowVo> getRowVoList(TableVo tableVo, List<Serializable[]> multipleRows) {
        List<RowVo> rowVoList = new ArrayList<>();
        for (Serializable[] aRow : multipleRows) {
            RowVo rowVo = getRowVo(tableVo, aRow);
            rowVoList.add(rowVo);
        }
        return rowVoList;
    }

    /**
     * Get row vo
     *
     * @param TableVo the table vo
     * @param Serializable[] a row
     * @return RowVo a row vo
     */
    public static RowVo getRowVo(TableVo tableVo, Serializable[] aRow) {
        List<ColumnDataVo> columnDataVoList = new ArrayList<>();
        for (int i = 0; i < aRow.length; i ++) {
            ColumnDataVo columnDataVo = new ColumnDataVo(tableVo.getColumnVoList().get(i), aRow[i]);
            columnDataVoList.add(columnDataVo);
        }
        return new RowVo(columnDataVoList);
    }

    /**
     * Get single name and value for set in update sql
     *
     * @param ColumnDataVo the column data vo
     * @return String the name and value string
     */
    public static String getSingleNameAndValueForSet(ColumnDataVo columnDataVo) {
        return columnDataVo.getColumnVo().getColumnName() + " = " + getSingleValue(columnDataVo);
    }

    /**
     * Get single name and value for where in update and delete sql
     *
     * @param ColumnDataVo the column data vo
     * @return String the name and value string
     */
    public static String getSingleNameAndValueForWhere(ColumnDataVo columnDataVo) {
        String value = getSingleValue(columnDataVo);
        if (value == null) {
            return columnDataVo.getColumnVo().getColumnName() + " is null";
        }
        return columnDataVo.getColumnVo().getColumnName() + " = " + value;
    }

    /**
     * Get single value
     *
     * @param ColumnDataVo the column data vo
     * @return String the value string
     */
    public static String getSingleValue(ColumnDataVo columnDataVo) {
        switch (columnDataVo.getColumnVo().getColumnType()) {
            case "int":
                return columnDataVo.getValue().toString();
            case "char":
                return addSingleQuotation(columnDataVo.getValue().toString());
            default: {
                return getValue(columnDataVo);
            }
        }
    }

    /**
     * Add single quotation
     *
     * @param String the value
     * @return String the modified value
     */
    public static String addSingleQuotation(String value) {
        return "'" + value + "'";
    }


    /**
     * Get other type value
     *
     * @param ColumnDataVo the column data vo
     * @return String the value
     */
    public static String getValue(ColumnDataVo columnDataVo) {
        if (columnDataVo.getValue() == null) {
            return null;
        }
        String columnType = columnDataVo.getColumnVo().getColumnType();
        if (varcharTypeSet.contains(columnType)) {
            return addSingleQuotation(columnDataVo.getValue().toString());
        } else if (digitalTypeSet.contains(columnType)) {
            return columnDataVo.getValue().toString();
        } else if (geometryTypeSet.contains(columnType)) {
            return getGeometryType(columnDataVo);
        } else if (datetimeTypeSet.contains(columnType)) {
            return getDatetimeType(columnDataVo, columnType);
        } else if (binaryTypeSet.contains(columnType)) {
            return getBinaryType(columnDataVo);
        } else if (otherTypeSet.contains(columnType)) {
            return getOtherType(columnDataVo, columnType);
        } else {
            return addSingleQuotation(columnDataVo.getValue().toString());
        }
    }

    private static String getGeometryType(ColumnDataVo columnDataVo) {
        System.out.println("几何类型暂时不支持");
        return "";
    }

    private static String getDatetimeType(ColumnDataVo columnDataVo, String columnType) {
        long time = (long) columnDataVo.getValue();
        Date date = new Date(time);
        SimpleDateFormat sdf = null;
        switch (columnType) {
            case "datetime":
                sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                break;
            case "date":
                sdf= new SimpleDateFormat("yyyy-MM-dd");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                break;
            case "time":
                sdf= new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                break;
            case "timestamp":
                sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            default:
                sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }
        return addSingleQuotation(sdf.format(date));
    }

    private static String getBinaryType(ColumnDataVo columnDataVo) {
        String value = new String((byte[])columnDataVo.getValue());
        return addSingleQuotation(value);
    }

    private static String getOtherType(ColumnDataVo columnDataVo, String columnType) {
        switch (columnType) {
            case "bit":
                return getBitType(columnDataVo);
            case "enum":
                return getEnumType(columnDataVo);
            case "set":
                return getSetType(columnDataVo);
            case "json":
                return getJsonType(columnDataVo);
            default:
                return addSingleQuotation(columnDataVo.getValue().toString());
        }
    }

    private static String getBitType(ColumnDataVo columnDataVo) {
        System.out.println("bit类型暂时不支持");
        return "";
    }

    private static String getEnumType(ColumnDataVo columnDataVo) {
        return columnDataVo.getValue().toString();
    }

    private static String getSetType(ColumnDataVo columnDataVo) {
        return columnDataVo.getValue().toString();
    }

    private static String getJsonType(ColumnDataVo columnDataVo) {
        System.out.println("Json类型暂时不支持");
        return "";
    }
}
