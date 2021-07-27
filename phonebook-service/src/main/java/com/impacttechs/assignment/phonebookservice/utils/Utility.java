package com.impacttechs.assignment.phonebookservice.utils;

import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static com.impacttechs.assignment.phonebookservice.constants.ExceptionConstants.ERROR_CANNOT_OPEN_RESOURCE;
import static com.impacttechs.assignment.phonebookservice.constants.ExceptionConstants.ERROR_CANNOT_READ_RESOURCE;
import static io.micrometer.core.instrument.util.StringUtils.isNotEmpty;
import static java.util.Collections.emptyList;

public final class Utility {
    private Utility() {}

    public static boolean validateNumber(String callerNumber) {
        return callerNumber.length()  > 0;
    }

    public static List<String[]> getContentsFromFileLocation(String location, Logger logger) {
        try {
            List<String[]> csvLines = new LinkedList<>();
            if (isNotEmpty(location)) {
                String csvRow[] = {};
                logger.info("Going to open a resource file: " + location);
                InputStream resource = Utility.class.getClassLoader().getResourceAsStream(location);
                logger.info("Is stream contents available [ {} ] ", resource.available());
                try (au.com.bytecode.opencsv.CSVReader reader = new CSVReader(
                        new InputStreamReader(resource, StandardCharsets.UTF_8), ',')) {
                    while ((csvRow = reader.readNext()) != null) {
                        csvLines.add(csvRow);
                    }
                    return csvLines;
                } catch (IOException e) {
                    logger.error(ERROR_CANNOT_READ_RESOURCE);
                }
            }
        } catch (IOException e) {
            logger.error(ERROR_CANNOT_OPEN_RESOURCE);
        }
        return emptyList();
    }

    public static boolean hasAlphabets(@Nullable String str) {
        return str != null && !str.isEmpty() && containsAlphabets(str);
    }

    private static boolean containsAlphabets(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (Character.isLetter(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
