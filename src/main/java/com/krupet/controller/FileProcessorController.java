package com.krupet.controller;

import com.krupet.entity.FileData;
import com.krupet.entity.ResultData;
import com.krupet.error.exceptions.ProcessingServiceException;
import com.krupet.error.exceptions.ServiceErrorCode;
import com.krupet.service.DataProcessorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.krupet.error.exceptions.ServiceErrorCode.BAD_REQUEST;

/**
 * The class FileProcessorController.
 */
@Controller
@RequestMapping(value = "dataProcessor")
public class FileProcessorController {

    private static final Logger LOG = LogManager.getLogger(FileProcessorController.class);

    /**
     * The file-processing service
     */
    @Autowired
    DataProcessorService dataProcessorService;

    /**
     * Add files to parse.
     *
     * @param files Array of files to parse.
     * @throws ProcessingServiceException the file parsing service exception.
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, consumes = "multipart/form-data")
    @ResponseStatus(value = HttpStatus.OK)
    public void processMultipleFiles(@RequestParam("files") MultipartFile... files) throws ProcessingServiceException {

        if (files == null || files.length == 0) {
            throw new ProcessingServiceException("Empty files list, you need to add at least one file for processing",
                    BAD_REQUEST);
        }
        LOG.info("Received request for data processing. Number of files: {}", files.length);

        FileData[] data = new FileData[files.length];

        try {
            for (int i = 0; i < files.length; i++) {
                data[i] = new FileData(files[i].getBytes());
            }
        } catch (IOException e) {
            throw new ProcessingServiceException("Can't get content of a file. " + e.getMessage(),
                    ServiceErrorCode.BAD_REQUEST);
        }

        dataProcessorService.processFiles(data);
        LOG.info("Files successfully passed on processing.");
    }

    /**
     * Get result of parsing previously added files.
     *
     * @return Sorted in descending order array of entities that represent results of file parsing.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResultData[]> getProcessingResult() {
        LOG.info("Received request for processing results.");
        ResultData[] result = dataProcessorService.getStatistic();
        LOG.info("Returning processing results. Result set size: {}", result.length);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Result set: {}", Arrays.toString(result));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Default ProcessingServiceException handler.
     *
     * @param ex the file parsing service exception.
     * @param response http servlet response.
     */
    @ExceptionHandler(ProcessingServiceException.class)
    public void handleException(ProcessingServiceException ex, HttpServletResponse response) {
        try {
            ServiceErrorCode errorCode = ex.getErrorCode();
            switch (errorCode) {
                case BAD_REQUEST:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            }
        } catch (IOException e) {
            LOG.error("Can't handle Exception.", e);
        }
    }
}
