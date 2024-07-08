package es.udc.redes.tutorial.info;

import java.io.File;
import java.util.Date;

public class Info {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Error: Invalid number of arguments\nUsage: java es.udc.redes.tutorial.info.Info <relative path>");
            System.exit(-1);
        }

        String input = args[0];
        File file = new File(input);

        if (!file.exists()) {
            System.err.println("Error: file '" + input + "' not found");
            System.exit(1);
        }

        long size = file.length();

        long lastModified = file.lastModified();
        Date lastModifiedDate = new Date(lastModified);

        String name = file.getName();
        String nameExtensionless, extension;
        int lastPoint = name.lastIndexOf(".");
        if (lastPoint != -1 && lastPoint < name.length() - 1) {
            nameExtensionless = name.substring(0, lastPoint);
            extension = name.substring(lastPoint + 1);
        } else {
            nameExtensionless = name;
            extension = "";
        }

        String fileType = getFileType(extension, file);

        String path = file.getAbsolutePath();

        System.out.println("size: " + size +
                         "\nlast modification: " + lastModifiedDate +
                         "\nname: " + nameExtensionless);
        if (!extension.isEmpty()) {
            System.out.println("extension: " + extension);
        }
        System.out.println("filetype: " + fileType +
                         "\nabsolute path: " + path);
    }

    private static String getFileType(String extension, File file) {
        String fileType;
        if (!extension.isEmpty()){
            if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png") || extension.equals("bmp")) {
                fileType = "image";
            } else if (extension.equals("txt") || extension.equals("doc") || extension.equals("docx") || extension.equals("odt")) {
                fileType = "text";
            } else {
                fileType = "unknown";
            }
        } else {
            if (file.isDirectory()) {
                fileType = "directory";
            } else {
                fileType = "unknown";
            }
        }
        return fileType;
    }
}
