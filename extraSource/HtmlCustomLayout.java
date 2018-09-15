import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

import org.apache.log4j.spi.LoggingEvent;  

/** 
 * This HTML Log Formatter is a simple replacement for the standard Log4J HTMLLayout formatter and 
 * replaces the default timestamp (milliseconds, relative to the start of the log) with a more readable 
 * timestamp (an example of the default format is 2008-11-21-18:35:21.472-0800). 
 * */  

public class   HtmlCustomLayout  
       extends org.apache.log4j.HTMLLayout  

{  
// RegEx pattern looks for <tr> <td> nnn...nnn </td> (all whitespace ignored)  

private static final String rxTimestamp = "\\s*<\\s*tr\\s*>\\s*<\\s*td\\s*>\\s*(\\d*)\\s*<\\s*/td\\s*>";  



private String timestampFormat = "yyyy-MM-dd HH:mm:ss"; // Default format. Example: 2008-11-21-18:35:21.472-0800  

private SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat);  

public HtmlCustomLayout()  
{  
super();  
}  

/** Override HTMLLayout's format() method */  

public String format(LoggingEvent event)  
{  
String record = super.format(event); // Get the log record in the default HTMLLayout format.  

Pattern pattern = Pattern.compile(rxTimestamp);  // RegEx to find the default timestamp  
Matcher matcher = pattern.matcher(record);  

if (!matcher.find()) // If default timestamp cannot be found,  
{  
return record; // Just return the unmodified log record.  
}  

StringBuffer buffer = new StringBuffer(record);   

buffer.replace(matcher.start(1), // Replace the default timestamp with one formatted as desired.  
       matcher.end(1),  
       sdf.format(new Date(event.timeStamp)));  

return buffer.toString(); // Return the log record with the desired timestamp format.  
}  

  

public void setTimestampFormat(String format)   
{  
    this.timestampFormat = format;  
this.sdf = new SimpleDateFormat(format); // Use the format specified by the TimestampFormat property  
}  

  

public String getTimestampFormat()  
{  
return this.timestampFormat;  
}  

private StringBuilder append(final StringBuilder sbuilder, final String s) {
    sbuilder.append(s);
    return sbuilder;
}
private StringBuilder appendLs(final StringBuilder sbuilder, final String s) {
    sbuilder.append(s).append("\n");
    return sbuilder;
}

@Override
public String getHeader() {
    final StringBuilder sbuf = new StringBuilder();
    append(sbuf, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
    appendLs(sbuf, "\"http://www.w3.org/TR/html4/loose.dtd\">");
    appendLs(sbuf, "<html>");
    appendLs(sbuf, "<head>");
    appendLs(sbuf, "\"/>");
    append(sbuf, "<title>").append("Cavisson Automation Team");
    appendLs(sbuf, "</title>");
    appendLs(sbuf, "<style type=\"text/css\">");
    appendLs(sbuf, "<!--");
    appendLs(sbuf, "th {background: #336699; color: #FFFFFF; text-align: left;}");
    appendLs(sbuf, "-->");
    appendLs(sbuf, "</style>");
    appendLs(sbuf, "</head>");
    appendLs(sbuf, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">");
    appendLs(sbuf, "<hr size=\"1\" noshade=\"noshade\">");
    appendLs(sbuf, "Log session start time " + new java.util.Date() + "<br>");
    appendLs(sbuf, "<br>");
    appendLs(sbuf,
            "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
    appendLs(sbuf, "<tr>");
    appendLs(sbuf, "<th>Time</th>");
    appendLs(sbuf, "<th>Thread</th>");
    appendLs(sbuf, "<th>Level</th>");
    appendLs(sbuf, "<th>Logger</th>");
    appendLs(sbuf, "<th>Message</th>");
    appendLs(sbuf, "</tr>");
    return sbuf.toString();
}

} 
