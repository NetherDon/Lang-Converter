//import * as a from "fileLoader";

Prism?.hooks.add("before-highlight", function (env) {
    env.code = env.element.textContent;
});

$("#lang-selector").ready(() =>
{
    var selector = document.getElementById("lang-selector");

    selector.onchange = () =>
    {
        this.langFiles.open(selector.value);
    };
    
    selector.setHiddenForOptions({
        flag: true,
        orElseFlag: false
    });
});

function asReadonly(dict, useForChild=false)
{
    function _create(dict, previous)
    {
        var result = {...dict};
        var params = {};
        var readonly = { writable: false };

        for (const [key, value] of Object.entries(result))
        {
            params[key] = readonly;
            if (useForChild && value?.constructor == Object && !(value in previous))
            {
                result[key] = _create(value, previous + [dict]);
            }

        }
        Object.defineProperties(result, params);
        return result;
    }

    return _create(dict, []);
}

function sendSelectedLanguage()
{
    var file = this.langFiles.mainPage?.file?.file;
    if (!file)
    {
        alert("Empty file");
        return;
    }

    var langSelector = document.getElementById("lang-selector");
    if (!langSelector?.value) return;

    var form = new FormData();
    form.append("file", file, file.name);
    form.append("requiredLanguageId", langSelector.value);

    langFiles.startLoading(langSelector.value);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/converter");
    xhr.onload = () => 
    {
        try
        {
            var data = JSON.parse(xhr.responseText);
            console.log(data);
            //setTimeout(() => langFiles.stopLoading(data.lang, data), 5000);
            langFiles.stopLoading(data.lang, data);
        }
        catch (e) 
        {
            langFiles.stopLoading(langSelector.value, null);
        }
    };
    xhr.onerror = (xhr, status, error) => 
    {
        alert('Error: ' + status);
    };
    xhr.send(form);
}

function downloadActiveFile()
{
    var langSelector = document.getElementById("lang-selector");
    //if (!langSelector?.value) return;

    var fileData = this.langFiles.getPage(langSelector.value)?.file;
    let name;
    if (langSelector?.value)
        name = fileData.name;
    else
        name = fileData.file.name;

    console.log(fileData);
    if (!fileData?.content || !name)
    {
        alert("Empty file");
        return;
    }

    var file = new File([fileData.content], name);

    const link = document.createElement('a')
    const url = URL.createObjectURL(file)

    link.href = url
    link.download = file.name
    document.body.appendChild(link)
    link.click()

    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
}