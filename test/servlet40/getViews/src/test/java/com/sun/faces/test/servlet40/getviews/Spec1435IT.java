/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.faces.test.servlet40.getviews;

import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_4;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M10;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class Spec1435IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.close();
    }
    
    // ### ViewHandler based tests
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViews() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("view: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPath() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("view: /foo.xhtml"));
        
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewsAsImplicit() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?implicit=true");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews"));
        assertTrue(content.contains("view: /foo"));
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        assertTrue(content.contains("/level2/level3/foo"));
        assertTrue(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("view: /foo.xhtml"));
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewsWithLimit2() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?maxDepth=2");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("view: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicit() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        assertTrue(content.contains("/level2/level3/foo"));
        assertTrue(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitWithLimit2() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&maxDepth=2");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        // Contains only the views up to level 2, not those of /level2/ + 2
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        
        assertFalse(content.contains("/level2/level3/foo"));
        assertFalse(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitWithLimit3() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&maxDepth=3");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        // Contains only the views up to level 3, not those of /level2/ + 2
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        assertTrue(content.contains("/level2/level3/foo"));
        
        assertFalse(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitWithLimit0() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&maxDepth=0");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        // Special case, maxDepth lower than level of requested path - views from requested path are returned
        // but no other paths are traversed
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        
        assertFalse(content.contains("/level2/level3/foo"));
        assertFalse(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    
    // ### ViewDeclarationLanguage based tests
    
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewsVDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?fromVDL=true");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("view: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathVDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&fromVDL=true");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("view: /foo.xhtml"));
        
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewsAsImplicitVDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?implicit=true&fromVDL=true");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews"));
        assertTrue(content.contains("view: /foo"));
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        assertTrue(content.contains("/level2/level3/foo"));
        assertTrue(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("view: /foo.xhtml"));
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewsWithLimit2VDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?maxDepth=2&fromVDL=true");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("view: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitVDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&fromVDL=true");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        assertTrue(content.contains("/level2/level3/foo"));
        assertTrue(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitWithLimit2VDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&maxDepth=2&fromVDL=true");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        // Contains only the views up to level 2, not those of /level2/ + 2
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        
        assertFalse(content.contains("/level2/level3/foo"));
        assertFalse(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitWithLimit3VDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&maxDepth=3&fromVDL=true");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        // Contains only the views up to level 3, not those of /level2/ + 2
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        assertTrue(content.contains("/level2/level3/foo"));
        
        assertFalse(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewsForPathImplicitWithLimit0VDL() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViews.jsf?path=%2Flevel2%2F&implicit=true&maxDepth=0&fromVDL=true");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews"));
        assertFalse(content.contains("view: /foo"));
        
        // Special case, maxDepth lower than level of requested path - views from requested path are returned
        // but no other paths are traversed
        assertTrue(content.contains("/level2/bar"));
        assertTrue(content.contains("/level2/foo"));
        
        assertFalse(content.contains("/level2/level3/foo"));
        assertFalse(content.contains("/level2/level3/level4/foo"));
        
        assertFalse(content.contains("/level2/bar.xhtml"));
        assertFalse(content.contains("/level2/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
        
        assertFalse(content.contains("/some_file"));
        assertFalse(content.contains("include"));
    }
    
    
    // ### ViewDeclarationLanguage based tests
    
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewResources() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViewResources.jsf");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("resource name: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/WEB-INF/include.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewResourcesForPath() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViewResources.jsf?path=%2Flevel2%2F");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("view: /foo.xhtml"));
        assertFalse(content.contains("/WEB-INF/include.xhtml"));
        
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewResourcesTopLevel() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViewResources.jsf?topLevel=true");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("resource name: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/WEB-INF/include.xhtml"));
        assertFalse(content.contains("/some_file.txt"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewResourcesWithLimit2() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViewResources.jsf?maxDepth=2");
        String content = page.asXml();
        
        assertTrue(content.contains("/getViews.xhtml"));
        assertTrue(content.contains("resource name: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        
        assertTrue(content.contains("/WEB-INF/include.xhtml"));
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetViewResourcesForPathWithLimit3() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViewResources.jsf?path=%2Flevel2%2F&maxDepth=3");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("resource name: /foo.xhtml"));
        assertFalse(content.contains("include.xtml"));
        
        // Contains only the view resources up to level 3, not those of /level2/ + 2
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        assertTrue(content.contains("/level2/level3/foo.xhtml"));
        
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    public void testGetAllViewResourcesForPathWithLimit0() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "getViewResources.jsf?path=%2Flevel2%2F&maxDepth=0");
        String content = page.asXml();
        
        assertFalse(content.contains("/getViews.xhtml"));
        assertFalse(content.contains("resource name: /foo.xhtml")); // include marker since is also subset of "/level2/foo.xhtml" etc
        assertFalse(content.contains("/WEB-INF/include.xhtml"));
        
        assertTrue(content.contains("/level2/bar.xhtml"));
        assertTrue(content.contains("/level2/foo.xhtml"));
        
        assertFalse(content.contains("/level2/level3/foo.xhtml"));
        assertFalse(content.contains("/level2/level3/level4/foo.xhtml"));
        
        assertFalse(content.contains("/some_file.txt"));
        assertFalse(content.contains("include.xtml"));
    }
    

    
   
}
