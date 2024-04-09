package zzandori.zzanmoa.shoppingcart.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class KoreanDecomposerService {

    // 초성, 중성, 종성 배열
    private static final String[] CHOSUNG = {
        "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };
    private static final String[] JUNGSUNG = {
        "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"
    };
    private static final String[] JONGSUNG = {
        "", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };

    public List<String> decomposeKoreanWordToPrefixes(String word) {
        List<String> prefixes = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            List<String> subPrefixes = decomposeKoreanCharacterToPrefixes(word.charAt(i));
            prefixes.addAll(subPrefixes);
        }

        return prefixes;
    }

    private List<String> decomposeKoreanCharacterToPrefixes(char ch) {
        List<String> prefixes = new ArrayList<>();

        // 한글 유니코드에서 '가'를 뺀 값
        int base = ch - 0xAC00;

        int jongSungIndex = base % 28;
        int jungSungIndex = ((base - jongSungIndex) / 28) % 21;
        int choSungIndex = ((base - jongSungIndex) / 28) / 21;

        String choSung = CHOSUNG[choSungIndex];
        String jungSung = JUNGSUNG[jungSungIndex];
        String jongSung = JONGSUNG[jongSungIndex];

        prefixes.add(choSung);
        prefixes.add(jungSung);
        if (!jongSung.equals("")) {
            prefixes.add(jongSung);
        }

        return prefixes;
    }

}
