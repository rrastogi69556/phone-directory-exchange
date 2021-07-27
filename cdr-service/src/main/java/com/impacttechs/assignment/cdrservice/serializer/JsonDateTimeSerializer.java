package com.impacttechs.assignment.cdrservice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class JsonDateTimeSerializer extends JsonSerializer<Date> {
    private static final Log LOGGER = LogFactory.getLog(JsonDateTimeSerializer.class);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatter.ISO_INSTANT.toString());

    public JsonDateTimeSerializer() {
        LOGGER.info("Initializing date format as;" + this.dateTimeFormatter.toString());
    }

    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        LocalDateTime localDateTime = date.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime();
        jsonGenerator.writeString(localDateTime.format(this.dateTimeFormatter));
    }
}