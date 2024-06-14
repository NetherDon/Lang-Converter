const El = {
    get inputSelector() { return $('#input-lang-selector'); },
    get outputSelector() { return $('#output-lang-selector'); },

    get fileInput() { return $('#load-file-input'); },
    
    btns: {
        get convert() { return $('#convert-btn'); },
        get loadFile() { return $('#load-file-btn'); },
        get downloadInput() { return $('#download-input-btn'); },
        get copyInput() { return $('#copy-input-btn'); },
        get openInputWebCompiler() { return $('#open-compiler-input-btn'); },
        get clearInput() { return $('#clear-input-btn'); },
        
        get downloadOutput() { return $('#download-output-btn'); },
        get copyOutput() { return $('#copy-output-btn'); },
        get openOutputWebCompiler() { return $('#open-compiler-output-btn'); },

        get showMessage() { return $('#show-message-btn'); }
    },

    inputText: {
        get textarea() { return $('#input-textarea'); },
        get highlighted() { return $('#input-highligted-text'); }
    },

    get outputText() { return $('#output-text'); },

    messageBox: {
        get bg() { return $('#message-bg'); },
        get panel() { return $('#message-panel'); },
        get text() { return $('#message-text'); },
        get closeButton() { return $('#message-close-btn'); }
    }
}

class LSItem
{
    #key = null;

    constructor(key)
    {
        this.#key = key;
    }

    get key() { return this.#key; }
    get value() { return localStorage.getItem(this.#key); }

    set(text)
    {
        localStorage.setItem(this.#key, text);
    }

    delete()
    {
        localStorage.removeItem(this.#key);
    }
}

const LS = {
    sourceCodeText: new LSItem("source-code-text"),
    sourceLanguage: new LSItem("source-language"),
    targetLanguage: new LSItem("target-language")
}