<html>
    <head>
        <script src="js/jquery.js"></script>
        <script src="https://unpkg.com/prismjs@1.29.0/prism.js"></script>
        <script src="https://unpkg.com/prismjs@1.29.0/components/prism-c.js"></script>
        <script src="https://unpkg.com/prismjs@1.29.0/plugins/normalize-whitespace/prism-normalize-whitespace.js"></script>
        ${prism_scripts}
        <script src="js/utils.js"></script>
        <script src="js/customSelect.js"></script>
        <script src="js/fileLoader.js"></script>
        <script src="js/main.js"></script>
        <!-- <link href="css/prism.css" rel="stylesheet"> -->
        <link href="https://unpkg.com/prismjs@1.29.0/themes/prism-okaidia.css" rel="stylesheet" />
        <link href="css/main.css" rel="stylesheet">

        <title>Конвертер</title>
        <meta charset="utf-8">
    </head>
    <body class="global-bg-panel">
        <label class="title">Конвертер языков программирования</label>
        <div class="text-bg">
            <table class="text-table">
                <tr>
                    <td>
                        <div class="file-input">
                            <label class="file-input">
                                <input type="file" accept="${lang_extensions}" id="text-file-input">
                                <span id = "text-file-input-text">Загрузить файл</span>
                            </label>
                        </div>
                    </td>
                    <td rowspan="4"></td>
                    <td>
                        <div id="editor-toolbar" class="toolbar">
                            <input type="button" class="button" value="Редактировать" style="width: fit-content;">
                            <input type="button" class="button" value="Редактировать" style="width: fit-content;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="convert-button" type="button" value="Преобразовать" class="button" onclick="sendSelectedLanguage()">
                    </td>
                    <td rowspan="3">
                        <table style="width: 100%; height: 100%; border-spacing: 0;">
                            <tr style="height: auto;">
                                <td>
                                    <div class="code-box">
                                        <table>
                                            <tr>
                                                <td class="orders" style="width: fit-content; width: 30px">
                                                    <div id="orders"></div>
                                                </td>
                                                <td id="code-column" style="vertical-align: top; padding: 0; margin: 0; width: auto;">
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <tr id="error-box-row">
                                <td>
                                    <textarea id="error-box" class="error-box"></textarea>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="download-button" type="button" value="Скачать" class="button" onclick="downloadActiveFile()" disabled>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="lang-select">
                            <div>
                                <custom-select id="lang-selector" name="custom-select" style="display: block">
                                    <custom-option id="loaded-file-option">Загруженный</custom-option>
                                    <label id="lang-select-message">Загрузите файл</label>
                                    ${lang_selector_values}
                                </custom-select>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>