package cz.cvut.kbss.datatools.xmlanalysis.partners.csat;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueProcessing {


    public static String decodeHTMLEncodedValue(String in){
        return StringEscapeUtils.unescapeHtml(in);
    }


    public static final String NO_BRAKING_SPACE = "&#160;";
    public static List<String> decodeStringList(String in){
        String txt = StringEscapeUtils.unescapeHtml(in);
        if(txt == null)
            return Collections.emptyList();

        String sep = "#####";
        txt = txt.replaceAll("(<p[^>]*>)", sep); // mark beginnings of new lines
        txt = txt.replaceAll("<[^>]+>", ""); // remove all html tags

        txt = StringEscapeUtils.unescapeHtml(txt);
        // remove no-breaking spaces
        txt = txt.replaceAll("\\u00A0", " ");

        txt = txt.replaceAll(sep + "\\s+\"", sep + "\""); // remove spaces between each start of new line followed by spaces and a quote
        txt = txt.replaceAll(sep + "([^\"])", "$1"); // remove new line starts not followed by quote




//        out.replaceAll("\"", "")
//        txt.replaceAll("\"\\s*\"", "\"\"");
        txt = txt.trim();
        // remove no-breaking spaces
        txt = txt.replaceAll("[\\s\\u00A0]+$", "");

        List<String> ret = Stream.of(txt.split(sep))
                .map(t -> t.trim())
                .map(t -> removeQuotesAndTrim(t, "\""))
                .map(t -> t.replaceAll("\"","'"))
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());

        return ret;



//        txt = removeQuotesAndTrim(txt, "\"");
//        return Arrays.asList(txt.split(sep));

//        Pattern p = Pattern.compile("(\\s*\"([^\"])\")+");
//        Matcher m = p.matcher(txt);
//        boolean b = m.matches();
//        if(b){
//            List<String> ret = new ArrayList();
//            for(int i = 1; i < m.groupCount() + 1; i ++){
//                ret.add(m.group(i).trim());
//            }
//        }
//        return Collections.emptyList();
    }

    public static String removeQuotesAndTrim(String in, String quote){
        if(in.startsWith(quote)) {
            in = in.substring(1);
            if (in.endsWith(quote)) // remove second ending quote only if there is a starting one
                in = in.substring(0, in.length() - 1);
        }
        return in.trim();
    }

        static String exampleValue = "&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;Project leader responsible for the check&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;nbsp;&lt;/span&gt;&lt;/p&gt;";
        static  String exampleValue2 = "&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Project leader responsible for the check does not lead meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Project leader responsible for the check does not carry out meeting daily at time arranged with the customer&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;nbsp;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Project leader (production planning) does not attend meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Shift leader (=foreman) does not attend meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Material purchaser does not attend meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Customer does not attend meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Engineering does not attend meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;quot;Marketing and Sales representative (in case of need) does not attend meeting&amp;quot;&lt;/span&gt;&lt;/p&gt;&lt;p style=\"text-align:left;text-indent:0pt;margin:0pt 0pt 0pt 0pt;\"&gt;&lt;span style=\"color:#000000;background-color:transparent;font-family:Segoe UI;font-size:8pt;font-weight:normal;font-style:normal;\"&gt;&amp;nbsp;&lt;/span&gt;&lt;/p&gt;";
    public static void decodeHTMLEncodedValueExperiment(){
        String decoded = decodeHTMLEncodedValue(exampleValue2);
        String decodedSecondTime = decodeHTMLEncodedValue(decoded);

        System.out.println("Input after decoded once.");
        System.out.println(decoded);
        System.out.println();
        System.out.println("Input after decoded twice.");
        System.out.println(decodedSecondTime);
    }

    public static void decodeHTMLEcodedStringListExperiment(){
        List<String> exported = decodeStringList(exampleValue);
//        List<String> exported = decodeStringList(exampleValue2);
        exported.forEach(s -> System.out.println(s));

    }



    public static final Class<? extends Function> stringToListClass = toClass(ValueProcessing::decodeStringList);

    public static Class<? extends Function> toClass(Function<String, ?> f){
        return f.getClass();
    }

    public static void main(String[] args) {
//        decodeHTMLEncodedValueExperiment();
        decodeHTMLEcodedStringListExperiment();
    }
}
