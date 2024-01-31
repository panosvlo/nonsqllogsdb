package gr.uoa.di.cs.nonsqllogsdb.service;

import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import gr.uoa.di.cs.nonsqllogsdb.model.LogDetail;
import gr.uoa.di.cs.nonsqllogsdb.model.LogType;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogParsingService {

    @Autowired
    private LogRepository logRepository;

    private static final Pattern ACCESS_LOG_PATTERN = Pattern.compile(
            "(\\S+) \\S+ \\S+ \\[(.+?)\\] \"(\\S+) (.*?) (\\S+)\" (\\d{3}) (\\d+|-) \"(.*?)\" \"(.*?)\""
    );

    public void parseAndStoreAccessLog(MultipartFile file, LogType logType) throws IOException, ParseException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Log logEntry = parseAccessLogLine(line, logType);
                if (logEntry != null) {
                    logRepository.save(logEntry);
                }
            }
        }
    }

    private Log parseAccessLogLine(String line, LogType logType) throws ParseException {
        Matcher matcher = ACCESS_LOG_PATTERN.matcher(line);

        if (matcher.matches()) {
            String ip = matcher.group(1);
            String timestampStr = matcher.group(2);
            String method = matcher.group(3);
            String resource = matcher.group(4);
            String protocol = matcher.group(5);
            String statusCode = matcher.group(6);
            String sizeStr = matcher.group(7).equals("-") ? "0" : matcher.group(7);
            String referer = matcher.group(8).equals("-") ? null : matcher.group(8);
            String userAgent = matcher.group(9);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            List<LogDetail> details = new ArrayList<>();
            details.add(new LogDetail("method", method));
            details.add(new LogDetail("resource", resource));
            details.add(new LogDetail("protocol", protocol));
            details.add(new LogDetail("status_code", statusCode));
            details.add(new LogDetail("size", sizeStr));
            if (referer != null) details.add(new LogDetail("referer", referer));
            details.add(new LogDetail("user_agent", userAgent));

            return new Log(logType, dateFormat.parse(timestampStr), ip, null, details);
        }

        return null;
    }
}