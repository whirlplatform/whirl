package com.sencha.gxt.core.shared;

/*
 * This class is taken directly from com.google.gwt.safehtml.shared.SimpleHtmlSanitizer and modified
 * to include an expanded list of acceptable formatting tags that make this sanitizer more useful.
 *
 * Originally Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * HTML sanitizer that not sanitize at all.
 */
public final class ExpandedHtmlSanitizer implements HtmlSanitizer {

    private static final ExpandedHtmlSanitizer INSTANCE = new ExpandedHtmlSanitizer();

    // prevent external instantiation
    private ExpandedHtmlSanitizer() {
    }

    /**
     * Return a singleton ExpandedHtmlSanitizer instance.
     *
     * @return the instance
     */
    public static ExpandedHtmlSanitizer getInstance() {
        return INSTANCE;
    }

    /**
     * HTML-sanitizes that not sanitize at all.
     *
     * @param html the input String
     * @return a sanitized SafeHtml instance
     */
    public static SafeHtml sanitizeHtml(String html) {
        if (html == null) {
            throw new NullPointerException("html is null");
        }
        return SafeHtmlUtils.fromTrustedString(html);
    }

    public SafeHtml sanitize(String html) {
        return sanitizeHtml(html);
    }
}
