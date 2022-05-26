package de.svedie.mayan_edms_export;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Exports the Mayan EDMS files.
 */
public class App {

    public static void main(String[] args) throws SQLException, IOException {

        if (args.length < 5) {
            System.out.println(
                    "please enter the parameters \n1. database ip and database (example:192.168.0.2/mayan) \n2. database user \n3. database password \n4. mayan document directory \n5. export directory");
            System.exit(0);
        }

        System.out.println("programm started ...");

        System.out.println("connecting to the database ...");
        String url = "jdbc:postgresql://" + args[0] + "?user=" + args[1] +
                "&password=" + args[2];
        Connection connection = DriverManager.getConnection(url);

        System.out.println("connection successfull ...");

        System.out.println("querying database table documents_documentfile ...");
        Statement statement = connection.createStatement();
        ResultSet query = statement
                .executeQuery("select file, document_id, filename from documents_documentfile order by document_id");

        List<Triple<String, Integer, String>> files = new ArrayList<>();
        while (query.next()) {
            String file = query.getString("file");
            int id = query.getInt("document_id");
            String filename = query.getString("filename");

            String absolutePath = Paths.get(args[3] + file).toFile().getAbsolutePath();

            Triple<String, Integer, String> filetriple = new Triple<>(absolutePath, id,
                    filename);
            files.add(filetriple);
        }

        if (files.size() > 0) {
            System.out.println(files.size() + " records in the database found ...");
        }

        System.out.println("starting copy process ...");
        for (Triple<String, Integer, String> file : files) {
            Path newdir = Path.of(args[4]);
            try {
                Files.copy(Path.of(file.getFile()), newdir.resolve(file.getFilename()));
            } catch (Exception e) {
                System.out.println("duplicate file: " + file.getFile() + " -#- filename: " +
                        file.getFilename());
            }
        }

        System.out.println("copy process finished ...");

        try {
            System.out.println("closing the database connection ...");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
