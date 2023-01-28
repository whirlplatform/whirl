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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;

public final class DBUtil {

    private DBUtil() {
        // Utility class
    }

    /**
     * Closes a sql resultset and logs exceptions.
     *
     * @param rs  the resultset to close
     * @param log the logger instance to use for logging
     * @return true on succes
     */
    public static boolean close(ResultSet rs, Logger log) {
        boolean b = false;
        try {
            if (rs != null) {
                rs.close();
            }
            b = true;
        } catch (SQLException e) {
            log.error("The resultset could not be closed!", e);
        }
        return b;
    }

    /**
     * Closes a JDBC-Connection and logs exceptions.
     */
    public static void close(Connection conn, Logger log) {
        if (conn != null) {
            log.info("Closing database connection");
            try {
                conn.close();
            } catch (Exception e) {
                log.error("Error closing connection", e);
            }
        }
    }
}
