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
import org.garden.utils.RandomUtils;
import org.garden.utils.TextUtils;
import org.garden.utils.VelocityUtils;




/**
 * AutoGenModel.java
 *
 * @author Garden
 * create on 2014年9月4日 下午5:49:37
 */
public class CodeGen {
	private static Log log = LogFactory.getLog(CodeGen.class);
	

	public static void main(String[] args) throws SQLException {
		String propFilePath = args[0];
		String userDefTables = args[1];
		String packageName = args[2];
		String outPath = args[3] + File.separator + TextUtils.transmitPakage2FolderPath(packageName);
		String templatePath = args[4];
		String version = args[5];
		String prefix = args[6];
		String postfix = args[7];
		String modelPkg = args.length >= 9 ? args[8] : null;
		String infcPkg = args.length >= 10 ? args[9] : null;
		String mapperPkg = args.length >= 11 ? args[10] : null;
		
		log.debug("userDefTables:" + userDefTables);
		log.debug("packageName:" + packageName);
		log.debug("outPath:" + outPath);
		log.debug("templatePath:" + templatePath);
		log.debug("version:" + version);
		log.debug("prefix:" + prefix);
		log.debug("postfix:" + postfix);
		log.debug("modelPkg:"+ modelPkg);
		log.debug("infcPkg:"+ infcPkg);
		log.debug("mapperPkg:"+ mapperPkg);
		
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
		
		for ( String targetTable : targetTables) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("shortName", TextUtils.getDbShortName(targetTable));
			params.put("classProp", TextUtils.lowerCaseFirstLetter(TextUtils.transmitDBName2JavaClassName(targetTable)));
			params.put("className", TextUtils.transmitDBName2JavaClassName(targetTable));
			params.put("author", Constants.CODEGEN_AUTHOR);
			params.put("version", version);
			params.put("date", new Date());
			params.put("packageName", packageName);
			params.put("table", targetTable.toUpperCase());
			params.put("properties", CodeGenUtils.transmitColumn(conn, targetTable));
			params.put("prefix", prefix);
			params.put("postfix", postfix);
			params.put("infcPkg", infcPkg);
			params.put("modelPkg", modelPkg);
			params.put("mapperPkg", mapperPkg);
			params.put("serialId", RandomUtils.generate(18, 0, true));
			
			String outFile = outPath + File.separator + prefix + TextUtils.transmitDBName2JavaClassName(targetTable) + postfix;
			
			VelocityUtils.template2File(templatePath, params, outFile);
		}
	}
}
