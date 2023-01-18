package com.old.linguatool;

import com.linguatool.LinguaToolApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {LinguaToolApplication.class})
@ExtendWith(SpringExtension.class)
class LinguaToolApplicationTests {

    @Test
    void contextLoads() {
    }

}
