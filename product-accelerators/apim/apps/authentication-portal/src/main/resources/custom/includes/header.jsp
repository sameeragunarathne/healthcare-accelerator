<%--
~ Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~    http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied.  See the License for the
~ specific language governing permissions and limitations
~ under the License.
--%>

<!-- localize.jsp MUST already be included in the calling script -->


<%@ page import="java.io.File" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="java.net.URI"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.wso2.healthcare.apim.core.api.server.DeploymentConfigAPI" %>

<%
  String tenant = request.getParameter("tenantDomain");
  if (tenant == null) {
      String cb = request.getParameter("callback");
      cb = StringUtils.replace(cb, " ", "");
      if (cb != null) {
          URI uri = new URI(cb);
          String decodedValue = uri.getQuery();
          String[] params = decodedValue.split("&");
          for (String param : params) {
              if (param.startsWith("tenantDomain=")) {
                  String[] keyVal = param.split("=");
                  tenant = keyVal[1];
              }
          }
      }
  }
  
  String headerTitle = DeploymentConfigAPI.getOHDeploymentName();
  String mainColor = DeploymentConfigAPI.getWebappMainColor();
  String pageTitle = DeploymentConfigAPI.getWebappTitle();
  String footerText = DeploymentConfigAPI.getWebappFooter();
  String faviconSrc = DeploymentConfigAPI.getWebappFavicon();
  String additionalFooterElement = DeploymentConfigAPI.getWebappFooterSecondaryHTML();
  String logoSrc = null;
  String logoHeight = "50";
  String logoWidth = "50";
  String logoAltText = "";
  File customCSSFile = null;
  String customCSS = "";
  String tenantThemeDirectoryName = "";
  boolean showCookiePolicy = true;
  boolean showPrivacyPolicy = true;
  String cookiePolicyText = null;
  String privacyPolicyText = null;
  if (tenant != null) {
      String current = new File(".").getCanonicalPath();
      String tenantConfLocation = "/repository/deployment/server/jaggeryapps/devportal/site/public/tenant_themes/";
      tenantThemeDirectoryName = tenant;
      String tenantThemeFile =  current + tenantConfLocation + tenantThemeDirectoryName + "/login/" + "loginTheme.json";
      customCSS = current + tenantConfLocation + tenantThemeDirectoryName + "/login/css/" + "loginTheme.css";
      File directory = new File(current + tenantConfLocation + tenantThemeDirectoryName);
      if (directory != null && directory.exists() && directory.isDirectory()) {
          File themeFile = new File(tenantThemeFile);
          customCSSFile = new File(customCSS);
          if (themeFile != null && themeFile.exists() && themeFile.isFile()) {
              FileReader fr = new FileReader(themeFile);
              JSONParser parser = new JSONParser();
              Object obj = parser.parse(fr);
              JSONObject jsonObject = (JSONObject) obj;
              pageTitle = (String)jsonObject.get("title") != null ? (String)jsonObject.get("title") : "WSO2 Open Healthcare";
              JSONObject headerThemeObj = (JSONObject)jsonObject.get("header");
              if (headerThemeObj != null) {
                  headerTitle = (String)(headerThemeObj.get("title")) != null ? (String)(headerThemeObj.get("title")) : "Open Healthcare";
              }
              JSONObject footerThemeObj = (JSONObject)jsonObject.get("footer");
              if (footerThemeObj != null) {
                  footerText = (String)(footerThemeObj.get("name"));
              }
              JSONObject faviconThemeObj = (JSONObject)jsonObject.get("favicon");
              if (faviconThemeObj != null) {
                  String fileName = (String)(faviconThemeObj.get("src"));
                  if (!StringUtils.isEmpty(fileName)) {
                      faviconSrc = "/devportal/site/public/tenant_themes/" + tenantThemeDirectoryName + "/login/images/"
                                + fileName;
                  }
              }
              JSONObject logoThemeObj = (JSONObject)jsonObject.get("logo");
              if (logoThemeObj != null) {
                  String fileName = (String)(logoThemeObj.get("src"));
                  if (!StringUtils.isEmpty(fileName)) {
                      logoSrc = "/devportal/site/public/tenant_themes/" + tenantThemeDirectoryName + "/login/images/"
                                + fileName;
                  }
                  logoHeight = (String)(logoThemeObj.get("height")) != null ? (String)(logoThemeObj.get("height")) : logoHeight;
                  logoWidth = (String)(logoThemeObj.get("width")) != null ? (String)(logoThemeObj.get("width")) : logoWidth;
                  logoAltText = (String)(logoThemeObj.get("alt"));
              }
              JSONObject cookiePolicyThemeObj = (JSONObject)jsonObject.get("cookie-policy");
              if (cookiePolicyThemeObj != null) {
                  showCookiePolicy = (Boolean)(cookiePolicyThemeObj.get("visible"));
                  cookiePolicyText = (String)cookiePolicyThemeObj.get("text");
              }
              JSONObject privacyPolicyThemeObj = (JSONObject)jsonObject.get("privacy-policy");
              if (privacyPolicyThemeObj != null) {
                  showPrivacyPolicy = (Boolean)(privacyPolicyThemeObj.get("visible"));
                  privacyPolicyText = (String)privacyPolicyThemeObj.get("text");
              }
          }
      }
  }
  request.setAttribute("pageTitle", pageTitle);
  request.setAttribute("footerText", footerText);
  request.setAttribute("additionalFooterElement", additionalFooterElement);
  request.setAttribute("faviconSrc", faviconSrc);
  request.setAttribute("showCookiePolicy", showCookiePolicy);
  request.setAttribute("showPrivacyPolicy", showPrivacyPolicy);
  request.setAttribute("cookiePolicyText", cookiePolicyText);
  request.setAttribute("privacyPolicyText", privacyPolicyText);
  request.setAttribute("logoSrc", logoSrc);
  request.setAttribute("logoHeight", logoHeight);
  request.setAttribute("logoWidth", logoWidth);
  request.setAttribute("logoAltText", logoAltText);
  if (customCSSFile != null && customCSSFile.exists() && customCSSFile.isFile()) {
  String cssRelativePath = "/devportal/site/public/tenant_themes/" + tenantThemeDirectoryName + "/login/css/" + "loginTheme.css";
      request.setAttribute("customCSS", cssRelativePath);
  } else {
      request.setAttribute("customCSS", "");
  }
%>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="icon" href=<%=request.getAttribute("faviconSrc")%> type="image/x-icon"/>

<title><%=request.getAttribute("pageTitle")%></title>

<style>
  html, body {
        height: 100%;
        margin: 0 auto;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
  }
  body {
        flex-direction: column;
        display: flex;
  }
  main {
        flex-shrink: 0;
  }
  main.center-segment {
        margin: auto;
        display: flex;
        align-items: center;
  }
  main.center-segment > .ui.container.medium {
        max-width: 450px !important;
  }
  main.center-segment > .ui.container.large {
        max-width: 700px !important;
  }
  main.center-segment > .ui.container > .ui.segment {
        margin-bottom: 40px;
        margin-top: 20px;
        width: 450px;
        height: auto;
        background: #ffffff;
        border: 1px solid #cccccc;
        padding-top: 20px;
        padding-bottom: 20px;
        padding-left: 50px;
        padding-right: 50px ;
        box-sizing: border-box;
        box-shadow: 0px 1px 20px rgba(0, 0, 0, 0.28);
        border-radius: 3px;
        text-align: center;
  }
  main.center-segment > .ui.container > .ui.segment .segment-form .buttons {
        margin-top: 1em;
  }
  main.center-segment > .ui.container > .ui.segment .segment-form .buttons.align-right button,
  main.center-segment > .ui.container > .ui.segment .segment-form .buttons.align-right input {
        margin: 0 0 0 0.25em;
  }
  main.center-segment > .ui.container > .ui.segment .segment-form .column .buttons.align-left button.link-button,
  main.center-segment > .ui.container > .ui.segment .segment-form .column .buttons.align-left input.link-button {
        padding: .78571429em 1.5em .78571429em 0;
  }
  main.center-segment > .ui.container > .ui.segment .segment-form {
        text-align: left;
  }
  main.center-segment > .ui.container > .ui.segment .segment-form .align-center {
        text-align: center;
  }
  main.center-segment > .ui.container > .ui.segment .segment-form .align-right {
        text-align: right;
  }
  .fieldContainer{
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 15px;
  }
  .material-textfield {
        position: relative;  
  }
  label {
        position: absolute;
        font-size: 15px;
        font-weight: 400;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        background-color: white;
        color: gray;
        padding: 0 0.3rem;
        margin: 0 0.5rem;
        transition: .1s ease-out;
        transform-origin: left top;
        pointer-events: none;
  }
  input {
        font-size: 1rem;
        outline: none;
        border: solid 1px #cccccc;
        border-radius: 3px;  
        padding: 5px 10px;
        font-size: 16px;
        font-weight: 400;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
        color: #000000;
        transition: 0.1s ease-out;
        height:30px;
        width: 330px;
  }
  input:focus {
        border: solid 1px #cccccc; 
  }
  input:focus + label {
        color: <%=mainColor%>;
        top: 0;
        transform: translateY(-50%) scale(.9);
        font-size: 12px;
  }
  input:not(:placeholder-shown) + label {
        top: 0;
        transform: translateY(-50%) scale(.9);
        font-size: 12px;
  }
  input:hover{
        border: solid 1px <%=mainColor%>; 
  }
  input:focus:hover{
        border: solid 1px #cccccc; 
  }
  .ui.visible.warning.message{
        color: #656565;
        text-align: left;
        font-size: 0.75rem;
        margin-top: 25px;
        margin-bottom: 35px;
        line-height: 20px;
  }
  .ui.checkbox{
        margin-bottom: 1em;
        display: flex;
  }
  .round {
        position: relative;
        margin-right: 10px;
  }
  .round label {
        background-color: #fff;
        border: 1px solid #ccc;
        border-radius: 50%;
        cursor: pointer;
        height: 18px;
        left: 0;
        position: absolute;
        top: 0;
        width: 18px;
  }
  .round label:after {
        border: 2px solid #fff;
        border-top: none;
        border-right: none;
        content: "";
        height: 4px;
        left: 3px;
        opacity: 0;
        position: absolute;
        top: 5px;
        transform: rotate(-45deg);
        width: 10px;
  }
  .round input[type="checkbox"] {
        visibility: hidden;
  }
  .round input[type="checkbox"]:checked + label {
        background-color: <%=mainColor%>;
        border-color: <%=mainColor%>;
  }
  .round input[type="checkbox"]:checked + label:after {
        opacity: 1;
  }
  .ui.checkbox.label{
        font-size: 12px;
        color: #656565;
        font-weight: 400;
        margin-top: 4px;
  }
  .ui.divider{
        border: 1px solid rgba(204, 204, 204, 0.5);
        width: 350px;
        margin-top: 5px;
        margin-bottom: 20px;
  }
  .ui.primary.large.button{
        cursor: pointer;
        width: 350px;
        margin-bottom: 20px;        
        background-color:<%=mainColor%>;
        border: none;
        font-size: 0.85rem;
        text-transform: none;
        border-radius: 3px;
        font-weight: 500;
        color: #ffffff;
        box-shadow: 0 0 0 1px rgba(34, 36, 38, 0.15) inset;
        padding: 10px;
        height: 36px;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
  }
  .ui.primary.large.button:hover{
        cursor: pointer;
        width: 350px;
        font-weight: 400;
        background-color: <%=mainColor%>;
        filter: brightness(90%);
        border: none;
        font-size: 0.85rem;
        text-transform: none;
        border-radius: 3px;
        font-weight: 500;
        color: #ffffff;
        box-shadow: 0 0 0 1px rgba(34, 36, 38, 0.15) inset;
        padding: 10px;
        height: 36px;
  }
  /* unvisited link */
  a:link {
        color: <%=mainColor%>;
        text-decoration: underline;
  }
  /* visited link */
  a:visited {
        color: <%=mainColor%>;
        text-decoration: underline;
  }
  /* mouse over link */
  a:hover {
        color: <%=mainColor%>;
        filter: brightness(90%);
        text-decoration: underline;
  }
  /* selected link */
  a:active {
        color: <%=mainColor%>;
        text-decoration: underline;
  }
  .ui.large.button.link-button{
        cursor: pointer;
        width: 350px;
        font-weight: 400;
        background-color: #ffffff;
        border: none;
        text-transform: none;
        text-align: center;
        text-decoration: none;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
  }
  .errorPage {
        margin: auto;
        display: flex;
        align-items: center;
  }
  .errorImageContainer {
        width: 25vw;
        height: auto;
        margin:0 auto;
        display: flex;
        align-items: center;
        justify-content: center;
  }
  .errorImage {
        width: 100%;
        height: auto;
  }
  .ui.visible.negative.message.header {
        color: <%=mainColor%>;
        font-size: 2rem;
        font-weight: 500;
        text-align: center;
  }
  .ui.visible.negative.message.body {
        color: #656565;
        font-size: 1.3rem;
        text-align: center;
        font-weight: 400;
        line-height: 2em;
	    width: 90%;
	    margin:0 auto;
  }
  .actions{
        margin:0 auto;
        display: flex;
        align-items: center;
        justify-content: center;
  }
  .ui.primary.button.cancel{
        cursor: pointer;
        width: 250px;
        font-weight: 300;
        background-color: <%=mainColor%>;
        border: none;
        font-size: 0.85rem;
        text-transform: none;
        border-radius: 3px;
        font-weight: 500;
        color: #ffffff;
        box-shadow: 0 0 0 1px rgba(34, 36, 38, 0.15) inset;
        padding: 10px;
        height: 36px;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
  }
  .ui.primary.button.cancel:hover{
        cursor: pointer;
        width: 250px;
        font-weight: 400;
        background-color: <%=mainColor%>;
        filter: brightness(90%);
        border: none;
        font-size: 0.85rem;
        text-transform: none;
        border-radius: 3px;
        font-weight: 500;
        color: #ffffff;
        box-shadow: 0 0 0 1px rgba(34, 36, 38, 0.15) inset;
        padding: 10px;
        height: 36px;
  }
  footer {
        padding: 2rem 0;
        font-size: 12px;
        color: #a3a3a3;
  }
  body .product-title .product-title-text {
        margin: 0 ;
  }
  .product-title{
        display: flex;
        justify-content: center;
  }
  .product-title-text{
        text-transform: capitalize;
        font-weight: 400;
        font-size: 1.2rem;
        padding-bottom: 5px;
  }
  .theme-icon.inline.auto.transparent.product-logo{
        width: 60px;
        height: auto;
        margin-right: 15px;
  }
  .svg.logo{
        width: 100%;
        height: auto;
  }
  body .center-segment .product-title .product-title-text {
        margin-bottom: 1em;
  }
  .ui.menu.fixed.app-header .product-logo {
        padding-left: 0;
  }
  .ui.visible.negative.message {
        color: #d4543c;
        margin-bottom: 15px;
        font-size: 14px;
  }
  .grantBox {
        padding:10px;
        border: #ccc 1px solid;
        text-align: left;
        margin-bottom: 20px;
  }
  .grantMainTitle {
        font-weight: 500;
        font-size: 16px;
        margin-bottom: 1.5em;
        line-height: 25px;
        text-align: left;
  }
  .grantBoxHeading {
        margin:5px 0;
        font-size: 14px;
        color: #1a1a1a;
        font-weight: 400;
  }
  .grantBoxContent {
        margin-top: 10px;
        margin-bottom: 5px;
        display: flex;
        align-items: center;
  }
  .grantRadioButton {
        width:20px;
        border: 2px solid <%=mainColor%>;
        margin-right: 10px;
  }
  .grantRadioLabel {
        font-size: 14px;
        font-weight: 400;
  }
  .consentStatement {
        font-size: 14px;
        color: #1a1a1a;
        font-weight: 400;
        line-height: 20px;
  }
  .grantCancelButton{
        cursor: pointer;
        width: 165px;
        margin-right: 20px;
        background-color: #ffffff;
        box-shadow: 0 0 0 1px rgba(34, 36, 38, 0.15);
        border: none;
        text-transform: none;
        text-align: center;
        text-decoration: none;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
        font-weight: 500;
        font-size: 0.85rem;
        border-radius: 3px;
        color: <%=mainColor%>;
        padding: 10px;
        height: 36px;
  }
  .grantCancelButton:hover {
        cursor: pointer;
        width: 165px;
        margin-right: 20px;
        background-color: #ffffff;
        box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.295);
        border: none;
        text-transform: none;
        text-align: center;
        text-decoration: none;
        font-family: -apple-system, BlinkMacSystemFont, Segoe WPC, Segoe UI,
        HelveticaNeue-Light, Ubuntu, Droid Sans, sans-serif, font-wso2,
        "Helvetica Neue", Arial, Helvetica, sans-serif;
        font-weight: 500;
        font-size: 0.85rem;
        border-radius: 3px;
        color: <%=mainColor%>;
        padding: 10px;
        height: 36px;
  }
  /* Table of content styling */
  main #toc {
        position: sticky;
        top: 93px;
  }
  main .ui.segment.toc {
        padding: 20px;
  }
  main .ui.segment.toc ul.ui.list.nav > li.sub {
        margin-left: 20px;
  }
  main .ui.segment.toc ul.ui.list.nav > li > a {
        color: rgba(0,0,0,.87);
        text-decoration: none;
  }
  main .ui.segment.toc ul.ui.list.nav > li:before {
        content: "\2219";
        font-weight: bold;
        font-size: 1.6em;
        line-height: 0.5em;
        display: inline-block;
        width: 1em;
        margin-left: -0.7em;
  }
  main .ui.segment.toc ul.ui.list.nav > li.sub:before {
        content: "\2192";
        margin-left: -1em;
  }
  main .ui.segment.toc ul.ui.list.nav > li:hover a {
        color: <%=mainColor%>;
        text-decoration: none;
  }
  main .ui.segment.toc ul.ui.list.nav > li:hover:before {
        color:  <%=mainColor%>;
  }
</style>

<%
  String cssPath = request.getAttribute("customCSS") + "";
  if (!StringUtils.isEmpty(cssPath)) {
%>
      <link href=<%=cssPath%> rel="stylesheet" type="text/css">
<%	}
%>

<script src="libs/jquery_3.5.0/jquery-3.5.0.js"></script>