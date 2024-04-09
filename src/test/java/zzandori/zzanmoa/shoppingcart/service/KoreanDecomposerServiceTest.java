package zzandori.zzanmoa.shoppingcart.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KoreanDecomposerServiceTest {

    @Autowired
    private KoreanDecomposerService koreanDecomposerService;

    @DisplayName("단어를 초성, 중성, 종성으로 분리한다.")
    @Test
    void test() {
        // given
        String word = "포도";

        // when
        List<String> prefixes = koreanDecomposerService.decomposeKoreanWordToPrefixes(word);

        // then
        assertThat(prefixes).hasSize(4);
        assertThat(prefixes.get(0)).isEqualTo("ㅍ");
        assertThat(prefixes.get(1)).isEqualTo("ㅗ");
        assertThat(prefixes.get(2)).isEqualTo("ㄷ");
        assertThat(prefixes.get(3)).isEqualTo("ㅗ");

        System.out.println(Arrays.toString(prefixes.toArray()));
    }

}