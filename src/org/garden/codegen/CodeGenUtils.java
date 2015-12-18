/*
 * Copyright (c) 2004, 2014, Garden Lee. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package org.garden.codegen;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.garden.utils.DBUtils;
import org.garden.utils.TextUtils;

/**
 * CodeGenUtils.java
 *
 * @author Garden
 * create on 2014年9月12日 上午10:40:57
 */
public class CodeGenUtils {
	private static Log log = LogFactory.getLog(CodeGenUtils.class);
	/**
	 * @param targetTable
	 * @return
	 * @throws SQLException 
	 */
	public static Column[] transmitColumn(Connection conn, String targetTable) throws SQLException {
		List<Column> rlt = new ArrayList<Column>();
		
		List<Map<String, String>> columns = DBUtils.getColumns(conn, targetTable);
		String[] pkNames = DBUtils.getPrimaryKey(conn, targetTable);
		
		for ( Map<String, String> column : columns) {
			String columnName = column.get("COLUMN_NAME");
			String columnType = column.get("TYPE_NAME");
			
			Column c = new Column();
			c.setColumnName(columnName);
			c.setPropName(TextUtils.transmitDBName2JavaPropName(columnName));
			c.setMethodName(TextUtils.transmitDBName2JavaClassName(columnName));
			c.setType(columnType);
			if ( isPk(columnName, pkNames)) {
				c.setPkFlag(true);
			} else {
				c.setPkFlag(false);
			}
			
			rlt.add(c);
		}
		
		return (Column[]) rlt.toArray(new Column[rlt.size()]);
	}

	/**
	 * @param columnName
	 * @param pkNames
	 * @return
	 */
	public static boolean isPk(String columnName, String[] pkNames) {
		for ( String pk : pkNames) {
			if ( columnName.equalsIgnoreCase(pk)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param dbTable
	 * @param talbeFilters
	 * @return
	 */
	public static boolean isFilted(String dbTable, String[] filters) {
		for ( String filter : filters) {
			if ( dbTable.toUpperCase().matches(filter.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public static class Column {
		private boolean pkFlag;
		private String type;
		private String propName;  // java属性
		private String columnName;  // 数据库字段
		private String methodName; // 方法名
		
		/**
		 * @return the pkFlag
		 */
		public boolean isPkFlag() {
			return pkFlag;
		}
		/**
		 * @param pkFlag the pkFlag to set
		 */
		public void setPkFlag(boolean pkFlag) {
			this.pkFlag = pkFlag;
		}
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the propName
		 */
		public String getPropName() {
			return propName;
		}
		/**
		 * @param propName the propName to set
		 */
		public void setPropName(String propName) {
			this.propName = propName;
		}
		/**
		 * @return the columnName
		 */
		public String getColumnName() {
			return columnName;
		}
		/**
		 * @param columnName the columnName to set
		 */
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		/**
		 * @return the methodName
		 */
		public String getMethodName() {
			return methodName;
		}
		/**
		 * @param methodName the methodName to set
		 */
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		
		
	}
}
