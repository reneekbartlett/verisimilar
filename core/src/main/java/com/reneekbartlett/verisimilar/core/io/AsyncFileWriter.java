package com.reneekbartlett.verisimilar.core.io;

import tools.jackson.core.JacksonException;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import tools.jackson.databind.ObjectMapper;
//import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.csv.CsvSchema;
//import tools.jackson.dataformat.csv.CsvSchema.Column;
//import tools.jackson.dataformat.csv.CsvSchema.ColumnType;
import tools.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

//preview
//import java.util.concurrent.StructuredTaskScope;
//import java.util.concurrent.StructuredTaskScope.Subtask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.reneekbartlett.portfolio.util.datagen.PersonGenerator.Person;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class AsyncFileWriter {

    private static Logger logger = LoggerFactory.getLogger(AsyncFileWriter.class);

    // SerializationFeature.INDENT_OUTPUT
    // Deprecated: SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
    private static final ObjectMapper JSON_MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            //.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    private static final CsvMapper CSV_MAPPER = CsvMapper.builder()
            //.addModule(new JavaTimeModule())
            //.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build(); 

    public static <T> CompletableFuture<Void> writeObjectsAsync(T[] objects, Path filePath) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return JSON_MAPPER.writeValueAsBytes(objects);
            } catch (JacksonException e) {
                throw new RuntimeException(e);
            }
        }).thenCompose(bytes -> writeBytesAsync(bytes, filePath));
    }

    public static <T> CompletableFuture<Void> writeCsvAsync(T[] objects, Path filePath) throws IOException {
        
        //Column.PLACEHOLDER;

        CsvSchema schema = CsvSchema.builder()
            .addColumn("firstName")
            .addColumn("middleName")
            .addColumn("lastName")
            //.addColumn("fullName")
            .addColumn("birthday")
            //.addColumn("postalAddress.address1")
            //.addColumn("postalAddress.address2")
            //.addColumn("postalAddress.cityStateZip.city")
            //.addColumn("postalAddress.cityStateZip.state")
            //.addColumn("postalAddress.cityStateZip.zip")
            .addColumn("addr1")
            .addColumn("addr2")
            .addColumn("city")
            .addColumn("state")
            .addColumn("zip")
            .addColumn("phoneNumber")
            .addColumn("emailAddress")
            .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return CSV_MAPPER.writer(schema).writeValueAsBytes(objects);
            } catch (JacksonException e) {
                throw new RuntimeException(e);
            }
        }).thenCompose(bytes -> writeBytesAsync(bytes, filePath));
    }

//    public static <T> void writeObjectsAsyncV2(T[] objects, Path file) throws Exception { 
//        // TODO:  Java 21 Preview
//        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
//            Subtask<byte[]> serializeFuture = scope.fork(() -> MAPPER.writeValueAsBytes(objects));
//            Subtask<AsynchronousFileChannel> channelFuture = scope.fork(() -> 
//                AsynchronousFileChannel.open(file, WRITE, CREATE, TRUNCATE_EXISTING)
//            );
//            scope.join();
//
//            // Wait for both tasks scope.throwIfFailed();
//            byte[] data = serializeFuture.get();
//
//            // Write
//            AsynchronousFileChannel channel = channelFuture.get();
//            CompletableFuture<Void> writeFuture = new CompletableFuture<>();
//            ByteBuffer buffer = ByteBuffer.wrap(data); 
//            channel.write(buffer, 0, new IoContext(channel, writeFuture), completionHandler);
//            writeFuture.join();
//        }
//    }

    private static CompletableFuture<Void> writeBytesAsync(byte[] data, Path filePath) {
        CompletableFuture<Void> writeFuture = new CompletableFuture<>();
        try {
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(filePath, WRITE, CREATE, TRUNCATE_EXISTING);
            ByteBuffer buffer = ByteBuffer.wrap(data);
            channel.write(buffer, 0, new IoContext(channel, writeFuture), completionHandler);
        } catch (IOException e) {
            writeFuture.completeExceptionally(e);
        }
        return writeFuture;
    }

    private record IoContext(AsynchronousChannel channel, CompletableFuture<Void> promise) {}

    private static final CompletionHandler<Integer, IoContext> completionHandler = new CompletionHandler<>() {
        @Override
        public void completed(Integer result, IoContext ctx) {
            closeQuietly(ctx.channel());
            ctx.promise().complete(null);
        }

        @Override
        public void failed(Throwable exc, IoContext ctx) {
            closeQuietly(ctx.channel());
            ctx.promise().completeExceptionally(exc);
        }

        private void closeQuietly(AsynchronousChannel ch) {
            try {
                if (ch != null && ch.isOpen()) {
                    ch.close();
                }
            } catch (IOException e) {
                logger.error("Failed to close channel", e);
            }
        }
    };
}
