const ButtonEvents = {
    common: {
        showMessageBox()
        {
            El.messageBox.bg.show();
        },
        
        closeMessageBox()
        {
            El.messageBox.bg.hide();
        }
    },
    input: {
        languageChanged(event)
        {
            TextEvents.input.highlightText();
            LS.sourceLanguage.set(El.inputSelector.val());
        },

        loadFileButtonClicked(event)
        {
            El.fileInput.get(0)?.click();
        },

        clearCode(event)
        {
            El.inputText.textarea.val("");
            El.inputText.textarea.trigger("input");
        },

        fileInputChanged(event)
        {
            if (event.target.files.length <= 0)
            {
                return;
            }

            var file = event.target.files[0];
            event.target.value = null;

            var reader = new FileReader();
            reader.readAsText(file, "UTF-8");
            reader.onload = (evt) => 
            {
                El.inputText.textarea.get(0).value = evt.target.result;

                var ext = file.name.split('.').pop();

                Requests.getIdByExtension({
                    extension: ext,
                    success(data)
                    {
                        var json = JSON.parse(data);
                        El.inputSelector.val(json.name);
                        El.inputSelector.trigger("change");
                        El.inputText.textarea.trigger('input');
                    },
                    error(data)
                    {
                        El.inputText.textarea.trigger('input');
                    }
                });
            }
        },
        
        convertClicked(event)
        {
            var outLangId = El.outputSelector.val();
            event.target.disabled = true;

            ButtonEvents.common.closeMessageBox();

            Requests.convert({
                text: El.inputText.textarea.val(),
                inputLanguage: El.inputSelector.val(),
                outputLanguage: outLangId,

                success(data)
                {
                    event.target.disabled = false;
                    var json = JSON.parse(data);
                    console.log(json);

                    var language = Prism.languages[outLangId];
                    if (typeof language != 'undefined')
                    {
                        let highlited = Prism.highlight(json.text, language, outLangId);
                        El.outputText.html($.parseHTML(highlited));
                    }
                    else
                    {
                        El.outputText.text(json.text);
                    }

                    El.outputText.prop("lang", outLangId);
                    El.outputText.trigger("change");

                    function printMessage(type, color, message)
                    {
                        El.messageBox.text.append(document.createTextNode("["));
                        
                        var typeText = document.createElement('a');
                        typeText.innerText = type;
                        typeText.style.cssText = `color: ${color};`;

                        El.messageBox.text.append(typeText);

                        if (message.row > 0)
                        {
                            var a = document.createElement('a');
                            var row = message.row;
                            var column = Math.max(0, message.column - 1);

                            a.innerText = row.toString() + "," + column;
                            a.href = `javascript:setCursor(${row-1}, ${column})`;
                            a.draggable = false;

                            El.messageBox.text.append(document.createTextNode(" ("));
                            El.messageBox.text.append(a);
                            El.messageBox.text.append(document.createTextNode(")"));
                        }

                        El.messageBox.text.append(document.createTextNode("] " + message.text + "\n"));
                    }

                    El.messageBox.text.empty();
                    for (var i = 0; i < json.errors.length; i++)
                    {
                        let error = json.errors[i];
                        printMessage("Ошибка", 'red', error);
                    }

                    for (var i = 0; i < json.warnings.length; i++)
                    {
                        let warning = json.warnings[i];
                        printMessage("Предупреждение", 'yellow', warning);
                    }

                    if (json.errors.length > 0 || json.warnings.length > 0)
                    {
                        El.btns.showMessage.show();
                        ButtonEvents.common.showMessageBox();
                    }
                    else
                    {
                        El.btns.showMessage.hide();
                    }
                },
                error()
                {
                    event.target.disabled = false;
                    El.messageBox.text.empty();
                    var text = document.createElement('a');
                    text.innerText = "Internal Error";
                    text.style.cssText = `color: red;`;

                    El.messageBox.text.append(text);
                    El.btns.showMessage.show();
                }
            });
        },

        saveTextClicked()
        {
            ButtonEvents.__saveToFile({
                languageId: El.inputSelector.val(),
                text: El.inputText.textarea.val(),
                name: "input_code"
            });
        },

        copyClicked()
        {
            var text = El.inputText.textarea.val();
            if (text != "" && typeof text == 'string')
            {
                navigator.clipboard.writeText(text);
            }
        },

        openCompiler()
        {
            Requests.getCompilerById({
                languageId: El.inputSelector.val(),
                success(data)
                {
                    var json = JSON.parse(data);
                    if (json.url)
                    {
                        window.open(json.url, "_blank").focus();
                    }
                }
            });
        }
    },

    output: {
        languageChanged(event)
        {
            LS.targetLanguage.set(El.outputSelector.val());
        },

        saveTextClicked()
        {
            ButtonEvents.__saveToFile({
                languageId: El.outputText.attr("lang"),
                text: El.outputText.text(),
                name: "output_code"
            });
        },

        copyClicked()
        {
            var text = El.outputText.text();
            if (text != "" && typeof text == 'string')
            {
                navigator.clipboard.writeText(text);
            }
        },

        openCompiler()
        {
            Requests.getCompilerById({
                languageId: El.outputSelector.val(),
                success(data)
                {
                    var json = JSON.parse(data);
                    if (json.url)
                    {
                        window.open(json.url, "_blank").focus();
                    }
                }
            });
        }
    },

    async __saveToFile(data)
    {
        async function save(ext)
        {
            var fileHandle = await window.showSaveFilePicker({
                suggestedName: `${data.name}.${ext}`
            });
            if (fileHandle.kind !== 'file')
            {
                return;
            }
    
            var writable = await fileHandle.createWritable();
            writable.write(data.text);
            writable.close();
        }

        if (typeof data.languageId == "undefined")
        {
            save("txt");
            return;
        }

        Requests.getExtensionById({
            languageId: data.languageId,
            success(data)
            {
                var json = JSON.parse(data);
                save(json.extension);
            },
            error(data)
            {
                save("txt");
            }
        });
    }
};