package sample.cafekiosk.learning;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

class GuavaLearningTest {

    @Test
    @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
    void testPartitionLearningTest(){
        //given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        //when
        List<List<Integer>> partition = Lists.partition(integers, 2);

        //then
        Assertions.assertThat(partition)
                .hasSize(3)
                .isEqualTo(List.of(
                        List.of(1, 2), List.of(3, 4), List.of(5, 6)
                ));
    }

    @Test
    @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
    void testPartitionLearningTest2(){
        //given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        //when
        List<List<Integer>> partition = Lists.partition(integers, 4);

        //then
        Assertions.assertThat(partition)
                .hasSize(2)
                .isEqualTo(List.of(
                        List.of(1, 2, 3, 4), List.of(5, 6)
                ));
    }

    @Test
    @DisplayName("멀티맵 기능 확인")
    void testMultiMapLearning(){
        //given
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "카푸치노");
        multimap.put("베이커리", "크루아상");
        multimap.put("베이커리", "식빵");

        //when
        Collection<String> coffee = multimap.get("커피");

        //then
        Assertions.assertThat(coffee).hasSize(3)
                .isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
    }

    @TestFactory
    @DisplayName("멀티맵 기능 확인")
    Collection<DynamicTest> testMultiMapLearning2(){
        //given
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "카푸치노");
        multimap.put("베이커리", "크루아상");
        multimap.put("베이커리", "식빵");

        return List.of(

                DynamicTest.dynamicTest("1개 value 삭제", () -> {
                    // when
                    multimap.remove("커피", "카푸치노");

                    // then
                    Collection<String> coffee = multimap.get("커피");
                    Assertions.assertThat(coffee).hasSize(2)
                            .isEqualTo(List.of("아메리카노", "카페라떼"));

                }),

                DynamicTest.dynamicTest("1개 value 삭제", () -> {
                    // when
                    multimap.removeAll("커피");

                    // then
                    Collection<String> coffee = multimap.get("커피");
                    Assertions.assertThat(coffee).isEmpty();

                })
        );
    }

}
