package ru.korovko.atm;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.korovko.atm.exception.IncorrectFileFormatException;
import ru.korovko.atm.repository.AtmRepairRepository;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AtmRepairControllerTest {

    private static final String PATH_TO_TEST_FILES = System.getProperty("user.dir") +
            "\\src\\test\\resources\\files\\";
    private static final String TEST_DATA = "test_file.xlsx";
    private static final String SINGLE_TEST_DATA = "test_file_with_single_data.xlsx";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AtmRepairRepository repository;
    private MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() throws Exception {
        multipartFile = new MockMultipartFile("file", "test.file.xlsx",
                "multipart/form-data", new FileInputStream(PATH_TO_TEST_FILES + TEST_DATA));
    }

    @AfterEach()
    void deleteDataFromTestDatabase() {
        repository.deleteAll();
    }

    @Test
    public void fileUploadsAndReturnsCountOfUploadedValues() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(10)));
    }

    @Test
    public void checkThatIncorrectFileFormatThrowsException() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.file.txt",
                "multipart/form-data", new FileInputStream(PATH_TO_TEST_FILES + TEST_DATA));
        assertThatThrownBy(() -> mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isInternalServerError()))
                .hasCause(new IncorrectFileFormatException("File format is incorrect"));
    }

    @Test
    public void getAllMethodReturnsCorrectJson() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.file.xlsx",
                "multipart/form-data", new FileInputStream(PATH_TO_TEST_FILES + SINGLE_TEST_DATA));
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile));
        mockMvc.perform(get("/atmRepair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].caseId").value(218728186))
                .andExpect(jsonPath("[0].atmId").value(398729))
                .andExpect(jsonPath("[0].reason").value("УС не выходит на связь с хостом"))
                .andExpect(jsonPath("[0].startDate").value("05-01-2020 03:07"))
                .andExpect(jsonPath("[0].endDate").value("05-01-2020 03:59"))
                .andExpect(jsonPath("[0].bankName").value("Банк ВТБ24"))
                .andExpect(jsonPath("[0].link").value("Банк"));
    }

    @Test
    public void deleteDataMethodWorksCorrect() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(10)));
        repository.deleteAll();
        assertEquals(repository.findAll().size(), 0);
    }

    @Test
    public void getTopThreeReasonsMethodWorksCorrect() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(10)));
        mockMvc.perform(get("/atmRepair/reasons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]").value("УС не выходит на связь с хостом"))
                .andExpect(jsonPath("[1]").value("Банкомат закрыт (Up Closed)"))
                .andExpect(jsonPath("[2]").value("CashIn - Ошибка"));
    }
}
