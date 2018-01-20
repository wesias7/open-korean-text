/*
 * Open Korean Text - Scala library to process Korean text
 *
 * Copyright 2014 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openkoreantext.processor;

import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import org.openkoreantext.processor.tokenizer.Sentence;
import org.openkoreantext.processor.util.KoreanPos;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.LinkedList;
import java.util.List;

/**
 * Java wrapper for TwitterKoreanProcessor using Builder pattern
 */
public final class OpenKoreanTextProcessorJava {

  /**
   * Normalize Korean text
   * 그랰ㅋㅋㅋㅋㅋㅋ -> 그래ㅋㅋ
   *
   * @param text Input text.
   * @return Normalized text.
   */
  public static CharSequence normalize(CharSequence text) {
    return OpenKoreanTextProcessor.normalize(text);
  }

  /**
   * Tokenize with the builder options.
   *
   * @param text Input text.
   * @return A list of Korean Tokens (run tokensToJavaList to transform to Java List)
   */
  public static Seq<KoreanToken> tokenize(CharSequence text) {
    return OpenKoreanTextProcessor.tokenize(
        text
    );
  }

  /**
   * Add user-defined words to the noun dictionary. Spaced words are ignored.
   *
   * @param words List of user nouns.
   */
  public static void addNounsToDictionary(List<String> words) {
    OpenKoreanTextProcessor.addNounsToDictionary(JavaConverters.asScalaBuffer(words));
  }

  /**
   * Add user-defined word List to the dictionary for the specified KoreanPos.
   *
   * @param pos KoreanPos of words to add.
   * @param words Sequence of words to add.
   */
  public static void addWordsToDictionary(KoreanPosJava pos, List<String> words) {
    OpenKoreanTextProcessor.addWordsToDictionary(KoreanPos.withName(pos.toString()), JavaConverters.asScalaBuffer(words));
  }

  /**
   * Remove user-defined word List from the dictionary for the specified KoreanPos.
   *
   * @param pos KoreanPos of words to add.
   * @param words Sequence of words to add.
   */
  public static void removeWordFromDictionary(KoreanPosJava pos, List<String> words) {
    OpenKoreanTextProcessor.removeWordsFromDictionary(KoreanPos.withName(pos.toString()), JavaConverters.asScalaBuffer(words));
  }

  /**
   * Transforms the tokenization output to List<KoreanTokenJava>
   *
   * @param tokens Korean tokens (output of tokenize(CharSequence text)).
   * @return List of KoreanTokenJava.
   */
  public static List<KoreanTokenJava> tokensToJavaKoreanTokenList(Seq<KoreanToken> tokens, boolean keepSpace) {
    Iterator<KoreanToken> tokenized = tokens.iterator();
    List<KoreanTokenJava> output = new LinkedList<>();
    while (tokenized.hasNext()) {
      KoreanToken token = tokenized.next();
      String stem = "";
      if (token.stem().nonEmpty()) {
        stem += token.stem().get();
      }
      if (keepSpace || token.pos() != KoreanPos.Space()) {
        output.add(new KoreanTokenJava(
            token.text(),
            KoreanPosJava.valueOf(token.pos().toString()),
            token.offset(),
            token.length(),
            token.unknown(),
            stem
        ));
      }
    }
    return output;
  }

  // Default behavior of keepSpace is false
  public static List<KoreanTokenJava> tokensToJavaKoreanTokenList(Seq<KoreanToken> tokens) {
    return tokensToJavaKoreanTokenList(tokens, false);
  }


  /**
   * Tokenize with the builder options into a String Iterable.
   *
   * @param tokens Korean tokens (output of tokenize(CharSequence text)).
   * @return List of token strings.
   */
  public static List<String> tokensToJavaStringList(Seq<KoreanToken> tokens, boolean keepSpace) {
    Iterator<KoreanToken> tokenized = tokens.iterator();
    List<String> output = new LinkedList<>();
    while (tokenized.hasNext()) {
      final KoreanToken token = tokenized.next();

      if (keepSpace || token.pos() != KoreanPos.Space()) {
        output.add(token.text());
      }
    }
    return output;
  }

  // Default behavior of keepSpace is false
  public static List<String> tokensToJavaStringList(Seq<KoreanToken> tokens) {
    return tokensToJavaStringList(tokens, false);
  }

  /**
   * Split input text into sentences.
   *
   * @param text Input text.
   * @return List of Sentence objects.
   */
  public static List<Sentence> splitSentences(CharSequence text) {
    return JavaConverters.seqAsJavaList(
        OpenKoreanTextProcessor.splitSentences(text)
    );
  }

  /**
   * Extract phrases from Korean input text
   *
   * @param tokens Korean tokens (output of tokenize(CharSequence text)).
   * @return List of phrase CharSequences.
   */
  public static List<KoreanPhraseExtractor.KoreanPhrase> extractPhrases(Seq<KoreanToken> tokens, boolean filterSpam, boolean includeHashtags) {
    return JavaConverters.seqAsJavaList(
        OpenKoreanTextProcessor.extractPhrases(tokens, filterSpam, includeHashtags)
    );
  }

  /**
   * Detokenize the input list of words.
   *
   * @param tokens List of words.
   * @return Detokenized string.
   */
  public static String detokenize(List<String> tokens) {
    return OpenKoreanTextProcessor.detokenize(JavaConverters.iterableAsScalaIterable(tokens));
  }
}
