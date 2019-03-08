package com.gzdb.supermarket.util;

import java.net.URL;

public class UrlUtil {
    public static String getFileExtension(String extUrl) {
        //URL: "http://photosaaaaa.net/photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg"
        // String filename = "";
        //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg

        String extension="";
//        String path = extUrl.getPath();
        //Checks for both forward and/or backslash
        //NOTE:**While backslashes are not supported in URL's
        //most browsers will autoreplace them with forward slashes
        //So technically if you're parsing an html page you could run into
        //a backslash , so i'm accounting for them here;
        String[] pathContents = extUrl.split("[\\\\/]");
        if(pathContents != null){
            int pathContentsLength = pathContents.length;
            //System.out.println("Path Contents Length: " + pathContentsLength);
//            for (int i = 0; i < pathContents.length; i++) {
//                System.out.println("Path " + i + ": " + pathContents[i]);
//            }
            //lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength-1];
            String[] lastPartContents = lastPart.split("\\.");
            if(lastPartContents != null && lastPartContents.length > 1){
                int lastPartContentLength = lastPartContents.length;
                // System.out.println("Last Part Length: " + lastPartContentLength);
                //filenames can contain . , so we assume everything before
                //the last . is the name, everything after the last . is the
                //extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    // System.out.println("Last Part " + i + ": "+ lastPartContents[i]);
                    if(i < (lastPartContents.length -1)){
                        name += lastPartContents[i] ;
                        if(i < (lastPartContentLength -2)){
                            name += ".";
                        }
                    }
                }
                extension = lastPartContents[lastPartContentLength -1];
                // filename = name + "." +extension;
                // System.out.println("Name: " + name);
                // System.out.println("Extension: " + extension);
                // System.out.println("Filename: " + filename);
            }
        }
        return extension;
    }
}
