/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letv.autoapk.utils;

import com.letv.autoapk.utils.translate.AggregateTranslator;
import com.letv.autoapk.utils.translate.CharSequenceTranslator;
import com.letv.autoapk.utils.translate.EntityArrays;
import com.letv.autoapk.utils.translate.LookupTranslator;
import com.letv.autoapk.utils.translate.NumericEntityUnescaper;
import com.letv.autoapk.utils.translate.OctalUnescaper;
import com.letv.autoapk.utils.translate.UnicodeEscaper;
import com.letv.autoapk.utils.translate.UnicodeUnescaper;

/**
 * <p>Escapes and unescapes {@code String}s for
 * Java, Java Script, HTML and XML.</p>
 *
 * <p>#ThreadSafe#</p>
 * @since 2.0
 * @version $Id: StringEscapeUtils.java 1148520 2011-07-19 20:53:23Z ggregory $
 */
public class StringEscapeUtils {

    /* ESCAPE TRANSLATORS */

    /**
     * Translator object for escaping Java. 
     * 
     * While {@link #escapeJava(String)} is the expected method of use, this 
     * object allows the Java escaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator ESCAPE_JAVA = 
          new LookupTranslator(
            new String[][] { 
              {"\"", "\\\""},
              {"\\", "\\\\"},
          }).with(
            new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE())
          ).with(
            UnicodeEscaper.outsideOf(32, 0x7f) 
        );

    /**
     * Translator object for escaping EcmaScript/JavaScript. 
     * 
     * While {@link #escapeEcmaScript(String)} is the expected method of use, this 
     * object allows the EcmaScript escaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = 
        new AggregateTranslator(
            new LookupTranslator(
                      new String[][] { 
                            {"'", "\\'"},
                            {"\"", "\\\""},
                            {"\\", "\\\\"},
                            {"/", "\\/"}
                      }),
            new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
            UnicodeEscaper.outsideOf(32, 0x7f) 
        );
            
    /**
     * Translator object for escaping XML.
     * 
     * While {@link #escapeXml(String)} is the expected method of use, this 
     * object allows the XML escaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator ESCAPE_XML = 
        new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_ESCAPE()),
            new LookupTranslator(EntityArrays.APOS_ESCAPE())
        );

    /**
     * Translator object for escaping HTML version 3.0.
     * 
     * While {@link #escapeHtml3(String)} is the expected method of use, this 
     * object allows the HTML escaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator ESCAPE_HTML3 = 
        new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_ESCAPE()),
            new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE())
        );

    /**
     * Translator object for escaping HTML version 4.0.
     * 
     * While {@link #escapeHtml4(String)} is the expected method of use, this 
     * object allows the HTML escaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator ESCAPE_HTML4 = 
        new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_ESCAPE()),
            new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
            new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())
        );


    /**
     * Translator object for unescaping escaped Java. 
     * 
     * While {@link #unescapeJava(String)} is the expected method of use, this 
     * object allows the Java unescaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    // TODO: throw "illegal character: \92" as an Exception if a \ on the end of the Java (as per the compiler)?
    public static final CharSequenceTranslator UNESCAPE_JAVA = 
        new AggregateTranslator(
            new OctalUnescaper(),     // .between('\1', '\377'),
            new UnicodeUnescaper(),
            new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()),
            new LookupTranslator(
                      new String[][] { 
                            {"\\\\", "\\"},
                            {"\\\"", "\""},
                            {"\\'", "'"},
                            {"\\", ""}
                      })
        );

    /**
     * Translator object for unescaping escaped EcmaScript. 
     * 
     * While {@link #unescapeEcmaScript(String)} is the expected method of use, this 
     * object allows the EcmaScript unescaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;

    /**
     * Translator object for unescaping escaped HTML 3.0. 
     * 
     * While {@link #unescapeHtml3(String)} is the expected method of use, this 
     * object allows the HTML unescaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator UNESCAPE_HTML3 = 
        new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_UNESCAPE()),
            new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()),
            new NumericEntityUnescaper()
        );

    /**
     * Translator object for unescaping escaped HTML 4.0. 
     * 
     * While {@link #unescapeHtml4(String)} is the expected method of use, this 
     * object allows the HTML unescaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator UNESCAPE_HTML4 = 
        new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_UNESCAPE()),
            new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()),
            new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()),
            new NumericEntityUnescaper()
        );

    /**
     * Translator object for unescaping escaped XML.
     * 
     * While {@link #unescapeXml(String)} is the expected method of use, this 
     * object allows the XML unescaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     */
    public static final CharSequenceTranslator UNESCAPE_XML = 
        new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_UNESCAPE()),
            new LookupTranslator(EntityArrays.APOS_UNESCAPE()),
            new NumericEntityUnescaper()
        );


    /**
     * <p>{@code StringEscapeUtils} instances should NOT be constructed in
     * standard programming.</p>
     *
     * <p>Instead, the class should be used as:
     * <pre>StringEscapeUtils.escapeJava("foo");</pre></p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public StringEscapeUtils() {
      super();
    }


    // HTML and XML
    //--------------------------------------------------------------------------
    /**
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     *
     * <p>
     * For example:
     * </p> 
     * <p><code>"bread" & "butter"</code></p>
     * becomes:
     * <p>
     * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
     * </p>
     *
     * <p>Supports all known HTML 4.0 entities, including funky accents.
     * Note that the commonly used apostrophe escape character (&amp;apos;)
     * is not a legal entity and so is not supported). </p>
     *
     * @param input  the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * 
     * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     * 
     * @since 3.0
     */
    public static final String escapeHtml4(String input) {
        return ESCAPE_HTML4.translate(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     * <p>Supports only the HTML 3.0 entities. </p>
     *
     * @param input  the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * 
     * @since 3.0
     */
    public static final String escapeHtml3(String input) {
        return ESCAPE_HTML3.translate(input);
    }
                
    //-----------------------------------------------------------------------
    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports HTML 4.0 entities.</p>
     *
     * <p>For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;"
     * will become "&lt;Fran&ccedil;ais&gt;"</p>
     *
     * <p>If an entity is unrecognized, it is left alone, and inserted
     * verbatim into the result string. e.g. "&amp;gt;&amp;zzzz;x" will
     * become "&gt;&amp;zzzz;x".</p>
     *
     * @param input  the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * 
     * @since 3.0
     */
    public static final String unescapeHtml4(String input) {
        return UNESCAPE_HTML4.translate(input);
    }

    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports only HTML 3.0 entities.</p>
     *
     * @param input  the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * 
     * @since 3.0
     */
    public static final String unescapeHtml3(String input) {
        return UNESCAPE_HTML3.translate(input);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Escapes the characters in a {@code String} using XML entities.</p>
     *
     * <p>For example: <tt>"bread" & "butter"</tt> =>
     * <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
     * </p>
     *
     * <p>Supports only the five basic XML entities (gt, lt, quot, amp, apos).
     * Does not support DTDs or external entities.</p>
     *
     * <p>Note that Unicode characters greater than 0x7f are as of 3.0, no longer 
     *    escaped. If you still wish this functionality, you can achieve it 
     *    via the following: 
     * {@code StringEscapeUtils.ESCAPE_XML.with( NumericEntityEscaper.between(0x7f, Integer.MAX_VALUE) );}</p>
     *
     * @param input  the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see #unescapeXml(String)
     */
    public static final String escapeXml(String input) {
        return ESCAPE_XML.translate(input);
    }
                

    //-----------------------------------------------------------------------
    /**
     * <p>Unescapes a string containing XML entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes.</p>
     *
     * <p>Supports only the five basic XML entities (gt, lt, quot, amp, apos).
     * Does not support DTDs or external entities.</p>
     *
     * <p>Note that numerical \\u Unicode codes are unescaped to their respective 
     *    Unicode characters. This may change in future releases. </p>
     *
     * @param input  the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * @see #escapeXml(String)
     */
    public static final String unescapeXml(String input) {
        return UNESCAPE_XML.translate(input);
    }
                

    //-----------------------------------------------------------------------


}
