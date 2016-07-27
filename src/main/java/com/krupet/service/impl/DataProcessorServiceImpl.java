package com.krupet.service.impl;

import com.krupet.entity.FileData;
import com.krupet.entity.ResultData;
import com.krupet.error.exceptions.ProcessingServiceException;
import com.krupet.error.exceptions.ServiceErrorCode;
import com.krupet.service.DataProcessorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default implementation of {@link DataProcessorService}.
 */
public class DataProcessorServiceImpl implements DataProcessorService {

    private static final Logger LOG = LogManager.getLogger(DataProcessorServiceImpl.class);

    /** Default number of simultaneously processed files. */
    private static final int DEFAULT_NUMBER_OF_WORKERS = 3;

    private final ExecutorService executor;

    /** Collection that contains results of file parsing - word frequency dictionary */
    private final ConcurrentHashMap<String, Long> collection = new ConcurrentHashMap<>();

    public DataProcessorServiceImpl() {
        this.executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_WORKERS);
    }

    public DataProcessorServiceImpl(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void processFiles(FileData... data) throws ProcessingServiceException {

        if (data == null || data.length < 1) {
            throw new ProcessingServiceException("No data to process.", ServiceErrorCode.BAD_REQUEST);
        }

        LOG.info("Start processing files, number of files to process: {}.", data.length);

        for (FileData fileData : data) {
            executor.execute(new FileParser(collection, fileData));
        }

        LOG.info("All files were added into processing queue.");
    }

    @Override
    public ResultData[] getStatistic() {

        LOG.info("Start generating processing result.");

        ResultData[] resultData;

        if (collection.isEmpty()) {
            return new ResultData[0];
        }

        Map<String, Long> result = new HashMap<>(collection);

        resultData = result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(o -> new ResultData(o.getKey(), o.getValue()))
                .toArray(ResultData[]::new);

        LOG.info("Returning processing results. Number of result strings: {}.", resultData.length);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Result set: {}", Arrays.toString(resultData));
        }

        return resultData;
    }

    @PreDestroy
    private void destroy() {
        executor.shutdownNow();
    }

    /**
     * The class FileParser.
     */
    private static class FileParser implements Runnable {

        public static final Logger LOG = LogManager.getLogger(FileParser.class);

        /** Map collection with word frequency dictionary */
        private final ConcurrentHashMap<String, Long> collection;

        /** Wrapper entity that represent content of particular file */
        private final FileData data;

        /** Pattern for splitting strings in a file. */
        private static final String splitterPattern = "\n";

        public FileParser(ConcurrentHashMap<String, Long> collection, FileData data) {
            this.collection = collection;
            this.data = data;
        }

        /**
         * Parse file content and update global word frequency dictionary with parsing results of a given file.
         */
        @Override
        public void run() {
            LOG.info("Start processing file with data, data file size: {}.", data.getContent().length);

            String[] strings = new String(data.getContent()).split(splitterPattern);

            LOG.info("Data file contains {} strings.", strings.length);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Data content: {}", Arrays.toString(strings));
            }

            Arrays.stream(strings)
                    .forEach(word -> collection.put(word, collection.getOrDefault(word, 0L) + 1L));

            LOG.info("Finished processing file data.");
        }
    }
}
