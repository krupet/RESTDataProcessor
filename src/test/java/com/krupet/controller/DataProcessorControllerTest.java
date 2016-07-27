package com.krupet.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krupet.BaseWebAppTest;
import com.krupet.entity.ResultData;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class DataProcessorControllerTest extends BaseWebAppTest {

    @Test
    public void postOneFileTestAndExpectedIsOk() throws Exception {

        InputStream is = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("files",is);


        mockMvc.perform(fileUpload("/dataProcessor").file(multipartFile))
                .andExpect(status().isOk());
    }

    @Test
    public void postFewFilesTestAndExpectedIsOk() throws Exception {

        InputStream is0 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv");
        MockMultipartFile multipartFile0 = new MockMultipartFile("files",is0);

        InputStream is1 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA (1).csv");
        MockMultipartFile multipartFile1 = new MockMultipartFile("files", is1);

        InputStream is2 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA (2).csv");
        MockMultipartFile multipartFile2 = new MockMultipartFile("files", is2);

        InputStream is3 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA (3).csv");
        MockMultipartFile multipartFile3 = new MockMultipartFile("files", is3);

        InputStream is4 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA (4).csv");
        MockMultipartFile multipartFile4 = new MockMultipartFile("files", is4);

        InputStream is5 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA (5).csv");
        MockMultipartFile multipartFile5 = new MockMultipartFile("files", is5);

        InputStream is6 = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA (6).csv");
        MockMultipartFile multipartFile6 = new MockMultipartFile("files", is6);


        mockMvc.perform(fileUpload("/dataProcessor")
                .file(multipartFile0)
                .file(multipartFile1)
                .file(multipartFile2)
                .file(multipartFile3)
                .file(multipartFile4)
                .file(multipartFile5)
                .file(multipartFile6))
                .andExpect(status().isOk());
    }

    @Test
    public void postOneFileAndRetrieveResultTestAndExpectedIsOk() throws Exception {

        InputStream is = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("files",is);


        mockMvc.perform(fileUpload("/dataProcessor").file(multipartFile)).andExpect(status().isOk());

        mockMvc.perform(get("/dataProcessor")).andExpect(status().isOk());
    }

    @Test
    public void postOneFileAndRetrieveResultAndValidResultExpectedTestAndExpectedIsOk() throws Exception {

        InputStream is = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("files",is);

        String string = new String(IOUtils.toByteArray(this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv")));

        Map<String, Long> expected = new HashMap<>();
        Arrays.stream(string.split("\n")).forEach(word -> expected.put(word, expected.getOrDefault(word, 0L) + 1L));

        ResultData[] expectedResult = expected.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(o -> new ResultData(o.getKey(), o.getValue())).toArray(ResultData[]::new);

        ObjectMapper mapper = new ObjectMapper();
        String expectedJSONResult = mapper.writeValueAsString(expectedResult);

        mockMvc.perform(fileUpload("/dataProcessor").file(multipartFile)).andExpect(status().isOk());

        MvcResult requestResult = mockMvc.perform(get("/dataProcessor")).andExpect(status().isOk()).andReturn();

        String resultJSONString = requestResult.getResponse().getContentAsString();

        assertEquals(expectedJSONResult, resultJSONString);
    }
}