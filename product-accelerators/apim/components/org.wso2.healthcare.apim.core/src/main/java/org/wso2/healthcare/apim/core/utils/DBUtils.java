/*
 * Copyright (c) 2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.healthcare.apim.core.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class containing database related util functions
 */
public class DBUtils {

    private static final Log LOG = LogFactory.getLog(DBUtils.class);

    /**
     * Utility method to close the connection streams.
     * @param preparedStatement PreparedStatement
     * @param connection Connection
     * @param resultSet ResultSet
     */
    public static void closeAllConnections(PreparedStatement preparedStatement, Connection connection,
                                           ResultSet resultSet) {
        closeConnection(connection);
        closeResultSet(resultSet);
        closeStatement(preparedStatement);
    }

    /**
     * Close Connection
     * @param dbConnection Connection
     */
    private static void closeConnection(Connection dbConnection) {
        if (dbConnection != null) {
            try {
                if (!dbConnection.getAutoCommit()) {
                    dbConnection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOG.warn("Error occurred while enabling auto commit back", e);
            }
            try {
                dbConnection.close();
            } catch (SQLException e) {
                LOG.warn("Database error. Could not close database connection. Continuing with " +
                        "others. - " + e.getMessage(), e);
            }
        }
    }

    /**
     * Close ResultSet
     * @param resultSet ResultSet
     */
    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOG.warn("Database error. Could not close ResultSet  - " + e.getMessage(), e);
            }
        }

    }

    /**
     * Close PreparedStatement
     * @param preparedStatement PreparedStatement
     */
    public static void closeStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                LOG.warn("Database error. Could not close PreparedStatement. Continuing with" +
                        " others. - " + e.getMessage(), e);
            }
        }

    }

    /**
     * Function converts IS to String
     * Used for handling blobs
     * @param is - The Input Stream
     * @return - The inputStream as a String
     */
    public static String getStringFromInputStream(InputStream is) {
        String str = null;
        try {
            str = IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            LOG.error("Error occurred while converting input stream to string.", e);
        }
        return str;
    }

    /**
     * Set autocommit state of the connection
     * @param dbConnection Connection
     * @param autoCommit autoCommitState
     */
    public static void setAutoCommit(Connection dbConnection, boolean autoCommit) {
        if (dbConnection != null) {
            try {
                dbConnection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                LOG.error("Could not set auto commit back to initial state", e);
            }
        }
    }
}
