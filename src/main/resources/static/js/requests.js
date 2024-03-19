const Requests = {
    convert(data)
    {
        $.ajax({
            url: "/conversion",
            method: "get",
            dataType: "text",
            data: { 
                "text": data.text,
                "input": data.inputLanguage,
                "output": data.outputLanguage 
            },
            success: data.success,
            error: data.error
        });
    },

    getExtensionById(data)
    {
        $.ajax({
            url: "/languages/extension-by-name",
            method: "get",
            dataType: "text",
            data: { "lang": data.languageId },
            success: data.success,
            error: data.error
        });
    },

    getIdByExtension(data)
    {
        $.ajax({
            url: "/languages/name-by-extension",
            method: "get",
            dataType: "text",
            data: { "ext": data.extension },
            success: data.success,
            error: data.error
        });
    }
};