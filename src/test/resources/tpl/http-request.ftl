<html lang="en">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<head>
    <meta http-equiv="content-type" content="text/html; charset = UTF-8">
    <title>HTTP-Request</title>
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
        <th colspan="3">
            <pre><code class="http"><#if data.method??>${data.method}<#else>GET</#if>: <#if data.url??>${data.url}<#else>Unknown</#if></code></pre>
        </th>
    </tr>
    <#if data.body??>
        <tr>
            <th nowrap>Body</th>
            <td colspan="2">
                <pre><code>${data.body}</code></pre>
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
    <#if (data.cookies)?has_content>
        <tr>
            <th colspan="3">Cookies</th>
        </tr>
        <#list data.cookies as name, value>
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${name}:</td>
                <td colspan="2">
                    <pre>${value}</pre>
                </td>
            </tr>
        </#list>
    </#if>
    <#if data.curl??>
        <tr>
            <th>Curl</th>
        </tr>
        <tr>
            <td colspan="3">
                <pre><code class="bash">${data.curl}</code></pre>
            </td>
        </tr>
    </#if>

    </tbody>
</table>


</body>
</html>