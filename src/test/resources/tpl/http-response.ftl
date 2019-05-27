<html lang="en">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<head>
    <meta http-equiv="content-type" content="text/html; charset = UTF-8">
    <title>HTTP-Response</title>
    <link href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.15.6/styles/default.min.css" rel="stylesheet"
          crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"
          crossorigin="anonymous">
    <script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.15.6/highlight.min.js"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"
            crossorigin="anonymous"></script>

    <script>hljs.initHighlightingOnLoad();</script>

    <style>
        pre {
            white-space: pre-wrap;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<table class="table table-sm table-hover">
    <tbody>
    <tr class="table-primary">
        <th nowrap>Status code</th>
        <th colspan="2"><#if data.responseCode??>${data.responseCode}<#else>Unknown</#if></th>
    </tr>
    <#if data.url??>
        <tr>
            <th nowrap>Data URL</th>
            <td colspan="2">
                <pre>${data.url}</pre>
            </td>
        </tr>
    </#if>
    <#if (data.headers)?has_content>
        <tr>
            <th colspan="3" nowrap>Headers</th>
        </tr>
        <#list data.headers as name, value>
            <tr>
                <td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${name}:</td>
                <td colspan="2">
                    <pre>${value}</pre>
                </td>
            </tr>
        </#list>
    </#if>
    <#if data.body??>
        <tr>
            <th colspan="3" nowrap>Body</th>
        </tr>
        <tr>
            <th colspan="3"><pre><code>${data.body}</code></pre></th>
        </tr>
    </#if>

    <#if (data.cookies)?has_content>
        <tr>
            <th colspan="3">Cookies</th>
        </tr>
        <#list data.cookies as name, value>
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${name}</td>
                <td colspan="2">
                    <pre>${value}</pre>
                </td>
            </tr>
        </#list>
    </#if>

    </tbody>
</table>

</body>
</html>