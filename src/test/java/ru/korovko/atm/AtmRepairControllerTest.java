package ru.korovko.atm;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.korovko.atm.exception.IncorrectFileExtensionException;
import ru.korovko.atm.repository.AtmRepairRepository;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AtmRepairControllerTest {

    private static final String PATH_TO_TEST_FILES = System.getProperty("user.dir") +
            "\\src\\test\\resources\\files\\";
    private static final String TEST_DATA_FILENAME = "test_file.xlsx";
    private static final String SINGLE_TEST_DATA_FILENAME = "test_file_with_single_data.xlsx";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AtmRepairRepository repository;
    private MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() throws Exception {
        multipartFile = new MockMultipartFile("file", "test.file.xlsx",
                "multipart/form-data", new FileInputStream(PATH_TO_TEST_FILES + TEST_DATA_FILENAME));
    }

    @AfterEach()
    void deleteDataFromTestDatabase() {
        repository.deleteAll();
    }

    @Test
    public void fileUploadsToDatabaseAndReturnsCountOfUploadedValues() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadedFilesCount", is(10)));
    }

    @Test
    public void getAllMethodReturnsCorrectJson() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.file.xlsx",
                "multipart/form-data", new FileInputStream(PATH_TO_TEST_FILES + SINGLE_TEST_DATA_FILENAME));
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile));
        mockMvc.perform(get("/atmRepair/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].caseId").value(218728186))
                .andExpect(jsonPath("[0].atmId").value(398729))
                .andExpect(jsonPath("[0].reason").value("УС не выходит на связь с хостом"))
                .andExpect(jsonPath("[0].startDate").value("05-01-2020 03:07"))
                .andExpect(jsonPath("[0].endDate").value("05-01-2020 03:59"))
                .andExpect(jsonPath("[0].bankName").value("Банк ВТБ24"))
                .andExpect(jsonPath("[0].channel").value("Банк"));
    }

    @Test
    public void deleteAllDataMethodWorksCorrect() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadedFilesCount", is(10)));
        mockMvc.perform(delete("/atmRepair"))
                .andExpect(status().isOk());
        assertEquals(repository.findAll().size(), 0);
    }

    @Test
    public void getTopThreeReasonsMethodWorksCorrect() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadedFilesCount", is(10)));
        mockMvc.perform(get("/atmRepair/reasons")
        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]").value("УС не выходит на связь с хостом"))
                .andExpect(jsonPath("[1]").value("CashIn - Ошибка"))
                .andExpect(jsonPath("[2]").value("Банкомат закрыт (Up Closed)"));
    }

    @Test
    public void getTopThreeLongestRepairsMethodWorksCorrect() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadedFilesCount", is(10)));
        mockMvc.perform(get("/atmRepair/longest")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].atmId").value(388251))
                .andExpect(jsonPath("[1].atmId").value(398729))
                .andExpect(jsonPath("[2].atmId").value(393592));
    }

    @Test
    public void getTopThreeRecurringRepairsMethodWorksCorrect() throws Exception {
        mockMvc.perform
                (multipart("/atmRepair")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadedFilesCount", is(10)));
        mockMvc.perform(get("/atmRepair/recurring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].atmId").value(398729))
                .andExpect(jsonPath("[1].atmId").value(372152));
    }
}
