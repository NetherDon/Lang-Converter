<%@ page contentType="text/html;charset=utf-8" %>
<!--${test}-->
<head>
    <!--Prism-->
    <script src="https://unpkg.com/prismjs@1.29.0/prism.js"></script>
    <script src="https://unpkg.com/prismjs@1.29.0/plugins/normalize-whitespace/prism-normalize-whitespace.js"></script>
    <link href="https://unpkg.com/prismjs@1.29.0/themes/prism-funky.css" rel="stylesheet" />

    <script src="https://unpkg.com/prismjs@1.29.0/components/prism-c.js"></script>
    <script src="https://unpkg.com/prismjs@1.29.0/components/prism-java.js"></script>
    <script src="https://unpkg.com/prismjs@1.29.0/components/prism-csharp.js"></script>
    <script src="https://unpkg.com/prismjs@1.29.0/components/prism-python.js"></script>

    <!--Own-->
    <script src="js/jquery.js"></script>
    <script src="js/common.js"></script>
    <script src="js/requests.js"></script>
    <script src="js/buttonEvents.js"></script>
    <script src="js/textEvents.js"></script>
    <script src="js/main.js"></script>
    <link href="css/main.css" rel="stylesheet">

    <!--Another-->
    <title>Конвертер</title>
    <meta charset="utf-8">
</head>
<body style="padding: auto 20px;">
    <p class="title">Конвертер кода</p>
    ${files_table}
    <div id="message-bg" style="display: none;">
        <div id="message-panel">
            <p>Предупреждения</p>
            <input id="message-close-btn" type="button" value="×">
            <pre id="message-text">Random text second line</pre>
        </div>
    </div>
    <div id="links-bg">
        <a id="conv-gen-btn" href="https://github.com/NetherDon/Lang-Converter-Gen" target="_blank">Генератор конвертеров</a>
        <a id="conv-gen-download-btn" href="https://github.com/NetherDon/Lang-Converter-Gen/releases/latest/download/ConverterGen.jar" target="_blank">Скачать</a>
        <a id="app-code-btn" href="https://github.com/NetherDon/Lang-Converter" target="_blank">Исходный код приложения</a>
    </div>
</body>