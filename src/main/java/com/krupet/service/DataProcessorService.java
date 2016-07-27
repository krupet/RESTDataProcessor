package com.krupet.service;

import com.krupet.entity.FileData;
import com.krupet.entity.ResultData;
import com.krupet.error.exceptions.ProcessingServiceException;

/**
 * The interface DataProcessorService.
 */
public interface DataProcessorService {

    /**
     * Add files for processing.
     *
     * @param data Array with entities tha represent file with data to parse.
     * @throws ProcessingServiceException the file parsing service exception.
     */
    void processFiles(FileData... data) throws ProcessingServiceException;

    /**
     * Get results of parsing files with strings.
     *
     * @return Sorted in descending order array of entities that represent results of file parsing.
     */
    ResultData[] getStatistic();
}
