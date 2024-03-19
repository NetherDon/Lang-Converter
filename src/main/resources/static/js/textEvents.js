const TAB_SIZE = 3;

const TextEvents = {
    input: {
        highlightText()
        {
            var languageId = El.inputSelector.val();
            var language = Prism.languages[languageId]

            if (typeof language != 'undefined')
            {
                let highlited = Prism.highlight(El.inputText.textarea.val(), language, languageId);
                El.inputText.highlighted.html($.parseHTML(highlited));
                return;
            }

            this.highlitedTextArea.text(this.textarea.value);
        },

        onTextareaKeyDown(event)
        {
            if (event.keyCode == 9)
            {
                document.execCommand('insertHTML', false, ' '.repeat(TAB_SIZE));
                return false;
            }
            else if (event.keyCode == 13)
            {
                var pos = event.target.selectionStart;
                
                var s = "";
                var spaces = 0;
                while (pos > 0)
                {
                    pos--;
                    var c = event.target.value.charAt(pos);

                    if (c == '\n')
                    {
                        break;
                    }
                    else if (c == ' ')
                    {
                        spaces++;
                    }
                    else
                    {
                        spaces = 0;
                    }

                    s += c;
                }

                document.execCommand('insertHTML', false, '\n' + ' '.repeat(spaces));
                return false;
            }

            return true;
        }
    },
    output: {
        textChanged(event)
        {
            var flag = event.target.innerText == '';
            El.btns.downloadOutput.get(0).disabled = flag;
            El.btns.copy.get(0).disabled = flag;
        }
    }
};