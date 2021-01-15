package net.qzwxsaedc.hiunattendedreport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Misc {
    public static BufferedReader getResourceAsStream(String path){
        return new BufferedReader(new InputStreamReader(Misc.class.getResourceAsStream(path)));
    }

    public static String getHtmlPage(String name) throws IOException {
        BufferedReader in = Misc.getResourceAsStream("/" + name);
        StringBuilder html = new StringBuilder();
        String line;
        while((line = in.readLine()) != null)   html.append(line.trim());
        return html.toString();
    }
}
