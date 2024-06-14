$(document).ready(() =>
{
    bindEvents();
    loadDataFromStorage();
});

function loadDataFromStorage()
{
    var text = LS.sourceCodeText.value;
    var sourceLang = LS.sourceLanguage.value;
    var targetLang = LS.targetLanguage.value;
    
    if (typeof text === 'string')
    {
        El.inputText.textarea.val(text);
        El.inputText.textarea.trigger("input");
    }

    if (typeof sourceLang === 'string')
    {
        setValueOfSelect(El.inputSelector, sourceLang);
        El.inputSelector.trigger('change');
    }

    if (typeof targetLang === 'string')
    {
        setValueOfSelect(El.outputSelector, targetLang);
        El.outputSelector.trigger('change');
    }
}

function setValueOfSelect(select, val)
{
    var exists = false;
    select.find('option').each(function(){
        console.log(val + " - " + this.value);
        if (this.value == val) {
            exists = true;
            return false;
        }
    });

    if (exists)
    {
        select.val(val);
    }
}

function bindEvents()
{
    El.btns.loadFile.click(ButtonEvents.input.loadFileButtonClicked);
    El.fileInput.change(ButtonEvents.input.fileInputChanged);
    El.btns.downloadInput.click(ButtonEvents.input.saveTextClicked);
    El.btns.convert.click(ButtonEvents.input.convertClicked);
    El.btns.copyInput.click(ButtonEvents.input.copyClicked);
    El.btns.openInputWebCompiler.click(ButtonEvents.input.openCompiler);
    El.btns.clearInput.click(ButtonEvents.input.clearCode);
    El.inputSelector.change(ButtonEvents.input.languageChanged);

    El.btns.downloadOutput.click(ButtonEvents.output.saveTextClicked);
    El.btns.copyOutput.click(ButtonEvents.output.copyClicked);
    El.btns.openOutputWebCompiler.click(ButtonEvents.output.openCompiler);

    El.outputText.change(TextEvents.output.textChanged);
    El.outputSelector.change(ButtonEvents.output.languageChanged);

    El.inputText.textarea.keydown(TextEvents.input.onTextareaKeyDown);
    El.inputText.textarea.on('input', TextEvents.input.textChanged);

    $("html").on('dragover', false) .on("drop", (event) =>
    {
        event.preventDefault();
        event.stopPropagation();

        let origin = event.originalEvent;
        if (origin.dataTransfer?.files?.[0])
        {
            El.fileInput.get(0).files = origin.dataTransfer.files;
            El.fileInput.trigger('change');
        }
    });

    El.messageBox.bg.mousedown((event) =>
    {
        if (event.target !== event.currentTarget) return;
        ButtonEvents.common.closeMessageBox();
    })

    El.messageBox.closeButton.click(ButtonEvents.common.closeMessageBox);
    El.btns.showMessage.click(ButtonEvents.common.showMessageBox);

    var testText = " Random text\n<p style=\"font-size:50px\">Abc d</p>";

    var div = document.createElement('a');
    div.innerText = "[Title]";
    div.href = "javascript:setCursor(0, 3)";

    El.messageBox.text.empty();
    El.messageBox.text.append(div);
    El.messageBox.text.append(document.createTextNode(testText));
}

function setCursor(row=0, col=0)
{
    var ta = El.inputText.textarea.get(0);

    var lines = ta.value.split('\n');
    console.log(lines);
    var count = Math.min(row, lines.length);
    var c = 0;
    for (var i = 0; i < count; i++)
    {
        c += lines[i].length;
    }

    ta.focus();
    var pos = c + col + row;
    ta.selectionStart = pos;
    ta.selectionEnd = pos;
}