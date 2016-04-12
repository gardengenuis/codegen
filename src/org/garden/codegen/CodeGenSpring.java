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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.garden.utils.DBUtils;
import org.garden.utils.TextUtils;
import org.garden.utils.VelocityUtils;

/**
 * CodeGenSpring.java
 *
 * @author Garden
 * create on 2014年9月13日 下午3:50:27
 */
public class CodeGenSpring {
	private static Log log = LogFactory.getLog(CodeGenSpring.class);
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		String propFilePath = args[0];
		String userDefTables = args[1];
		String packageName = args[2];
		String outPath = args[3] + File.separator + TextUtils.transmitPakage2FolderPath(packageName);
		String templatePath = args[4];
		String version = args[5];
		String fileName = args[6];

		File outPathFile = new File(outPath);
		outPathFile.mkdirs();
		
		Connection conn = StringUtils.isEmpty(propFilePath) ? DBUtils.getConnection() : DBUtils.getConnection(new File(propFilePath));
		
		String[] dbTables = DBUtils.getTables(conn);
		List<String> targetTables = new ArrayList<String>();
		
		if ( StringUtils.isNotEmpty(userDefTables)) {
			String[] talbeFilters = userDefTables.split(Constants.TABLE_DELMETER);
			
			for ( String dbTable : dbTables) {
				if ( CodeGenUtils.isFilted( dbTable, talbeFilters)) {
					targetTables.add(dbTable);
				}
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, String>> clzList = new ArrayList<Map<String,String>>();
		params.put("author", Constants.CODEGEN_AUTHOR);
		params.put("version", version);
		params.put("date", new Date());
		params.put("packageName", packageName);
		params.put("class", clzList);
		
		for ( String targetTable : targetTables) {
			Map<String, String> clz = new HashMap<String, String>();
			
			clz.put("className", TextUtils.transmitDBName2JavaClassName(targetTable));
			clz.put("classNameL", TextUtils.transmitDBName2JavaPropName(targetTable));
			
			log.debug("tableName : " + targetTable + ", C[" + clz.get("className") + "], L[" + clz.get("classNameL") + "]");
			clzList.add(clz);
		}
		
		String outFile = outPath + File.separator + fileName;
		
		VelocityUtils.template2File(templatePath, params, outFile);
	}

}
