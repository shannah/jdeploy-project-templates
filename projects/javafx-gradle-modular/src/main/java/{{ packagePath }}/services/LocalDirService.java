package {{ packageName }}.services;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LocalDirService {

    private static String appLocalDirName;
    public static String getUserDataDirPath() throws IOException {

        if(appLocalDirName == null){
            ResourceBundle resourceBundle =
                    ResourceBundle.getBundle("config");
            appLocalDirName = resourceBundle.getString("lowercase_name");
        }

        AppDirs appDirs = AppDirsFactory.getInstance();
        String path = appDirs.getUserDataDir(appLocalDirName, null, null);
        Files.createDirectories(Paths.get(path));
        return path;
    }

}
