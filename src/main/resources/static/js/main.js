$(document).ready(() =>
{
    El.btns.loadFile.click(ButtonEvents.input.loadFileButtonClicked);
    El.fileInput.change(ButtonEvents.input.fileInputChanged);
    El.btns.downloadInput.click(ButtonEvents.input.downloadTextClicked);
    El.btns.convert.click(ButtonEvents.input.convertClicked);
    El.inputSelector.change(ButtonEvents.input.languageChanged);

    El.btns.downloadOutput.click(ButtonEvents.output.downloadTextClicked);
    El.btns.copy.click(ButtonEvents.output.copyClicked);

    El.outputText.change(TextEvents.output.textChanged);

    El.inputText.textarea.keydown(TextEvents.input.onTextareaKeyDown);
    El.inputText.textarea.on('input', TextEvents.input.highlightText);

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
        closeMessageBox();
    })

    El.messageBox.closeButton.click(closeMessageBox);
    El.btns.showMessage.click(showMessageBox);

    var testText = " Random text\n<p style=\"font-size:50px\">Abc d</p>";

    var div = document.createElement('a');
    div.innerText = "[Title]";
    div.href = "javascript:setCursor(0, 3)";

    El.messageBox.text.empty();
    El.messageBox.text.append(div);
    El.messageBox.text.append(document.createTextNode(testText));
});

function closeMessageBox()
{
    El.messageBox.bg.hide();
}

function showMessageBox()
{
    El.messageBox.bg.show();
}

function setCursor(row=0, col=0)
{
    console.log(row + " - " + col);
    closeMessageBox();
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