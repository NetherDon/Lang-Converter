$(document).ready(() =>
{
    this.langFiles = new LangFiles({
        downloadButton: document.getElementById("download-button"),
        convertButton: document.getElementById("convert-button"),
        orderCol: document.getElementById("orders"),
        codeCol: document.getElementById("code-column"),
        toolbar: document.getElementById("editor-toolbar"),
        errorObj: {
            row: document.getElementById("error-box-row"),
            box: document.getElementById("error-box")
        }
    });

    this.langFiles.onchange = (id, file) =>
    {
        if (id == null)
        {
            var loadedOption = document.getElementById("loaded-file-option");
            loadedOption.innerHTML = "Загруженный";
            
            if (file)
            {
                langFiles.reset();
                for (const id of file.langData.existingConverters)
                {
                    langFiles.createPage(id);
                }

                var langSelector = document.getElementById("lang-selector");
                loadedOption.innerHTML = `${file?.file?.name ?? "Загруженный"}<br>(${file.langData.name})`; 
                langSelector.setHiddenForOptions({
                    flag: false,
                    orElseFlag: true,
                    condition: (opt) => opt.value == null || (file.langData.existingConverters?.includes(opt.value) ?? false)
                });
                
                var selectMessage = $("#lang-select-message");
                if (langSelector.visibleOptionCount <= 1)
                {
                    selectMessage.show();
                }
                else
                {
                    selectMessage.hide();
                }
            }
        }
    };

    let loadFile = (file) =>
    {
        if (file)
        {
            langFiles.startLoading(null);

            initUserLangFileData({
                file: file,
                success: (fileData) => 
                {
                    if (fileData.isValid)
                    {
                        this.langFiles.stopLoading(null, fileData);
                        var selectMessage = document.getElementById("lang-select-message");
                        if (selectMessage)
                        {
                            selectMessage.innerHTML = "Отсутствуют доступные языки";
                        }

                        document.getElementById("lang-selector")?.select(null);
    
                        console.log(`File "${fileData.file.name}" was loaded`);
                        console.log(fileData);
                    }
                    else
                    {
                        langFiles.stopLoading();
                    }
                },
                error: (error) =>
                {
                    var selectMessage = document.getElementById("lang-select-message");
                    if (selectMessage)
                    {
                        const messages = {};
                        messages[FileLoadingErrors.NullFile] = "Загрузите файл";
                        messages[FileLoadingErrors.InvalidExtension] = "Расширение файла не поддерживается";
                        messages[FileLoadingErrors.FileReading] = "Не удалось прочитать файл";
                        messages[FileLoadingErrors.LargeFileSize] = `Размер файла превышает ${MaxFileSize.str}`;

                        selectMessage.innerHTML = messages[error];
                    }
                    
                    langFiles.stopLoading();
                }
            });
        }
    }

    $("html").on('dragover', false) .on("drop", (ev) =>
    {
        ev.preventDefault();
        ev.stopPropagation();

        let origin = ev.originalEvent;
        if (origin.dataTransfer?.files?.[0])
        {
            loadFile(origin.dataTransfer.files[0]);
        }
    });

    $("#text-file-input").change((event) =>
    {
        loadFile(event.target.files[0]);
        event.target.value = "";
    });
});

const FileLoadingErrors = { 
    NullFile: "null-file", 
    InvalidExtension: "invalid-extension",
    FileReading: "file-reading",
    LargeFileSize: "large-file-size"
}

const MaxFileSize = {
    value: 50*(1024**2),
    str: "50Мб"
};

function initUserLangFileData(args)
{
    var data = { 
        file: args?.file,
        extension: null,
        langData: null,
        content: "",

        get isValid()
        {
            return !!this.langData && !!this.file;
        }
    };

    function runSuccess()
    {
        args?.success?.(asReadonly(data, true));
    }

    function runError(type, message)
    {
        args?.error?.(type, message);
    }

    if (data.file == null)
    {
        runError(FileLoadingErrors.NullFile);
        return;
    }

    if (data.file.size > MaxFileSize.value)
    {
        runError(FileLoadingErrors.LargeFileSize);
        return;
    }

    data.extension = data.file.name.split('.').pop();

    $.ajax({
        url: "/lang",
        method: "get",
        dataType: "text",
        data: { "extension": data.extension },
        success: (data2) => 
        {
            try
            {
                data.langData = JSON.parse(data2);

                const reader = new FileReader();
                reader.addEventListener('load', (event) => 
                {
                    data.content = event.target.result;
                    runSuccess();
                });
                reader.addEventListener('error', (event) =>
                {
                    runError(FileLoadingErrors.FileReading);
                });
                reader.readAsText(data.file);
            }
            catch (e)
            {
                runError(FileLoadingErrors.FileReading);
            }
        },
        error : (xhr, status, error) => 
        {
            runError(FileLoadingErrors.InvalidExtension);
        }
    });
}

class Loading
{
    static #stages = [".", "..", "..."];
    #cursor = 0;
    #interval = null;

    get stage()
    {
        var length = Loading.#stages.length;
        return Loading.#stages[this.#cursor%length];
    }

    start()
    {
        if (this.#interval)
            return;

        this.#interval = setInterval(() => 
        {
            this.#cursor++;
            this.onchange?.(this);
        }, 250);
    }

    stop()
    {
        if(this.#interval)
            clearInterval(this.#interval);
        this.#interval = null;
    }
}

class LangError
{
    #message;
    #row;

    constructor(message = "", row = -1)
    {
        this.#message = message;
        this.#row = typeof row == 'number' ? row : -1;
    }

    get message() { return this.#message; }
    get row() { return this.#row; }
}

class LangFilesPage
{
    #id;
    #error = null;
    #file = null;
    #isLoading = false;

    constructor(id)
    {
        this.#id = id;
        this.#file = null;
    }

    get id() { return this.#id; }
    get error() { return this.isEditable ? null : this.#error; }
    get isLoading() { return this.#isLoading; }
    get file() { return this.#file; }
    get lang() { return this.file?.lang; }
    get isEditable() { return false; }

    get toolbarButtons()
    {
        let result = [];
        this._addCopyButton(result);
        
        const editorUrl = this.file?.editorUrl;
        if (editorUrl)
        {
            result.push({
                text: "Онлайн компилятор",
                onclick: () => window.open(editorUrl, '_blank').focus()
            });
        }

        return result;
    }

    set error(value)
    {
        change(value instanceof LangError ? value : null, this.#error, (error) => this.#error = error, this.onErrorChange);
    }

    set isLoading(flag) 
    { 
        change(!!flag, this.#isLoading, (flag) => this.#isLoading = flag, this.onLoadingChange);
    }

    set file(value)
    {
        change(value, this.#file, (file) => this.#file = file, (val, old) =>
        {
            this._onFileChange(val, old);
            this.onFileChange?.(val, old);
        });
    }

    _onFileChange(val, old)
    {
        this.error = null;
    }

    _addCopyButton(buttons)
    {
        if (this.file?.content)
        {
            buttons.push({
                text: "Копировать",
                onclick: () =>
                {
                    if (this.file?.content)
                    {
                        navigator.clipboard.writeText(this.file.content);
                    }
                }
            });
        }
    }

    getContent(loadingStr = "")
    {
        return this.#isLoading ? loadingStr : this.file?.content ?? "";
    }
}

class MainLangFilesPage extends LangFilesPage
{
    #isEditable = false;
    #textarea = null;
    #fileEdited = false;

    constructor()
    {
        super(null);
    }

    get lang() { return this.file?.langData?.id; }
    get isEditable() { return this.#isEditable; }
    get isEdited() { return this.#fileEdited; }

    getOrCreateTextarea()
    {
        if (!this.#textarea)
        {
            this.#textarea = document.createElement("textarea");
            Object.defineProperty(this.#textarea, 'resize', {
                get() 
                { 
                    return () =>
                    {
                        this.style.height = 'auto';
                        this.style.width = 'auto';
                        this.style.height = this.scrollHeight + 'px';
                        this.style.width = this.scrollWidth + 'px';
                        this.onContentChange?.(this.value);
                    };
                }
            });

            this.#textarea.classList.add("code-text-edit");
            this.#textarea.addEventListener('input', this.#textarea.resize, false);
            $(this.#textarea).keydown((ev) =>
            {
                let tabs = "";
                if (ev.keyCode == 9)
                {
                    document.execCommand('insertHTML', false, '\t');
                    return false;
                }
                else if (ev.keyCode == 13)
                {
                    var pos = $(this.#textarea).prop("selectionStart");
                    if (pos >= 0)
                    {
                        let lenbuf = 0;
                        let lines = this.#textarea.value.split('\n');
                        let i = -1;
                        for (let j = 0; j < lines.length; j++)
                        {
                            let line = lines[j];
                            let len = line.length;
                            if (j != lines.length-1)
                            {
                                len++;
                            }

                            if (pos < lenbuf + len)
                            {
                                i = j;
                                break;
                            }
                            
                            lenbuf += line.length+1;
                        }
                        
                        if (i != -1)
                        {
                            let line = lines[i];
                            for (let j = 0; j < line.length; j++)
                            {
                                let char = line.charAt(j);
                                if (char == '\t' || char == ' ')
                                {
                                    tabs += char;
                                }
                                else
                                {
                                    break;
                                }
                            }
                        }
                    }
                    
                    document.execCommand('insertHTML', false, '\n' + tabs);
                    return false;
                }
            });
            this.#textarea.resize();
        }

        return this.#textarea;
    }

    get toolbarButtons()
    {
        let result = [];
        if (this.file)
        {
            this._addCopyButton(result);

            if (this.isEditable)
            {
                result.push(
                    {
                        text: "Сохранить",
                        update: true,
                        onclick: () =>
                        {
                            let newFileData = {...this.file};
                            let content = this.#textarea.value;
                            newFileData.content = content;
                            newFileData.file = new File([content], newFileData.file.name);
                            this.file = asReadonly(newFileData);
                            this.#fileEdited = true;
                        }
                    },
                    {
                        text: "Отмена",
                        update: true,
                        onclick: () => this.isEditable = false
                    }
                );
            }
            else
            {
                result.push({
                    text: "Редактировать",
                    update: true,
                    onclick: () => 
                    {
                        this.isEditable = true;

                        let textarea = this.getOrCreateTextarea(); 
                        textarea.value = this.file?.content ?? "";

                        setTimeout(() => textarea.resize(), 0.001);
                    }
                });
            }
        }

        return result;
    }

    set isEditable(flag)
    {
        change(!!flag, this.#isEditable, (flag) => this.#isEditable = flag, this.onEditableChange);
    }

    _onFileChange(val, old)
    {
        super._onFileChange(val, old);
        this.#fileEdited = false;
        this.isEditable = false;
    }
}

class LangFiles
{
    static UpdateType = { Buttons: "buttons", Error: "error", CodeArea: "codearea", Toolbar: "toolbar" };

    #pages = {};
    #activePage = null;

    #loading = new Loading();
    #downloadButton = null;
    #convertButton = null;
    #orderCol = null;
    #errorObj = null;
    #codeCol = null;
    #toolbar = null;

    constructor(data)
    {
        this.loaded = null;
        this.#downloadButton = data?.downloadButton;
        this.#convertButton = data?.convertButton;

        this.#orderCol = data?.orderCol;
        this.#errorObj = data?.errorObj;

        this.#codeCol = data?.codeCol;
        this.#toolbar = data?.toolbar;

        let mainPage = new MainLangFilesPage();
        this.#pages[mainPage.id] = mainPage;
        this.#activePage = mainPage;

        mainPage.onFileChange = (file) =>
        {
            this.onchange?.(null, file);
            this.update();
        };

        mainPage.onErrorChange = (error) =>
        {
            this.update([LangFiles.UpdateType.Error, LangFiles.UpdateType.CodeArea]);
        }

        this.#loading.onchange = (loading) =>
        {
            if (this.activePage?.isLoading ?? false)
            {
                this.update([LangFiles.UpdateType.CodeArea]);
            }
        };

        this.update();
    }

    getPage(id) { return this.#pages[id]; }

    get mainPage() { return this.getPage(null); }
    get activePage() { return this.#activePage; }

    open(id) 
    {
        if (id === undefined)
            id = null;

        let page = this.getPage(id);
        this.#activePage = page ?? this.mainPage;
        this.update();
    }
    
    reset()
    {
        let main = this.mainPage;
        this.#pages = {};
        this.#pages[main.id] = main;

        this.open(main.id);
    }

    update(types)
    {
        if (types === undefined)
        {
            types = Array.from(Object.values(LangFiles.UpdateType));
        }
        else
        {
            if (!Array.isArray(types))
            {
                return;
            }
        }

        types.forEach(type =>
        {
            if (type == LangFiles.UpdateType.Buttons)
            {
                if (this.#downloadButton)
                {
                    if (this.activePage.id)
                    {
                        this.#downloadButton.disabled = !this.activePage.file;
                    }
                    else
                    {
                        this.#downloadButton.disabled = !this.activePage.isEdited ?? true;
                    }
                }

                if (this.#convertButton)
                {
                    this.#convertButton.disabled = !this.activePage.id;
                }
            }
            else if (type == LangFiles.UpdateType.CodeArea)
            {
                if (this.#codeCol)
                {
                    let updateOrders = (text) =>
                    {
                        if (this.#orderCol)
                        {
                            let order = [];
                            
                            let errorRow = this.activePage.error?.row ?? -1;
                            Array.from(Array(lineCount(text)).keys()).forEach(
                                (i) => 
                                {
                                    let num = "<span";
                                    if (errorRow == i)
                                    {
                                        num += ' class="error"';
                                    }
                                    num += ">" + (i+1) + "</span>";

                                    order.push(num);
                                }
                            );
                            
                            this.#orderCol.innerHTML = order.join("<br>");
                        }
                    };

                    const page = this.#activePage;
                    const text = page.file?.content ?? "";
                    if (page.isEditable)
                    {
                        this.#codeCol.innerHTML = "";
                        let textarea = page.getOrCreateTextarea();
                        textarea.onContentChange = updateOrders;
                        this.#codeCol.appendChild(textarea);
                        updateOrders(text);
                    }
                    else
                    {
                        var lang = this.#activePage.lang ?? "none";

                        this.#codeCol.innerHTML = `<div class="code-text">
                                                        <div>
                                                            <pre>
                                                                <code class="language-${lang}" id="script-text">
                                                                </code>
                                                            </pre>
                                                        </div>
                                                    </div>`
                        
                        let script = document.getElementById("script-text");
                        script.textContent = text;
                        
                        Prism.highlightElement(script);
                        updateOrders(text);
                    }
                }
            }
            else if (type == LangFiles.UpdateType.Error)
            {
                if (this.#errorObj?.box && this.#errorObj?.row)
                {
                    let error = this.activePage?.error;
                    if (error)
                    {
                        this.#errorObj.box.value = error.message;
                        this.#errorObj.row.style.display = "table-row";
                    }
                    else
                    {
                        this.#errorObj.row.style.display = "none";
                    }
                }
            }
            else if (type == LangFiles.UpdateType.Toolbar)
            {
                if (this.#toolbar)
                {
                    this.#toolbar.innerHTML = "";
                    for (const butData of this.activePage.toolbarButtons)
                    {
                        let button = this.#createToolbarButton(butData.text, (ev) =>
                        {
                            butData.onclick?.(ev);
                            if (butData.update ?? false)
                            {
                                this.update(butData.updateTypes);
                            }
                        });

                        this.#toolbar.appendChild(button);
                    }
                }
            }
        });
    }

    createPage(id)
    {
        if (!id) return;
        let page = new LangFilesPage(id);
        this.#pages[id] = page;
        page.onFileChange = (file) =>
        {
            this.onchange?.(id, file);
            this.update();
        };
        this.update();
    }

    startLoading(id)
    {
        let page = this.getPage(id);
        if (!page) return;

        page.isLoading = true;
        this.#loading.start();
        this.update([LangFiles.UpdateType.CodeArea]);
    }

    stopLoading(id, file)
    {
        if (id === undefined)
            id = null;

        if (file)
        {
            let page = this.getPage(id);
            if (page)
            {
                page.file = file;
                page.isLoading = false;
            }
        }

        if (!this.#anyLoading)
            this.#loading.stop();
    }

    get #anyLoading()
    {
        return Object.values(this.#pages).some((page) => page.isLoading);
    }

    #createToolbarButton(text, onclick)
    {
        let input = document.createElement('input');
        input.type = "button";
        input.value = text;
        input.classList.add("button");
        input.onclick = onclick;
        return input;
    }
}

var langFiles;