package jp.mirm.mirmapp.utils;

import android.text.Html;
import android.text.Spanned;

public class ConsoleTextFormatter {

    public static String format(String text) {
        text = text.replaceAll("\\<p\\>", "");
        text = text.replaceAll("\\</p\\>", "\n");

        StringBuffer buf = new StringBuffer();

        for (String str : text.split("\n")) {
            StringBuilder builder = new StringBuilder("<br>" + str + "</br>");

            if (!builder.toString().contains("[")) {
                buf.append(builder.toString());
                continue;
            }

            builder.insert(builder.toString().indexOf("["), "<font color=\"#26C6DA\">");
            builder.insert(builder.toString().indexOf("]") + 1, "</font>");

            if (builder.toString().contains("[Server thread/ALERT]")) {
                builder.insert(builder.toString().indexOf("[Server thread/ALERT]"), "<font color=\"#C62828\">");
                builder.insert(builder.toString().indexOf("[Server thread/ALERT]")  + "[Server thread/ALERT]".length(), "</font>");

            } else if (builder.toString().contains("[Server thread/NOTICE]")) {
                builder.insert(builder.toString().indexOf("</font>") + 7, "<font color=\"#26C6DA\">");
                builder.insert(builder.toString().length(), "</font>");

            } else if (builder.toString().contains("[Server thread/CRITICAL]")) {
                builder.insert(builder.toString().indexOf("</font>") + 7, "<font color=\"#C62828\">");
                builder.insert(builder.toString().length(), "</font>");

            } else if (builder.toString().contains("[Server thread/WARNING]")) {
                builder.insert(builder.toString().indexOf("</font>") + 7, "<font color=\"#FFEA00\">");
                builder.insert(builder.toString().length(), "</font>");

            } else if (builder.toString().contains("[Server thread/INFO]: [Server]")) {
                builder.insert(builder.toString().indexOf("</font>") + "[Server thread/INFO]: [Server]".length(), "<font color=\"#E91E63\">");
                builder.insert(builder.toString().length(), "</font>");

            }

            buf.append(builder.toString());
        }

        return buf.toString();
    }

    public static Spanned formatToSpanned(String html) {
        return Html.fromHtml(html);
    }

}
