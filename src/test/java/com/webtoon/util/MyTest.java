package com.webtoon.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class MyTest {

}
