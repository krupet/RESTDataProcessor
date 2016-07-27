package com.krupet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.krupet.BaseWebAppTest;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

public class DataProcessorControllerTest extends BaseWebAppTest {

    @Test
    public void processFilesTest() throws Exception {

        InputStream is = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("files",is);


        mockMvc.perform(fileUpload("/dataProcessor").file(multipartFile))
                /*.andDo(print())*/
                .andExpect(status().isOk());
    }

    @Test
    public void getResultsTest() throws Exception {

        InputStream is = this.getClass().getResourceAsStream("/" + "test_data/MOCK_DATA.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("files",is);


        mockMvc.perform(fileUpload("/dataProcessor").file(multipartFile)).andExpect(status().isOk());

        mockMvc.perform(get("/dataProcessor"))/*.andDo(print())*/.andExpect(status().isOk());
    }
}