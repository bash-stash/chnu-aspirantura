package com.chnu.aspirantura.backup;

import com.chnu.aspirantura.Settings;
import com.chnu.aspirantura.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BackupExecutor {

    public static void makeBackup() throws InterruptedException, IOException {

            Settings.loadDatabaseProperties();
            Settings.loadProperties();

            String dbPort = Settings.getDatabaseProperty("database.port");
            String dbHost = Settings.getDatabaseProperty("database.host");

            String dbName = Settings.getDatabaseProperty("database.name");
            String dbUser = Settings.getDatabaseProperty("database.user");
            String dbPass = Settings.getDatabaseProperty("database.pass");
            String location = Settings.getDatabaseProperty("mysqldump.location");

            String backupLocation = Settings.getProperty("backup.location");

            String backup = Settings.getCommonProperties("backup.count");
            int backupCount = (Integer.parseInt(backup) + 1);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.now();
            String executeCmd = location + " -u" + dbUser + " -p" + dbPass + " " + dbName + " -r "+backupLocation+"backup-(" + backupCount + ")-" + localDate.format(dateFormatter) + ".sql";
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                Utils.logger.debug("backup-(" + backupCount + ")-" + localDate.format(dateFormatter)+" was created!");
                Settings.setSettings("backup.count", String.valueOf(backupCount));
            } else {
                Utils.logger.debug("backup-(" + backupCount + ")-" + localDate.format(dateFormatter)+" was NOT created!");
                throw new InterruptedException("backup was not created");
            }
    }
}
