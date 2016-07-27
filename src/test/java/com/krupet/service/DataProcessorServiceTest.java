package com.krupet.service;

import com.krupet.BaseWebAppTest;
import com.krupet.entity.FileData;
import com.krupet.entity.ResultData;
import com.krupet.error.exceptions.ProcessingServiceException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class DataProcessorServiceTest extends BaseWebAppTest {

    @Autowired
    private DataProcessorService dataProcessorService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void processingTaskTest() throws IOException, ProcessingServiceException {

        byte[] fileContent = IOUtils.toByteArray(this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv"));
        String string = new String(fileContent);

        Map<String, Long> expected = new HashMap<>();

        Arrays.stream(string.split("\n")).forEach(word -> expected.put(word, expected.getOrDefault(word, 0L) + 1L));

        ResultData[] expectedResult = expected.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(o -> new ResultData(o.getKey(), o.getValue())).toArray(ResultData[]::new);

        dataProcessorService.processFiles(new FileData(fileContent));

        assertTrue(Arrays.equals(dataProcessorService.getStatistic(), expectedResult));

    }
}
