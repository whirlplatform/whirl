/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.whirlplatform.editor.server.db.retrive;

import org.apache.empire.xml.XMLConfiguration;

public class DBRetriverConfig extends XMLConfiguration {

	// generation options
	/**
	 * name of the database catalog (may be null)
	 */
	private String dbCatalog = null;

	/**
	 * name of the database schema (may be null)
	 */
	private String dbSchema = null;

	/**
	 * name of the table pattern (may be null)
	 */
	private String dbTablePattern = null;
	/**
	 * Name of the timestamp column used for optimistic locking (may be null)
	 * e.g. "UPDATE_TIMESTAMP";
	 */
	private String timestampColumn = null;

	// ------- generation options -------

	public String getDbCatalog() {
		return dbCatalog;
	}

	public void setDbCatalog(String dbCatalog) {
		this.dbCatalog = dbCatalog;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getDbTablePattern() {
		return dbTablePattern;
	}

	public void setDbTablePattern(String dbTablePattern) {
		this.dbTablePattern = dbTablePattern;
	}

	public String getTimestampColumn() {
		return timestampColumn;
	}

	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
	}

}
