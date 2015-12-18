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
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * CodeGenProject.java
 *
 * @author Garden
 * create on 2014年12月9日 上午11:34:39
 */
public class CodeGenPakage {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		String oldPkg = args[0];		// 旧包名
		String newPkg = args[1];		// 新包名
		String srcPath = args[2];		// 源码文件夹
		String prjName = args[3];		// 源码文件夹
		
		File srcDir = new File(srcPath);
		File destDir = new File(".." + File.separator + prjName);
		
		if ( destDir.exists()) {
			FileUtils.deleteDirectory(destDir);
		}
		
		FileUtils.copyDirectory(srcDir, destDir);
		
		// 删除无用文件夹
		deleteFolders( prjName);
		
		// 替换文件
	}

	/**
	 * @param baseDir
	 * @throws IOException 
	 */
	private static void deleteFolders(String prjName) throws IOException {
		File svnDir = new File(".." + File.separator + prjName + File.separator + ".svn");
		
		if ( svnDir.exists()) {
			FileUtils.deleteDirectory(svnDir);
		}
		
		File buildDir = new File(".." + File.separator + prjName + File.separator + "build");
		
		if ( buildDir.exists()) {
			FileUtils.deleteDirectory(buildDir);
		}
		
		File docDir = new File(".." + File.separator + prjName + File.separator + "doc");
		
		if ( docDir.exists()) {
			FileUtils.deleteDirectory(docDir);
		}
	}


}
