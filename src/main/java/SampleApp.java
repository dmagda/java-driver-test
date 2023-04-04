
/**
 Copyright 2022 Yugabyte

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.PreferQueryMode;

public class SampleApp {
    public static void main(String[] args) {

        Properties settings = new Properties();
        try {
            settings.load(SampleApp.class.getResourceAsStream("app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        printWelcomeMessage();

        while (true) {
            int mode = scanner.nextInt();

            switch (mode) {
                case 1:
                    runQuery(settings, PreferQueryMode.SIMPLE);
                    break;
                case 2:
                    runQuery(settings, PreferQueryMode.EXTENDED);
                    break;
                case 3:
                    runQuery(settings, PreferQueryMode.EXTENDED_FOR_PREPARED);
                    break;
                default:
                    System.err.println("Wrong command " + mode);
                    printWelcomeMessage();
            }
        }
    }

    private static void printWelcomeMessage() {
        System.out.printf("""
                Select a protocol mode and hit Enter:
                    1 - simple
                    2 - extended
                    3 - extended for prepared
                    \n
                """);
    }

    private static void runQuery(Properties settings, PreferQueryMode mode) {
        PGSimpleDataSource ds;
        Connection conn = null;

        try {
            ds = new PGSimpleDataSource();

            ds.setUrl("jdbc:postgresql://" + settings.getProperty("host") + ":"
                    + settings.getProperty("port") + "/postgres");
            ds.setUser(settings.getProperty("dbUser"));
            ds.setPassword(settings.getProperty("dbPassword"));
            ds.setPreferQueryMode(mode);

            conn = ds.getConnection();

            System.out.printf("Connected to the database [mode=%s]\n", mode.toString());

            ResultSet result;

            if (mode == PreferQueryMode.EXTENDED_FOR_PREPARED) {
                PreparedStatement stmt = conn.prepareStatement("SELECT now()");

                result = stmt.executeQuery();

            } else {
                Statement stmt = conn.createStatement();

                result = stmt.executeQuery("SELECT now()");
            }

            result.next();

            System.out.printf("Executed the query. Result %s\n", result.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}