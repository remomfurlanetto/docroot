package rmf.utils.docroot;

import java.io.File;

public class OSUtils {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static String getDefaultMountedPoint(){
        if(isWindowsFileSeparator()){
            return "c:/";
        } else {
            return "/";
        }
    }

    public static boolean isWindowsFileSeparator(){
        return File.separator.equals("\\");
    }

}