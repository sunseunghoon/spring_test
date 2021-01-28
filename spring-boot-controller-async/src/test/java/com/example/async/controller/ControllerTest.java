package com.example.async.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ContextConfiguration
@RunWith(SpringRunner.class)
public class ControllerTest {

    private MockMvc mockMvc;

    @MockBean
    Controller controller;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test1_성공() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/click?code=11&reason=ss"))
               .andExpect(status().isOk())
//               .andExpect(jsonPath("$['title']", containsString("Home")))
               .andDo(print());
    }

    @Test
    public void test2_404() throws Exception {
        //given
        given(this.controller.click(11, "ss"))
                .willReturn(ResponseEntity.status(404).body("ss"));
        //when
        //then
        mockMvc.perform(get("/click?code=11&reason=ss"))
               .andExpect(status().is4xxClientError())
//               .andExpect(jsonPath("$['title']", containsString("Home")))
               .andDo(print());
    }
}